package com.diandi.demo.model;

import com.diandi.demo.util.TimeUtil;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * *******************************************************************************
 * *********    Author : klob(kloblic@gmail.com) .
 * *********    Date : 2014-11-29  .
 * *********    Time : 11:46 .
 * *********    Project name : Diandi1.18 .
 * *********    Version : 1.0
 * *********    Copyright @ 2014, klob, All Rights Reserved
 * *******************************************************************************
 */

@DatabaseTable(tableName = "plan")
public class Plan implements Serializable {
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String PROGRESS = "progress";
    public static final String CATEGORY_APLAN = "A计划";
    public static final String CATEGORY_BPLAN = "B计划";
    public static final String CATEGORY_CPLAN = "C计划";
    public static final String IOP = "top";


    public static final String PLAN_NOTE = "note";
    public final static String PLAN_ID = "_id";

    public final static int URGENT_TOP = 1;
    public final static int URGENT_EXTRA = 2;
    public final static int URGENT_HIGH = 3;
    public final static int URGENT_MIDDLE = 4;
    public final static int URGENT_LOW = 5;
    public final static int URGENT_FINISHED = 6;
    @DatabaseField(useGetSet = true, generatedId = true)
    private int _id;
    @DatabaseField(useGetSet = true, defaultValue = "")
    private String title;
    @DatabaseField(dataType = DataType.DATE, useGetSet = true)
    private Date planDate;
    @DatabaseField(useGetSet = true, defaultValue = "0")
    private int progress;
    @DatabaseField(useGetSet = true, defaultValue = "0")
    private String category;
    @DatabaseField(useGetSet = true, defaultValue = "0")
    private String top;
    @DatabaseField(useGetSet = true, defaultValue = "")
    private String note;
    @DatabaseField(useGetSet = true, defaultValue = "6")
    private int type;

    private Date endDate;

    public Plan() {
    }

    @Override
    public String toString() {
        return "Plan{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", date=" + TimeUtil.getDatetimeString(planDate) +
                ", progress=" + progress +
                ", category='" + category + '\'' +
                ", top='" + top + '\'' +
                ", note='" + note + '\'' +
                ", type=" + type +
                '}';
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
