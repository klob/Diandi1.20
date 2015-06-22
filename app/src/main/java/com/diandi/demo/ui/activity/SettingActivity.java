package com.diandi.demo.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diandi.demo.BuildConfig;
import com.diandi.demo.R;
import com.diandi.demo.util.SharePreferenceUtil;
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

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout mCheckUpdateLayout, rl_switch_notification, rl_switch_voice,
            rl_switch_vibrate;
    ImageView iv_open_notification, iv_close_notification, iv_open_voice,
            iv_close_voice, iv_open_vibrate, iv_close_vibrate;
    SharePreferenceUtil mSharedUtil;
    View view1, view2;
    private TextView mVersionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    public void findView() {
        setContentView(R.layout.activity_setting);
        rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
        rl_switch_voice = (RelativeLayout) findViewById(R.id.rl_switch_voice);
        rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
        mCheckUpdateLayout = (RelativeLayout) findViewById(R.id.activity_set_check_update_layout);
        mVersionText = (TextView) findViewById(R.id.activity_set_version_text);
        rl_switch_notification.setOnClickListener(this);
        rl_switch_voice.setOnClickListener(this);
        rl_switch_vibrate.setOnClickListener(this);
        mCheckUpdateLayout.setOnClickListener(this);

        iv_open_notification = (ImageView) findViewById(R.id.iv_open_notification);
        iv_close_notification = (ImageView) findViewById(R.id.iv_close_notification);
        iv_open_voice = (ImageView) findViewById(R.id.iv_open_voice);
        iv_close_voice = (ImageView) findViewById(R.id.iv_close_voice);
        iv_open_vibrate = (ImageView) findViewById(R.id.iv_open_vibrate);
        iv_close_vibrate = (ImageView) findViewById(R.id.iv_close_vibrate);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);

    }


    void initView() {
        initTopBarForLeft("消息提醒设置");
        mVersionText.setText("v" + BuildConfig.VERSION_NAME);
        initData();
        bindEvent();
    }

    void initData() {
        mSharedUtil = mApplication.getSpUtil();
        boolean isAllowNotify = mSharedUtil.isAllowPushNotify();
        if (isAllowNotify) {
            iv_open_notification.setVisibility(View.VISIBLE);
            iv_close_notification.setVisibility(View.INVISIBLE);
        } else {
            iv_open_notification.setVisibility(View.INVISIBLE);
            iv_close_notification.setVisibility(View.VISIBLE);
        }
        boolean isAllowVoice = mSharedUtil.isAllowVoice();
        if (isAllowVoice) {
            iv_open_voice.setVisibility(View.VISIBLE);
            iv_close_voice.setVisibility(View.INVISIBLE);
        } else {
            iv_open_voice.setVisibility(View.INVISIBLE);
            iv_close_voice.setVisibility(View.VISIBLE);
        }
        boolean isAllowVibrate = mSharedUtil.isAllowVibrate();
        if (isAllowVibrate) {
            iv_open_vibrate.setVisibility(View.VISIBLE);
            iv_close_vibrate.setVisibility(View.INVISIBLE);
        } else {
            iv_open_vibrate.setVisibility(View.INVISIBLE);
            iv_close_vibrate.setVisibility(View.VISIBLE);
        }
    }

    void bindEvent() {
        rl_switch_notification.setOnClickListener(this);
        rl_switch_voice.setOnClickListener(this);
        rl_switch_vibrate.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_switch_notification:
                if (iv_open_notification.getVisibility() == View.VISIBLE) {
                    iv_open_notification.setVisibility(View.INVISIBLE);
                    iv_close_notification.setVisibility(View.VISIBLE);
                    mSharedUtil.setPushNotifyEnable(false);
                    rl_switch_vibrate.setVisibility(View.GONE);
                    rl_switch_voice.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                } else {
                    iv_open_notification.setVisibility(View.VISIBLE);
                    iv_close_notification.setVisibility(View.INVISIBLE);
                    mSharedUtil.setPushNotifyEnable(true);
                    rl_switch_vibrate.setVisibility(View.VISIBLE);
                    rl_switch_voice.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.rl_switch_voice:
                if (iv_open_voice.getVisibility() == View.VISIBLE) {
                    iv_open_voice.setVisibility(View.INVISIBLE);
                    iv_close_voice.setVisibility(View.VISIBLE);
                    mSharedUtil.setAllowVoiceEnable(false);
                } else {
                    iv_open_voice.setVisibility(View.VISIBLE);
                    iv_close_voice.setVisibility(View.INVISIBLE);
                    mSharedUtil.setAllowVoiceEnable(true);
                }

                break;
            case R.id.rl_switch_vibrate:
                if (iv_open_vibrate.getVisibility() == View.VISIBLE) {
                    iv_open_vibrate.setVisibility(View.INVISIBLE);
                    iv_close_vibrate.setVisibility(View.VISIBLE);
                    mSharedUtil.setAllowVibrateEnable(false);
                } else {
                    iv_open_vibrate.setVisibility(View.VISIBLE);
                    iv_close_vibrate.setVisibility(View.INVISIBLE);
                    mSharedUtil.setAllowVibrateEnable(true);
                }
                break;
            case R.id.activity_set_check_update_layout:
                checkUpdate();
                break;

        }
    }

    private void checkUpdate() {
        Toast.makeText(mContext, "正在检查。。。", Toast.LENGTH_SHORT).show();
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        Log.e(TAG, "有更新");
                        UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(mContext);
    }
}
