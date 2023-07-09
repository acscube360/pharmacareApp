package com.example.pharmacare.model;

public class Supplier {

    private String name;
    private String address;
    private String phone;
    private String email;
    private String contactPerson;
    private String contactPersonPhone;
    private String contactPersonEmail;
    private Object supplierOrders;
    private Object itemBatchExpirations;
    private int id;
    private String created;
    private String createdBy;
    private String lastModified;
    private String lastModifiedBy;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public Object getSupplierOrders() {
        return supplierOrders;
    }

    public void setSupplierOrders(Object supplierOrders) {
        this.supplierOrders = supplierOrders;
    }

    public Object getItemBatchExpirations() {
        return itemBatchExpirations;
    }

    public void setItemBatchExpirations(Object itemBatchExpirations) {
        this.itemBatchExpirations = itemBatchExpirations;
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
