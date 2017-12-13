package com.example.lg.payday;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by LG on 2017-12-04.
 */

public class ListViewItem {
    private String workSpace;
    private String wage;
    ListViewItem(){

    }
    public String getWorkSpace() {
        return workSpace;
    }


    public void setWorkSpace(String workSpace) {
        this.workSpace = workSpace;

    }
    public String getWage() {

        return wage;
    }


    public void setWage(String wage) {
        this.wage = wage;

    }

}
