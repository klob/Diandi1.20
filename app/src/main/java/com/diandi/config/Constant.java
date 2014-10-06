package com.diandi.config;

import android.annotation.SuppressLint;
import android.os.Environment;


/**
 * @author smile
 * @ClassName: BmobConstants
 * @Description: TODO
 * @date 2014-6-19 下午2:48:33
 */
@SuppressLint("SdCardPath")
public class Constant {

    /**
     * 拍照回调
     */
    public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//拍照修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像
    public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//拍照
    public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//本地图片
    public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;//位置
    public static final String EXTRA_STRING = "extra_string";
    public static final String ACTION_REGISTER_SUCCESS_FINISH = "register.success.finish";//注册成功后登陆页面退出
    public static final String BMOB_APP_ID = "";
    public static final String TABLE_AI = "Mood";
    public static final String TABLE_COMMENT = "Comment";
    public static final String NETWORK_TYPE_WIFI = "wifi";
    public static final String NETWORK_TYPE_MOBILE = "mobile";
    public static final String NETWORK_TYPE_ERROR = "error";
    public static final int AI = 0;
    public static final int HEN = 1;
    public static final int CHUN_LIAN = 2;
    public static final int BIAN_BAI = 3;
    public static final int CONTENT_TYPE = 4;
    public static final String PRE_NAME = "my_pre";
    public static final int PUBLISH_COMMENT = 1;
    public static final int NUMBERS_PER_PAGE = 15;//每次请求返回评论条数
    public static final int SAVE_FAVOURITE = 2;
    public static final int GET_FAVOURITE = 3;
    public static final int GO_SETTINGS = 4;
    public static final String SEX_MALE = "male";
    public static final String SEX_FEMALE = "female";
    public static final String UPDATE_BACK_CONTENT = "updateBackContent";
    public static final String UPDATE_ACTIONBAR_NAME = "updateActionbarName";
    public static final String UPDATE_TEXT = "updateText";
    public static final String UPDATE_EDIT_HINT = "UPDATE_EDIT_HINT";
    public static final String UPDATE_EDIT_TEXT = "UPDATE_EDIT_TEXT";
    /**
     * 存放发送图片的目录
     */
    public static String BMOB_PICTURE_PATH = Environment.getExternalStorageDirectory() + "/点滴/image/";
    /**
     * 我的头像保存目录
     */
    public static String MyAvatarDir = "/sdcard/点滴/avatar/";
    public static String FALSE = "false";
    public static String TRUE = "true";
    public static String OFFICIAL_WEBSITE = "http://diandi.bmob.cn/";

    public final static int LEAST_NUM=6;
}
