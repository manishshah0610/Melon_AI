package com.example.android.my_app;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoCall extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {

    private static String API_KEY = "46307562";
    private static String SESSION_ID = "2_MX40NjMwNzU2Mn5-MTU1NTA5NDk4ODQxNX5COUtGM1d5V3JiWmhpaUFkQmt5TkVCa01-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjMwNzU2MiZzaWc9YTAwNTc3ZmNkZWFhNWQ5MjI1Zjg3MmFiYjYzMmNmYWZiOTA2NDJlMjpzZXNzaW9uX2lkPTJfTVg0ME5qTXdOelUyTW41LU1UVTFOVEE1TkRrNE9EUXhOWDVDT1V0R00xZDVWM0ppV21ocGFVRmtRbXQ1VGtWQ2EwMS1mZyZjcmVhdGVfdGltZT0xNTU1MDk1MDEzJm5vbmNlPTAuMzY3NjQ3NzIzMTcxNzI4NTYmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTU1NzY4NzA0OCZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static String LOG_TAG = VideoCall.class.getSimpleName();
    private static final int RC_SETTINGS = 123;

    private Session session;

    private FrameLayout PublisherContainer;
    private FrameLayout SubscriberContainer;

    private Publisher publisher;

    private Subscriber subscriber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        requestPermissions();
        PublisherContainer = (FrameLayout)findViewById(R.id.publisher_container);
        SubscriberContainer = (FrameLayout)findViewById(R.id.subscriber_container);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @AfterPermissionGranted(RC_SETTINGS)
    private void requestPermissions()
    {
        String[] perm = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this,perm))
        {
            session = new Session.Builder(this, API_KEY, SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);
        }
        else
        {
            EasyPermissions.requestPermissions(this, "This app needs your access for your camera and mic", RC_SETTINGS, perm);

        }
    }

    @Override
    public void onConnected(Session session) {

        publisher = new Publisher.Builder(this).build();
        publisher.setPublisherListener(this);

        PublisherContainer.addView(publisher.getView());
        session.publish(publisher);

    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        if(subscriber == null)
        {
            subscriber = new Subscriber.Builder(this,stream).build();
            session.subscribe(subscriber);
            SubscriberContainer.addView(subscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        if(subscriber!= null)
        {
            subscriber = null;
            SubscriberContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
