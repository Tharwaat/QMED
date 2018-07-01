package com.example.root.qmed;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        final EditText medicine = (EditText) findViewById(R.id.medrequest);

        final Button findbtn = (Button) findViewById(R.id.findbtn);
        findbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             String med = medicine.getText().toString();
             Bundle b = getIntent().getExtras();
             b.putString("reqMed", med);

             Intent intent = new Intent(getApplicationContext(),ResultsActivity.class);
             intent.putExtras(b);

             startActivity(intent);
             finish();
            }
        });




    }
}
