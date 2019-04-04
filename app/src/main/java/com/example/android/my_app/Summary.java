package com.example.android.my_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Summary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
    }
    public void setting(View view){
        Intent set = new Intent(Summary.this, Settings.class);
        startActivity(set);
    }

    public void homed(View view){
        Intent set = new Intent(Summary.this, MainActivity.class);
        //startActivity(set);
        finish();
    }
}
