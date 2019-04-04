package com.example.android.my_app;

import android.app.Service;
import android.media.MediaPlayer;
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

        //if there is no music playing
        if(!this.isRunning && startId==1){
            Log.e("there is music","and you want start");
            media_song=MediaPlayer.create(this,R.raw.baarish);
            media_song.start();
            this.isRunning=true;
            startId=0;
        }
        else if(this.isRunning && startId==0){
            Log.e("there is music","and you want end");
            media_song.stop();
            media_song.reset();
            this.isRunning=false;
            startId=0;

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






        return START_NOT_STICKY;
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

