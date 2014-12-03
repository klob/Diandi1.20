package com.diandi.bussiness.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tencent.connect.UserInfo;

import java.io.File;

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

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something
    // appropriate for your app
    private static final String DATABASE_NAME = "CHENGYIJI.db";

    // any time you make changes to your database objects, you may have to
    // increase the database version
    private static final int DATABASE_VERSION = 1;

// the DAO object we use to access the SimpleData table

    //数据库默认路径SDCard
    private static String DATABASE_PATH = Environment.getExternalStorageDirectory()
            + "/CHENGYIJI.db";
    private static String DATABASE_PATH_JOURN = Environment.getExternalStorageDirectory()
            + "/CHEYIJI.db-journal";
    private Context mContext;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        initDtaBasePath();
        try {

            File f = new File(DATABASE_PATH);
            if (!f.exists()) {
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH, null);
                onCreate(db);
                db.close();
            }
        } catch (Exception e) {
        }
    }

    //如果没有SDCard 默认存储在项目文件目录下
    private void initDtaBasePath() {
        DATABASE_PATH = mContext.getFilesDir().getAbsolutePath() + "/CHENGYIJI.db";
        DATABASE_PATH_JOURN = mContext.getFilesDir().getAbsolutePath() + "/CHEYIJI.db-journal";
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * This is called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        Log.i(DatabaseHelper.class.getName(), "onCreate");
        try {
            TableUtils.createTable(connectionSource, UserInfo.class);
        } catch (java.sql.SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * This is called when your application is upgraded and it has a higher
     * version number. This allows you to adjust the various data to match the
     * new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        Log.i(DatabaseHelper.class.getName(), "onUpgrade");
        try {
            TableUtils.dropTable(connectionSource, UserInfo.class, true);
            onCreate(db, connectionSource);
        } catch (java.sql.SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    public void deleteDB() {
        if (mContext != null) {
            File f = mContext.getDatabasePath(DATABASE_NAME);
            if (f.exists()) {
                // mContext.deleteDatabase(DATABASE_NAME);
                Log.e("DB", "---delete SDCard DB---");
                f.delete();
            } else {
                Log.e("DB", "---delete App DB---");
                mContext.deleteDatabase(DATABASE_NAME);
            }

            File file = mContext.getDatabasePath(DATABASE_PATH);
            if (file.exists()) {
                Log.e("DB", "---delete SDCard DB 222---");
                file.delete();
            }

            File file2 = mContext.getDatabasePath(DATABASE_PATH_JOURN);
            if (file2.exists()) {
                Log.e("DB", "---delete SDCard DB 333---");
                file2.delete();
            }
        }
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
    }
}
