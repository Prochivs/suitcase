package com.example.suit_case_application.model;

public class ItemDetail {
    private String itemId;
    private String itemImage;
    private String itemName;
    private String itemPrice;
    private String itemDate;
    private String itemAddress;
    private String itemDescription;
    public double itemLatitude;
    public double itemLongitude;

    public ItemDetail() {
    }

    public ItemDetail(String itemId, String itemImage, String itemName, String itemPrice, String itemDate, String itemAddress, String itemDescription, double itemLatitude, double itemLongitude) {
        this.itemId = itemId;
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDate = itemDate;
        this.itemAddress = itemAddress;
        this.itemDescription = itemDescription;
        this.itemLatitude = itemLatitude;
        this.itemLongitude = itemLongitude;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

    public String getItemAddress() {
        return itemAddress;
    }

    public void setItemAddress(String itemAddress) {
        this.itemAddress = itemAddress;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getItemLatitude() {
        return itemLatitude;
    }

    public void setItemLatitude(double itemLatitude) {
        this.itemLatitude = itemLatitude;
    }

    public double getItemLongitude() {
        return itemLongitude;
    }

    public void setItemLongitude(double itemLongitude) {
        this.itemLongitude = itemLongitude;
    }
}