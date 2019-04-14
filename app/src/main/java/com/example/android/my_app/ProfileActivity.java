package com.example.android.my_app;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileName,profileAge,profileEmail,profileDeaf,profileFName,profileRelation,profileNumber;
    private Button profileUpdate,changePassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileName = (TextView) findViewById(R.id.tvProfileName);
        profileAge = (TextView) findViewById(R.id.tvProfileAge);
        profileEmail=(TextView) findViewById(R.id.tvProfileEmail);
        profileUpdate = (Button) findViewById(R.id.btnProfileUpdate);
        changePassword = (Button) findViewById(R.id.tvChangePassword);
        profileDeaf =(TextView) findViewById(R.id.tvProfileDeaf);
        profileFName =(TextView)findViewById(R.id.tvProfilefname);
        profileRelation= (TextView) findViewById(R.id.tvProfilerelation);
        profileNumber = (TextView) findViewById(R.id.tvProfilenumber);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profile Profile = dataSnapshot.getValue(profile.class);
                profileName.setText("Name :"+Profile.getUserName());
                profileAge.setText("Age :"+Profile.getUserAge());
                profileEmail.setText("Email:"+Profile.getUserEmail());
                profileDeaf.setText("User type:" +Profile.getUserDeaf());
                profileFName.setText("Familly member:"+Profile.getfName());
                profileRelation.setText("Relation with user:"+Profile.getfRelation());
                profileNumber.setText("Mobile no.:" +Profile.getfNo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();


            }
        });
        profileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,UpdateProfile.class));
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,changePassword.class));
            }
        });
    }
}
