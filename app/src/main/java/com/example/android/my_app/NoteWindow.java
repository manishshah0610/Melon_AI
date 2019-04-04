package com.example.android.my_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class NoteWindow extends AppCompatActivity {
    EditText note_title_view;
    EditText note_view;
    ImageButton save_note_button;
    int c;
    String oldTitle;
    String oldData;
    String newTitle;
    String newData;
    Boolean first_open;
    Integer touch_pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_window);

        Intent intent = getIntent();
        c = intent.getIntExtra("file_count",0);
        first_open = intent.getBooleanExtra("first_open",true);
        touch_pos = intent.getIntExtra("touch_position",0);
        note_title_view = findViewById(R.id.noteTitleValue);
        note_view = findViewById(R.id.editNote);
        save_note_button = (ImageButton)findViewById(R.id.saveButton);

        if(!first_open)
        {
            oldTitle=getTitleMapped("notes_"+c+".txt");
            oldData=fileDataRead();
            note_title_view.setText(oldTitle);
            note_view.setText(oldData);
        }
        else
        {
            oldTitle = "";
            oldData = "";
        }
        save_note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newData = note_view.getText().toString();
                fileDataSave(newData);
                oldData = newData;
                newTitle = note_title_view.getText().toString();
                if (first_open) mapSaveFirstTime(newTitle);
                else mapModifyTitle(oldTitle, newTitle);
                oldTitle = newTitle;
                first_open = false;
                Toast.makeText(getApplicationContext(), "Saved Note", Toast.LENGTH_SHORT);
            }
        });
    }
    OutputStreamWriter mapOut;

    public void onBackPressed() {
        newData = note_view.getText().toString();
        newTitle = note_title_view.getText().toString();
        if (oldData == null) oldData = "";
        if (oldTitle == null) oldTitle = "";

        if ((newData.equals(oldData) && newTitle.equals(oldTitle) && first_open) || (!newData.equals(oldData)) || (!newTitle.equals(oldTitle))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(note_view.getContext());
            builder.setMessage("Save Note?");
            builder.setPositiveButton(
                    "YES",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            fileDataSave(newData);
                            if (first_open) {
                                mapSaveFirstTime(newTitle);
                            } else mapModifyTitle(oldTitle, newTitle);
                            oldData = newData;
                            oldTitle = newTitle;
                            first_open = false;
                            Toast.makeText(getApplicationContext(), "Saved Note", Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();

                            Intent intent = new Intent();
                            intent.putExtra("SAVED", true);
                            intent.putExtra("FILE_NO", c);
                            intent.putExtra("touch_position", touch_pos);
                            //data.setData(Uri.parse("CHANGED"));
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
            builder.setNegativeButton(
                    "NO",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            Intent intent =new Intent();
                            intent.putExtra("SAVED",false);
                            intent.putExtra("FILE_NO",c);
                            intent.putExtra("touch_position",touch_pos);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
            builder.setNeutralButton(
                    "STAY",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }
            );
            builder.show();
        } else {
            Intent intent =new Intent();
            intent.putExtra("SAVED",true);
            intent.putExtra("FILE_NO",c);
            intent.putExtra("touch_position",touch_pos);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
    public String getTitleMapped(String actual_name)
    {
        StringBuilder title_value;
        FileReader fr;
        try {
            fr = new FileReader(getFilesDir()+"/myMapsFile");
            title_value = new StringBuilder();
            int i;
            while ((i = fr.read()) != -1) {
                title_value.append((char) i);
            }
            fr.close();
        }
        catch (IOException e){ Toast.makeText(getApplicationContext(),"Failed to read from mymap",Toast.LENGTH_SHORT).show();
            return null;
        }
        String title = title_value.toString();
        int l = actual_name.length()-4;
        String tem =actual_name.substring(6,l);
        String sub = "FILE_NUMBER_START"+tem+"FILE_NUMBER_END";
        int indexStart = title.indexOf(sub);
        int indexEnd = indexStart;
        boolean flag = false;
        while((indexStart-15)>=0)
        {
            if(title.substring(indexStart-15,indexStart).equals("FILE_NUMBER_END"))
            {
                flag = true;
                break;
            }
            indexStart--;
        }
        if(!flag){indexStart=0;}
        title = title.substring(indexStart,indexEnd);
        if(indexStart==indexEnd){title="";}
        return title;
    }

    public Boolean fileDataSave(String data)
    {
        if(data==null)data="";
        FileOutputStream fOut;
        try {
            fOut = openFileOutput("notes_"+ c + ".txt", MODE_PRIVATE);
            fOut.write(data.getBytes());
            fOut.close();
        }
        catch(IOException e){
            Toast.makeText(getApplicationContext(),"ERROR SAVING NOTE",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public String fileDataRead()
    {
        StringBuilder note_data = new StringBuilder();
        String note_t = "";
        try {
            FileReader fr = new FileReader(getFilesDir() + "/notes_" + c + ".txt");
            int i;
            while((i=fr.read())!=-1)
            {
                note_data.append((char)i);
            }
            fr.close();
            note_t = note_data.toString();
            if(note_t==null)note_t="";
        }
        catch (IOException e)
        {
            Toast.makeText(getApplicationContext(),"ERROR READING NOTE",Toast.LENGTH_SHORT).show();
        }
        return note_t;
    }
    public Boolean checkMapFile()
    {
        File titleFilenameMap =new File(getFilesDir()+"myMapsFile");
        if(!titleFilenameMap.isFile()) {
            try {
                boolean i = titleFilenameMap.createNewFile();
                if (i) {
                    Toast.makeText(getApplicationContext(), "Failed to open myMapFile", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Failed to open myMapsFile here 1", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    public Boolean mapSaveFirstTime(String Title)
    {
        if(Title==null)Title="";
        if(!checkMapFile())
        {return false;}
        try{
            mapOut = new OutputStreamWriter(save_note_button.getContext().openFileOutput("myMapsFile",MODE_APPEND));
            mapOut.write(Title);
            mapOut.write("FILE_NUMBER_START");
            mapOut.write(c + "FILE_NUMBER_END");
            mapOut.close();
        }
        catch(IOException e){
            Toast.makeText(getApplicationContext(), "Failed to open myMapsFile here", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public Boolean mapModifyTitle(String oldT,String newT)
    {
        if(oldT==null)oldT="";
        if(newT==null)newT="";
        if(!checkMapFile())
        {return false;}
        FileReader fr;
        FileWriter fw;
        try {
            fr = new FileReader(getFilesDir() + "/myMapsFile");
            StringBuilder maps = new StringBuilder();
            int i;
            while ((i = fr.read()) != -1) {
                maps.append((char) i);
            }
            fr.close();
            String m = maps.toString();
            int pos = m.indexOf("FILE_NUMBER_START" + c + "FILE_NUMBER_END");
            StringBuilder finalMap = new StringBuilder();
            int x=oldT.length();
            if((pos - x)!= 0)
                finalMap.append(maps.substring(0, pos - x));
            finalMap.append(newT);
            finalMap.append(maps.substring(pos, maps.length()));
            String fm = finalMap.toString();
            fw = new FileWriter(getFilesDir() + "/myMapsFile", false);
            fw.write(fm);
            fw.close();
            return true;
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            Log.d("ERROR HERE: ", "don't know why", e);
            return false;
        }
    }
}