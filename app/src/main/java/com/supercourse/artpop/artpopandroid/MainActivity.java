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

import java.net.URI;
import java.net.URL;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static final int MAX_NUM_OF_SOUNDSTREAMS = 3;

    public static SoundPool soundPool;
    public static MediaPlayer mediaPlayer;

    private URI websiteURI, rootURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton startButton = (ImageButton) findViewById(R.id.startButton);

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

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public int playSound(int sound){return soundPool.play(sound, 0.4f,0.4f,1,0,1);}

    public static class Sounds{
        static int popSound;

        //Returns the int that you put into soundpool to play. raw I made for random resources, just drop the sound files in there.
        public static void loadSounds(Context context){
            popSound =  soundPool.load(context, R.raw.pop_sound,1);

        }
    }

    /*public URI rootURI(){
        URL webUrl = new URL("http", "moon.scs.ryerson.ca","");
        return websiteURI = new URL("http://www.google.com").toURI();
    }*/
}
