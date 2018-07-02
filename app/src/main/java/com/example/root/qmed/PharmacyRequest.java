package com.example.root.qmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PharmacyRequest extends AppCompatActivity {
    TextView name,address,phone,medicine,userAction;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    String pharmacyID;
    String customerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_request);
        name = (TextView)findViewById(R.id.customerName);
        address = (TextView)findViewById(R.id.customerAddress);
        phone = (TextView)findViewById(R.id.customerPhone);
        medicine =(TextView)findViewById(R.id.medicine);
        userAction  =(TextView)findViewById(R.id.userAction);
        Intent i = getIntent();
        Request request = (Request) i.getSerializableExtra("sampleObject");
        name.setText(request.getCustomerName());
        address.setText(request.getCustomerAddress());
        phone.setText(request.getCustomerPhone());
        medicine.setText(request.getMedicine());
        userAction.setText(request.getUserAction());
        customerID = request.getCustomerID();

    }


    public void acceptRequest(View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        pharmacyID= firebaseAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Requests").child(pharmacyID).child(customerID).child("state").setValue("accepted");
        startActivity(new Intent(getApplicationContext(), PharmacyHomeActivity.class));
        finish();

    }


    public void rejectRequest(View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        pharmacyID= firebaseAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Requests").child(pharmacyID).child(customerID).child("state").setValue("rejected");
        startActivity(new Intent(getApplicationContext(), PharmacyHomeActivity.class));
        finish();
    }
}
