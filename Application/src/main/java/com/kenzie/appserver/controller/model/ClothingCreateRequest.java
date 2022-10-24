package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class ClothingCreateRequest {

    @NotEmpty
    @JsonProperty("clothingId")
    private String clothingId;

    @NotEmpty
    @JsonProperty("itemName")
    private String itemName;

    @JsonProperty("inStock")
    private Boolean inStock;

    @Min(0)
    @JsonProperty("price")
    private Double price;

    @NotEmpty
    @JsonProperty("itemSize")
    private String itemSize;

    public String getClothingId() {
        return clothingId;
    }

    public void setClothingId(String clothingId) {
        this.clothingId = clothingId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }
}
