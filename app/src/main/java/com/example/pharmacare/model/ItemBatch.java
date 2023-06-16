package com.example.pharmacare.model;

import java.io.Serializable;

public class ItemBatch implements Serializable {
    private int id;
    private String batchName;
    private Item item;
    private int stock;
    private int receivedStock;
    private int unitCost;
    private int unitPrice;

    private String expiryDate   ;

    public ItemBatch(int id, String batchName,int stock) {
        this.id = id;
        this.batchName = batchName;
        this.stock = stock;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getReceivedStock() {
        return receivedStock;
    }

    public void setReceivedStock(int receivedStock) {
        this.receivedStock = receivedStock;
    }

    public int getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(int unitCost) {
        this.unitCost = unitCost;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }


    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
