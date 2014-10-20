package com.diandi.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.diandi.R;

import net.frakbot.jumpingbeans.JumpingBeans;

/**
 * ************************************************************
 * *********    User : SuLinger(462679107@qq.com) .
 * *********    Date : 2014-10-19  .
 * *********    Time:  2014-10-19  .
 * *********    Project name :Loading jump .
 * *********    Copyright @ 2014, SuLinger, All Rights Reserved
 * *************************************************************
 */


public class LoadingDialog extends Dialog {
    private TextView mTextView;
    private JumpingBeans mJumpingBeans;

    public LoadingDialog(Context context) {
        super(context, R.style.ListDialog);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mJumpingBeans.stopJumping();
    }

    private void findView() {
        setContentView(R.layout.dialog_loading);
        mTextView = (TextView) findViewById(R.id.loading_text);
    }

    private void initView() {
        mJumpingBeans = new JumpingBeans.Builder()
                .appendJumpingDots(mTextView)
                .build();
    }

}
