package com.example.android.my_app;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {
    private EditText newUserName,newUserAge,newProfileDeaf,newProfileFName,newProfileRelation,newProfileNumber;
    private Button save;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    String newUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        newUserAge = (EditText)findViewById(R.id.etAgeUpdate);
        newUserName=(EditText) findViewById(R.id.etNameUpdate);
        newProfileDeaf =(EditText)findViewById(R.id.etDeafUpdate);
        newProfileFName=findViewById(R.id.etfNameUpdate);
        newProfileRelation= findViewById(R.id.etfRelationUpdate);
        newProfileNumber=findViewById(R.id.etnumberUpdate);
        save = (Button) findViewById(R.id.btnSave);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profile Profile = dataSnapshot.getValue(profile.class);

                newUserName.setText(Profile.getUserName());
                newUserEmail = Profile.getUserEmail();
                newUserAge.setText(Profile.getUserAge());
                newProfileDeaf.setText(Profile.getUserDeaf());
                newProfileFName.setText(Profile.getfName());
                newProfileRelation.setText(Profile.getfRelation());
                newProfileNumber.setText(Profile.getfNo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateProfile.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = newUserName.getText().toString();
                String email = newUserEmail;
                String age = newUserAge.getText().toString();
                String deaf = newProfileDeaf.getText().toString();
                String fname = newProfileFName.getText().toString();
                String relation = newProfileRelation.getText().toString();
                String number = newProfileNumber.getText().toString();
                profile Profile = new profile(age,email,name,deaf,fname,relation,number);
                databaseReference.setValue(Profile);
                finish();
            }
        });

    }

}

