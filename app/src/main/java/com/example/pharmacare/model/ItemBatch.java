package com.example.pharmacare.model;

public class ItemBatch {
    private int id;
    private String batchName;
    private Item item;

    public ItemBatch(int id, String batchName) {
        this.id = id;
        this.batchName = batchName;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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
