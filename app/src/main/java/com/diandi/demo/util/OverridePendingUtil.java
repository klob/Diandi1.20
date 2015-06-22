package com.diandi.demo.util;

import android.app.Activity;
import android.os.Build;

import com.umeng.fb.res.AnimMapper;

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


public class OverridePendingUtil {
    public static void in(final Activity activity) {
        if (Build.VERSION.SDK_INT > 4)
            new Object() {
                public void overridePendingTransition(Activity c) {
                    c.overridePendingTransition(AnimMapper.umeng_fb_slide_in_from_right(activity), AnimMapper.umeng_fb_slide_out_from_left(activity));
                }
            }.overridePendingTransition(activity);
    }

    public static void out(final Activity activity) {
        if (Build.VERSION.SDK_INT > 4)
            new Object() {
                public void overridePendingTransition(Activity c) {
                    c.overridePendingTransition(AnimMapper.umeng_fb_slide_in_from_left(activity), AnimMapper.umeng_fb_slide_out_from_right(activity));
                }
            }.overridePendingTransition(activity);
    }
}
