package com.example.android.my_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Alarm2 extends AppCompatActivity {

    AlarmManager alarm_manager;
    TimePicker alarm_time_picker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    Calendar calendar;
    Intent my_intent;
    int AlarmId;
    String alarm_time;
    boolean first_open;
    Intent intent2;
    int initial_hour;
    int initial_minute;
    String newTime;
    String oldTime;
    boolean isValid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm2);
        this.context = getApplicationContext();

        intent2 = getIntent();
        AlarmId = intent2.getIntExtra("alarm_ID",0);
        first_open = intent2.getBooleanExtra("first_open",true);

        alarm_manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarm_time_picker=(TimePicker)findViewById(R.id.time_picker);
        update_text = (TextView)findViewById(R.id.alarm_update);
        calendar = Calendar.getInstance();


        if(!first_open){
        alarm_time = getIntent().getStringExtra("alarm_string");
        initial_hour = Integer.parseInt(alarm_time.substring(0,2));
        initial_minute = Integer.parseInt(alarm_time.substring(2,4));
        alarm_time_picker.setHour(initial_hour);
        alarm_time_picker.setMinute(initial_minute);
        oldTime = alarm_time.substring(0,2)+alarm_time.substring(2,4)+" "+AlarmId+" 1/";
        //calendar.set(Calendar.MINUTE,initial_minute);
        //calendar.set(Calendar.HOUR_OF_DAY,initial_hour);
        }



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
                if(hour<10){
                    hour_string = "0" + hour_string;
                }
                if(minute<10){
                    minute_string = "0" + minute_string;
                }
                boolean i = checkAlarmExists(hour_string+minute_string);
                if(i) {
                    Toast.makeText(context,"An alarm with given time already exists!",Toast.LENGTH_SHORT).show();
                    return;
                }
                long diff = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
                if(diff>0)calendar.add(Calendar.HOUR_OF_DAY,24);
                set_alarm_text("Alarm set to " + hour_string + ":" + minute_string);
                isValid = true;
                if(first_open)
                {
                    my_intent = new Intent(context,Alarm_Receiver.class);
                    my_intent.putExtra("AlarmAction","Turn On");
                    my_intent.putExtra("AlarmId",AlarmId);
                    my_intent.putExtra("AlarmTime",hour_string+minute_string);
                    pending_intent = PendingIntent.getBroadcast(context,AlarmId,
                            my_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    alarm_manager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pending_intent);
                    addAlarmToFile(hour_string+minute_string+" "+AlarmId+" 1/");
                    first_open = false;
                    oldTime = hour_string + minute_string + " " + AlarmId + " 1/";
                }
                else
                {
                    my_intent = new Intent(context,Alarm_Receiver.class);
                    pending_intent = PendingIntent.getBroadcast(context,AlarmId,
                            my_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    pending_intent.cancel();
                    alarm_manager.cancel(pending_intent);

                    my_intent = new Intent(context,Alarm_Receiver.class);
                    my_intent.putExtra("AlarmAction","Turn On");
                    my_intent.putExtra("AlarmId",AlarmId);
                    my_intent.putExtra("AlarmTime",hour_string+minute_string);
                    pending_intent = PendingIntent.getBroadcast(context,AlarmId,
                            my_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    alarm_manager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pending_intent);
                    newTime = hour_string+minute_string+" "+AlarmId+" 1/";
                    modifyAlarmFile(oldTime,newTime);
                    oldTime = newTime;
                    first_open = false;
                }
            }
        });

    }
    public boolean checkAlarmExists(String alarm)
    {
        try {
            FileReader fr = new FileReader(getFilesDir() + "/myAlarmsFile");
            StringBuilder sr = new StringBuilder();
            int j;
            while((j=fr.read())!=-1)
            {
                sr.append((char)j);
            }
            fr.close();
            String s = sr.toString();
            if(s.indexOf(alarm)==-1)return false;
            else return true;
        }
        catch (IOException e){
            Log.d("Error Reading Alarms","WHYY",e);
            Toast.makeText(context,"Error Reading Alarm", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
    public void addAlarmToFile(String s){
        try {
            FileWriter fr = new FileWriter(getFilesDir() + "/myAlarmsFile",true);
            fr.write(s);
            fr.close();
        }
        catch (IOException e) {
            Log.d("Error Writing To Alarm","Why",e);
            Toast.makeText(context,"Error Writing Alarm", Toast.LENGTH_SHORT).show();
        }
    }
    public void modifyAlarmFile(String oldTime,String newTime)
    {
        try {
            FileReader fr = new FileReader(getFilesDir() + "/myAlarmsFile");
            StringBuilder sr = new StringBuilder();
            int j;
            while((j=fr.read())!=-1)
            {
                sr.append((char)j);
            }
            fr.close();
            String s = sr.toString();
            s.replaceFirst(oldTime,newTime);
            FileWriter fw = new FileWriter(getFilesDir()+"/myAlarmsFile",false);
            fw.write(s);
            fw.close();
        }
        catch (IOException e) {
            Log.d("Error Modifying Alarm","Why This",e);
            Toast.makeText(context,"Error Modifying Alarm", Toast.LENGTH_SHORT).show();
        }
    }
    public void onBackPressed()
    {
        int hour = alarm_time_picker.getHour();
        int minute = alarm_time_picker.getMinute();
        String hour_string = String.valueOf(hour);
        String minute_string = String.valueOf(minute);
        if(hour<10){
            hour_string = "0" + hour_string;
        }
        if(minute<10){
            minute_string = "0" + minute_string;
        }
        newTime = hour_string+minute_string;

        Intent intent = new Intent();
        intent.putExtra("Alarm_String",newTime+" "+AlarmId+" 1/");
        if(isValid)
        setResult(RESULT_OK,intent);
        else setResult(RESULT_CANCELED,intent);
        finish();
    }

    private void set_alarm_text(String output){
        update_text.setText(output);
    }

    public void setting(View view){
        Intent set = new Intent(Alarm2.this, Settings.class);
        startActivity(set);
    }

    public void homed(View view){
        Intent set = new Intent(Alarm2.this, MainActivity.class);
        //startActivity(set);
        finish();
    }
}
