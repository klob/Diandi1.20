package com.diandi.demo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.diandi.demo.R;
import com.diandi.demo.config.Constant;

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
public class LinkAcitivity extends Activity {

    private ImageView mLinkImg;
    private TextView mLinkText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    void findView() {
        setContentView(R.layout.activity_link_acitivity);
        mLinkImg = (ImageView) findViewById(R.id.activity_link_img);
        mLinkText = (TextView) findViewById(R.id.activity_link_link_text);
        mLinkText.setText("diandi.bmob.cn");
    }

    void initView() {
        bindEvent();
    }

    void bindEvent() {
        mLinkImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(Constant.OFFICIAL_WEBSITE);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
    }
}
