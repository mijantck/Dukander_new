package com.mrsoftit.dukander;

public class MyInfoNote {

    private String myid;
    private String dukanName;
    private String dukanphone;
    private String dukanaddress;
    private String dukanaddpicurl;
    private boolean firsttime;
    private String picName;
    private double invesment;
    private double withdrow;
    private double activeBalance;
    private double totalpaybil;
    private int date;
    private String investmentDeleils;
    private String withdrowDeleils;
    private String productname;
    private double product;
    private String token;

    public MyInfoNote() {
    }

    public MyInfoNote(String myid, String dukanName, String dukanphone, String dukanaddress, String dukanaddpicurl, boolean firsttime, String picName, double invesment, double activeBalance, double totalpaybil, int date, String token) {
        this.myid = myid;
        this.dukanName = dukanName;
        this.dukanphone = dukanphone;
        this.dukanaddress = dukanaddress;
        this.dukanaddpicurl = dukanaddpicurl;
        this.firsttime = firsttime;
        this.picName = picName;
        this.invesment = invesment;
        this.activeBalance = activeBalance;
        this.totalpaybil = totalpaybil;
        this.date = date;
        this.token = token;

    }

    public MyInfoNote(String myid, String dukanName, String dukanphone, String dukanaddress, boolean firsttime, String picName, double invesment, double activeBalance, double totalpaybil, int date, String token) {
        this.myid = myid;
        this.dukanName = dukanName;
        this.dukanphone = dukanphone;
        this.dukanaddress = dukanaddress;
        this.firsttime = firsttime;
        this.picName = picName;
        this.invesment = invesment;
        this.activeBalance = activeBalance;
        this.totalpaybil = totalpaybil;
        this.date = date;
        this.token = token;
    }

    public MyInfoNote(String productname, double product, String myid, Double invesment, int date, String investmentDeleils) {
        this.productname = productname;
        this.product = product;
        this.myid = myid;
        this.invesment = invesment;
        this.date = date;
        this.investmentDeleils = investmentDeleils;
    }

    public MyInfoNote(String myid, boolean firsttime, Double withdrow, int date, String withdrowDeleils) {
        this.myid = myid;
        this.firsttime = firsttime;
        this.withdrow = withdrow;
        this.date = date;
        this.withdrowDeleils = withdrowDeleils;
    }


    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getDukanName() {
        return dukanName;
    }

    public void setDukanName(String dukanName) {
        this.dukanName = dukanName;
    }

    public String getDukanphone() {
        return dukanphone;
    }

    public void setDukanphone(String dukanphone) {
        this.dukanphone = dukanphone;
    }

    public String getDukanaddress() {
        return dukanaddress;
    }

    public void setDukanaddress(String dukanaddress) {
        this.dukanaddress = dukanaddress;
    }

    public String getDukanaddpicurl() {
        return dukanaddpicurl;
    }

    public void setDukanaddpicurl(String dukanaddpicurl) {
        this.dukanaddpicurl = dukanaddpicurl;
    }

    public boolean isFirsttime() {
        return firsttime;
    }

    public void setFirsttime(boolean firsttime) {
        this.firsttime = firsttime;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public double getInvesment() {
        return invesment;
    }

    public void setInvesment(double invesment) {
        this.invesment = invesment;
    }

    public double getWithdrow() {
        return withdrow;
    }

    public void setWithdrow(double withdrow) {
        this.withdrow = withdrow;
    }

    public double getActiveBalance() {
        return activeBalance;
    }

    public void setActiveBalance(double activeBalance) {
        this.activeBalance = activeBalance;
    }

    public double getTotalpaybil() {
        return totalpaybil;
    }

    public void setTotalpaybil(double totalpaybil) {
        this.totalpaybil = totalpaybil;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getInvestmentDeleils() {
        return investmentDeleils;
    }

    public void setInvestmentDeleils(String investmentDeleils) {
        this.investmentDeleils = investmentDeleils;
    }

    public String getWithdrowDeleils() {
        return withdrowDeleils;
    }

    public void setWithdrowDeleils(String withdrowDeleils) {
        this.withdrowDeleils = withdrowDeleils;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public double getProduct() {
        return product;
    }

    public void setProduct(double product) {
        this.product = product;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
