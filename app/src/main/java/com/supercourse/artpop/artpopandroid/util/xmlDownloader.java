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

    private static final String TAG = "Downloader";

    public static void DownloadFromURL(String webURL, Context context ){
        try{
            URL url = new URL(webURL);

            Log.d(TAG, "Starting Download");

            URLConnection ucon = url.openConnection();

            Log.i(TAG, "Opened Connection");

            InputStream is = ucon.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            Log.i(TAG, "Got InputStream and BufferedInputStream");

            File xmlDownloadFile = new File(context.getCacheDir(), "artPop_xml_download.xml");
            FileOutputStream fileOutputStream = new FileOutputStream(xmlDownloadFile);

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            Log.i(TAG, "Got FileOutputStream and BufferedOutputStream");

            byte data[] = new byte[1024];

            int count;
            while ((count = bufferedInputStream.read(data)) != -1) {
                bufferedOutputStream.write(data, 0, count);
            }

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

