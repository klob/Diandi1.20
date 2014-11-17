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
import com.diandi.config.Constant;
import com.diandi.proxy.UserProxy;
import com.diandi.util.CommonUtils;

public class RegisterActivity extends BaseActivity implements UserProxy.ISignUpListener {

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
        mUserProxy = new UserProxy(this);
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
        mUserProxy.setOnSignUpListener(this);
        mUserProxy.signUp(name, password);
    }

    @Override
    public void onSignUpFailure(String msg) {
        ShowToast("注册失败");
        progress.dismiss();
    }

    @Override
    public void onSignUpSuccess() {
        progress.dismiss();
        ShowToast("注册成功");
        mUserManager.bindInstallationForRegister(et_username.getText().toString());
        updateUserLocation();
        sendBroadcast(new Intent(Constant.ACTION_REGISTER_SUCCESS_FINISH));
        Intent intent = new Intent(RegisterActivity.this, TestActivity.class);
        startActivity(intent);
        finish();
    }
}
