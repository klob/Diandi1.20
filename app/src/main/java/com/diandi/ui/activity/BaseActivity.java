package com.diandi.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.bean.User;
import com.diandi.sync.UserHelper;
import com.diandi.util.CollectionUtils;
import com.diandi.util.OverridePendingUtil;
import com.diandi.view.HeaderLayout;
import com.diandi.view.HeaderLayout.HeaderStyle;
import com.diandi.view.HeaderLayout.onLeftImageButtonClickListener;
import com.diandi.view.HeaderLayout.onRightImageButtonClickListener;

import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 基类
 *
 * @author smile
 * @ClassName: BaseActivity
 * @Description: TODO
 * @date 2014-6-13 下午5:05:38
 */
abstract class BaseActivity extends FragmentActivity {
    public final String TAG = getClass().getName();

    protected BmobUserManager mUserManager;
    protected BmobChatManager mChatManager;

    protected HeaderLayout mHeaderLayout;

    protected CustomApplication mApplication;
    protected Context mContext;
    protected Toast mToast;
    protected ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConfigure();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(true);
    }

    abstract void findView();

    abstract void initView();

    abstract void bindEvent();

    private void initConfigure() {
        if (mUserManager == null) {
            mUserManager = BmobUserManager.getInstance(this);
        }
        if (mChatManager == null) {
            mChatManager = BmobChatManager.getInstance(this);
        }
        if (mApplication == null) {
            mApplication = CustomApplication.getInstance();
        }

        mContext = this;
        mApplication.addActivity(this);
    }

    public void ShowToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (mToast == null) {
                            mToast = Toast.makeText(getApplicationContext(), text,
                                    Toast.LENGTH_LONG);
                        } else {
                            mToast.setText(text);
                        }
                        mToast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public void ShowToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mToast == null) {
                        mToast = Toast.makeText(BaseActivity.this.getApplicationContext(), resId,
                                Toast.LENGTH_LONG);
                    } else {
                        mToast.setText(resId);
                    }
                    mToast.show();
                    Log.d(TAG, getString(resId));
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void LogE(String str) {
        Log.e(TAG, str);
    }

    public void initTopBarForOnlyTitle(String titleName) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(titleName);
    }

    public void initTopBarForBoth(String titleName, int rightDrawableId, String text,
                                  onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightButton(titleName, rightDrawableId, text,
                listener);
    }

    public void initTopBarForBoth(String titleName, int rightDrawableId,
                                  onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
                listener);
    }

    public void initTopBarForLeft(String titleName) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
    }

    public void startAnimActivity(Class<?> cla) {
        this.startActivity(new Intent(this, cla));
        OverridePendingUtil.in(BaseActivity.this);
    }

    public void startAnimActivity(Intent intent) {
        this.startActivity(intent);
        OverridePendingUtil.in(BaseActivity.this);
    }

    public void updateUserInfos() {
        //更新地理位置信息
        updateUserLocation();
        //查询该用户的好友列表(这个好友列表是去除黑名单用户的哦),目前支持的查询好友个数为100，如需修改请在调用这个方法前设置BmobConfig.LIMIT_CONTACTS即可。
        //这里默认采取的是登陆成功之后即将好于列表存储到数据库中，并更新到当前内存中,
        mUserManager.queryCurrentContactList(new FindListener<BmobChatUser>() {
            @Override
            public void onError(int arg0, String arg1) {
                if (arg0 == BmobConfig.CODE_COMMON_NONE) {
                    LogE(arg1);
                } else {
                    LogE("查询好友列表失败：" + arg1);
                }
            }

            @Override
            public void onSuccess(List<BmobChatUser> arg0) {
                // 保存到application中方便比较
                LogE("查询好友列表成功");
                CustomApplication.getInstance().setContactList(CollectionUtils.list2map(arg0));
            }
        });
    }

    /**
     * 更新用户的经纬度信息
     */
    public void updateUserLocation() {
        if (CustomApplication.lastPoint != null) {
            String saveLatitude = mApplication.getLatitude();
            String saveLongtitude = mApplication.getLongtitude();
            String newLat = String.valueOf(CustomApplication.lastPoint.getLatitude());
            String newLong = String.valueOf(CustomApplication.lastPoint.getLongitude());
//			ShowLog("saveLatitude ="+saveLatitude+",saveLongtitude = "+saveLongtitude);
//			ShowLog("newLat ="+newLat+",newLong = "+newLong);
            if (!saveLatitude.equals(newLat) || !saveLongtitude.equals(newLong)) {//只有位置有变化就更新当前位置，达到实时更新的目的
                final User user = (User) mUserManager.getCurrentUser(User.class);
                user.setLocation(CustomApplication.lastPoint);
                user.update(this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        CustomApplication.getInstance().setLatitude(String.valueOf(user.getLocation().getLatitude()));
                        CustomApplication.getInstance().setLongtitude(String.valueOf(user.getLocation().getLongitude()));
//						ShowLog("经纬度更新成功");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
//						ShowLog("经纬度更新 失败:"+msg);
                    }
                });
            } else {
//				ShowLog("用户位置未发生过变化");
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        OverridePendingUtil.out(this);
    }

    public User getCurrentUser() {
        return UserHelper.getCurrentUser(mContext);
    }

    public class OnLeftButtonClickListener implements
            onLeftImageButtonClickListener {
        @Override
        public void onClick() {
            finish();
            OverridePendingUtil.out(BaseActivity.this);
        }
    }

}
