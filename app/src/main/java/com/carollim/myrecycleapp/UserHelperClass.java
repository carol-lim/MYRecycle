package com.carollim.myrecycleapp;

public class UserHelperClass {
    String name, email, countryCode,  phNum, address, postalCode, city, state, userType;

    public UserHelperClass() {
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhNum() {
        return phNum;
    }

    public void setPhNum(String phNum) {
        this.phNum = phNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public UserHelperClass(String name, String email, String countryCode, String phNum, String address, String postalCode, String city, String state, String userType) {
        this.name = name;
        this.email = email;
        this.countryCode = countryCode;
        this.phNum = phNum;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.userType = userType;
    }


}
