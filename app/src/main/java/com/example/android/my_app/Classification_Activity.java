package com.example.android.my_app;
import android.content.res.AssetFileDescriptor;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import org.tensorflow.lite.Interpreter;

//import com.example.sound_class.MFCC;


public class Classification_Activity extends Activity{
    private static final int SampleRate = 22050;
    private static final int Duration = 2970;
    private static final int RecordLength = (int)(SampleRate*Duration/1000);
    private static final String InputNodeName = "IP_input";
    private static final String OutputNode = "OP/Softmax";
    private static final String ModelFile = "model.tflite";
    private static final String LG = Classification_Activity.class.getSimpleName();
    private static final int REQUEST_RECORD_AUDIO = 13;
    private TextView OutText;
    public Button StartButton,StopButton;
    private static int foo = 0;
    short[] RecordingBuffer = new short[RecordLength];
    short[] RecordingBuffer_1 = new short[RecordLength];
    int RecordOffset = 0;
    boolean InfRec = true;
    private Thread RecordingThread;
    private Thread ClassificationThread;
    private Thread ClassificationThread_1;

    private final ReentrantLock RecordingBufferLock = new ReentrantLock();
    private Interpreter tflite;
    private static float[][] OUTPUT = new float[1][10];
    private static String res = "";
    private static float THRESH = (float)0.1;
    private static MFCC mf = new MFCC();
    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification_);
        StartButton = (Button)findViewById(R.id.Rec);
        StopButton = (Button)findViewById(R.id.stoprec);
        //StartButton.setText("Start Recording");
        //StopButton.setText("Stop Recording");
        //Activity activity = new Activity();
        try {
            Log.i("TR","try");
            tflite = new Interpreter(loadModel(Classification_Activity.this));
        }
        catch(Exception IOExceptio){
            Log.i("CTCH","catch");
        }
        /*ClassificationThread = new Thread(
                        new Runnable() {
                    @Override
                    public void run() {
                        Classify(foo);
                    }
                }
        );
        ClassificationThread_1 = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Classify(foo);
                    }
                }
        );
        /*RecordingThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Record();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );*/
        StartButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RecordingThread = new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Record();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );
                        try {
                            StartButton.setClickable(false);
                            StopButton.setClickable(true);
                            InfRec = true;
                            StartRecording();
                        } catch (InterruptedException e) {
                            Log.d("THREADIO","Thread Error");
                            e.printStackTrace();

                        }

                    }
                }
        );
        StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfRec = false;
                //OutText.setText("");
                StartButton.setClickable(true);
                StopButton.setClickable(false);
            }
        });
        OutText = (TextView)findViewById(R.id.text);
        //requestMicrophonePermission();

    }
    /*
    private void requestMicrophonePermission() {
        requestPermissions(
                new String[] {android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }
    */
    private MappedByteBuffer loadModel(Activity activity) throws IOException {
        Log.i("IO1","PASS");
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(ModelFile);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        Log.i("IO","PASSED");
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
    public synchronized void StartRecording() throws InterruptedException {
        /*if(RecordingThread!=null){
            return;
        }*/


        //InfRec = true;
        res = "";
        RecordOffset = 0;
        RecordingThread.start();
        //Record();
    }

    private void Record() throws InterruptedException {
        //Set the pproroty of the thread for recording audio
        InfRec = true;
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

        int BufferSize = AudioRecord.getMinBufferSize(SampleRate,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
        if(BufferSize == AudioRecord.ERROR || BufferSize == AudioRecord.ERROR_BAD_VALUE){
            BufferSize = SampleRate*2;
        }
        short[] AudioBuffer = new short[BufferSize/2];

        AudioRecord record =
                new AudioRecord(
                        MediaRecorder.AudioSource.DEFAULT,
                        SampleRate,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        BufferSize);

        record.startRecording();

        while(InfRec){
            int NumRead = record.read(AudioBuffer,0,AudioBuffer.length,(int)0);
            if (foo==0) {
                RecordingBufferLock.lock();
                if (RecordOffset + NumRead < RecordLength) {
                    System.arraycopy(AudioBuffer, 0, RecordingBuffer, RecordOffset, NumRead);
                    RecordOffset += NumRead;
                    RecordingBufferLock.unlock();
                } else {
                    foo = 1;
                    ClassificationThread = new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Classify(foo);
                                }
                            }
                    );
                    RecordOffset = 0;
                    RecordingBufferLock.unlock();
                    //startClassification(ClassificationThread,0);
                    //Classify(0);
                    Log.i("REC1","RECa");
                    ClassificationThread.start();
                    //Thread.sleep(6000);
                    /*this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OutText.setText(res);
                        }
                    });*/
                    //OutText.setText(res);
                    Thread.sleep(2500);
                }

            }else{
                RecordingBufferLock.lock();
                if (RecordOffset + NumRead < RecordLength) {
                    System.arraycopy(AudioBuffer, 0, RecordingBuffer_1, RecordOffset, NumRead);
                    RecordOffset += NumRead;
                    RecordingBufferLock.unlock();
                } else {
                    //InfRec = false;
                    foo = 0;
                    ClassificationThread_1 = new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Classify(foo);
                                }
                            }
                    );
                    RecordOffset = 0;
                    RecordingBufferLock.unlock();
                    //startClassification(ClassificationThread_1,1);
                    //Classify(1);
                    ClassificationThread_1.start();
                    Log.i("REC2","RECb");
                    Thread.sleep(2500);
                    //OutText.setText(res);
                    /*this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OutText.setText(res);
                        }
                    });*/
                }

            }
        }
        record.stop();
        record.release();
        Log.i("abc","Here");
        //OutText.setText(res);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OutText.setText("");
            }
        });


    }

    /* public synchronized void startClassification(Thread th, final int x){
         /*if (ClassificationThread!=null){
             return;
         }
         ClassificationThread = new Thread(
                 new Runnable() {
                     @Override
                     public void run() {
                         Classify();
                     }
                 }
         );
         //ClassificationThread.start();
         //Classify();
         th = new Thread(
                 new Runnable() {
                     @Override
                     public void run() {
                         Classify(x);
                     }
                 }
         );
         th.start();
     }*/
    private void Classify(int x){
       /*this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OutText.setText(res);
            }
        });*/
        double[] buff = new double[RecordLength];
        Log.d(LG,"Here");
        if (x==1){
            for(int i = 0;i<RecordLength;i++){
                buff[i] = RecordingBuffer[i]/65535.0;
            }
        }
        else{
            for(int i = 0;i<RecordLength;i++){
                buff[i] = RecordingBuffer_1[i]/65535.0;
            }
        }
        float[][][][] spec = mf.process(buff);

        /*float[][][][] feed = new float[1][128][128][1];
        for(int i = 0;i<128;i++){
            for(int j = 0;j<128;j++){
                feed[0][i][j][0] = (float)spec[i][j];
            }
        }*/
        Log.i("Before","Before detection");
        int mx = -1;
        float mx_score = 0;
        //tflite.getInputIndex(OutputNode);
        tflite.run(spec,OUTPUT);
        Log.i("after","After Detection" + Float.toString(OUTPUT[0][0]));
        for(int i = 0;i<10;i++){
            if (OUTPUT[0][i]>mx_score){
                mx = i;
                mx_score = OUTPUT[0][i];
            }
        }
        if (mx_score<THRESH){
            res = "Tell everyone to SHUT THE FUCK UP...It's very noisy here";
        }
        else{
            switch (mx){
                case 0:
                    res = "Air Conditioner";
                    break;
                case 1:
                    res = "Car Horn";
                    break;
                case 2:
                    res = "Children Playing";
                    break;
                case 3:
                    res = "dog barking";
                    break;
                case 4:
                    res = "Drilling";
                    break;
                case 5:
                    res = "Engine Idling";
                    break;
                case 6:
                    res = "Gun shot";
                    break;
                case 7:
                    res = "Jackhammer";
                    break;
                case 8:
                    res = "Siren";
                    break;
                case 9:
                    res = "Street music";
                    break;
            }
        }
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OutText.setText(res);
            }
        });
        //OutText.setText(res);
    }
}

/*
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Classification_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification_);
    }
}
*/
