package com.example.android.my_app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import static android.content.Context.ALARM_SERVICE;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {

    List<String> alarmsDataset;
    File f;
    Context con;
    Set<Integer> availableIDs;
    List<Boolean> alarmsOn;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View alarm;
        public MyViewHolder(View v) {
            super(v);
            alarm = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
//    public MyAdapter(String[] myDataset) {
    //      mDataset = myDataset;
    //}
    public MyAdapter2(List<String> myDataset, Context c, Set<Integer> availableID,List<Boolean> alarmsState) {
        f = c.getFilesDir();
        alarmsDataset = myDataset;
        con = c;
        availableIDs = availableID;
        alarmsOn = alarmsState;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_view,parent,false);

        MyAdapter2.MyViewHolder vh = new MyAdapter2.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    TextView alarm1;
    String alarmPos;
    ImageButton removeButton;
    Switch alarmSwitch;
    Intent my_intent;
    PendingIntent pendingIntent;
    AlarmManager alarm_manager;

    @Override
    public void onBindViewHolder(final MyAdapter2.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.textView.setText(mDataset[position]);
        alarm1 = holder.alarm.findViewById(R.id.note_name);
        removeButton = holder.alarm.findViewById(R.id.removeButton);
        alarmSwitch = holder.alarm.findViewById(R.id.AlarmStatusSwitch);
        alarmPos = alarmsDataset.get(position);
        String temp = alarmPos.substring(0,2)+":"+alarmPos.substring(2,4);
        alarm1.setText(temp);
        alarmSwitch.setChecked(alarmsOn.get(position));

        alarm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Intent intent = new Intent(con,Alarm2.class);
                intent.putExtra("alarm_ID",Integer.parseInt(alarmPos.substring(5,alarmPos.length()-3)));
                intent.putExtra("alarm_string",alarmPos);
                intent.putExtra("first_open",false);
                intent.putExtra("touch_position",pos);
                ((Activity)con).startActivityForResult(intent,2);
                alarmSwitch.setChecked(true);
                alarmsOn.set(pos,true);

            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                String alarm = alarmsDataset.get(pos);
                my_intent = new Intent(con,Alarm_Receiver.class);
                int AlarmId = Integer.parseInt(alarm.substring(5,alarm.length()-3));
                pendingIntent = PendingIntent.getBroadcast(con,AlarmId,
                        my_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                pendingIntent.cancel();
                alarm_manager=(AlarmManager)con.getSystemService(ALARM_SERVICE);
                alarm_manager.cancel(pendingIntent);
                alarmRemoveFromFile(alarm);
                alarmsDataset.remove(pos);
                alarmsOn.remove(pos);
                Toast.makeText(con,"REMOVED!!",Toast.LENGTH_SHORT).show();
                notifyItemRemoved(pos);

            }
        });
        alarmSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alarmSwitch.isChecked()){
                    int pos = holder.getAdapterPosition();
                    String alarm = alarmsDataset.get(pos);
                    alarmsOn.set(pos,true);
                    updateAlarmState(alarm,true);

                    my_intent = new Intent(con,Alarm_Receiver.class);
                    int AlarmId = Integer.parseInt(alarm.substring(5,alarm.length()-3));

                    pendingIntent = PendingIntent.getBroadcast(con,AlarmId,
                            my_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    pendingIntent.cancel();
                    alarm_manager=(AlarmManager)con.getSystemService(ALARM_SERVICE);
                    alarm_manager.cancel(pendingIntent);

                    my_intent = new Intent(con,Alarm_Receiver.class);
                    my_intent.putExtra("AlarmAction","Turn On");
                    pendingIntent = PendingIntent.getBroadcast(con,AlarmId,
                            my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(alarm.substring(0,2)));
                    calendar.set(Calendar.MINUTE,Integer.parseInt(alarm.substring(2,4)));
                    long diff = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
                    if(diff>0)calendar.add(Calendar.HOUR_OF_DAY,24);
                    alarm_manager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                    notifyItemChanged(pos);
                }
                else{
                    int pos = holder.getAdapterPosition();
                    String alarm = alarmsDataset.get(pos);
                    alarmsOn.set(pos,false);
                    updateAlarmState(alarm,false);

                    my_intent = new Intent(con,Alarm_Receiver.class);
                    int AlarmId = Integer.parseInt(alarm.substring(5,alarm.length()-3));
                    pendingIntent = PendingIntent.getBroadcast(con,AlarmId,
                            my_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    pendingIntent.cancel();
                    alarm_manager=(AlarmManager)con.getSystemService(ALARM_SERVICE);
                    alarm_manager.cancel(pendingIntent);
                }
            }
        });
    }
    public void updateAlarmState(String alarm,boolean state) {
        try {
            FileReader fr = new FileReader(f + "/myAlarmsFile");
            StringBuilder sr = new StringBuilder();
            int j;
            while((j=fr.read())!=-1)
            {
                sr.append((char)j);
            }
            fr.close();
            String s = sr.toString();
            j = s.indexOf(alarm);
            StringBuilder finalV = new StringBuilder(s);
            if(state==true)
               finalV.setCharAt(j+alarm.length()-2,'1');
            else
                finalV.setCharAt(j+alarm.length()-2,'0');

            FileWriter fw = new FileWriter(f+"/myAlarmsFile",false);
            fw.write(finalV.toString());
            fw.close();
        }
        catch (IOException e){
            Log.d("Error Reading Alarms","WHYY",e);
            Toast.makeText(con,"Error Reading Alarm", Toast.LENGTH_SHORT).show();
        }
    }
    public void alarmRemoveFromFile(String alarm){
        try {
            FileReader fr = new FileReader(f + "/myAlarmsFile");
            StringBuilder sr = new StringBuilder();
            int j;
            while((j=fr.read())!=-1)
            {
                sr.append((char)j);
            }
            fr.close();
            String s = sr.toString();
            j = s.indexOf(alarm);
            StringBuilder finalV = new StringBuilder();
            finalV.append(s.substring(0,j));
            finalV.append(s.substring(j+alarm.length()));
            FileWriter fw = new FileWriter(f+"/myAlarmsFile",false);
            fw.write(finalV.toString());
            fw.close();
        }
        catch (IOException e){
            Log.d("Error Reading Alarms","WHYY",e);
            Toast.makeText(con,"Error Reading Alarm", Toast.LENGTH_SHORT).show();
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return alarmsDataset.size();
    }

}
