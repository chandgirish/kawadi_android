package com.example.swornim.kawadi.DataStructure;

import java.io.Serializable;

public class Status implements Serializable {
    private String type;
    private String sourceId;
    private String driverPhoneNumber;
    private String sourceStatus;
    private String truckDriverPnumber;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getTruckDriverPnumber() {
        return truckDriverPnumber;
    }

    public void setTruckDriverPnumber(String truckDriverPnumber) {
        this.truckDriverPnumber = truckDriverPnumber;
    }
}
