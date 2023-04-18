package com.example.pharmacare.model;

import java.util.ArrayList;

public class Item {
    private int id;
    private String name;
    private String measurement;
    private String imageUrl;
    private String barcode;
    private String expirationThreshold;
    private String itemSubCategoryId;
    private ItemSubCategory itemSubCategory;
    private String status;
    private int minimumOrderQuantity;
    private String strength;
    private String drugCode;
    private ArrayList<ItemSellingType> itemSellingTypes;
    private String itemBatches;
    private String created;
    private String createdBy;
    private String lastModified;
    private String lastModifiedBy;





    public Item(int id, String name, String measurement, String imageUrl, String barcode, String expirationThreshold, String itemSubCategoryId, ItemSubCategory itemSubCategory, String status, int minimumOrderQuantity, String strength, String drugCode, ArrayList<ItemSellingType> itemSellingTypes, String itemBatches, String created, String createdBy, String lastModified, String lastModifiedBy) {
        this.id = id;
        this.name = name;
        this.measurement = measurement;
        this.imageUrl = imageUrl;
        this.barcode = barcode;
        this.expirationThreshold = expirationThreshold;
        this.itemSubCategoryId = itemSubCategoryId;
        this.itemSubCategory = itemSubCategory;
        this.status = status;
        this.minimumOrderQuantity = minimumOrderQuantity;
        this.strength = strength;
        this.drugCode = drugCode;
        this.itemSellingTypes = itemSellingTypes;
        this.itemBatches = itemBatches;
        this.created = created;
        this.createdBy = createdBy;
        this.lastModified = lastModified;
        this.lastModifiedBy = lastModifiedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getExpirationThreshold() {
        return expirationThreshold;
    }

    public void setExpirationThreshold(String expirationThreshold) {
        this.expirationThreshold = expirationThreshold;
    }

    public String getItemSubCategoryId() {
        return itemSubCategoryId;
    }

    public void setItemSubCategoryId(String itemSubCategoryId) {
        this.itemSubCategoryId = itemSubCategoryId;
    }

    public ItemSubCategory getItemSubCategory() {
        return itemSubCategory;
    }

    public void setItemSubCategory(ItemSubCategory itemSubCategory) {
        this.itemSubCategory = itemSubCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMinimumOrderQuantity() {
        return minimumOrderQuantity;
    }

    public void setMinimumOrderQuantity(int minimumOrderQuantity) {
        this.minimumOrderQuantity = minimumOrderQuantity;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public ArrayList<ItemSellingType> getItemSellingTypes() {
        return itemSellingTypes;
    }

    public void setItemSellingTypes(ArrayList<ItemSellingType> itemSellingTypes) {
        this.itemSellingTypes = itemSellingTypes;
    }

    public String getItemBatches() {
        return itemBatches;
    }

    public void setItemBatches(String itemBatches) {
        this.itemBatches = itemBatches;
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
