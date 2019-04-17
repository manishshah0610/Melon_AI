package com.example.android.my_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
    MediaPlayer mp;


    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        isRunning = intent.getBooleanExtra("RingAction",true);
        //if there is no music playing
        if(isRunning) {
            //Log.e("there is music", "and you want start");
            Uri song = Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/raw/alarm_sound");
            media_song = MediaPlayer.create(this, R.raw.alarm_sound);

                   // );
            media_song.start();
            /*try{mp = new MediaPlayer();
                mp.setAudioStreamType(AudioManager.STREAM_ALARM);
                mp.setDataSource(getApplicationContext(),song);
                mp.prepare();
                mp.start();
            }
            catch (Exception e){
                Log.d("Alarm Sound Error","Cannot Play Alarm Sound",e);
            }*/
            vibrator.vibrate(300000000);
        }
        else {
            media_song.stop();
            //mp.stop();
            vibrator.cancel();
            media_song.reset();
        }
        /*
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
        */
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

