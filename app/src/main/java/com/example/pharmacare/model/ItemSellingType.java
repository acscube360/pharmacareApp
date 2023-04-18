package com.example.pharmacare.model;

public class ItemSellingType {
    private int sellingTypeId;
    private String sellingType;
    private int itemId;
    private String item;
    private int capacity;
    private int id;
    private String created;
    private String createdBy;
    private String lastModified;
    private String lastModifiedBy;


    public ItemSellingType(int sellingTypeId, String sellingType, int itemId, String item, int capacity, int id, String created, String createdBy, String lastModified, String lastModifiedBy) {
        this.sellingTypeId = sellingTypeId;
        this.sellingType = sellingType;
        this.itemId = itemId;
        this.item = item;
        this.capacity = capacity;
        this.id = id;
        this.created = created;
        this.createdBy = createdBy;
        this.lastModified = lastModified;
        this.lastModifiedBy = lastModifiedBy;
    }

    public int getSellingTypeId() {
        return sellingTypeId;
    }

    public void setSellingTypeId(int sellingTypeId) {
        this.sellingTypeId = sellingTypeId;
    }

    public String getSellingType() {
        return sellingType;
    }

    public void setSellingType(String sellingType) {
        this.sellingType = sellingType;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
