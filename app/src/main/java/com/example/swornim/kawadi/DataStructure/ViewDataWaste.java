package com.example.swornim.kawadi.DataStructure;

import java.io.Serializable;
import java.util.List;

/**
 * Created by swornim on 2/7/18.
 */

public class ViewDataWaste implements Serializable{

    private List<WasteData> totalWastes;

    public List<WasteData> getTotalWastes() {
        return totalWastes;
    }

    public void setTotalWastes(List<WasteData> totalWastes) {
        this.totalWastes = totalWastes;
    }
}
