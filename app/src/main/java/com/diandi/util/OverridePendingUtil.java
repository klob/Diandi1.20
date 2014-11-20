package com.diandi.util;

import android.app.Activity;
import android.os.Build;

import com.umeng.fb.res.AnimMapper;

/**
 * ************************************************************
 * *********    User : SuLinger(462679107@qq.com) .
 * *********    Date : 2014-09-21  .
 * *********    Time:  2014-09-21  .
 * *********    Project name :DianDi1.1.5 .
 * *********    Copyright @ 2014, SuLinger, All Rights Reserved
 * *************************************************************
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
