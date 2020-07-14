package com.mrsoftit.dukander.modle;

public class NotificationNote {

    private String notiTaile;
    private String notiProductName;
    private String notiImageURL;
    private String notiFromName;
    private String notiProductId;
    private String notiproductDetails;
    private int notidate;

    public NotificationNote(){
    }


    public NotificationNote(String notiTaile, String notiProductName, String notiImageURL,
                            String notiFromName, String notiProductId, String notiproductDetails, int notidate) {
        this.notiTaile = notiTaile;
        this.notiProductName = notiProductName;
        this.notiImageURL = notiImageURL;
        this.notiFromName = notiFromName;
        this.notiProductId = notiProductId;
        this.notiproductDetails = notiproductDetails;
        this.notidate = notidate;
    }

    public String getNotiProductId() {
        return notiProductId;
    }

    public void setNotiProductId(String notiProductId) {
        this.notiProductId = notiProductId;
    }

    public String getNotiImageURL() {
        return notiImageURL;
    }

    public void setNotiImageURL(String notiImageURL) {
        this.notiImageURL = notiImageURL;
    }

    public String getNotiTaile() {
        return notiTaile;
    }

    public void setNotiTaile(String notiTaile) {
        this.notiTaile = notiTaile;
    }

    public String getNotiProductName() {
        return notiProductName;
    }

    public void setNotiProductName(String notiProductName) {
        this.notiProductName = notiProductName;
    }

    public String getNotiFromName() {
        return notiFromName;
    }

    public void setNotiFromName(String notiFromName) {
        this.notiFromName = notiFromName;
    }

    public String getNotiproductDetails() {
        return notiproductDetails;
    }

    public void setNotiproductDetails(String notiproductDetails) {
        this.notiproductDetails = notiproductDetails;
    }

    public int getNotidate() {
        return notidate;
    }

    public void setNotidate(int notidate) {
        this.notidate = notidate;
    }
}
