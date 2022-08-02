package com.carollim.myrecycleapp;

public class HistoryListData {
    private String weightCategory;
    private String companyCollector;
    private String earnPrice;
    private String collectDateTime;


    public HistoryListData(String weightCategory, String companyCollector, String earnPrice, String collectDateTime) {
        this.weightCategory = weightCategory;
        this.companyCollector = companyCollector;
        this.earnPrice = earnPrice;
        this.collectDateTime = collectDateTime;
    }

    public String getWeightCategory() {
        return weightCategory;
    }

    public void setWeightCategory(String weightCategory) {
        this.weightCategory = weightCategory;
    }

    public String getCompanyCollector() {
        return companyCollector;
    }

    public void setCompanyCollector(String companyCollector) {
        this.companyCollector = companyCollector;
    }

    public String getEarnPrice() {
        return earnPrice;
    }

    public void setEarnPrice(String earnPrice) {
        this.earnPrice = earnPrice;
    }

    public String getCollectDateTime() {
        return collectDateTime;
    }

    public void setCollectDateTime(String collectDateTime) {
        this.collectDateTime = collectDateTime;
    }
}
