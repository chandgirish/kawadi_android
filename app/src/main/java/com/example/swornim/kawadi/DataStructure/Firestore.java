package com.example.swornim.kawadi.DataStructure;

import java.io.Serializable;

/**
 * Created by swornim on 1/5/18.
 */

public  class   Firestore {
    private Drivers drivers;
    private Wastages wastages;

    public Firestore() {}

    public Firestore(Drivers drivers, Wastages wastages) {
        this.drivers = drivers;
        this.wastages = wastages;
    }

    public Drivers getDrivers() {
        return drivers;
    }

    public void setDrivers(Drivers drivers) {
        this.drivers = drivers;
    }

    public Wastages getWastages() {
        return wastages;
    }

    public void setWastages(Wastages wastages) {
        this.wastages = wastages;
    }

    public class Drivers{
        private String driverName;
        private String driverAge;
        private String driverSex;

        public Drivers() {}

        public Drivers(String driverName, String driverAge, String driverSex) {
            this.driverName = driverName;
            this.driverAge = driverAge;
            this.driverSex = driverSex;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getDriverAge() {
            return driverAge;
        }

        public void setDriverAge(String driverAge) {
            this.driverAge = driverAge;
        }

        public String getDriverSex() {
            return driverSex;
        }

        public void setDriverSex(String driverSex) {
            this.driverSex = driverSex;
        }
    }

    public class Wastages{
        private String wasteTypes;
        private String wasteSource;

        public Wastages() {}

        public Wastages(String wasteTypes, String wasteSource) {
            this.wasteTypes = wasteTypes;
            this.wasteSource = wasteSource;
        }

        public String getWasteTypes() {
            return wasteTypes;
        }

        public void setWasteTypes(String wasteTypes) {
            this.wasteTypes = wasteTypes;
        }

        public String getWasteSource() {
            return wasteSource;
        }

        public void setWasteSource(String wasteSource) {
            this.wasteSource = wasteSource;
        }
    }


}
