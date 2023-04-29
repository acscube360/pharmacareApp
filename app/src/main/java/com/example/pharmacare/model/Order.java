package com.example.pharmacare.model;

import java.io.Serializable;

public class Order implements Serializable {

    private String remark;
    private int status;
    private String imageUrl;
    private int id;
    private String created;
    private String createdBy;
    private String lastModified;
    private String lastModifiedBy;

    public Order() {
    }

    public Order(String remark, String created, String lastModifiedBy) {
        this.remark = remark;
        this.created = created;
        this.lastModifiedBy = lastModifiedBy;
    }

    public Order(String remark, int status, String imageUrl, int id, String created, String createdBy, String lastModified, String lastModifiedBy) {
        this.remark = remark;
        this.status = status;
        this.imageUrl = imageUrl;
        this.id = id;
        this.created = created;
        this.createdBy = createdBy;
        this.lastModified = lastModified;
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
