package com.mrsoftit.dukander;

public class PinNote {

    private  String id;
    private String gmail;
    private String pin;
    private  boolean fairtTime;

    public PinNote(){}


    public PinNote(String id, String gmail, String pin, boolean fairtTime) {
        this.id = id;
        this.gmail = gmail;
        this.pin = pin;
        this.fairtTime = fairtTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isFairtTime() {
        return fairtTime;
    }

    public void setFairtTime(boolean fairtTime) {
        this.fairtTime = fairtTime;
    }
}
