package com.example.android.my_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class myAlarms extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<String> myAlarmsList = new ArrayList<String>();
    Set<Integer> availableID = new HashSet<Integer>();
    List<Boolean> alarmsState = new ArrayList<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_alarms);
        recyclerView = findViewById(R.id.my_recycler_view_2);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        File titleFilenameMap =new File(getFilesDir()+"/myAlarmsFile");
        for(int i=0;i<1000;i++)availableID.add(i);
        if(!titleFilenameMap.isFile())
        {
            try{
                titleFilenameMap.createNewFile();
            }
            catch (IOException e)
            {
                Toast.makeText(recyclerView.getContext(),"Alarms file failed to create",Toast.LENGTH_SHORT).show();
            }
        }
        try {
            FileReader fr = new FileReader(getFilesDir() + "/myAlarmsFile");
            int i,j=0;
            StringBuilder s = new StringBuilder();
            while((i=fr.read())!=-1)
            {
                s.append((char)i);
                j++;
                if(i=='/')
                {
                    j=0;
                    String ss = s.toString();
                    myAlarmsList.add(ss);
                    if(ss.substring(ss.length()-2,ss.length()-1).equals("0"))
                        alarmsState.add(false);
                    else alarmsState.add(true);
                    Integer p = Integer.parseInt(ss.substring(5,ss.length()-3));
                    availableID.remove(p);
                    s = new StringBuilder();
                }
            }
            fr.close();
        }
        catch(IOException e)
        {
            Log.d("AlarmFile:","Error in Alarm File Creation",e);
            Toast.makeText(getApplicationContext(),"Problem Reading from alarms",Toast.LENGTH_SHORT).show();
        }
        mAdapter = new MyAdapter2(myAlarmsList,myAlarms.this,availableID,alarmsState);
        recyclerView.setAdapter(mAdapter);
        mAdapter.getItemViewType(1);

        findViewById(R.id.fab_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myAlarms.this,Alarm2.class);
                int k = 0;
                for(int j:availableID)
                {
                    k=j;
                    break;
                }
                intent.putExtra("alarm_ID",k);
                intent.putExtra("first_open",true);
                intent.putExtra("alarm_State",true);
                startActivityForResult(intent,1);
            }
        });
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String temp = data.getStringExtra("Alarm_String");
                availableID.remove(Integer.parseInt(temp.substring(5,temp.length()-3)));
                myAlarmsList.add(temp);
                alarmsState.add(true);
                mAdapter.notifyDataSetChanged();
            }
        }
        else{
            if(resultCode == RESULT_OK){
                int pos = data.getIntExtra("pos",0);
                String temp = data.getStringExtra("Alarm_String");
                myAlarmsList.set(pos,temp);
                alarmsState.set(pos,true);
                mAdapter.notifyDataSetChanged();
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
