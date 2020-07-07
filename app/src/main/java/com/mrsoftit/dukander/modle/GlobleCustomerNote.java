package com.mrsoftit.dukander.modle;

public class GlobleCustomerNote {
    private String glovleCustomerID;
    private String Id;
    private String customerType;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private int coine;
    private String referCode;
    private boolean firstTimeRafer;

    public GlobleCustomerNote(){}


    public GlobleCustomerNote(String glovleCustomerID, String id, String customerType, String name, String email, String phoneNumber, String address, int coine, String referCode, boolean firstTimeRafer) {
        this.glovleCustomerID = glovleCustomerID;
        Id = id;
        this.customerType = customerType;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.coine = coine;
        this.referCode = referCode;
        this.firstTimeRafer = firstTimeRafer;
    }

    public boolean isFirstTimeRafer() {
        return firstTimeRafer;
    }

    public void setFirstTimeRafer(boolean firstTimeRafer) {
        this.firstTimeRafer = firstTimeRafer;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getCoine() {
        return coine;
    }

    public void setCoine(int coine) {
        this.coine = coine;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getGlovleCustomerID() {
        return glovleCustomerID;
    }

    public void setGlovleCustomerID(String glovleCustomerID) {
        this.glovleCustomerID = glovleCustomerID;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
