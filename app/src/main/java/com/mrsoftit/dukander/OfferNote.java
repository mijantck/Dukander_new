package com.mrsoftit.dukander;

public class OfferNote {

    private String offerCoin;
    private String cointaka;

    public OfferNote(){}

    public OfferNote(String offerCoin, String cointaka) {
        this.offerCoin = offerCoin;
        this.cointaka = cointaka;
    }

    public String getOfferCoin() {
        return offerCoin;
    }

    public void setOfferCoin(String offerCoin) {
        this.offerCoin = offerCoin;
    }

    public String getCointaka() {
        return cointaka;
    }

    public void setCointaka(String cointaka) {
        this.cointaka = cointaka;
    }
}
