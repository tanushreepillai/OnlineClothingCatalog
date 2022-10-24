package com.kenzie.appserver.service.model;

public class Clothing {
    private final String clothingId;
    private final String itemName;
    private final Boolean inStock;
    private final Double price;
    private final String itemSize;

    public Clothing(String clothingId, String itemName, Boolean inStock, Double price, String itemSize) {
        this.clothingId = clothingId;
        this.itemName = itemName;
        this.inStock = inStock;
        this.price = price;
        this.itemSize = itemSize;
    }

    public String getClothingId() {
        return clothingId;
    }

    public String getItemName() {
        return itemName;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public Double getPrice() {
        return price;
    }

    public String getItemSize() {
        return itemSize;
    }
}
