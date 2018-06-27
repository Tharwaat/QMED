package com.example.root.qmed;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResultsActivity extends AppCompatActivity {


    LatLng orderLocation;
    TextView resultMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultMed = (TextView) findViewById(R.id.resulttxt);

        Bundle b = getIntent().getExtras();

        resultMed.setText(b.getString("reqMed"));

        double curlon = b.getDouble("curlon");
        double curlat = b.getDouble("curlat");

        orderLocation = new LatLng(curlat,curlon);

         int a = 0;
         int c = 0;
        getClosestDriver(orderLocation);


    }



    private int radius = 1;
    private Boolean pharmacyFound = false;
    private String pharmacyFoundID;

    private void getClosestDriver(final LatLng curLocation){
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

                    resultMed.setText(pharmacyFoundID);
                    /*DatabaseReference pharmacyRef = FirebaseDatabase.getInstance().getReference().child("Users").child(pharmacyFoundID);
                    String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    HashMap map = new HashMap();
                    map.put("customerRequestId", customerId);
                    pharmacyRef.updateChildren(map);

                    getPharmacyLocation();*/

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
                    getClosestDriver(curLocation);
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
}
