package com.example.root.qmed;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResultsActivity extends AppCompatActivity {


    LatLng orderLocation;
    TextView resultMed;
    private int radius = 1;
    private Boolean pharmacyFound = false;
    private String pharmacyFoundID;
    String uID;
    Button mRefresh;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        uID = firebaseAuth.getCurrentUser().getUid();

        resultMed = (TextView) findViewById(R.id.resulttxt);
        mRefresh = (Button) findViewById(R.id.refreshbtn);

        Bundle b = getIntent().getExtras();
        //resultMed.setText(b.getString("reqMed"));
        String medname = b.getString("reqMed");
        double curlon = b.getDouble("curlon");
        double curlat = b.getDouble("curlat");
        orderLocation = new LatLng(curlat,curlon);

        getClosestPharmacy(orderLocation, medname);

        /*Intent intent = new Intent(getApplicationContext(),ResultsActivity.class);
        startActivity(intent);*/
    }





    private void getClosestPharmacy(final LatLng curLocation, final String mname){
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
                    getClosestPharmacy(curLocation,mname);
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    public void CreateRequest(String MedName, String PID){


        DatabaseReference newReq = FirebaseDatabase.getInstance().getReference().child("Requests")
                                    .child(PID).child(uID).child("medicine");
        newReq.setValue(MedName);

        newReq = FirebaseDatabase.getInstance().getReference().child("Requests")
                .child(PID).child(uID).child("state");

        newReq.setValue("stall");
    }

    public void CheckState(DataSnapshot ds){
        String state = (String) ds.child("Requests").child(pharmacyFoundID).child(uID).child("state").getValue();
        /*if (state.equals("accepted"))*/ resultMed.setText(state);

    }


    public void CheckRequest(View view) {
        DatabaseReference newReq = FirebaseDatabase.getInstance().getReference();

        newReq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CheckState(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
