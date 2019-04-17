package com.example.android.my_app;

import android.content.Intent;
import android.sax.StartElementListener;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class register extends AppCompatActivity {


    private EditText userName,userPassword,userEmail,userAge,userDeaf,fName,fRelation,fNo;

    private Button regButton;

    private TextView userLogin;

    private FirebaseAuth firebaseAuth;

    String name,password,email,age,deaf,familyMember,relation,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIviews();
        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendEmailVerification();
                            } else
                            {
                                Toast.makeText(register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(register.this,login.class));

            }
        });
    }
    private void setupUIviews(){
        userName = (EditText) findViewById(R.id.etUserName);
        userPassword = (EditText) findViewById(R.id.etUserPassword);
        userEmail= (EditText)findViewById(R.id.etUserEmail);
        regButton = (Button) findViewById(R.id.btnRegister);
        userLogin = (TextView) findViewById(R.id.tvUserLogin);
        userAge = (EditText) findViewById(R.id.etAge);
        userDeaf = (EditText) findViewById(R.id.etdeaf);
        fName = (EditText) findViewById(R.id.etfName);
        fRelation = (EditText) findViewById(R.id.etRelation);
        fNo = (EditText) findViewById(R.id.etMobile);
    }


    public boolean validate()
    {

        Boolean result = false;

        name = userName.getText().toString();

        password = userPassword.getText().toString();

        email = userEmail.getText().toString();

        age = userAge.getText().toString();

        deaf = userDeaf.getText().toString();

        familyMember = fName.getText().toString();

        relation = fRelation.getText().toString();

        number = fNo.getText().toString();

        final String EMAIL_PATTERN ="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        final Matcher matcher = pattern.matcher(email);

        boolean isEmailValid=matcher.matches();


        if(name.isEmpty() || password.isEmpty() || email.isEmpty() || age.isEmpty() || !isEmailValid) {

            if (!isEmailValid) {


                Toast.makeText(this, "Enter valid email id", Toast.LENGTH_LONG).show();
            }

            else {

                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT);

                //startActivity(new Intent(register.this, login.class));

            }

        }

        else
        {
            if(password.length()<6)
                Toast.makeText(this,"Password should be of atleast 6 characters",Toast.LENGTH_LONG).show();
            else if(Integer.parseInt(age) <= 0) {
                Toast.makeText(this,"Age can't be non-positive number",Toast.LENGTH_LONG).show();
            } else
                result =true;
        }
        return result;
    }

/*    ///VALIDATE FUNCTION FOR TESTING-----------------------------------------------------------
    public boolean validate()
    {
        Boolean result = false;
        String n = this.name;
       String p = this.password;
        String e = this.email;
        String a = this.age;

        final String EMAIL_PATTERN ="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);
        boolean isEmailValid=matcher.matches();
        if(name.isEmpty() || password.isEmpty() || email.isEmpty() || age.isEmpty() || !isEmailValid) {

            result=false;
        }
        else
        {
            if(password.length()<6)
                result = false;
            else if(Integer.parseInt(age) <= 0) {
                result = false;
                //Toast.makeText(this,"Age can't be non-positive number",Toast.LENGTH_LONG).show();
            } else
                result =true;
        }
        return result;
    }
//    ----------------------------------------------------------------------------------------

*/


    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserData();
                        Toast.makeText(register.this, "Registratioin sucessfull,Verification mail sent", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(register.this, login.class));
                    } else {
                        Toast.makeText(register.this, "Please enter Valid email id", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendUserData()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        profile Profile = new profile(age,email,name,deaf,familyMember,relation,number);
        myRef.setValue(Profile);
    }
}
