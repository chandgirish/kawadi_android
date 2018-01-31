package com.example.swornim.kawadi.DataStructure;

/**
 * Created by swornim on 1/19/18.
 */

public class NearbyRequest {
    private Waste waste;//this is the waste to pickup
    private Boolean status;//did the server find the waste for this
    private String truckId;//which driver requested for handling that data# search all except this id


    public NearbyRequest(Waste waste, Boolean status, String truckId) {
        this.waste = waste;
        this.status = status;
        this.truckId = truckId;
    }

    public NearbyRequest() {}

    public Waste getWaste() {
        return waste;
    }

    public void setWaste(Waste waste) {
        this.waste = waste;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }
}
