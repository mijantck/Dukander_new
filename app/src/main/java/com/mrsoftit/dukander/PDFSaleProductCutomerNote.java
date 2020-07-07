package com.mrsoftit.dukander;

public class PDFSaleProductCutomerNote {

   private String saleProductId;
    String itemName;
    Double price;
    Double quantedt;
    Double totalPrice;
    int date;
    int invoiceNumber;
    String invoiceId;
    Boolean update ;
    Boolean paid;
    String TotalSaleid;

public PDFSaleProductCutomerNote(){}

    public PDFSaleProductCutomerNote(String saleProductId, String itemName, Double price, Double quantedt, Double totalPrice, int date, int invoiceNumber, Boolean update, Boolean paid) {
       this.saleProductId = saleProductId;
        this.itemName = itemName;
        this.price = price;
        this.quantedt = quantedt;
        this.totalPrice = totalPrice;
        this.date = date;
        this.invoiceNumber = invoiceNumber;
        this.update = update;
        this.paid = paid;
    }


    public String getSaleProductId() {
        return saleProductId;
    }

    public void setSaleProductId(String saleProductId) {
        this.saleProductId = saleProductId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuantedt() {
        return quantedt;
    }

    public void setQuantedt(Double quantedt) {
        this.quantedt = quantedt;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }
}
