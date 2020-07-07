package com.mrsoftit.dukander;

public class CustomerNote {
    private String customerIdDucunt;
    private String nameCUstomer;
    private String phone;
    private double taka;
    private String addres;
    private String imageUrl;
    private double lastTotal;
    private double pdfTotal;
    private String TaxtId;
    private String NID;

    public  CustomerNote(){}

    public CustomerNote(String customerIdDucunt, String nameCUstomer, String phone, double taka, String addres, String imageUrl, double lastTotal,double pdfTotal,String TaxtId,String NID) {
        this.customerIdDucunt = customerIdDucunt;
        this.nameCUstomer = nameCUstomer;
        this.phone = phone;
        this.taka = taka;
        this.addres = addres;
        this.imageUrl = imageUrl;
        this.lastTotal = lastTotal;
        this.pdfTotal = pdfTotal;
        this.TaxtId = TaxtId;
        this.NID = NID;
    }

    public CustomerNote(String customerIdDucunt, String nameCUstomer, String phone, double taka, String addres, double lastTotal,double pdfTotal,String TaxtId,String NID) {
        this.customerIdDucunt = customerIdDucunt;
        this.nameCUstomer = nameCUstomer;
        this.phone = phone;
        this.taka = taka;
        this.addres = addres;
        this.lastTotal = lastTotal;
        this.pdfTotal = pdfTotal;
        this.TaxtId = TaxtId;
        this.NID = NID;

    }


    public String getCustomerIdDucunt() {
        return customerIdDucunt;
    }

    public String getNameCUstomer() {
        return nameCUstomer;
    }

    public String getPhone() {
        return phone;
    }

    public double getTaka() {
        return taka;
    }

    public String getAddres() {
        return addres;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getLastTotal() {
        return lastTotal;
    }

    public double getPdfTotal() {
        return pdfTotal;
    }

    public void setPdfTotal(double pdfTotal) {
        this.pdfTotal = pdfTotal;
    }

    public String getTaxtId() {
        return TaxtId;
    }

    public void setTaxtId(String taxtId) {
        TaxtId = taxtId;
    }

    public String getNID() {
        return NID;
    }

    public void setNID(String NID) {
        this.NID = NID;
    }
}
