package com.diandi.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.diandi.bean.LocalDiandi;
import com.diandi.bean.Plan;
import com.diandi.sync.UserHelper;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * ************************************************************
 * *********    User : SuLinger(462679107@qq.com) .
 * *********    Date : 2014-09-10  .
 * *********    Time:  2014-09-10  .
 * *********    Project name :PBOX1.3 .
 * *********    Copyright @ 2014, SuLinger, All Rights Reserved
 * *************************************************************
 */


public class DataHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = UserHelper.getUserId() + "_diandi.db";
    private static final int DATABASE_VERSION = 1;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Plan.class);
            TableUtils.createTable(connectionSource, LocalDiandi.class);
        } catch (SQLException e) {
            Log.e(DataHelper.class.getName(), "创建数据库失败", e);
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, Plan.class, true);
        } catch (SQLException e) {
            Log.e(DataHelper.class.getName(), "更新数据库失败", e);
            e.printStackTrace();
        }
    }
}
