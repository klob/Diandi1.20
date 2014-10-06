package com.diandi.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * ************************************************************
 * *********    User : SuLinger(462679107@qq.com) .
 * *********    Date : 2014-09-19  .
 * *********    Time:  2014-09-19  .
 * *********    Project name :DianDi1.1.5 .
 * *********    Copyright @ 2014, SuLinger, All Rights Reserved
 * *************************************************************
 */

@DatabaseTable(tableName = "diandi")
public class LocalDiandi {

    public static String _ID = "_id";
    public static String USER_ID = "userId";
    public static String OBJECT_ID = "objectId";
    public static String IS_LOVE = "isLove";
    public static String IS_FAV = "isFav";


    @DatabaseField(useGetSet = true, generatedId = true)
    private int _id;
    @DatabaseField(useGetSet = true)
    private String userId;
    @DatabaseField(useGetSet = true)
    private String objectId;
    @DatabaseField(useGetSet = true, defaultValue = "false")
    private String isLove;
    @DatabaseField(useGetSet = true, defaultValue = "false")
    private String isFav;

    public String getIsFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }

    public String getIsLove() {
        return isLove;
    }

    public void setIsLove(String isLove) {
        this.isLove = isLove;
    }

    @Override
    public String toString() {
        return "LocalDiandi{" +
                "_id=" + _id +
                ", userId='" + userId + '\'' +
                ", objectId='" + objectId + '\'' +
                ", isLove=" + isLove +
                ", isFav=" + isFav +
                '}';
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }



}
