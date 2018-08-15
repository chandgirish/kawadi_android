package com.example.swornim.kawadi.DataStructure;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swornim on 2/7/18.
 */

public class WasteData implements Serializable {

    /*wastedata class represents the wastes collection */

    private String sourceType;//HOMES OR OFFICES OR OTHERS
    private String sourceLat;
    private String sourceLon;
    private String sourceId;//unique id of the application# drivers inputs this id
    private String sourceStatus;
    private String distance;
    private String duration;
    private String sourceAmount;//weight of the source

    private String sourceWeight;//weight of the source
    private String address;//geo location address name
    private String sourceOwner;//user phone number
    private String sourcePicker;//drive number who picked


    public String getSourcePicker() {
        return sourcePicker;
    }

    public void setSourcePicker(String sourcePicker) {
        this.sourcePicker = sourcePicker;
    }

    public String getSourceOwner() {
        return sourceOwner;
    }

    public void setSourceOwner(String sourceOwner) {
        this.sourceOwner = sourceOwner;
    }

    private List<Paths> paths = new ArrayList<>();


    public String getSourceAmount() {
        return sourceAmount;
    }

    public void setSourceAmount(String sourceAmount) {
        this.sourceAmount = sourceAmount;
    }

    public String getSourceWeight() {
        return sourceWeight;
    }

    public void setSourceWeight(String sourceWeight) {
        this.sourceWeight = sourceWeight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public List<Paths> getPaths() {
        return paths;
    }

    public void setPaths(List<Paths> paths) {
        this.paths = paths;
    }




}
