package com.carollim.myrecycleapp;

public class MyReserveListData {
    private String rId, wasteCategoryName, reserveDateTime, reserveStatus, date, time , address, collector, user;

    public MyReserveListData() {
    }

    public MyReserveListData(String rId, String wasteCategoryName, String reserveDateTime, String reserveStatus, String date, String time, String address, String collector, String user) {
        this.rId = rId;
        this.wasteCategoryName = wasteCategoryName;
        this.reserveDateTime = reserveDateTime;
        this.reserveStatus = reserveStatus;
        this.date = date;
        this.time = time;
        this.address = address;
        this.collector = collector;
        this.user = user;
    }

    public String getRId() {
        return rId;
    }

    public void setRId(String rId) {
        this.rId = rId;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getWasteCategoryName() {
        return wasteCategoryName;
    }

    public void setWasteCategoryName(String wasteCategoryName) {
        this.wasteCategoryName = wasteCategoryName;
    }

    public String getReserveDateTime() {
        return reserveDateTime;
    }

    public void setReserveDateTime(String reserveDateTime) {
        this.reserveDateTime = reserveDateTime;
    }

    public String getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(String reserveStatus) {
        this.reserveStatus = reserveStatus;
    }


}
