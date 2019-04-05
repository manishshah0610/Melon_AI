package com.example.android.my_app;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDataset;
    List<String> notesDataset;
    File f;
    Context con;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View note;
        public MyViewHolder(View v) {
            super(v);
            note = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
//    public MyAdapter(String[] myDataset) {
    //      mDataset = myDataset;
    //}
    public MyAdapter(List<String> myDataset, Context c) {
        f = c.getFilesDir();
        notesDataset = myDataset;
        con = c;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_view,parent,false);
        // create a new view
        //TextView v = (TextView) LayoutInflater.from(parent.getContext())
        //       .inflate(R.layout.my_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    public String getTitleMapped(String actual_name)
    {
        StringBuilder title_value;
        FileReader fr;
        try {
            fr = new FileReader(f+"/myMapsFile");
            title_value = new StringBuilder();
            int i;
            while ((i = fr.read()) != -1) {
                title_value.append((char) i);
            }
            fr.close();
        }
        catch (IOException e){ Toast.makeText(con,"Failed to read from mymap",Toast.LENGTH_SHORT).show();
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
    // Replace the contents of a view (invoked by the layout manager)
    TextView note1;
    String fname;
    ImageButton removeButton;
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.textView.setText(mDataset[position]);
        note1 = holder.note.findViewById(R.id.note_name);
        removeButton = holder.note.findViewById(R.id.removeButton);
        fname = notesDataset.get(position);
        Log.d("fname" ,fname);
        String title = getTitleMapped(fname);
        note1.setText(title);
        note1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                String test_fname;// = note1.getText().toString();
                test_fname = fname.substring(6,fname.length()-4);
                int p = Integer.parseInt(test_fname);
                Intent intent = new Intent(con,NoteWindow.class);
                intent.putExtra("file_count",p);
                intent.putExtra("first_open",false);
                intent.putExtra("touch_position",pos);
                ((Activity)con).startActivityForResult(intent,2);
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if(pos>notesDataset.size())
                {Toast.makeText(con,"ERROR FINDING FILE",Toast.LENGTH_SHORT).show();return;}
                String fname = notesDataset.get(pos);
                int l =fname.length()-4;
                int fpos = Integer.parseInt(fname.substring(6,l));
                File fr = new File(f+"/notes_"+fpos+".txt");
                if(fr.exists())
                {
                    String title = getTitleMapped(fname);
                    deleteMap(fpos);
                    fr.delete();
                    notesDataset.remove(pos);
                    notifyItemRemoved(pos);
                    Toast.makeText(con,title+" Deleted",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(con,"ERROR MAP DOES NOT EXIST",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public Boolean checkMapFile()
    {
        File titleFilenameMap =new File(f+"/myMapsFile");
        if(!titleFilenameMap.isFile()) {
            try {
                boolean i = titleFilenameMap.createNewFile();
                if (i) {
                    Toast.makeText(con, "myMapFile just created", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
            catch (IOException e) {
                Toast.makeText(con, "Failed to open myMapsFile here 1", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    public void deleteMap(int pos)
    {
        Boolean i = checkMapFile();
        if(!i){return;}
        try {
            FileReader fr = new FileReader(f + "/myMapsFile");
            int j;
            StringBuilder sb = new StringBuilder();
            while((j=fr.read())!=-1)
            {
                sb.append((char)j);
            }
            fr.close();
            String full = sb.toString();
            String sub = getTitleMapped("notes_"+pos+".txt")+ "FILE_NUMBER_START"+pos+"FILE_NUMBER_END";
            j = full.indexOf(sub);
            StringBuilder fin = new StringBuilder();
            if(j>0)fin.append(full.substring(0,j));
            if(full.length()>j+sub.length())fin.append(full.substring(j+sub.length(),full.length()));
            FileWriter fw = new FileWriter(f+"/myMapsFile",false);
            fw.write(fin.toString());
            fw.close();
        }catch (IOException e)
        {
            Toast.makeText(con,"ERROR CHANGING MAP",Toast.LENGTH_SHORT).show();
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notesDataset.size();
    }
}