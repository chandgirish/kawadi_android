package com.example.swornim.kawadi.DataStructure;

public class Kawadi{

        /*
        represents the kawadi people who comes in the cycles
         */

    private String kName;
    private String kPNumber;
    private String kStatus;
    private String kProfile;//url of the image
    private String kLocation;




    public Kawadi(){}


    public String getkLocation() {
        return kLocation;
    }

    public void setkLocation(String kLocation) {
        this.kLocation = kLocation;
    }

    public String getkName() {
        return kName;
    }

    public void setkName(String kName) {
        this.kName = kName;
    }

    public String getkPNumber() {
        return kPNumber;
    }

    public void setkPNumber(String kPNumber) {
        this.kPNumber = kPNumber;
    }

    public String getkStatus() {
        return kStatus;
    }

    public void setkStatus(String kStatus) {
        this.kStatus = kStatus;
    }

    public String getkProfile() {
        return kProfile;
    }

    public void setkProfile(String kProfile) {
        this.kProfile = kProfile;
    }
}
