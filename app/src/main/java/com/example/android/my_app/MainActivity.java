package com.example.android.my_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity {
    private ImageButton profile;
    private static final int REQUEST_RECORD_AUDIO = 13;

    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profile= findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });
        // initiate a Switch
       /* Switch simpleSwitch = (Switch) findViewById(R.id.simpleSwitch);

// check current state of a Switch (true or false).
        Boolean switchState = simpleSwitch.isChecked();*/
       requestMicrophonePermission();
    }

    public void tutorial(View view){
        Intent tut = new Intent(MainActivity.this, Tutorial.class);
        startActivity(tut);
    }

    public void notepad(View view){
        Intent tut = new Intent(MainActivity.this, Notes.class);
        startActivity(tut);
    }


    public void summary(View view){
        Intent sum = new Intent(MainActivity.this, Summary.class);
        startActivity(sum);
    }

    public void setting(View view){
        Intent set = new Intent(MainActivity.this, Settings.class);
        startActivity(set);
    }

    public void alarm(View view){
        Intent set = new Intent(MainActivity.this, Alarm.class);
        startActivity(set);
    }

    public void homed(View view){
        Intent set = new Intent(MainActivity.this, MainActivity.class);
     //   startActivity(set);
        finish();
    }
    public void rec(View view){
        Intent set = new Intent(MainActivity.this,Classification_Activity.class);
        startActivity(set);
        finish();
    }
    private void requestMicrophonePermission() {
        requestPermissions(
                new String[] {android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }
}
   /* private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel_0";
            String description = "App Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}*/
