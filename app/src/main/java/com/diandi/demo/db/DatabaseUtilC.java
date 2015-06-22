package com.diandi.demo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.diandi.demo.db.DBHelperC.FavTable;
import com.diandi.demo.model.diandi.DianDi;
import com.diandi.demo.sync.UserHelper;
import com.diandi.demo.util.L;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
public class DatabaseUtilC {
    private static final String TAG = "DatabaseUtil";

    private static DatabaseUtilC instance;

    /**
     * 数据库帮助类 *
     */
    private DBHelperC mDbHelperC;

    /**
     * 初始化
     *
     * @param context
     */
    private DatabaseUtilC(Context context) {
        mDbHelperC = new DBHelperC(context);
    }

    public synchronized static DatabaseUtilC getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseUtilC(context);
        }
        return instance;
    }

    /**
     * 销毁
     */
    public static void destory() {
        if (instance != null) {
            instance.onDestory();
        }
    }

    /**
     * 销毁
     */
    public void onDestory() {
        instance = null;
        if (mDbHelperC != null) {
            mDbHelperC.close();
            mDbHelperC = null;
        }
    }


    public void deleteFav(DianDi qy) {
        Cursor cursor = null;
        String where = FavTable.USER_ID + " = '" + UserHelper.getUserId()
                + "' AND " + FavTable.OBJECT_ID + " = '" + qy.getObjectId() + "'";
        cursor = mDbHelperC.query(DBHelperC.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int isLove = cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE));
            if (isLove == 0) {
                mDbHelperC.delete(DBHelperC.TABLE_NAME, where, null);
            } else {
                ContentValues cv = new ContentValues();
                cv.put(FavTable.IS_FAV, 0);
                mDbHelperC.update(DBHelperC.TABLE_NAME, cv, where, null);
            }
        }
        if (cursor != null) {
            cursor.close();
            mDbHelperC.close();
        }
    }


    public boolean isLoved(DianDi qy) {
        Cursor cursor = null;
        String where = FavTable.USER_ID + " = '" + UserHelper.getUserId()
                + "' AND " + FavTable.OBJECT_ID + " = '" + qy.getObjectId() + "'";
        cursor = mDbHelperC.query(DBHelperC.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE)) == 1) {
                return true;
            }
        }
        return false;
    }

    public long insertFav(DianDi qy) {
        long uri = 0;
        Cursor cursor = null;
        String where = FavTable.USER_ID + " = '" + UserHelper.getUserId()
                + "' AND " + FavTable.OBJECT_ID + " = '" + qy.getObjectId() + "'";
        cursor = mDbHelperC.query(DBHelperC.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues conv = new ContentValues();
            conv.put(FavTable.IS_FAV, 1);
            conv.put(FavTable.IS_LOVE, 1);
            mDbHelperC.update(DBHelperC.TABLE_NAME, conv, where, null);
        } else {
            ContentValues cv = new ContentValues();
            cv.put(FavTable.USER_ID, UserHelper.getUserId());
            cv.put(FavTable.OBJECT_ID, qy.getObjectId());
            cv.put(FavTable.IS_LOVE, qy.getMyLove() == true ? 1 : 0);
            cv.put(FavTable.IS_FAV, qy.getMyFav() == true ? 1 : 0);
            uri = mDbHelperC.insert(DBHelperC.TABLE_NAME, null, cv);
        }
        if (cursor != null) {
            cursor.close();
            mDbHelperC.close();
        }
        return uri;
    }

//	    public int deleteFav(DianDi qy){
//	    	int row = 0;
//	    	String where = FavTable.USER_ID+" = "+qy.getAuthor().getObjectId()
//	    			+" AND "+FavTable.OBJECT_ID+" = "+qy.getObjectId();
//	    	row = dbHelper.delete(DBHelper.TABLE_NAME, where, null);
//	    	return row;
//	    }


    /**
     * 设置内容的收藏状态
     *
     * @param lists
     */
    public List<DianDi> setFav(List<DianDi> lists) {
        Cursor cursor = null;
        if (lists != null && lists.size() > 0) {
            for (Iterator iterator = lists.iterator(); iterator.hasNext(); ) {
                DianDi content = (DianDi) iterator.next();
                String where = FavTable.USER_ID + " = '" + UserHelper.getUserId()//content.getAuthor().getObjectId()
                        + "' AND " + FavTable.OBJECT_ID + " = '" + content.getObjectId() + "'";
                cursor = mDbHelperC.query(DBHelperC.TABLE_NAME, null, where, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    if (cursor.getInt(cursor.getColumnIndex(FavTable.IS_FAV)) == 1) {
                        content.setMyFav(true);
                    } else {
                        content.setMyFav(false);
                    }
                    if (cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE)) == 1) {
                        content.setMyLove(true);
                    } else {
                        content.setMyLove(false);
                    }
                }
                L.i(TAG, content.getMyFav() + ".." + content.getMyLove());
            }
        }
        if (cursor != null) {
            cursor.close();
            mDbHelperC.close();
        }
        return lists;
    }

    /**
     * 设置内容的收藏状态
     *
     * @param lists
     */
    public List<DianDi> setFavInFav(List<DianDi> lists) {
        Cursor cursor = null;
        if (lists != null && lists.size() > 0) {
            for (Iterator iterator = lists.iterator(); iterator.hasNext(); ) {
                DianDi content = (DianDi) iterator.next();
                content.setMyFav(true);
                String where = FavTable.USER_ID + " = '" + UserHelper.getUserId()
                        + "' AND " + FavTable.OBJECT_ID + " = '" + content.getObjectId() + "'";
                cursor = mDbHelperC.query(DBHelperC.TABLE_NAME, null, where, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    if (cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE)) == 1) {
                        content.setMyLove(true);
                    } else {
                        content.setMyLove(false);
                    }
                }
                L.i(TAG, content.getMyFav() + ".." + content.getMyLove());
            }
        }
        if (cursor != null) {
            cursor.close();
            mDbHelperC.close();
        }
        return lists;
    }


    public ArrayList<DianDi> queryFav() {
        ArrayList<DianDi> contents = null;
        // ContentResolver resolver = context.getContentResolver();
        Cursor cursor = mDbHelperC.query(DBHelperC.TABLE_NAME, null, null, null, null, null, null);
        L.i(TAG, cursor.getCount() + "");
        if (cursor == null) {
            return null;
        }
        contents = new ArrayList<DianDi>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            DianDi content = new DianDi();
            content.setMyFav(cursor.getInt(3) == 1 ? true : false);
            content.setMyLove(cursor.getInt(4) == 1 ? true : false);
            L.i(TAG, cursor.getColumnIndex("isfav") + ".." + cursor.getColumnIndex("islove") + ".." + content.getMyFav() + "..." + content.getMyLove());
            contents.add(content);
        }
        if (cursor != null) {
            cursor.close();
        }
        // if (contents.size() > 0) {
        // return contents;
        // }
        return contents;
    }

}
