package com.diandi.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.diandi.R;
import com.frakbot.jumpingbeans.JumpingBeans;

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
