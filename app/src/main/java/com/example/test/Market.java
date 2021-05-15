package com.example.test;

public class Market {

    public int _id;
    public String storeName;
    public String franchise;
    public double latitude;
    public double longitude;
    public String address;

    // set function
    public void setId(int _id){ this._id = _id; }
    public void setStoreName(String storeName){ this.storeName = storeName; }
    public void setFranchise(String franchise){ this.franchise = franchise; }
    public void setLatitude(double latitude){ this.latitude = latitude; }
    public void setLongitude(double longitude){ this.longitude = longitude; }
    public void setAddress(String address){ this.address = address; }

    // get function
    public int getId(){ return this._id; }
    public String getStoreName(){ return this.storeName; }
    public String getFranchise(){ return this.franchise; }
    public double getLatitude(){ return this.latitude; }
    public double getLongitude(){ return this.longitude; }
    public String getAddress(){ return this.address; }

}
