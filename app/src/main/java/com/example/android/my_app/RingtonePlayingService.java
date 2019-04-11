package com.example.android.my_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.security.Provider;
import java.util.List;

public class RingtonePlayingService extends Service {



    MediaPlayer media_song;
    boolean isRunning;
    Vibrator vibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        //fetch the extra string values
        String state = intent.getExtras().getString("extra");

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        //if there is no music playing
        if(!this.isRunning && startId==1){
            Log.e("there is music","and you want start");
            media_song=MediaPlayer.create(this,R.raw.baarish);
            media_song.start();
            vibrator.vibrate(30000);
            this.isRunning=true;
            startId=0;

            //set up the notification manager
           // NotificationManager notify_manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            //set the intent that goes to Main Activity
          //  Intent intent_alarm_activity = new Intent(this.getApplicationContext(),Alarm.class);

            //set up a pending intent
            //PendingIntent pending_intent_alarm = PendingIntent.getActivity(this,0,intent_alarm_activity,0) ;


            //notification parameters

            //put the notification here
            //Notification notification_popup = new Notification.Builder(this).setContentTitle("An alarm is going off")
              //      .setContentText("CLick me!")
                //    .setContentIntent(pending_intent_alarm)
                  //  .setAutoCancel(true)
                   // .build();

            //set up notification call command

        }
        else if(this.isRunning && startId==0){
            Log.e("there is music","and you want end");
            media_song.stop();
            vibrator.cancel();
            media_song.reset();
            this.isRunning=false;
            startId=0;//

        }
        else if(!this.isRunning && startId==0){
            Log.e("there is music","and you want end");
            this.isRunning=false;
            startId=0;

        }
        else if(this.isRunning && startId==1){
            Log.e("there is music","and you want start");
            this.isRunning=true;
            startId=1;

        }
        else{

        }




        return START_NOT_STICKY; //tells the system not to bother to restart the service, even when it has sufficient memory.
    }

    @Override
    public void onDestroy() {
        //Tell the users, we stopped
        Log.e("on Destroy called","ta da");
        super.onDestroy();
        this.isRunning=false;
        //Toast.makeText(this, "on Destroy called", Toast.LENGTH_SHORT).show();
    }



}

