package com.diandi.util;

/**
 * Created by Administrator on 2014/7/16.
 */
public class FormatUtil {
    FormatUtil() {
    }

    public static String formatString(String str) {
        String res = String.format("%2s", str);
        res = res.replaceAll("\\s", "0");
        return res;
    }

    public static String formatString(String weishu, String str) {
        String res = String.format("%" + weishu + "s", str);
        res = res.replaceAll("\\s", "0");
        return res;
    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static String padHour(int c) {
        if (c == 12)
            return String.valueOf(c);
        if (c == 00)
            return String.valueOf(c + 12);
        if (c > 12)
            return String.valueOf(c - 12);
        else
            return String.valueOf(c);
    }

    public static String padAP(int c) {
        if (c == 12)
            return " PM";
        if (c == 00)
            return " AM";
        if (c > 12)
            return " PM";
        else
            return " AM";
    }

    public static String week2String(int week) {
        switch (week) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";

            default:
                return "";
        }
    }

}
