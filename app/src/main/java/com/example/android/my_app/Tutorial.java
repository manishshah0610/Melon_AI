package com.example.android.my_app;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Button;


public class Tutorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        WebView webView = (WebView)findViewById(R.id.youtubeview);
        webView.loadUrl("https://www.youtube.com/watch?v=Raa0vBXA8OQ");
    }
    public void onBackPressed()
    {
        finish();
    }
}
