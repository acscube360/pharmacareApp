package com.example.pharmacare.model;

public class ItemBatch {
    private int id;
    private String batchName;

    public ItemBatch(int id, String batchName) {
        this.id = id;
        this.batchName = batchName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return batchName;
    }

    public void setName(String batchName) {
        this.batchName = batchName;
    }
}
