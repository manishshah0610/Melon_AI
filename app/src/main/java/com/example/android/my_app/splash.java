
package com.example.android.my_app;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT =4000;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo= findViewById(R.id.logo);

        Animation maim = AnimationUtils.loadAnimation(this,R.anim.mysplashanimation);
        logo.startAnimation(maim);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent homeIntent = new Intent(splash.this,login.class);
                //to run the next activity
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
    }

