package com.diandi.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

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
@SuppressLint("CommitPrefEdits")
public class SharePreferenceUtil {
    private static SharedPreferences.Editor editor;
    Context mContext;
    private SharedPreferences mSharedPreferences;
    private String SHARED_KEY_NOTIFY = "shared_key_notify";
    private String SHARED_KEY_VOICE = "shared_key_sound";
    private String SHARED_KEY_VIBRATE = "shared_key_vibrate";
    private String SHARED_KEY_LOCK = "shared_key_lock";
    private String SHARED_KEY_UPDATE = "shared_key_update";
    private String SHARED_KEY_FIRST_START = "shared_key_first_start";

    public SharePreferenceUtil(Context context, String name) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    // 是否允许推送通知
    public boolean isAllowPushNotify() {
        return mSharedPreferences.getBoolean(SHARED_KEY_NOTIFY, true);
    }

    public void setPushNotifyEnable(boolean isChecked) {
        editor.putBoolean(SHARED_KEY_NOTIFY, isChecked);
        editor.commit();
    }

    public void checkUpdate() {
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        editor.putBoolean(SHARED_KEY_UPDATE, true);
                        editor.commit();
                        break;
                    case UpdateStatus.No:
                        editor.putBoolean(SHARED_KEY_UPDATE, false);
                        editor.commit();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(mContext);


    }

    public boolean isUpdate() {
        return mSharedPreferences.getBoolean(SHARED_KEY_UPDATE, false);
    }

    // 允许声音
    public boolean isAllowVoice() {
        return mSharedPreferences.getBoolean(SHARED_KEY_VOICE, true);
    }

    public void setAllowVoiceEnable(boolean isChecked) {
        editor.putBoolean(SHARED_KEY_VOICE, isChecked);
        editor.commit();
    }

    // 允许震动
    public boolean isAllowVibrate() {
        return mSharedPreferences.getBoolean(SHARED_KEY_VIBRATE, true);
    }

    public void setAllowVibrateEnable(boolean isChecked) {
        editor.putBoolean(SHARED_KEY_VIBRATE, isChecked);
        editor.commit();
    }

    // 允许密码锁
    public boolean isAllowLock() {
        return mSharedPreferences.getBoolean(SHARED_KEY_LOCK, true);
    }

    public void setAllowLockEnable(boolean isChecked) {
        editor.putBoolean(SHARED_KEY_LOCK, isChecked);
        editor.commit();
    }


    // 判断首次开启
    public boolean isFirstStart() {
        return mSharedPreferences.getBoolean(SHARED_KEY_FIRST_START, true);
    }

    public void setFirstStart(boolean isChecked) {
        editor.putBoolean(SHARED_KEY_FIRST_START, isChecked);
        editor.commit();
    }

}
