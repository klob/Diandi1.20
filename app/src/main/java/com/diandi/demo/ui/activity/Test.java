package com.diandi.demo.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.diandi.demo.R;
import com.ecloud.pulltozoomview.PullToZoomScrollView;

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
public class Test extends Activity {
    PullToZoomScrollView mPullToZoomScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mPullToZoomScrollView = (PullToZoomScrollView) findViewById(R.id.scroll_view);
    }


}
