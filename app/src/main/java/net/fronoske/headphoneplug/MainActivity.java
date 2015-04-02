package net.fronoske.headphoneplug;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends Activity {

    String cmdPlugIn = "/system/bin/sendevent /dev/input/event8 5 2 1";
    String cmdPlugOut = "/system/bin/sendevent /dev/input/event8 5 2 0";
    String cmdSync = "/system/bin/sendevent /dev/input/event8 0 0 0";

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
        TextView textCurrentStatus = (TextView) findViewById(R.id.txtCurrentStatus);
        textCurrentStatus.setText(status);
        Log.v("headphoneplug", "(status) " + status);
    }

    public void onClickButtonPlugIn(View v) {
        Log.v("headphoneplug", "btnPlugIn was clicked.");
        try {
            // Executes the command.
            Process process = Runtime.getRuntime().exec(new String[] {"su", "-c", cmdPlugIn});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[] {"su", "-c", cmdSync});
            process.waitFor();
            this.onResume();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void onClickButtonPlugOut(View v) {
        Log.v("headphoneplug", "btnPlugOut was clicked.");
        try {
            // Executes the command.
            Process process = Runtime.getRuntime().exec(new String[] {"su", "-c", cmdPlugOut});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[] {"su", "-c", cmdSync});
            process.waitFor();
            this.onResume();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
