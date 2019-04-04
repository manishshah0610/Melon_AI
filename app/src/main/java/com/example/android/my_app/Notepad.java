package com.example.android.my_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Notepad extends AppCompatActivity {
    EditText username;
     Button but1;
    String usernm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);


        username = findViewById(R.id.userNameTextView);
        usernm = username.getText().toString();
        but1 = findViewById(R.id.button);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernm = username.getText().toString();
                Toast.makeText(getApplicationContext(), usernm, Toast.LENGTH_SHORT).show();
                username.setText("OK");
                but1.setText("PRESSED");
            }
        });


    }
    public void onBackPressed()
    {
        finish();
    }
    public void setting(View view){
        Intent set = new Intent(Notepad.this, Settings.class);
        startActivity(set);
    }

    public void homed(View view){
        Intent set = new Intent(Notepad.this, MainActivity.class);
        //startActivity(set);
        finish();
    }
}
