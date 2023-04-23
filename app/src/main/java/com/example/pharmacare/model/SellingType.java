package com.example.pharmacare.model;

public class SellingType {

    private String name;
    private int id;

    public SellingType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public SellingType() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
