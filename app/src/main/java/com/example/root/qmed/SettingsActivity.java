package com.example.root.qmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.CollationElementIterator;


public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    TextView first ;
    TextView second ;
    EditText editText ;
    EditText editText2 ;
   // Button mButton ;
    String userID;
    String selected;
    FirebaseUser user;
    TextView confirm;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Spinner spinner = findViewById(R.id.spinner);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText.setVisibility(View.INVISIBLE);
        editText2.setVisibility(View.INVISIBLE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.settings,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        confirm = (TextView) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
       // mButton = findViewById(R.id.button);
       // mButton.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selected = adapterView.getItemAtPosition(i).toString();
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        if(selected.equals("Change user-name"))
        {

            first.setText("Enter the new user-name");
            second.setText("Confirm the entered user-name");
            editText.setVisibility(View.VISIBLE);
            editText2.setVisibility(View.VISIBLE);

        }
        else if (selected.equals("Change password"))
        {
            editText.setVisibility(View.VISIBLE);
            editText2.setVisibility(View.VISIBLE);
            first.setText("Enter the new password");
            second.setText("Confirm the entered password");
        }

        else if (selected.equals("Change address"))
        {
            editText.setVisibility(View.VISIBLE);
            editText2.setVisibility(View.VISIBLE);
            first.setText("Enter the new address");
            second.setText("Confirm the entered address");
        }
        else if (selected.equals("Change phone"))
        {
            editText.setVisibility(View.VISIBLE);
            editText2.setVisibility(View.VISIBLE);
            first.setText("Enter the new phone");
            second.setText("Confirm the entered phone");
        }else if(selected.equals("Settings"))
        {
            editText.setVisibility(View.INVISIBLE);
            editText2.setVisibility(View.INVISIBLE);
            first.setText("");
            second.setText("");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    public void onClick(View view) {
        String editone = editText.getText().toString();
        String edittwo = editText2.getText().toString();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (!editone.equals(edittwo))
        {
            Toast.makeText(this, "Not Matching",
                    Toast.LENGTH_LONG).show();
        }else
        {
            if(selected.equals("Change user-name"))
            {
                Toast.makeText(this, "Updated",
                        Toast.LENGTH_SHORT).show();
                mDatabase.child("Users").child(userID).child("name").setValue(editone);
                Intent intent = new Intent(this , UserHomeActivity.class);
                startActivity(intent);
                finish();

            }
            else if (selected.equals("Change password"))
            {
                Toast.makeText(this, "Updated",
                        Toast.LENGTH_SHORT).show();
                mDatabase.child("Users").child(userID).child("password").setValue(editone);
                user.updatePassword(editone);
                Intent intent = new Intent(this , UserHomeActivity.class);
                startActivity(intent);
                finish();
            }

            else if (selected.equals("Change address"))
            {
                Toast.makeText(this, "Updated",
                        Toast.LENGTH_SHORT).show();
                mDatabase.child("Users").child(userID).child("address").setValue(editone);
                Intent intent = new Intent(this , UserHomeActivity.class);
                startActivity(intent);
                finish();
            }
            else if (selected.equals("Change phone"))
            {
                Toast.makeText(this, "Updated",
                        Toast.LENGTH_SHORT).show();
                mDatabase.child("Users").child(userID).child("phone").setValue(editone);
                Intent intent = new Intent(this , UserHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }


    }
}
