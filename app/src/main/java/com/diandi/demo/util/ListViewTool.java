package com.diandi.demo.util;

import android.util.Log;

/**
 * *******************************************************************************
 * *********    Author : klob(kloblic@gmail.com) .
 * *********    Date : 2015-08-09  .
 * *********    Time : 20:27 .
 * *********    Version : 1.0
 * *********    Copyright © 2015, klob, All Rights Reserved
 * *******************************************************************************
 */

public class ListViewTool {
    private final static String TAG = "ListViewTool";
    private static double total = 0;
    private double start = 0;
    private double end = 0;
    private double ava = 0;
    private static int time = 0;
    private boolean isDebug;

    public ListViewTool(boolean isDebug) {
        this.isDebug = isDebug;
        init();
    }

    public void init() {
        total = 0;
        start = 0;
        end = 0;
        time = 0;
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void end() {
        end = System.currentTimeMillis();
        time++;
        total = total + end - start;
    }

    public double getAva(int position) {
        end();
        ava = total / time;
        if (time % 100 == 0) {
            Log.e(TAG, "---------------------------------------------------------------------------------------------------------\n" +
                    "position:  " + position + "   total: " + total + "   time: " + time + "    ava: " + ava
                    + "\n------------------------------------------------------------------------------------------------------");

        }
        if (isDebug) {
            Log.e(TAG, "position:  " + position + "   total: " + total + "   time: " + time + "    ava: " + ava);
        }
        return ava;
    }


}
