package com.example.swornim.kawadi.DataStructure;

public class Authentication {
    private String uPNumber;
    private String uName;
    private String uPassword;
    private String uType;
    private String uLocationLat;
    private String uLocationLon;

    public String getuLocationLat() {
        return uLocationLat;
    }

    public void setuLocationLat(String uLocationLat) {
        this.uLocationLat = uLocationLat;
    }

    public String getuLocationLon() {
        return uLocationLon;
    }

    public void setuLocationLon(String uLocationLon) {
        this.uLocationLon = uLocationLon;
    }

    public String getuType() {
        return uType;
    }

    public void setuType(String uType) {
        this.uType = uType;
    }

    public String getuPNumber() {
        return uPNumber;
    }

    public void setuPNumber(String uPNumber) {
        this.uPNumber = uPNumber;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }
}
