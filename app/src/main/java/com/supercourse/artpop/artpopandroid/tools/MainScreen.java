package com.supercourse.artpop.artpopandroid.tools;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.supercourse.artpop.artpopandroid.R;
import com.supercourse.artpop.artpopandroid.util.ArtPiece;
import com.supercourse.artpop.artpopandroid.util.DirectoryListing;
import com.supercourse.artpop.artpopandroid.util.xmlPullParser;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainScreen extends Activity {

    public static final String TAG = "MainScreen";

    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", null, null, null);

    //Mostly for reference but also good for calls
    private static final String MR_BLUE_BEACON_MAC_ADDRESS = "C5:13:36:CB:3C:28";
    private static final String MR_PURPLE_BEACON_MAC_ADDRESS = "D5:40:D4:D0:E4:65";
    private static final String MR_GREEN_BEACON_MAC_ADDRESS = "C6:D2:2C:49:CD:89";

    private String currentClosestBeaconMacAddress;
    private static Beacon closestBeacon;
    private static boolean closestBeaconChanged = false;
    private static boolean firstRun = true, done = false;


    private BeaconManager beaconManager = new BeaconManager(this);
    ArrayList<DirectoryListing> directoryList = new ArrayList<DirectoryListing>();

    Context context;
    ArtPiece currentArtPiece;
    View view;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        context = this.getApplicationContext();
        view = this.findViewById(android.R.id.content);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();


        directoryList = xmlPullParser.createBeaconDirectory(context);
        Log.d(TAG, "directory of size "+directoryList.size()+" created");
        /*
        Log.d(TAG, "entry 1: "+directoryList.get(0).getInstallationName()+ ", "+directoryList.get(0).getMacAddress()+", "+directoryList.get(0).getInstallationXML());
        Log.d(TAG, "entry 2: "+directoryList.get(1).getInstallationName()+ ", "+directoryList.get(1).getMacAddress()+", "+directoryList.get(1).getInstallationXML());
        Log.d(TAG, "entry 3: "+directoryList.get(2).getInstallationName()+ ", "+directoryList.get(2).getMacAddress()+", "+directoryList.get(2).getInstallationXML());
        */

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                for (Beacon beacon : beacons) {
                    Log.d(TAG, "Detected beacon: " + beacon.getMacAddress() + " Beacon's Strength: " + beacon.getRssi() + "\n");
                    closestBeacon = beacons.get(0);
                    Log.d(TAG, "Closet beacon: " + closestBeacon.getMacAddress() + " Beacon's Strength: " + closestBeacon.getRssi() + "\n");

                   /* if(currentClosestBeaconMacAddress !=null){
                        if(!currentClosestBeaconMacAddress.equals(closestBeacon.getMacAddress())){
                            closestBeaconChanged=true;
                            Log.d(TAG, "Closest beacon changed. Old closest beacon: "+currentClosestBeaconMacAddress+" New closest beacon: "+ closestBeacon.getMacAddress());
                            currentClosestBeaconMacAddress = closestBeacon.getMacAddress();
                        }
                        else{
                            closestBeaconChanged = false;
                            Log.d(TAG, "Closest beacon not changed");
                        }
                    }
*/
                }

                if (closestBeacon != null) {
                    String closestInstallationXML = getClosestInstallationUrl(closestBeacon.getMacAddress(), directoryList);
                    Log.d(TAG, "Closest beacon xml " + closestInstallationXML);
                    currentArtPiece = xmlPullParser.getArtPieceFromXML(context, closestInstallationXML);
                    if(firstRun){
                        currentClosestBeaconMacAddress = closestBeacon.getMacAddress();
                        populateView(currentArtPiece);
                        firstRun = false;
                    }
                }

                if(!currentClosestBeaconMacAddress.equals(closestBeacon.getMacAddress())) {
                    Log.d(TAG, "Content update on closest beacon changed");
                    populateView(currentArtPiece);
                    currentClosestBeaconMacAddress = closestBeacon.getMacAddress();
                }

                //populateView(currentArtPiece);
            }

        });

        Buttons buttons = new Buttons();
        buttons.setButtonOnClicks();

        populateView(currentArtPiece);

    }

    @Override
    protected void onPause() {
        super.onPause();
        done = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        done = false;
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
        done = true;
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        done = true;
        beaconManager.disconnect();
    }



    @Override
    protected void onResume() {
        super.onResume();
        done = false;
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
        public int ARTIST_BUTTON=1, MAP_BUTTON =2;
        Fragments fragments = new Fragments();
        FragmentManager fragmentManager = getFragmentManager();

        public void setButtonOnClicks(){
            getButton(ARTIST_BUTTON).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.fragment_container, fragments.getFragment("artist"));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            getButton(MAP_BUTTON).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.fragment_container, fragments.getFragment("map"));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }

        public ImageButton getButton(int button){
            ImageButton buttonRequest = null;

            switch (button){
                case(1):
                    ImageButton artistButton = (ImageButton) findViewById(R.id.artistInfoButton);
                    buttonRequest = artistButton;
                    break;
                case(2):
                    ImageButton mapButton = (ImageButton) findViewById(R.id.mapButton);
                    buttonRequest = mapButton;
                    break;
            }
            return buttonRequest;
        }
    }

    private class Fragments{

        public Fragment getFragment(String fragmentType){
            HashMap<String, Fragment> fragmentMap = new HashMap<String, Fragment>();
            fragmentMap.put("artist", new ArtistFragment(currentArtPiece));
            fragmentMap.put("map", new MapFragment());

            return fragmentMap.get(fragmentType);
        }
    }

    //Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getClosestInstallationUrl(String closestBeaconMacAddress, ArrayList<DirectoryListing> directoryList){
        String artPieceXML="";

        for(DirectoryListing installation : directoryList){
            if (closestBeaconMacAddress.equals(installation.getMacAddress()))   artPieceXML = installation.getInstallationXML();
        }

        return artPieceXML;
    }

    public void populateView(ArtPiece currentArtPiece){
        if(currentArtPiece == null){
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.main_screen_layout);
            layout.setAlpha(0);
            /*RelativeLayout backLayout = (RelativeLayout) view.findViewById(R.id.back_layout);
            backLayout.setBackground(r);*/
        }
        else{
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.main_screen_layout);
            layout.setAlpha(1.0f);

            Log.d(TAG, "current installation " + currentArtPiece.getPieceName());
            //String url = "http://www.scs.ryerson.ca/~m7antoni/ArtPop/PandoricaOpens.jpg";
            //String url = "http://scs.ryerson.ca/~m7antoni/ArtPop/FlightStop-MichaelSnow.mp4";

            VideoView videoView = (VideoView) findViewById(R.id.media_video);
            SurfaceView menu = (SurfaceView) findViewById(R.id.media_image_menu);
            img = (ImageView) findViewById(R.id.media_image);

            //menu.setLayoutParams();
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            Uri video = Uri.parse(currentArtPiece.getVideoAddress());
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.start();
            /*
            if (!currentArtPiece.getVideoAddresses().isEmpty()) {
                //menu.setLayoutParams();
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                Uri video = Uri.parse(currentArtPiece.getVideoAddresses().get(0));
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(video);
                videoView.start();
            }
            else{ //its an image
                img = (ImageView) findViewById(R.id.media_image);
                img.setAlpha(1.0f);
                new LoadImage().execute(currentArtPiece.getPhotoAddresses().get(0));
            }*/

        }
    }
}
