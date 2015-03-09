package com.supercourse.artpop.artpopandroid;

import com.supercourse.artpop.artpopandroid.tools.MainScreen;
import com.supercourse.artpop.artpopandroid.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.BeaconManager.MonitoringListener;
import com.estimote.sdk.Region;

import java.net.URI;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
    private static final int MAX_NUM_OF_SOUNDSTREAMS = 3;

    public static BeaconManager beaconManager;
    public static SoundPool soundPool;
    public static MediaPlayer mediaPlayer;

    private URI websiteURI, rootURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton startButton = (ImageButton) findViewById(R.id.startButton);

        //TODO: Stop beacons from being bitches
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                for(Beacon beacon : beacons){
                    Log.d(TAG, "Detected beacon: " + beacon.getMacAddress() + " Beacon's Strength: " + beacon.getRssi() + "\n");
                }
                Log.d(TAG, "Detected beacon: " + beacons + "\n");

            }
        });

        //Creates the soundpool instance and loads sounds, if you wanna add more sounds look at the class at the bottom
        soundPool = new SoundPool(MAX_NUM_OF_SOUNDSTREAMS, AudioManager.STREAM_MUSIC,0);
        Sounds.loadSounds(this);

        //Haven't figured out how it works yet, just put it here for starters
        mediaPlayer = new MediaPlayer();

        //After everything is loaded up allow user to click start, i.e. don't put any loader classes after this
        startButton.setAlpha((float) 1.0);
        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainScreen.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
    }

    public int playSound(int sound){return soundPool.play(sound, 0.4f,0.4f,1,0,1);}

    public static class Sounds{
        static int popSound;

        //Returns the int that you put into soundpool to play. raw I made for random resources, just drop the sound files in there.
        public static void loadSounds(Context context){
            popSound =  soundPool.load(context, R.raw.pop_sound,1);

        }
    }
}
