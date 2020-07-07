package com.mrsoftit.dukander;

public class ProductSaleNote {

    private String productSaleId;
    private String proName;
    private double price;
    private double producQuantidy;
    private double totalPrice;
    private int date;




    public ProductSaleNote(){}

    public ProductSaleNote(String productSaleId, String proName, double price, double producQuantidy, double totalPrice, int date) {
        this.productSaleId = productSaleId;
        this.proName = proName;
        this.price = price;
        this.producQuantidy = producQuantidy;
        this.totalPrice = totalPrice;
        this.date = date;
    }


    public String getProductSaleId() {
        return productSaleId;
    }

    public String getProName() {
        return proName;
    }

    public double getPrice() {
        return price;
    }

    public double getProducQuantidy() {
        return producQuantidy;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getDate() {
        return date;
    }

}
