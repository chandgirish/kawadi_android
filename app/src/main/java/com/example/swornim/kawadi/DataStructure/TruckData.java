package com.example.swornim.kawadi.DataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TruckData implements Serializable{
    private String truckId;
    private String truckDriverName;
    private String truckDriverPnumber;
    private String truckPosLat;//current lattitude
    private String truckPosLon;//current longitude
    private String timestamp;
    private String distance;//to compare other trucks distance for sorting nearby trucks from that waste
    private String duration;//to compare other trucks duration time from that request wastes
    private boolean selfRequest;//request get me the nearby wastes
    private boolean status;//currently busy or free #busy means someones load is handled free means you can assign him/her the new pending task
    private String truckwaste;


    public String getTruckwaste() {
        return truckwaste;
    }

    public void setTruckwaste(String truckwaste) {
        this.truckwaste = truckwaste;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getTruckDriverName() {
        return truckDriverName;
    }

    public void setTruckDriverName(String truckDriverName) {
        this.truckDriverName = truckDriverName;
    }

    public String getTruckDriverPnumber() {
        return truckDriverPnumber;
    }

    public void setTruckDriverPnumber(String truckDriverPnumber) {
        this.truckDriverPnumber = truckDriverPnumber;
    }

    public String getTruckPosLat() {
        return truckPosLat;
    }

    public void setTruckPosLat(String truckPosLat) {
        this.truckPosLat = truckPosLat;
    }

    public String getTruckPosLon() {
        return truckPosLon;
    }

    public void setTruckPosLon(String truckPosLon) {
        this.truckPosLon = truckPosLon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isSelfRequest() {
        return selfRequest;
    }

    public void setSelfRequest(boolean selfRequest) {
        this.selfRequest = selfRequest;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }



}
