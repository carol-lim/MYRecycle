package com.carollim.myrecycleapp;

public class Reservation {

    String address, category, date, time, status, uid, collectorId;

    public Reservation(String address, String category, String date, String time, String status, String uid, String collectorId) {
        this.address = address;
        this.category = category;
        this.date = date;
        this.time = time;
        this.status = status;
        this.uid = uid;
        this.collectorId = collectorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
