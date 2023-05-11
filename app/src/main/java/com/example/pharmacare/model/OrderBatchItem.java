package com.example.pharmacare.model;

public class OrderBatchItem {
    private int id;
    private String created;
    private String createdBy;
    private String lastModified;
    private int itemBatchId;
    private ItemBatch itemBatch;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private double unitCost;
    private double totalCost;
    private boolean isReachedMoq;
    private int sellingTypeId;
    private SellingType sellingType;
    private int sellingTypeCapacity;
    private int orderId;
    private Order order;


    public OrderBatchItem() {
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

    public int getItemBatchId() {
        return itemBatchId;
    }

    public void setItemBatchId(int itemBatchId) {
        this.itemBatchId = itemBatchId;
    }

    public ItemBatch getItemBatch() {
        return itemBatch;
    }

    public void setItemBatch(ItemBatch itemBatch) {
        this.itemBatch = itemBatch;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public boolean isReachedMoq() {
        return isReachedMoq;
    }

    public void setReachedMoq(boolean reachedMoq) {
        isReachedMoq = reachedMoq;
    }

    public int getSellingTypeId() {
        return sellingTypeId;
    }

    public void setSellingTypeId(int sellingTypeId) {
        this.sellingTypeId = sellingTypeId;
    }

    public SellingType getSellingType() {
        return sellingType;
    }

    public void setSellingType(SellingType sellingType) {
        this.sellingType = sellingType;
    }

    public int getSellingTypeCapacity() {
        return sellingTypeCapacity;
    }

    public void setSellingTypeCapacity(int sellingTypeCapacity) {
        this.sellingTypeCapacity = sellingTypeCapacity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
