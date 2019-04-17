package com.example.android.my_app;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class feedback extends AppCompatActivity {
    private RatingBar rb;
    private EditText message;
    private Button send;
    private float value;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    String newUserName,newUserEmail,newUserAge,newProfileDeaf,newProfileFName,newProfileRelation,newProfileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        rb=(RatingBar) findViewById(R.id.ratingBar);
        message = (EditText) findViewById(R.id.etfeedback);
        send = (Button) findViewById(R.id.btnSend);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                value=v;
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        final DatabaseReference dbRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profile Pf= dataSnapshot.getValue(profile.class);

                newUserName=Pf.getUserName();
                newUserEmail = Pf.getUserEmail();
                newUserAge=Pf.getUserAge();
                newProfileDeaf=Pf.getUserDeaf();
                newProfileFName=Pf.getfName();
                newProfileRelation=Pf.getfRelation();
                newProfileNumber=Pf.getfNo();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(feedback.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();


            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message.getText().toString();
                profile Profile = new profile(newUserAge,newUserEmail,newUserName,newProfileDeaf,newProfileFName,newProfileRelation,newProfileNumber,value,msg);
                dbRef.setValue(Profile);
                Toast.makeText(feedback.this,"Feedback submitted",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
