package com.diandi.demo.util;

import android.content.Context;
import android.widget.Toast;
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
public class ToastUtil {
    private static Context context = null;
    private static Toast toast = null;

    public static Toast getToast(Context context, String text) {
        if (ToastUtil.context == context) {
            // toast.cancel();
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);

        } else {
            ToastUtil.context = context;
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        return toast;
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
