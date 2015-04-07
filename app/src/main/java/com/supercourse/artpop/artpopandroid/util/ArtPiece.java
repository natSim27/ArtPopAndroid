package com.supercourse.artpop.artpopandroid.util;

import java.util.ArrayList;

/**
 * Created by Nathalie on 2015-03-29.
 */
public class ArtPiece {
    String pieceName, macAddress;
    String artistName, artistBlurb, artistPhotoAddress, artistSite,artistFacebook, artistTwitter, artistInstagram, videoAddress, photoAddress;

    public ArtPiece(){
        pieceName = null;
        macAddress = null;

        videoAddress = null;
        photoAddress = null;

        artistName = null;
        artistBlurb = null;
        artistPhotoAddress = null;
        artistSite = null;
        artistFacebook = null;
        artistTwitter = null;
        artistInstagram = null;
    }

    public void setPieceName(String pieceName) {this.pieceName = pieceName;}
    public String getPieceName(){return pieceName;}

    public void setMacAddress(String macAddress){this.macAddress = macAddress;}
    public String getMacAddress() {return macAddress;}

    public void setVideoAddress(String videoAddress){this.videoAddress = videoAddress;}
    public String getVideoAddress() {return videoAddress;}

    public void setPhotoAddress(String photoAddress){this.photoAddress = photoAddress;}
    public String getPhotoAddress() {return photoAddress;}

    public void setArtistName(String artistName) {this.artistName = artistName;}
    public String getArtistName(){return artistName;}

    public void setArtistBlurb(String artistBlurb) {this.artistBlurb = artistBlurb;}
    public String getArtistBlurb(){return artistBlurb;}

    public void setArtistPhotoAddress(String artistPhotoAddress) {this.artistPhotoAddress = artistPhotoAddress;}
    public String getArtistPhotoAddress(){return artistPhotoAddress;}

    public void setArtistSite(String artistSite) {this.artistSite = artistSite;}
    public String getArtistSite(){return artistSite;}

    public void setArtistFacebook(String artistFacebook) {this.artistFacebook = artistFacebook;}
    public String getArtistFacebook(){return artistFacebook;}

    public void setArtistTwitter(String artistTwitter) {this.artistTwitter = artistTwitter;}
    public String getArtistTwitter(){return artistTwitter;}

    public void setArtistInstagram(String artistInstagram) {this.artistInstagram = artistInstagram;}
    public String getArtistInstagram(){return artistInstagram;}
}
