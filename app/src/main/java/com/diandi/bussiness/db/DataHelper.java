package com.diandi.bussiness.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.diandi.model.Plan;
import com.diandi.model.diandi.LocalDiandi;
import com.diandi.sync.UserHelper;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

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
