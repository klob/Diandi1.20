package com.diandi.bean;

import java.io.Serializable;

/**
 * ************************************************************
 * *********    User : SuLinger(462679107@qq.com) .
 * *********    Date : 2014-09-17  .
 * *********    Time:  2014-09-17  .
 * *********    Project name :DianDi1.1.0 .
 * *********    Copyright @ 2014, SuLinger, All Rights Reserved
 * *************************************************************
 */


public class ViewDian implements Serializable{

    private String id;

    private boolean isViewEd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isViewEd() {
        return isViewEd;
    }

    public void setViewEd(boolean isViewEd) {
        this.isViewEd = isViewEd;
    }
}
