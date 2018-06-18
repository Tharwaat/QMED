package com.example.root.qmed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView resultMed = (TextView) findViewById(R.id.resulttxt);

        resultMed.setText(getIntent().getStringExtra("reqMed"));
    }
}
