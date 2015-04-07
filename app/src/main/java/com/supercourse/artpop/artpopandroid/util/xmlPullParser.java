package com.supercourse.artpop.artpopandroid.util;

import android.content.Context;
import android.util.Log;

import com.supercourse.artpop.artpopandroid.tools.MainScreen;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nathalie on 2015-03-17.
 */
public class xmlPullParser extends MainScreen{

    static final String TAG = "Pull Parser";

    //Installation XML variables
    static final String KEY_BEACON_ID = "beaconID";
    static final String KEY_INSTALLATION_NAME = "installationName";
    static final String KEY_PHOTO = "photo";
    static final String KEY_VIDEO = "video";

    static final String KEY_ARTIST_NAME = "artistName";
    static final String KEY_ARTIST_BLURB = "artistBlurb";
    static final String KEY_ARTIST_PHOTO = "artistPhoto";
    static final String KEY_ARTIST_SITE = "artistSite";
    static final String KEY_SOCIAL_MEDIA = "socialMedia";
    static final String KEY_ARTIST_FACEBOOK = "artistFacebook";
    static final String KEY_ARTIST_TWITTER = "artistTwitter";
    static final String KEY_ARTIST_INSTAGRAM = "artistInstagram";

    //Directory XML variables
    static final String KEY_INSTALLATION = "installation";
    static final String KEY_BEACON_MAC_ADDRESS = "beaconMacAddress";
    static final String KEY_NAME_INSTALLATION = "installationName";
    static final String KEY_INSTALLATION_XML = "installationXML";

    public static ArtPiece getArtPieceFromXML(Context context, String artPieceXML){
        ArtPiece artPiece = new ArtPiece();
        String temp = "";
        boolean isVideo = false;

        try{
            // Get our factory and PullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            InputStream stream = context.getAssets().open(artPieceXML);
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xpp.setInput(stream, null);

            // get initial eventType
            int eventType = xpp.getEventType();

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the current tag
                String tagname = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        temp = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase(KEY_BEACON_ID)) {
                            artPiece.setMacAddress(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_INSTALLATION_NAME)) {
                            artPiece.setPieceName(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_PHOTO)){
                            artPiece.setPhotoAddress(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_VIDEO)){
                            artPiece.setVideoAddress(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_NAME)) {
                            artPiece.setArtistName(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_BLURB)) {
                            artPiece.setArtistBlurb(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_PHOTO)) {
                            artPiece.setArtistPhotoAddress(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_SITE)) {
                            artPiece.setArtistSite(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_FACEBOOK)) {
                            artPiece.setArtistFacebook(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_TWITTER)){
                            artPiece.setArtistTwitter(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_INSTAGRAM)){
                            artPiece.setArtistInstagram(temp);
                        }
                        break;
                    default:
                        break;
                }
                //move on to next iteration
                eventType = xpp.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return artPiece;
    }

    public static ArrayList<DirectoryListing> createBeaconDirectory(Context context){
        String temp = "";
        ArrayList<DirectoryListing> directory = new ArrayList<DirectoryListing>();
        DirectoryListing directoryEntry = new DirectoryListing();

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            InputStream stream = context.getAssets().open("beacon_to_installation.xml");
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xpp.setInput(stream, null);

            int eventType = xpp.getEventType();

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the current tag
                String tagname = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if(tagname.equalsIgnoreCase(KEY_INSTALLATION)){
                            directoryEntry = new DirectoryListing();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        temp = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase(KEY_BEACON_MAC_ADDRESS)) {
                            directoryEntry.setMacAddress(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_NAME_INSTALLATION)) {
                            directoryEntry.setInstallationName(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_INSTALLATION_XML)) {
                            directoryEntry.setInstallationXML(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_INSTALLATION)) {
                            directory.add(directoryEntry);
                        }
                }
                eventType = xpp.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, "Parser class done");
        return directory;
    }
}
