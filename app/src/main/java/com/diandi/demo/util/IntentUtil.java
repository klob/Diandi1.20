package com.diandi.demo.util;

import android.app.Activity;
import android.content.Intent;

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


public class IntentUtil {

    public void startAnimActivity(Activity activity, Class<?> cla) {
        activity.startActivity(new Intent(activity, cla));
        OverridePendingUtil.in(activity);
    }


    public void startAnimActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        OverridePendingUtil.in(activity);
    }
}
