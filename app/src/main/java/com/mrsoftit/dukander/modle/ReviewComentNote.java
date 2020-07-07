package com.mrsoftit.dukander.modle;

public class ReviewComentNote {
    private String reviewID;
    private String reviewCustomerID;
    private String reviewShopMainID;
    private String reviewCustomerName;
    private String producrName;
    private String productEmail;
    private String reviewCustomerEmail;
    private String reviewComment;
    private int dateAndTime;

    public ReviewComentNote(){}

    public ReviewComentNote(String reviewID, String reviewCustomerID, String reviewCustomerName, String reviewComment, int dateAndTime) {
        this.reviewID = reviewID;
        this.reviewCustomerID = reviewCustomerID;
        this.reviewCustomerName = reviewCustomerName;
        this.reviewComment = reviewComment;
        this.dateAndTime = dateAndTime;
    }


    public ReviewComentNote(String reviewID, String reviewCustomerID, String reviewCustomerName, String reviewCustomerEmail, String reviewComment, int dateAndTime) {
        this.reviewID = reviewID;
        this.reviewCustomerID = reviewCustomerID;
        this.reviewCustomerName = reviewCustomerName;
        this.reviewCustomerEmail = reviewCustomerEmail;
        this.reviewComment = reviewComment;
        this.dateAndTime = dateAndTime;
    }

    public String getReviewShopMainID() {
        return reviewShopMainID;
    }

    public void setReviewShopMainID(String reviewShopMainID) {
        this.reviewShopMainID = reviewShopMainID;
    }

    public String getProducrName() {
        return producrName;
    }

    public void setProducrName(String producrName) {
        this.producrName = producrName;
    }

    public String getProductEmail() {
        return productEmail;
    }

    public void setProductEmail(String productEmail) {
        this.productEmail = productEmail;
    }

    public String getReviewCustomerEmail() {
        return reviewCustomerEmail;
    }

    public void setReviewCustomerEmail(String reviewCustomerEmail) {
        this.reviewCustomerEmail = reviewCustomerEmail;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getReviewCustomerID() {
        return reviewCustomerID;
    }

    public void setReviewCustomerID(String reviewCustomerID) {
        this.reviewCustomerID = reviewCustomerID;
    }

    public String getReviewCustomerName() {
        return reviewCustomerName;
    }

    public void setReviewCustomerName(String reviewCustomerName) {
        this.reviewCustomerName = reviewCustomerName;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public int getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(int dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
