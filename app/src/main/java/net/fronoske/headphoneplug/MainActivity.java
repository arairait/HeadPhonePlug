package net.fronoske.headphoneplug;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends Activity {

    String cmdPlugIn = "/system/bin/sendevent /dev/input/event8 5 2 1";
    String cmdPlugOut = "/system/bin/sendevent /dev/input/event8 5 2 0";
    String cmdSync = "/system/bin/sendevent /dev/input/event8 0 0 0";
    TextView textCurrentStatus;
    Activity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // get current status
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        boolean spOn = am.isSpeakerphoneOn();
        int vol = am.getStreamVolume(AudioManager.STREAM_RING);
        String status = "Speaker=" + String.valueOf(spOn) + " Volume=" + String.valueOf(vol);
        textCurrentStatus = (TextView) findViewById(R.id.txtCurrentStatus);
        textCurrentStatus.setText(status);
        Log.v("headphoneplug", "(status) " + status);
    }

    public void onClickButtonPlugIn(View v) {
        textCurrentStatus.setText("Processing...");
        Log.v("headphoneplug", "btnPlugIn was clicked.");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Executes the command.
                    Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", cmdPlugIn});
                    process.waitFor();
                    process = Runtime.getRuntime().exec(new String[]{"su", "-c", cmdSync});
                    process.waitFor();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        textCurrentStatus.setText("Done.");
                    }
                });
            }
        }).start();
    }
    public void onClickButtonPlugOut(View v) {
        textCurrentStatus.setText("Processing...");
        Log.v("headphoneplug", "btnPlugOut was clicked.");
        // Executes the command.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", cmdPlugOut});
                    process.waitFor();
                    process = Runtime.getRuntime().exec(new String[]{"su", "-c", cmdSync});
                    process.waitFor();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        textCurrentStatus.setText("Done.");
                    }
                });
            }
        }).start();
        /*
        Notification n = new Notification();
        n.icon = R.drawable.coda; // アイコンの設定
        n.tickerText = "This is a notification message..."; // メッセージの設定
        // PendingIntentの生成
        Intent i = new Intent(getApplicationContext(), NotificationActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        // 詳細情報の設定とPendingIntentの設定
        n.setLatestEventInfo(getApplicationContext(), "TITLE", "TEXT", pi);
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, n);
        */
    }
}

