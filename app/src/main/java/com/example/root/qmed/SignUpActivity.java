package com.example.root.qmed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ////
    private Button signupBt;
    private EditText etemail;
    private EditText etpassword;
    private EditText etname;
    private EditText etaddress;
    private EditText etphone;
    private TextView tvlogin;
    private ProgressDialog pd;
    Customer customer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        signupBt = (Button) findViewById(R.id.SignUpbtn);
        etemail = (EditText) findViewById(R.id.SignUpmail);
        etpassword = (EditText) findViewById(R.id.SignUpPass);
        etname = (EditText)findViewById(R.id.SignUpName);
        etaddress = (EditText)findViewById(R.id.SignUpAddress);
        etphone = (EditText)findViewById(R.id.SignUpPhone);
        tvlogin = (TextView) findViewById(R.id.tvSignIn);
        pd = new ProgressDialog(this);
        ////
        signupBt.setOnClickListener(this);
        tvlogin.setOnClickListener(this);
    }


    public void register(){
        final String name = etname.getText().toString().trim();
        final String address = etaddress.getText().toString().trim();
        final String phone = etphone.getText().toString().trim();
        final String email = etemail.getText().toString().trim();
        final String password = etpassword.getText().toString().trim();
        final String type = "Customer";


        ///Validating
        if(name.isEmpty()){
            Toast.makeText(this,"Please enter your name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(address.isEmpty()){
            Toast.makeText(this,"Please enter your Address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone.isEmpty()){
            Toast.makeText(this,"Please enter your phone!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(email.isEmpty()){
            Toast.makeText(this,"Please enter your email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etemail.setError("Please enter a valid email address!");
            etemail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            Toast.makeText(this,"Please fill in your password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            etpassword.setError("Password should be at least 6 characters!");
            etpassword.requestFocus();
            return;
        }

        //

        pd.setMessage("Registering account...");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {  // Registration successfull!
                            pd.dismiss();
                            String user_id = mAuth.getCurrentUser().getUid();
                            customer = new Customer(name,address,phone,email,password,type);

                            DatabaseReference user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                            user_db.setValue(customer);

                            Toast.makeText(SignUpActivity.this, "Registration completed!",Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(),SignInActivity.class));

                            finish();
                        }
                        else{                      // Already existed email.
                            pd.dismiss();
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(SignUpActivity.this, "This email is already in use!. Sign up with a different one.", Toast.LENGTH_LONG).show();
                            }

                        }

                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == signupBt) register();
        else if (view == tvlogin){
            startActivity(new Intent(this,SignInActivity.class));
            finish();
        }
    }
}
