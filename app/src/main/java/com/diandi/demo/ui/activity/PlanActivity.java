package com.diandi.demo.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.diandi.demo.R;
import com.diandi.demo.model.Plan;
import com.diandi.demo.ui.fragment.PlanFragment;
import com.diandi.demo.widget.HeaderLayout;
import com.diandi.demo.widget.PagerSlidingTabStrip;

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
public class PlanActivity extends BaseActivity {
    private PagerSlidingTabStrip mTabs;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    @Override
    void findView() {
        setContentView(R.layout.activity_plan);
        mPager = (ViewPager) findViewById(R.id.pager);
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
    }

    @Override
    void initView() {
        initTopBarForBoth("计划格子", R.drawable.base_action_bar_add_bg_selector,
                new HeaderLayout.onRightImageButtonClickListener() {
                    @Override
                    public void onClick() {
                        startAnimActivity(WritePlanActivity.class);
                    }
                }
        );
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs.setViewPager(mPager);
        initTabsValue();
    }


    @Override
    void bindEvent() {

    }

    private void initTabsValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        // 设置Tab是自动填充满屏幕的
        mTabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        mTabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        mTabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        mTabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        // 设置Tab标题文字的大小
        mTabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        mTabs.setIndicatorColor(Color.parseColor("#45c01a"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        mTabs.setSelectedTextColor(Color.parseColor("#45c01a"));
        // 取消点击Tab时的背景色
        mTabs.setTabBackground(0);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLE = new String[]{"A计划", "B计划", "C计划"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position];
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }

        public Fragment getItem(int position) {
            PlanFragment planFragment = new PlanFragment();
            planFragment.setPlanCategory(Plan.CATEGORY_APLAN);
            if (position == 0) {
                planFragment.setPlanCategory(Plan.CATEGORY_APLAN);
            }
            if (position == 1) {
                planFragment.setPlanCategory(Plan.CATEGORY_BPLAN);
            }
            if (position == 2) {
                planFragment.setPlanCategory(Plan.CATEGORY_CPLAN);
            }
            return planFragment;
        }
    }


}
