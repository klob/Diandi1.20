package com.diandi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.diandi.R;
import com.diandi.sync.sns.TencentShareConstants;
import com.umeng.fb.FeedbackAgent;

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
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mShareLayout, mFeedBackLayout, mLinkLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    void findView() {
        setContentView(R.layout.activity_about);
        initTopBarForLeft("关于点滴");
        mShareLayout = (RelativeLayout) findViewById(R.id.activity_about_share_layout);
        mFeedBackLayout = (RelativeLayout) findViewById(R.id.activity_about_feedback_layout);
        mLinkLayout = (RelativeLayout) findViewById(R.id.activity_about_link_layout);
    }

    void initView() {
        bindEvent();
    }

    void bindEvent() {
        mShareLayout.setOnClickListener(this);
        mFeedBackLayout.setOnClickListener(this);
        mLinkLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_about_share_layout:
                shareToFriend();
                break;
            case R.id.activity_about_feedback_layout:
                FeedbackAgent agent = new FeedbackAgent(AboutActivity.this);
                agent.startFeedbackActivity();
                break;
            case R.id.activity_about_link_layout:
                startActivity(new Intent(AboutActivity.this, LinkAcitivity.class));
                break;
        }
    }


    private void shareToFriend() {
        Intent localIntent1 = new Intent("android.intent.action.SEND");
        localIntent1.setType("text/plain");
        localIntent1.putExtra("android.intent.extra.SUBJECT", "分享");
        localIntent1.putExtra("android.intent.extra.TEXT", "点滴是记录生活中重要的日子的小工具。还在为女友突然问你们相恋了多久而瞠目结舌吗？还在为关键时刻忘记女友的生日而发愁吗？那么这个小工具正是你需要的。赶快去前往" + TencentShareConstants.TARGET_URL + "下载吧！/\n");
        startActivity(Intent.createChooser(localIntent1, "分享给好友"));

    }


}
