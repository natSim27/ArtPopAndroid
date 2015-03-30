package com.supercourse.artpop.artpopandroid.util;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by Nathalie on 2015-03-17.
 */
public class xmlPullParser {

    static final String KEY_BEACON_ID = "beaconID";
    static final String KEY_INSTALLATION_NAME = "installationName";
    static final String KEY_PHOTOS = "photos";
    static final String KEY_PHOTO = "photo";
    static final String KEY_VIDEOS = "videos";
    static final String KEY_VIDEO = "video";
    static final String KEY_URL = "url";

    static final String KEY_ARTIST_NAME = "artistName";
    static final String KEY_ARTIST_BLURB = "artistBlurb";
    static final String KEY_ARTIST_PHOTO = "artistPhoto";
    static final String KEY_ARTIST_SITE = "artistSite";
    static final String KEY_SOCIAL_MEDIA = "socialMedia";
    static final String KEY_ARTIST_FACEBOOK = "artistFacebook";
    static final String KEY_ARTIST_TWITTER = "artistTwitter";
    static final String KEY_ARTIST_INSTAGRAM = "artistInstagram";

    public static ArtPiece getArtPieceFromXML(Context context){
        ArtPiece artPiece = new ArtPiece();
        String temp = "";
        boolean isVideo = false;

        try{
            // Get our factory and PullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            // Open up InputStream and Reader of our file.
            FileInputStream fileInputStream = context.openFileInput("artPop_xml_download.xml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

            // point the parser to our file.
            xpp.setInput(reader);

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
                        else if (tagname.equalsIgnoreCase(KEY_PHOTOS)){
                            isVideo = false;
                        }
                        else if (tagname.equalsIgnoreCase(KEY_VIDEOS)){
                            isVideo = true;
                        }
                        else if (tagname.equalsIgnoreCase(KEY_URL)){
                            if(isVideo) artPiece.addVideoAddress(temp);
                            else    artPiece.addPhotoAddress(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_NAME)) {
                            artPiece.artist.setArtistName(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_BLURB)) {
                            artPiece.artist.setArtistBlurb(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_PHOTO)) {
                            artPiece.artist.setArtistPhotoAddress(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_SITE)) {
                            artPiece.artist.setArtistSite(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_FACEBOOK)) {
                            artPiece.artist.setArtistFacebook(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_TWITTER)){
                            artPiece.artist.setArtistTwitter(temp);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_ARTIST_INSTAGRAM)){
                            artPiece.artist.setArtistInstagram(temp);
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
}
