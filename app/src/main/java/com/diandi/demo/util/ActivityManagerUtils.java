package com.diandi.demo.util;

import android.app.Activity;

import java.util.ArrayList;

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

public class ActivityManagerUtils {

    private static ActivityManagerUtils activityManagerUtils;
    private ArrayList<Activity> activityList = new ArrayList<Activity>();

    private ActivityManagerUtils() {

    }

    public static ActivityManagerUtils getInstance() {
        if (null == activityManagerUtils) {
            activityManagerUtils = new ActivityManagerUtils();
        }
        return activityManagerUtils;
    }

    public Activity getTopActivity() {
        return activityList.get(activityList.size() - 1);
    }

    public void removeMainActivity() {
        Activity activity = activityList.get(activityList.size() - 2);
        if (null != activity) {
            if (!activity.isFinishing()) {
                activity.finish();
                //     L.e("finish ");
            }
            activity = null;
            //   L.e("finish  2 ");
        }
        //  L.e("finish  3 ");

    }

    public void addActivity(Activity ac) {
        activityList.add(ac);
    }

    public void removeAllActivity() {
        for (Activity ac : activityList) {
            if (null != ac) {
                if (!ac.isFinishing()) {
                    ac.finish();
                }
                ac = null;
            }
        }
        activityList.clear();
    }
}
