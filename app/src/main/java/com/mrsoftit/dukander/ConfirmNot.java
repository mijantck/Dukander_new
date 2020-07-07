package com.mrsoftit.dukander;

public class ConfirmNot {
    private String id ;
    private boolean firstTime;
    private String code;

    public void ConfirmNot(){}

    public ConfirmNot(String id, boolean firstTime, String code) {
        this.id = id;
        this.firstTime = firstTime;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
