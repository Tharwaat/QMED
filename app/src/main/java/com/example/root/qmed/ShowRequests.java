package com.example.root.qmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowRequests extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    //ArrayList<Request> requests = new ArrayList<Request>();
    String pharmacyID;
    String customerID = "W5YVs58k7HYonR0fsn1X6lGWWpa2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_requests);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        pharmacyID = firebaseAuth.getCurrentUser().getUid();
       // Request request = new Request("omar","3ezzaby","ahram","01111301983","accepted","asprin","W5YVs58k7HYonR0fsn1X6lGWWpa2","book");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getRequests(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
        private void getRequests(DataSnapshot dataSnapshot) {
            Request request = new Request();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            request.setCustomerAddress((String) dataSnapshot.child("Users").child(customerID).child("address").getValue());
            request.setCustomerName((String) dataSnapshot.child("Users").child(customerID).child("name").getValue());
            request.setCustomerPhone((String) dataSnapshot.child("Users").child(customerID).child("phone").getValue());
            request.setMedicine((String) dataSnapshot.child("Requests").child(pharmacyID).child(customerID).child("medicine").getValue());
            request.setPharmacy((String) dataSnapshot.child("Users").child(pharmacyID).child("name").getValue());
            request.setState((String) dataSnapshot.child("Requests").child(pharmacyID).child(customerID).child("state").getValue());
            request.setUserAction((String) dataSnapshot.child("Requests").child(pharmacyID).child(customerID).child("userAction").getValue());
        }
            Intent i = new Intent(this, PharmacyRequest.class);
            i.putExtra("sampleObject", request);
            startActivity(i);
            finish();


    }
}
