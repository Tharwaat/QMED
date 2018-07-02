package com.example.root.qmed;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class RequestActivity extends AppCompatActivity {

    LatLng orderLocation;
    private int radius = 1;
    private Boolean pharmacyFound = false;
    private String pharmacyFoundID;
    String uID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        uID = firebaseAuth.getCurrentUser().getUid();

        final EditText medicine = (EditText) findViewById(R.id.medrequest);

        final Button findbtn = (Button) findViewById(R.id.findbtn);

        findbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String med = medicine.getText().toString();
                Bundle b = getIntent().getExtras();

                //resultMed.setText(b.getString("reqMed"));

                double curlon = b.getDouble("curlon");
                double curlat = b.getDouble("curlat");
                orderLocation = new LatLng(curlat,curlon);

                getClosestPharmacy(orderLocation, med);



                /*b.putString("reqMed", med);

                Intent intent = new Intent(getApplicationContext(),ResultsActivity.class);
                intent.putExtras(b);

                startActivity(intent);
                finish();*/
            }
        });
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

        newReq = FirebaseDatabase.getInstance().getReference().child("PendingRequests").child(uID);
        newReq.setValue(PID);

        Toast.makeText(RequestActivity.this,"Your request has been created, you can check it's state by clicking on check state",Toast.LENGTH_LONG).show();

        //Bundle b = new Bundle();
        //b.putString("pid",PID);

        Intent intent = new Intent(getApplicationContext(),UserHomeActivity.class);
        //intent.putExtras(b);
        startActivity(intent);
        finish();
    }


}