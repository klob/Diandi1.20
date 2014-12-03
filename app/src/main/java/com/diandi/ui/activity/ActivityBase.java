package com.diandi.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.diandi.CustomApplication;
import com.diandi.sync.UserHelper;
import com.diandi.widget.dialog.DialogTips;

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
public class ActivityBase extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLogin();
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

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    public void showOfflineDialog(final Context context) {
        DialogTips dialog = new DialogTips(this, "您的账号已在其他设备上登录!", "重新登录");
        // 设置成功事件
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                CustomApplication.getInstance().logout();
                startActivity(new Intent(context, LoginActivity.class));
                finish();
                dialogInterface.dismiss();
            }
        });
        dialog.show();
        dialog = null;
    }

    public void checkLogin() {
        if (UserHelper.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            LogE("用户未登陆");
        } else {
            LogE(UserHelper.getCurrentUser().toString());
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
