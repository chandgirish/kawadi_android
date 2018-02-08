package com.example.swornim.kawadi.DataStructure;

import java.io.Serializable;
import java.util.List;

/**
 * Created by swornim on 2/7/18.
 */

public class ViewDataWaste implements Serializable{

    private List<Waste> totalWastes;

    public List<Waste> getTotalWastes() {
        return totalWastes;
    }

    public void setTotalWastes(List<Waste> totalWastes) {
        this.totalWastes = totalWastes;
    }
}
