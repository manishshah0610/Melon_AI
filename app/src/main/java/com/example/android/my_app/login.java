package com.example.android.my_app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class login extends AppCompatActivity {
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Name = (EditText) findViewById(R.id.etName);
        Password = (EditText) findViewById(R.id.etPassword);
        Info = (TextView) findViewById(R.id.tvInfo);
        Login = (Button) findViewById(R.id.btnLogin);
        userRegistration = (TextView) findViewById(R.id.tvRegister);
        Info.setText("No of attempts remaining: 5");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        forgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        if (user != null) {
            finish();
            startActivity(new Intent(login.this, MainActivity.class));
        }
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Name.getText().toString().isEmpty() || Password.getText().toString().isEmpty()) {
                    Toast.makeText(login.this, "Please enter all details to login", Toast.LENGTH_SHORT).show();
                } else {
                    validate(Name.getText().toString(), Password.getText().toString());
                }
            }
        });
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, register.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, password.class));
            }
        });
    }
    private void validate(String userName, String userPassword){
        progressDialog.setMessage("Logging in");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    checkEmailVerification();
                }
                else {
                    Toast.makeText(login.this,"Login failed",Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("No. of attempts remaining : "+counter);
                    progressDialog.dismiss();
                    if(counter==0)
                    {
                        Login.setEnabled(false);

                    }
                }
            }
        });
    }

   /* public boolean validate(String userName, String userPassword){
        //progressDialog.setMessage("Logging in");
        //progressDialog.show();
        boolean result;
        result= false;
        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    result = true;
                    //progressDialog.dismiss();
                    //checkEmailVerification();
                }
                else {
                    //Toast.makeText(login.this,"Login failed",Toast.LENGTH_SHORT).show();
                    counter--;
                    //Info.setText("No. of attempts remaining : "+counter);
                    //progressDialog.dismiss();
                    if(counter==0)
                    {
                       // Login.setEnabled(false);
                        result = false;
                    }
                }
            }
        });
      return result;
    }
    */

    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser =firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();
        if(emailflag)
        {
            finish();
            startActivity(new Intent(login.this,MainActivity.class));
        }
        else
        {
            Toast.makeText(login.this,"Verify your email first",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}
