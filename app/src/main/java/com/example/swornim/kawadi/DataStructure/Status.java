package com.example.swornim.kawadi.DataStructure;

import java.io.Serializable;

public class Status implements Serializable {
    private String statusCode;
    private String statusType;


    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }
}
