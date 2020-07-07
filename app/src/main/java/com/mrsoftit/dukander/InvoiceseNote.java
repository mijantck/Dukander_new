package com.mrsoftit.dukander;

public class InvoiceseNote {

    private String id;
    private  int invoice;

    public  InvoiceseNote(){}

    public InvoiceseNote(String id, int invoice) {
        this.id = id;
        this.invoice = invoice;
    }

    public String getId() {
        return id;
    }

    public int getInvoice() {
        return invoice;
    }
}
