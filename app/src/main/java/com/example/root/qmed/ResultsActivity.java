package com.example.root.qmed;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ResultsActivity extends AppCompatActivity {



    TextView resultMed;
    private String pid;
    Button book, order;
    String uID;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        uID = firebaseAuth.getCurrentUser().getUid();

        book = (Button) findViewById(R.id.book);
        order = (Button) findViewById(R.id.order);
        book.setEnabled(false);
        order.setEnabled(false);

        resultMed = (TextView) findViewById(R.id.resulttxt);
        /*Bundle b = getIntent().getExtras();
        pid = b.getString("pid");*/

        DatabaseReference newReq = FirebaseDatabase.getInstance().getReference();
        newReq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pid = (String) dataSnapshot.child("PendingRequests").child(uID).getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //resultMed.setText(b.getString("reqMed"));
        /*String medname = b.getString("reqMed");
        double curlon = b.getDouble("curlon");
        double curlat = b.getDouble("curlat");
        orderLocation = new LatLng(curlat,curlon);*/

        //getClosestPharmacy(orderLocation, medname);

        /*Intent intent = new Intent(getApplicationContext(),ResultsActivity.class);
        startActivity(intent);*/
    }





   /* private void getClosestPharmacy(final LatLng curLocation, final String mname){
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
    }*/

    public void CheckState(DataSnapshot ds){
        String state = (String) ds.child("Requests").child(pid).child(uID).child("state").getValue();
        if(state.equals("accepted")){
            book.setEnabled(true);
            order.setEnabled(true);
            Toast.makeText(ResultsActivity.this,"Your request's been approved, you can either book or order your medicine",Toast.LENGTH_LONG).show();
        }else if (state.equals("rejected")){
            Toast.makeText(ResultsActivity.this,"Your request's been rejected",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(ResultsActivity.this,"Still no response, please wait",Toast.LENGTH_LONG).show();
        }
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

    public void order(View view){
        DatabaseReference newReq = FirebaseDatabase.getInstance().getReference().child("Requests")
                .child(pid).child(uID).child("userAction");

        newReq.setValue("order");
    }

    public void book(View view){
        DatabaseReference newReq = FirebaseDatabase.getInstance().getReference().child("Requests")
                .child(pid).child(uID).child("userAction");

        newReq.setValue("book");
    }
 }
