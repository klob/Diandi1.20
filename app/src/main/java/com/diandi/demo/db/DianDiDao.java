package com.diandi.demo.db;

import android.content.Context;
import android.util.Log;

import com.diandi.demo.config.Constant;
import com.diandi.demo.model.diandi.DianDi;
import com.diandi.demo.model.diandi.LocalDiandi;
import com.diandi.demo.sync.UserHelper;
import com.j256.ormlite.dao.Dao;

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


public class DianDiDao extends BaseDao<LocalDiandi> {
    private static final String TAG = "DianDiDao";
    private static DianDiDao mInstance;

    public DianDiDao(Context context) {
        super(context);
    }

    public static DianDiDao getInstance(Context context) {

        if (mInstance != null) {
            return mInstance;
        } else {
            return new DianDiDao(context);
        }
    }

    @Override
    public Dao<LocalDiandi, Integer> getDao() {
        if (mDao == null) {
            try {
                mDao = getHelper().getDao(LocalDiandi.class);
            } catch (SQLException e) {
                Log.e(TAG, "得到Dao失败");
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean isLoved(DianDi dianDi) {
        LocalDiandi localDiandi = null;
        try {
            localDiandi = queryByParams(new String[]{LocalDiandi.USER_ID, LocalDiandi.OBJECT_ID}, new String[]{UserHelper.getUserId(), dianDi.getObjectId()});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return localDiandi.getIsLove().equals(Constant.TRUE);
    }

    public void deleteFav(DianDi dianDi) {
        LocalDiandi localDiandi = null;
        try {
            localDiandi = queryByParams(new String[]{LocalDiandi.USER_ID, LocalDiandi.OBJECT_ID}, new String[]{UserHelper.getUserId(), dianDi.getObjectId()});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        localDiandi.setIsFav(Constant.FALSE);
        create(localDiandi);
    }

    public void setFav(DianDi dianDi) {
        LocalDiandi localDiandi = null;
        localDiandi.setUserId(UserHelper.getUserId());
        localDiandi.setObjectId(dianDi.getObjectId());
        localDiandi.setIsFav(dianDi.getMyLove() == true ? Constant.TRUE : Constant.FALSE);
        localDiandi.setIsFav(dianDi.getMyFav() == true ? Constant.TRUE : Constant.FALSE);
        create(localDiandi);
    }

}
