package com.example.android.my_app;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Notes extends AppCompatActivity {

    private TextView mTextMessage;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    List<String> myNotes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        File titleFilenameMap =new File(getFilesDir()+"/myMapsFile");
        if(!titleFilenameMap.isFile())
        {
            try{
                titleFilenameMap.createNewFile();
            }
            catch (IOException e)
            {
                Toast.makeText(recyclerView.getContext(),"Map file failed to create",Toast.LENGTH_SHORT).show();
            }
        }
        File defdir = getFilesDir();
        File files[] = defdir.listFiles();
        final int count_files = files.length;

        String[] myDataset = new String[count_files];
        for(int i=0;i<count_files;i++)
        {
            myDataset[i] = files[i].getName();
            String fname = files[i].getName();
            int l = fname.length();
            if(l>4 && fname.charAt(l-1)=='t' && fname.charAt(l-2)=='x' && fname.charAt(l-3)=='t' && fname.charAt(l-4)=='.')
            {
                myNotes.add(fname);
            }
        }
        mAdapter = new MyAdapter(myNotes,Notes.this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.getItemViewType(1);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {
                                                          Intent intent = new Intent(Notes.this,NoteWindow.class);
                                                          intent.putExtra("file_count",myNotes.size());
                                                          intent.putExtra("first_open",true);
                                                          startActivityForResult(intent,1);
                                                      }
                                                  }
        );
    }
    Integer file_pos;
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Intent intent = data;
                Boolean changed = intent.getBooleanExtra("SAVED", false);
                if (changed) {
                    file_pos = intent.getIntExtra("FILE_NO", 0);
                    String p = fileDataRead();
                    if (p == null) {
                        Log.d("ERROR HERE: ","WHY");}
                    else{
                        myNotes.add("notes_"+file_pos+".txt");}
                    mAdapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Intent intent = data;
                Boolean changed = intent.getBooleanExtra("SAVED", false);
                if (changed) {
                    file_pos = intent.getIntExtra("touch_position", 0);
                    mAdapter.notifyItemChanged(file_pos);
                }
            }
        }
    }
    public String fileDataRead()
    {
        StringBuilder note_data = new StringBuilder();
        String note_t = "";
        try {
            FileReader fr = new FileReader(getFilesDir() + "/notes_" + file_pos + ".txt");
            int i;
            while((i=fr.read())!=-1)
            {
                note_data.append((char)i);
            }
            fr.close();
            note_t = note_data.toString();
        }
        catch (IOException e)
        {
            Toast.makeText(getApplicationContext(),"ERROR READING NOTE",Toast.LENGTH_SHORT).show();
        }
        return note_t;
    }
}