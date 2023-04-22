package com.example.pharmacare.model;

public class OrderItem {
    private String name;
    private String batch_no;
    private SellingType  sellingType;
    private int quantity;


    public OrderItem(String name, String batch_no, SellingType sellingType, int quantity) {
        this.name = name;
        this.batch_no = batch_no;
        this.sellingType = sellingType;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public SellingType getSellingType() {
        return sellingType;
    }

    public void setSellingType(SellingType sellingType) {
        this.sellingType = sellingType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
