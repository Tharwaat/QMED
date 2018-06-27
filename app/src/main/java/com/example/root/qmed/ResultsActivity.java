package com.example.root.qmed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResultsActivity extends AppCompatActivity {


    LatLng orderLocation;
    TextView resultMed;
    private int radius = 1;
    private Boolean pharmacyFound = false;
    private String pharmacyFoundID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //resultMed = (TextView) findViewById(R.id.resulttxt);

        Bundle b = getIntent().getExtras();

        //resultMed.setText(b.getString("reqMed"));

        String medname = b.getString("reqMed");
        double curlon = b.getDouble("curlon");
        double curlat = b.getDouble("curlat");

        orderLocation = new LatLng(curlat,curlon);

        getClosestDriver(orderLocation, medname);
    }





    private String getClosestDriver(final LatLng curLocation, final String mname){
        DatabaseReference pharmacyLocation = FirebaseDatabase.getInstance().getReference().child("availPharmacies");

        GeoFire geoFire = new GeoFire(pharmacyLocation);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(curLocation.latitude, curLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!pharmacyFound){
                    pharmacyFound = true;
                    pharmacyFoundID = key;

                    CreateRequest(mname, pharmacyFoundID);


                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!pharmacyFound)
                {
                    radius++;
                    getClosestDriver(curLocation,mname);
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
        return pharmacyFoundID;
    }

    public void CreateRequest(String MedName, String PID){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uID = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference newReq = FirebaseDatabase.getInstance().getReference().child("Requests")
                                    .child(PID).child(uID).child("medicine");
        newReq.setValue(MedName);

        newReq = FirebaseDatabase.getInstance().getReference().child("Requests")
                .child(PID).child(uID).child("state");

        newReq.setValue("stall");
    }
}
