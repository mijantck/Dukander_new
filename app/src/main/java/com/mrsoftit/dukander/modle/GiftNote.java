package com.mrsoftit.dukander.modle;

public class GiftNote {
   private String giftName;
   private String giftOfferName;
   private String giftPicURL;
   private String giftNewL;
   private int giftCoin;


   public GiftNote(){}

    public String getGiftNewL() {
        return giftNewL;
    }

    public void setGiftNewL(String giftNewL) {
        this.giftNewL = giftNewL;
    }

    public String getGiftOfferName() {
        return giftOfferName;
    }

    public void setGiftOfferName(String giftOfferName) {
        this.giftOfferName = giftOfferName;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftPicURL() {
        return giftPicURL;
    }

    public void setGiftPicURL(String giftPicURL) {
        this.giftPicURL = giftPicURL;
    }

    public int getGiftCoin() {
        return giftCoin;
    }

    public void setGiftCoin(int giftCoin) {
        this.giftCoin = giftCoin;
    }
}
