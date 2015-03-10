package com.supercourse.artpop.artpopandroid.tools;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.supercourse.artpop.artpopandroid.MainActivity;
import com.supercourse.artpop.artpopandroid.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MainScreen extends Activity {

    public static final String TAG = "MainScreen";

    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);

    private BeaconManager beaconManager = new BeaconManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Buttons buttons = new Buttons();

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                Log.d(TAG, "Ranged beacons: " + beacons);
            }
        });

        /*
        //TODO: Stop beacons from being bitches

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                Log.d(TAG, "Ranged beacons: " + beacons);
               *//* Log.d(TAG, "Detected beacon: " + beacons + "\n");
                for(Beacon beacon : beacons){
                    Log.d(TAG, "Detected beacon: " + beacon.getMacAddress() + " Beacon's Strength: " + beacon.getRssi() + "\n");
                }*//*
            }
        });*/

        buttons.getButton(buttons.ARTIST_BUTTON).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                ArtistFragment artistFragment = new ArtistFragment();
                transaction.add(R.id.fragment_container, artistFragment);

                transaction.commit();
            }
        });
        buttons.getButton(buttons.USER_ACCOUNT_BUTTON).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                UserAccountFragment userFragment = new UserAccountFragment();
                transaction.add(R.id.fragment_container, userFragment);

                transaction.commit();
            }
        });
        buttons.getButton(buttons.COMMENT_BUTTON).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                CommentFragment commentFragment = new CommentFragment();
                transaction.add(R.id.fragment_container, commentFragment);

                transaction.commit();
            }
        });


        //String url = "http://www.scs.ryerson.ca/~m7antoni/ArtPop/PandoricaOpens.jpg";
        String url = "http://www.scs.ryerson.ca/~m7antoni/ArtPop/Icebreaker.mp4";

        VideoView videoView = (VideoView) findViewById(R.id.media_video);
        SurfaceView menu = (SurfaceView) findViewById(R.id.media_image_menu);
        img = (ImageView) findViewById(R.id.media_image);

        if (url.endsWith(".mp4")) {
            //menu.setLayoutParams();
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            Uri video = Uri.parse(url);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.start();
        }
        else{ //its an image
            img = (ImageView) findViewById(R.id.media_image);
            img.setAlpha(1.0f);
            new LoadImage().execute(url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    //http://www.learn2crack.com/2014/06/android-load-image-from-internet.html
    ImageView img;
    ProgressDialog pDialog;
    Bitmap bitmap;

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainScreen.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();
        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {

            if(image != null){
                img.setImageBitmap(image);
                pDialog.dismiss();
            }else{
                pDialog.dismiss();
                Toast.makeText(MainScreen.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Buttons{
        public int ARTIST_BUTTON=1, USER_ACCOUNT_BUTTON=2, COMMENT_BUTTON=3,SHARE_BUTTON=4;

        public ImageButton getButton(int button){
            ImageButton buttonRequest = null;

            switch (button){
                case(1):
                    ImageButton artistButton = (ImageButton) findViewById(R.id.artistInfoButton);
                    buttonRequest = artistButton;
                    break;
                case(2):
                    ImageButton userAccountButton = (ImageButton) findViewById(R.id.userAccountButton);
                    buttonRequest = userAccountButton;
                    break;
                case(3):
                    ImageButton commentButton = (ImageButton) findViewById(R.id.commentPageButton);
                    buttonRequest = commentButton;
                    break;
                case(4):
                    ImageButton shareButton = (ImageButton) findViewById(R.id.shareButton);
                    buttonRequest = shareButton;
                    break;
            }
            return buttonRequest;
        }
    }

    /*private static class startFragment{
        public startFragment(){

        transaction.attach();//returns a fragment
        transaction.commit();//returns an int
        }
    }*/

}
