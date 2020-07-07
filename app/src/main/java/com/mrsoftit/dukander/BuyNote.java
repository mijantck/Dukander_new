package com.mrsoftit.dukander;

public class BuyNote {
    private long time;
    private String id;
    private  String userId;
    private  String userGmail;
    private String sms;

    public BuyNote(){}


    public BuyNote(long time, String id, String userId, String userGmail, String sms) {
        this.time = time;
        this.id = id;
        this.userId = userId;
        this.userGmail = userGmail;
        this.sms = sms;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserGmail() {
        return userGmail;
    }

    public void setUserGmail(String userGmail) {
        this.userGmail = userGmail;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }
}
