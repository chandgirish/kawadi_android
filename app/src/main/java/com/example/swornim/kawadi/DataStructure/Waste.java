package com.example.swornim.kawadi.DataStructure;

import com.example.swornim.kawadi.MapsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swornim on 1/5/18.
 */

public class Waste {
    private String sourceType;//HOMES OR OFFICES OR OTHERS
    private String sourceLat;
    private String sourceLon;
    private String sourceId;//unique id of the application# drivers inputs this id
    private String sourceStatus;
    private String distance;
    private String duration;
    private String paths;//Json string of arrays format


    public Waste() {}

    public Waste(String sourceType, String sourceLat, String sourceLon, String sourceId, String sourceStatus,String distance,String duration) {
        this.sourceType = sourceType;
        this.sourceLat = sourceLat;
        this.sourceLon = sourceLon;
        this.sourceId = sourceId;
        this.sourceStatus = sourceStatus;
        this.distance = distance;
        this.duration = duration;
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

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(String sourceLat) {
        this.sourceLat = sourceLat;
    }

    public String getSourceLon() {
        return sourceLon;
    }

    public void setSourceLon(String sourceLon) {
        this.sourceLon = sourceLon;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }
}