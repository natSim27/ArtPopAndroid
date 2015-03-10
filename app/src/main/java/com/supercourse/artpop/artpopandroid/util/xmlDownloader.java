package com.supercourse.artpop.artpopandroid.util;

import android.util.Log;

import com.supercourse.artpop.artpopandroid.MainActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Nathalie on 2015-03-09.
 */
public class xmlDownloader {

    private static final String TAG = "Downloader";

    public static void DownloadFromURL(String webURL ){
        try{
            URL url = new URL(webURL);

            Log.d(TAG, "Starting Download");

            URLConnection ucon = url.openConnection();

            Log.i(TAG, "Opened Connection");

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Log.i(TAG, "Got InputStream and BufferedInputStream");

            //BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream());
            Log.i(TAG, "Got FileOutputStream and BufferedOutputStream");



        }
        catch (IOException e) {
            Log.e(TAG, "Error: " + e);

        }
    }

}

