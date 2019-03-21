package com.example.android.my_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tutorial(View view){
        Intent tut = new Intent(MainActivity.this, Tutorial.class);
        startActivity(tut);
    }

    public void alarm(View view){
        Intent alm = new Intent(MainActivity.this, Alarm.class);
        startActivity(alm);
    }

    public void notepad(View view){
        Intent ntp = new Intent(MainActivity.this, Notepad.class);
        startActivity(ntp);
    }

    public void summary(View view){
        Intent sum = new Intent(MainActivity.this, Summary.class);
        startActivity(sum);
    }
}
