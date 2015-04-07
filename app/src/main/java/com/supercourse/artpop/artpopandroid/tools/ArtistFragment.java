package com.supercourse.artpop.artpopandroid.tools;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supercourse.artpop.artpopandroid.R;
import com.supercourse.artpop.artpopandroid.util.ArtPiece;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ArtistFragment extends Fragment {

    ArtPiece installation;
    URL artistImageURL;

    public ArtistFragment(){}

    public ArtistFragment(ArtPiece artPiece) {
        // Required empty public constructor
        installation = artPiece;
        try{
            new URL(artPiece.getArtistPhotoAddress());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_artist, container, false);

        if(installation == null){
            LinearLayout main_artist_layout = (LinearLayout) view.findViewById(R.id.main_artist_layout);
            main_artist_layout.setAlpha(0);
        }
        else{
            TextView artistName = (TextView) view.findViewById(R.id.artist_name);
            artistName.setText(installation.getArtistName());

            //ImageView artistImage = (ImageView) view.findViewById(R.id.artist_image);
            new DownloadImageTask((ImageView) view.findViewById(R.id.artist_image)).execute(installation.getArtistPhotoAddress());

            TextView artistBlurb = (TextView) view.findViewById(R.id.artist_blurb);
            artistBlurb.setText(installation.getArtistBlurb());
        }

        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
