package com.supercourse.artpop.artpopandroid.util;

/**
 * Created by Nathalie on 2015-04-06.
 */
public class DirectoryListing{
    String installationName, macAddress, installationXML;

    public DirectoryListing(){
        installationName = null;
        macAddress = null;
        installationXML = null;
    }

    public void setInstallationName(String installationName) {this.installationName = installationName;}
    public String getInstallationName() {return installationName;}

    public void setMacAddress(String macAddress) {this.macAddress = macAddress;}
    public String getMacAddress() {return macAddress;}

    public void setInstallationXML(String installationXML) {this.installationXML = installationXML;}
    public String getInstallationXML() {return installationXML;}
}