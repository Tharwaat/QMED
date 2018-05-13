package com.example.root.qmed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{


    private Button buttonSignIn;
    private EditText email;
    private EditText password;
    private TextView textViewSignup;
    private FirebaseAuth firebaseAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    String userId ;
    //read from db
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (firebaseAuth.getCurrentUser()!=null){
            userId = firebaseAuth.getCurrentUser().getUid();

            //there's profile activvity already
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    home(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        buttonSignIn = (Button) findViewById(R.id.signin);
        email = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.password);
        textViewSignup = (TextView) findViewById(R.id.tvSignUp);
        progressDialog = new ProgressDialog(this);
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }



    private void userLogin() {
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        //white fields
        if (TextUtils.isEmpty(mail)){
            Toast.makeText(this,"Email is required!",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Password is required!",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Logging in, please wait....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    //start activity of home profile
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userId = firebaseAuth.getCurrentUser().getUid();
                            checkUserType(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{

                    Toast.makeText(SignInActivity.this,"Either email or password is wrong!. Please try again.",Toast.LENGTH_LONG).show();

                }

            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view==buttonSignIn){
            userLogin();
        }
        if (view==textViewSignup){
            startActivity(new Intent(this,SignUpActivity.class));//hena el mfrood  n5leh yro7 3la register
            finish();
        }
    }

    private void checkUserType(DataSnapshot dataSnapshot) {
        String type = (String) dataSnapshot.child("Users").child(userId).child("type").getValue();

        if(type.equals("Customer")) {
            startActivity(new Intent(getApplicationContext(), UserMapActivity.class));
            finish();
        }
        else {
            startActivity(new Intent(getApplicationContext(), PharmacyHomeActivity.class));
            finish();
        }


    }

    private void home(DataSnapshot dataSnapshot) {
        String type = null;
        type = (String) dataSnapshot.child("Users").child(userId).child("type").getValue();

        if(type.equals("Customer")) {
            startActivity(new Intent(getApplicationContext(), UserHomeActivity.class));
            finish();
        }
        else {
            startActivity(new Intent(getApplicationContext(), PharmacyHomeActivity.class));

            finish();
        }


    }
}
