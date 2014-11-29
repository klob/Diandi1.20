package com.diandi.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

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
public class CustomViewPager extends ViewPager {

    private boolean mIsEnable = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsEnable) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mIsEnable) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public void setAdapter(PagerAdapter arg0) {
        super.setAdapter(arg0);
    }

    public void setAdapter(PagerAdapter arg0, int index) {
        super.setAdapter(arg0);
        setCurrentItem(index, false);
    }

    public void setEnableTouchScroll(boolean isEnable) {
        mIsEnable = isEnable;
    }
}
