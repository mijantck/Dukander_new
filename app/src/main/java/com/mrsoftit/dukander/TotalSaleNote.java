package com.mrsoftit.dukander;

public class TotalSaleNote {
    private int date;
    private String itemName;
    private boolean paid;
    private boolean confirm;
    private String saleProductId;
    private String customerID;
    private String unknownCustomerID;
    private double quantedt;
    private double totalPrice;
    private double payment;

    public TotalSaleNote(){}

    public TotalSaleNote(int date, String itemName, boolean paid, String saleProductId, double quantedt, double totalPrice) {
        this.date = date;
        this.itemName = itemName;
        this.paid = paid;
        this.saleProductId = saleProductId;
        this.quantedt = quantedt;
        this.totalPrice = totalPrice;
    }

    public TotalSaleNote( String itemName,int date, double payment) {
        this.itemName = itemName;
        this.date = date;
        this.payment = payment;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public String getSaleProductId() {
        return saleProductId;
    }

    public void setSaleProductId(String saleProductId) {
        this.saleProductId = saleProductId;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getUnknownCustomerID() {
        return unknownCustomerID;
    }

    public void setUnknownCustomerID(String unknownCustomerID) {
        this.unknownCustomerID = unknownCustomerID;
    }

    public double getQuantedt() {
        return quantedt;
    }

    public void setQuantedt(double quantedt) {
        this.quantedt = quantedt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
