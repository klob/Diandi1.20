package com.diandi.demo.sync.sns;

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

public interface TencentShareConstants {

    String TITLE = "推荐一个好应用哦~";

    String IMG_URL = "http://mydata123.oss-cn-hangzhou.aliyuncs.com/%E7%82%B9%E6%BB%B4%E5%8F%91%E5%B8%83/launcher.png";

    String TARGET_URL = "http://diandiyun.oss-cn-beijing.aliyuncs.com/release/diandi.apk";

    String SUMMARY = "点滴,用心,助你翻开新的篇章";

    String WEB = "http://diandi.bmob.cn";

    String COMMENT = "";

    String DEFAULT_IMG_URL = "http://mydata123.oss-cn-hangzhou.aliyuncs.com/%E7%82%B9%E6%BB%B4%E5%8F%91%E5%B8%83/launcher.png";


    public static final String SHARE_TO_QQ_IMAGE_URL = "imageUrl";
    public static final String SHARE_TO_QQ_IMAGE_LOCAL_URL = "imageLocalUrl";
    public static final String SHARE_TO_QQ_TITLE = "title";
    public static final String SHARE_TO_QQ_SUMMARY = "summary";
    public static final String SHARE_TO_QQ_SITE = "site";
    public static final String SHARE_TO_QQ_TARGET_URL = "targetUrl";
    public static final String SHARE_TO_QQ_APP_NAME = "appName";
    public static final String SHARE_TO_QQ_AUDIO_URL = "audio_url";
    public static final String SHARE_TO_QQ_KEY_TYPE = "req_type";
    public static final String SHARE_TO_QQ_EXT_STR = "share_qq_ext_str";
    public static final String SHARE_TO_QQ_EXT_INT = "cflag";
    public static final int SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN = 1;
    public static final int SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE = 2;
    public static final int SHARE_TO_QQ_TYPE_DEFAULT = 1;
    public static final int SHARE_TO_QQ_TYPE_AUDIO = 2;
    public static final int SHARE_TO_QQ_TYPE_IMAGE = 5;
    public static final int SHARE_TO_QQ_TYPE_APP = 6;
}
