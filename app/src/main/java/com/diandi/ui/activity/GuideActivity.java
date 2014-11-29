package com.diandi.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.view.ScrollLayout;

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
public class GuideActivity extends BaseActivity implements ScrollLayout.OnViewChangeListener {

    private ScrollLayout mScrollLayout;
    private LinearLayout mPointLayout;
    private ImageView[] miPointImgs;
    private Button mStartBtn;

    private int mCount;
    private int mCurrentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    @Override
    void findView() {
        setContentView(R.layout.activity_guide);
        mScrollLayout = (ScrollLayout) findViewById(R.id.activity_guide_scroll_layout);
        mPointLayout = (LinearLayout) findViewById(R.id.activity_guide_point_layout);
        mStartBtn = (Button) findViewById(R.id.activity_guide_start_btn);
    }

    @Override
    void initView() {
        mCount = mScrollLayout.getChildCount();
        miPointImgs = new ImageView[mCount];
        for (int i = 0; i < mCount; i++) {
            miPointImgs[i] = (ImageView) mPointLayout.getChildAt(i);
            miPointImgs[i].setEnabled(true);
            miPointImgs[i].setTag(i);
        }
        mCurrentItem = 0;
        miPointImgs[mCurrentItem].setEnabled(false);
        bindEvent();
    }

    @Override
    void bindEvent() {
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimActivity(LoginActivity.class);
                CustomApplication.getInstance().getSpUtil().setFirstStart(false);
                finish();
            }
        });
        mScrollLayout.SetOnViewChangeListener(this);
    }

    @Override
    public void OnViewChange(int position) {
        setcurrentPoint(position);
    }

    private void setcurrentPoint(int position) {
        if (position < 0 || position > mCount - 1 || mCurrentItem == position) {
            return;
        }
        miPointImgs[mCurrentItem].setEnabled(true);
        miPointImgs[position].setEnabled(false);
        mCurrentItem = position;
    }
}
