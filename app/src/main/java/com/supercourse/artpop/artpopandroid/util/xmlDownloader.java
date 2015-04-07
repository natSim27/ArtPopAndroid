package com.supercourse.artpop.artpopandroid.util;

import android.content.Context;
import android.util.Log;

import com.supercourse.artpop.artpopandroid.MainActivity;
import com.supercourse.artpop.artpopandroid.tools.MainScreen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
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

    private static final String TAG = "XMLDownloader";

    public static void DownloadFromURL(String URL, Context context, boolean ART_PIECE){
        try{
            URL url = new URL("http://scs.ryerson.ca/~m7antoni/ArtPop/BeaconToInstallation.xml");
            /*URL url = new URL(URL);
            if(!ART_PIECE) {
                url = new URL("http://scs.ryerson.ca/~m7antoni/ArtPop/BeaconToInstallation.xml");
            }
            if(ART_PIECE) {
                url = new URL(URL);
            }*/
            Log.d(TAG, "Starting Download");

            URLConnection ucon = url.openConnection();

            Log.i(TAG, "Opened Connection");

            InputStream is = ucon.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            Log.i(TAG, "Got InputStream and BufferedInputStream");

            FileOutputStream fileOutputStream;

            if(ART_PIECE){
                //fileOutputStream = new FileOutputStream(new File(context.getCacheDir().getAbsolutePath().toString()+"/artPop_xml_download.xml"),true);
                //xmlDownloadFile = new File(context.getCacheDir() + "artPop_xml_download.xml");
                //fileOutputStream = context.openFileOutput("artPop_xml_download", Context.MODE_PRIVATE);
                fileOutputStream = new FileOutputStream(new File(context.getFilesDir(),"artPop_xml_download"));
            }
            else{
                //fileOutputStream = new FileOutputStream(new File(context.getCacheDir().getAbsolutePath().toString()+"/artPop_directory.xml"),true);
                //xmlDownloadFile = new File(context.getCacheDir()+ "artPop_directory.xml");
                //fileOutputStream = context.openFileOutput("artPop_directory", Context.MODE_PRIVATE);
                fileOutputStream = new FileOutputStream(new File(context.getFilesDir(),"artPop_directory"));
            }
            // FileOutputStream fileOutputStream = new FileOutputStream(xmlDownloadFile);

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            Log.i(TAG, "Got FileOutputStream and BufferedOutputStream");

            byte data[] = new byte[1024];
            Log.d(TAG, "got to byte data");

            int count;
            while ((count = bufferedInputStream.read(data)) != -1) {
                Log.i(TAG, "In while loop");
                bufferedOutputStream.write(data, 0, count);

            }
            Log.i(TAG, "Finished while loop");

            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            fileOutputStream.flush();
            fileOutputStream.close();

        }
        catch (IOException e) {
            Log.e(TAG, "Error: " + e);

        }
    }

}

