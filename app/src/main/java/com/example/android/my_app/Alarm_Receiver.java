package com.example.android.my_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class Alarm_Receiver extends BroadcastReceiver {
    Context con;
    @Override
    public void onReceive(Context context, Intent intent){

        con = context;
        Intent service_intent=new Intent(context, RingtonePlayingService.class);
        int alarmId = intent.getIntExtra("AlarmId", 0);


        boolean alarm_kill = intent.getBooleanExtra("END_ALARM",false);
        if(alarm_kill)
        {
            NotificationManagerCompat nc = NotificationManagerCompat.from(context);
            nc.cancel(alarmId);

            service_intent.putExtra("RingAction",false);
            context.startService(service_intent);
        }
        else {
            String action = intent.getStringExtra("AlarmAction");
            String time = intent.getStringExtra("AlarmTime");

            Intent endAlarmIntent = new Intent(context, Alarm_Receiver.class);
            endAlarmIntent.putExtra("END_ALARM", true);
            endAlarmIntent.putExtra("AlarmId",alarmId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, endAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            service_intent.putExtra("RingAction", true);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "0")
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle("Alarm")
                    .setContentText("Wake Up Buddy!!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .addAction(R.drawable.ic_alarm_off_black_24dp, "Turn Off Alarm", pendingIntent);

            if (action.compareTo("Turn On") == 0) {
                NotificationManagerCompat nc = NotificationManagerCompat.from(context);
                nc.notify(alarmId, builder.build());
            } else {
                service_intent.putExtra("RingAction", false);
                NotificationManagerCompat nc = NotificationManagerCompat.from(context);
                nc.cancel(alarmId);
            }
            //pass the extra string from Alarm to the ringtoneplaying service
            //service_intent.putExtra("extra",get_your_string);
            context.startService(service_intent);
        }
    }



}
