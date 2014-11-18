package com.diandi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.diandi.R;
import com.diandi.bean.User;
import com.diandi.config.Constant;
import com.diandi.util.CommonUtils;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {

    TextView btn_register;
    EditText et_username, et_password, et_email;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();

    }

    @Override
    void findView() {
        setContentView(R.layout.activity_register);

        initTopBarForLeft("注册");

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);

        btn_register = (TextView) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                register();
            }
        });

    }

    @Override
    void initView() {
        bindEvent();
    }


    @Override
    void bindEvent() {

    }

    private void register() {
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();
        String pwd_again = et_email.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ShowToast(R.string.toast_error_username_null);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ShowToast(R.string.toast_error_password_null);
            return;
        }
        if (!pwd_again.equals(password)) {
            ShowToast(R.string.toast_error_comfirm_password);
            return;
        }
        if (name.length() < Constant.LEAST_NUM) {
            ShowToast(R.string.toast_error_username_short);
            return;
        }
        if (password.length() < Constant.LEAST_NUM) {
            ShowToast(R.string.toast_error_password_short);
            return;
        }

        boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
        if (!isNetConnected) {
            ShowToast(R.string.network_tips);
            return;
        }
        progress = new ProgressDialog(RegisterActivity.this);
        progress.setMessage("正在注册...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        signUp(name, password);
    }

    public void signUp(String userName, String password) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setSignature("这个家伙很懒，什么也不说。。。");
        user.setDeviceType("android");
        user.setNick("无名氏");
        user.setOfficial(false);
        user.setInstallId(BmobInstallation.getInstallationId(mContext));
        user.signUp(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                progress.dismiss();
                ShowToast("注册成功");
                mUserManager.bindInstallationForRegister(et_username.getText().toString());
                updateUserLocation();
                sendBroadcast(new Intent(Constant.ACTION_REGISTER_SUCCESS_FINISH));
                Intent intent = new Intent(RegisterActivity.this, TestActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int arg0, String msg) {
                ShowToast("用户已存在或网络不佳");
                progress.dismiss();
            }
        });
    }


}
