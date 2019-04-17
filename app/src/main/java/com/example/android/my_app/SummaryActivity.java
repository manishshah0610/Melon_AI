package com.example.android.my_app;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class SummaryActivity extends AppCompatActivity {

    private TextView txvResult;
    ImageButton speak;
    boolean startListen = true;
    SpeechRecognizer sr;
    Intent intent;
    int j =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        txvResult = findViewById(R.id.txvResult);
        speak = findViewById(R.id.btnSpeak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechRec();
            }
        });
        sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra("android.speech.extra.DICTATION_MODE",true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);

        sr.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
            }
            @Override
            public void onEndOfSpeech() {
                j++;
                Log.d("Called:", Integer.toString(j));
            }


            @Override
            public void onError(int i) {
                Toast.makeText(getApplicationContext(),"ERROR1",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle bundle) {
                Log.d("Not Called:", Integer.toString(j));

                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches!=null) {
                    Log.d("Matches",matches.get(0));
                    improved_summary s1 = new improved_summary(matches.get(0),getApplicationContext());
                    String sum = s1.returnSummary();
                    Toast.makeText(getApplicationContext(),Integer.toString(sum.length()),Toast.LENGTH_SHORT).show();
                    //Log.i("SUMMMM",Integer.toString(sum.length()));
                    if(sum==null ||sum.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "ERROR2", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Log.i("SUMMARY","HEREHREREFFF");
                        txvResult.setText(sum);
                    }

                    startListen = true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "ERROR3", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }
    public void speechRec() {
        if(startListen)
        {
            sr.startListening(intent);
            startListen = false;
        }
        else{
            sr.stopListening();
            startListen = true;
        }

    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getApplicationContext(),"h1",Toast.LENGTH_SHORT).show();

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    Toast.makeText(getApplicationContext(),"h2",Toast.LENGTH_SHORT).show();

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txvResult.setText(result.get(0));
                    improved_summary s1 = new improved_summary(result.get(0),getApplicationContext());
                    String sum = s1.returnSummary();
                    if(sum==null ||sum.isEmpty())
                        Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
                    else
                    {Toast.makeText(getApplicationContext(),sum,Toast.LENGTH_SHORT).show();
                        txvResult.setText(sum);}
                    //improved_summary summary = new improved_summary();
                }
                break;
        }
    }
    */
}
