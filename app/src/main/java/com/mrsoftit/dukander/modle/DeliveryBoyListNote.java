package com.mrsoftit.dukander.modle;

public class DeliveryBoyListNote {
    private String id;
    private String boyName;
    private String boyPhone;
    private String boyOnline;
    private String boyAddress;
    private String Aria;
    private String approved;




    public DeliveryBoyListNote(){}


    public DeliveryBoyListNote(String id, String boyName, String boyPhone, String boyOnline, String boyAddress, String aria, String approved) {
        this.id = id;
        this.boyName = boyName;
        this.boyPhone = boyPhone;
        this.boyOnline = boyOnline;
        this.boyAddress = boyAddress;
        Aria = aria;
        this.approved = approved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getAria() {
        return Aria;
    }

    public void setAria(String aria) {
        Aria = aria;
    }

    public String getBoyName() {
        return boyName;
    }

    public void setBoyName(String boyName) {
        this.boyName = boyName;
    }

    public String getBoyPhone() {
        return boyPhone;
    }

    public void setBoyPhone(String boyPhone) {
        this.boyPhone = boyPhone;
    }

    public String getBoyOnline() {
        return boyOnline;
    }

    public void setBoyOnline(String boyOnline) {
        this.boyOnline = boyOnline;
    }

    public String getBoyAddress() {
        return boyAddress;
    }

    public void setBoyAddress(String boyAddress) {
        this.boyAddress = boyAddress;
    }
}
