package com.mrsoftit.dukander;

public class CouponCode {

    private String NameProvider;
    private String couponCode;
    private int copuOffer;

    public void CouponCode(){}


    public String getNameProvider() {
        return NameProvider;
    }

    public void setNameProvider(String nameProvider) {
        NameProvider = nameProvider;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public int getCopuOffer() {
        return copuOffer;
    }

    public void setCopuOffer(int copuOffer) {
        this.copuOffer = copuOffer;
    }
}
