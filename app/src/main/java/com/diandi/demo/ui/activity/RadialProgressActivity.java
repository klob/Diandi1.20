package com.diandi.demo.ui.activity;

import android.os.Bundle;
import android.view.WindowManager;

import com.diandi.demo.R;
import com.diandi.demo.db.PlanDao;
import com.diandi.demo.model.Plan;
import com.diandi.demo.widget.HeaderLayout;
import com.touchmenotapps.widget.radialmenu.progress.widget.RadialProgressWidget;
import com.touchmenotapps.widget.radialmenu.progress.widget.RadialProgressWidget.OnRadialViewValueChanged;

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
public class RadialProgressActivity extends BaseActivity {

    private RadialProgressWidget mView;
    private Plan mPlan;
    private PlanDao mPlanDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    @Override
    void findView() {
        setContentView(R.layout.activity_radial_progress);
        mView = (RadialProgressWidget) findViewById(R.id.radial_view);
    }

    @Override
    void initView() {
        initTopBarForBoth("进度", R.drawable.base_action_bar_true_bg_selector, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                mView.getCurrentValue();
                mPlan.setProgress(mView.getCurrentValue());
                mPlanDao.createPlan(mPlan);
                finish();
            }
        });
        mPlanDao = new PlanDao(this);
        int mPlanId = getIntent().getIntExtra(Plan.PLAN_ID, 0);
        mPlan = mPlanDao.getPlanById(mPlanId);
        mView.setSecondaryText("进度");
        //Use this to switch between static progress view and an interactive one
        //mView.setTouchEnabled(false);
        bindEvent();
    }


    @Override
    void bindEvent() {
        mView.setOnRadialViewValueChanged(new OnRadialViewValueChanged() {
            @Override
            public void onValueChanged(int value) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.screenBrightness = value / 100.0f;
                //getWindow().setAttributes(lp);
                if (value == 99) {
                    mView.setCurrentValue(100);
                }
            }
        });

        if ((int) (getWindow().getAttributes().screenBrightness * 100) < 0)
            mView.setCurrentValue(mPlan.getProgress());
        else
            mView.setCurrentValue((int) (getWindow().getAttributes().screenBrightness * 100));

    }
}
