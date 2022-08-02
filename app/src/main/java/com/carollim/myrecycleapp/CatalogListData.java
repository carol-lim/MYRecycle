package com.carollim.myrecycleapp;

public class CatalogListData {

    private String category, imageUrl;
    private Double kgPrice;
    private String desc;

    public CatalogListData(String imageUrl, String category, Double kgPrice, String desc) {
        this.imageUrl = imageUrl;
        this.category = category;
        this.kgPrice = kgPrice;
        this.desc = desc;
    }

    public CatalogListData() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getKgPrice() {
        return kgPrice;
    }

    public void setKgPrice(Double kgPrice) {
        this.kgPrice = kgPrice;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
