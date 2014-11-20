package com.diandi.util.number;

/**
 * *************************************************************
 * *********    User : SuLinger(462679107@qq.com) .        ********
 * *********    Date : 2014-08-14  .          ********
 * *********    Time:  2014-08-14  .          ********
 * *********    Project name :PBOXS .            ********
 * *********    Copyright @ 2014, SuLinger, All Rights Reserved  **
 * **************************************************************
 */

import com.diandi.util.TimeUtil;

import java.util.Calendar;
import java.util.Date;

public class Calculator {
    private final static String start_day = TimeUtil.getDatetimeString();
    ;

    public Calculator() {
    }

    public static String calculate(String year, String month, String day) {
        int startYear, startMonth, startDay, endYear, endMonth, endDay;
        startYear = Integer.valueOf(start_day.substring(0, 4));
        startMonth = Integer.valueOf(start_day.substring(5, 7));
        startDay = Integer.valueOf(start_day.substring(8, 10));


        endYear = Integer.valueOf(year);
        endMonth = Integer.valueOf(month);
        endDay = Integer.valueOf(day);
        Date startdate = new Date(startYear, startMonth, startDay);
        Date enddate = new Date(endYear, endMonth, endDay);
        return String.valueOf(daysBetween(startdate, enddate));
    }

    public static int calculate(String end_day) {
        int startYear, startMonth, startDay, endYear, endMonth, endDay;
        startYear = Integer.valueOf(start_day.substring(0, 4));
        startMonth = Integer.valueOf(start_day.substring(5, 7));
        startDay = Integer.valueOf(start_day.substring(8, 10));


        endYear = Integer.valueOf(end_day.substring(0, 4));
        endMonth = Integer.valueOf(end_day.substring(5, 7));
        endDay = Integer.valueOf(end_day.substring(8, 10));
        Date startdate = new Date(startYear, startMonth, startDay);
        Date enddate = new Date(endYear, endMonth, endDay);
        return daysBetween(startdate, enddate);
    }

    public static int daysBetween(Date dateFrom, Date dateEnd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFrom);
        long time1 = cal.getTimeInMillis();
        cal.setTime(dateEnd);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        int m = Integer.parseInt(String.valueOf(between_days));
        return m;
    }

}
