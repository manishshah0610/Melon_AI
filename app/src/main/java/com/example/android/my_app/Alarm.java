package com.example.android.my_app;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

import java.sql.Time;
import java.util.Calendar;

public class Alarm extends AppCompatActivity {

    AlarmManager alarm_manager;
    TimePicker alarm_time_picker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        this.context = this;

        alarm_manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarm_time_picker=(TimePicker)findViewById(R.id.time_picker);
        update_text = (TextView)findViewById(R.id.alarm_update);
        final Calendar calendar = Calendar.getInstance();

       final Intent my_intent = new Intent(this.context,Alarm_Receiver.class);

        Button start_alarm = (Button) findViewById(R.id.start_alarm);

        start_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                calendar.set(Calendar.HOUR_OF_DAY, alarm_time_picker.getHour());
                calendar.set(Calendar.MINUTE, alarm_time_picker.getMinute());


                int hour = alarm_time_picker.getHour();
                int minute = alarm_time_picker.getMinute();

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }
                if (minute < 10) {
                    minute_string = "0" + String.valueOf(minute);
                }
                set_alarm_text("Alarm set to " + hour_string + ":" + minute_string);

            my_intent.putExtra("extra", "alarm on");

            pending_intent =PendingIntent.getBroadcast(Alarm .this,0,
            my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

            alarm_manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pending_intent);



        }
        });

        Button end_alarm = (Button) findViewById(R.id.end_alarm);
        end_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_alarm_text("Alarm off");
                alarm_manager.cancel(pending_intent);
                my_intent.putExtra("extra","alarm off");
                sendBroadcast(my_intent);
            }
        });




    }
    public void onBackPressed()
    {
        finish();
    }

    private void set_alarm_text(String output){
        update_text.setText(output);
    }

    public void setting(View view){
        Intent set = new Intent(Alarm.this, Settings.class);
        startActivity(set);
    }

    public void homed(View view){
        Intent set = new Intent(Alarm.this, MainActivity.class);
        //startActivity(set);
        finish();
    }
}
