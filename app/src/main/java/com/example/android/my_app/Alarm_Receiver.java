package com.example.android.my_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class Alarm_Receiver extends BroadcastReceiver {
    Context con;
    @RequiresApi(api = Build.VERSION_CODES.O)
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

            Uri song = Uri.parse("android.resource://"+context.getPackageName()+"/raw/baarish");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "0")
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle("Alarm")
                    .setContentText("Wake Up Buddy!!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(false)
                    .addAction(R.drawable.ic_alarm_off_black_24dp, "Turn Off Alarm", pendingIntent)
                    .setOngoing(false);


            int notificationId = 1;
            String channelId = "channel-01";
            String channelName = "Channel Name";
            int importance = NotificationManager.IMPORTANCE_HIGH;


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Alarm")
                    .setContentText("Wake Up!")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_alarm_off_black_24dp, "Turn Off Alarm", pendingIntent)
                    .setOngoing(false);

            NotificationManager nc = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                nc.createNotificationChannel(mChannel);
            }
            if (action.compareTo("Turn On") == 0) {
                //NotificationManagerCompat nc = NotificationManagerCompat.from(context);
                //nc.notify(alarmId, builder.build());
                nc.notify(notificationId, mBuilder.build());

            } else {
                service_intent.putExtra("RingAction", false);
                nc = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                //NotificationManagerCompat nc = NotificationManagerCompat.from(context);
                nc.cancel(alarmId);
            }
            //pass the extra string from Alarm to the ringtoneplaying service
            //service_intent.putExtra("extra",get_your_string);
            //context.startForegroundService(service_intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(service_intent);
            } else {
                context.startService(service_intent);
            }
        }
    }



}
