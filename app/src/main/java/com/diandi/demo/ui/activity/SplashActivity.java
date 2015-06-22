package com.diandi.demo.ui.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.diandi.demo.CustomApplication;
import com.diandi.demo.R;
import com.diandi.demo.config.Config;

import cn.bmob.im.BmobChat;
import cn.bmob.v3.Bmob;

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
public class SplashActivity extends BaseActivity {

    private static final int GO_HOME = 100;
    private static final int GO_LOGIN = 200;
    private static final int GO_GUIDE = 300;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
                case GO_LOGIN:
                    startAnimActivity(LoginActivity.class);
                    finish();
                    break;
                case GO_GUIDE:
                    startAnimActivity(GuideActivity.class);
                    finish();
                    break;
            }
        }
    };
    // 定位获取当前用户的地理位置
    private LocationClient mLocationClient;
    private BaiduReceiver mReceiver;// 注册广播接收器，用于监听网络以及验证key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Bmob.initialize(this, Config.applicationId);
        BmobChat.getInstance(this).init(Config.applicationId);
        initLocClient();
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new BaiduReceiver();
        registerReceiver(mReceiver, iFilter);
        if (CustomApplication.getInstance().getSpUtil().isFirstStart()) {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, 2000);
        } else if (mUserManager.getCurrentUser() != null) {
            // 每次自动登陆的时候就需要更新下当前位置和好友的资料，因为好友的头像，昵称啥的是经常变动的
            updateUserInfos();
            mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
        }
        CustomApplication.getInstance().getSpUtil().checkUpdate();

    }

    @Override
    void findView() {

    }

    @Override
    void initView() {

    }


    @Override
    void bindEvent() {

    }

    /**
     * 开启定位，更新当前用户的经纬度坐标
     *
     * @param
     * @return void
     * @throws
     * @Title: initLocClient
     * @Description: TODO
     */
    private void initLocClient() {
        mLocationClient = CustomApplication.getInstance().mLocationClient;
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式:高精度模式
        option.setCoorType("bd09ll"); // 设置坐标类型:百度经纬度
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms:低于1000为手动定位一次，大于或等于1000则为定时定位
        option.setIsNeedAddress(false);// 不需要包含地址信息
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class BaiduReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                ShowToast("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s
                    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                ShowToast("当前网络连接不稳定，请检查您的网络设置!");
            }
        }
    }

}
