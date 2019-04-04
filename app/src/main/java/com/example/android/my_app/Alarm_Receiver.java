package com.example.android.my_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){

        Log.e("We are in the receiver","Yay");

        //fetch extra string
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("What is the key",get_your_string);


        Intent service_intent=new Intent(context, RingtonePlayingService.class);

        //pass the extra string from Alarm to the ringtoneplaying service
        service_intent.putExtra("extra",get_your_string);
        context.startService(service_intent);
    }

}
