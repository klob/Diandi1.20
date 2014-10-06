package com.diandi.proxy;

import android.content.Context;

import com.diandi.bean.User;
import com.diandi.config.Constant;
import com.diandi.util.LogUtils;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserProxy {

    public static final String TAG = "UserProxy";

    private static volatile UserProxy mUserProxy;
    private Context mContext;
    private ISignUpListener signUpLister;
    private ILoginListener loginListener;
    private IUpdateListener updateListener;
    private IResetPasswordListener resetPasswordListener;

    public static UserProxy getInstance(Context context) {
        if (mUserProxy == null) {
            return new UserProxy(context);
        } else {
            return mUserProxy;
        }
    }

    public UserProxy(Context context) {
        this.mContext = context;
    }

    public User getCurrentUser() {
        User user = BmobUser.getCurrentUser(mContext, User.class);
        if (user != null) {
            LogUtils.i(TAG, "本地用户信息" + user.getObjectId() + "-"
                    + user.getUsername() + "-"
                    + user.getSessionToken() + "-"
                    + user.getCreatedAt() + "-"
                    + user.getUpdatedAt() + "-"
                    + user.getSignature() + "-"
                    + user.getSex());
            return user;
        } else {
            LogUtils.i(TAG, "本地用户为null,请登录。");
        }
        return null;
    }

    /**
     * 注册
     */

    public static boolean checkUser(User user) {
        if (user != null) {
            return true;
        }
        else return false;
    }

    public void signUp(String userName, String password) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setOfficial(true);
        user.setSex(Constant.SEX_FEMALE);
        user.setSignature("这个家伙很懒，什么也不说。。。");
        user.setDeviceType("android");
        user.setNick("无名氏");
        user.setOfficial(false);
        user.setInstallId(BmobInstallation.getInstallationId(mContext));
        user.signUp(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                if (signUpLister != null) {
                    signUpLister.onSignUpSuccess();
                } else {
                    LogUtils.i(TAG, "signup listener is null,you must set one!");
                }


            }

            @Override
            public void onFailure(int arg0, String msg) {
                // TODO Auto-generated method stub
                if (signUpLister != null) {
                    signUpLister.onSignUpFailure(msg);
                } else {
                    LogUtils.i(TAG, "signup listener is null,you must set one!");
                }
            }
        });
    }

    public void setOnSignUpListener(ISignUpListener signUpLister) {
        this.signUpLister = signUpLister;
    }

    /**
     * 登录
     */

    public void login(String userName, String password) {
        final User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.login(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                if (loginListener != null) {
                    loginListener.onLoginSuccess();
                } else {
                    LogUtils.i(TAG, "login listener is null,you must set one!");
                }
            }

            @Override
            public void onFailure(int arg0, String msg) {
                // TODO Auto-generated method stub
                if (loginListener != null) {
                    loginListener.onLoginFailure(msg);
                } else {
                    LogUtils.i(TAG, "login listener is null,you must set one!");
                }
            }
        });
    }

    public void setOnLoginListener(ILoginListener loginListener) {
        this.loginListener = loginListener;
    }

    /**
     * 登出
     */

    public void logout() {
        BmobUser.logOut(mContext);
        LogUtils.i(TAG, "logout result:" + (null == getCurrentUser()));
    }

    /**
     * 更新
     */

    public void update(String... args) {
        User user = getCurrentUser();
        user.setUsername(args[0]);
        user.setEmail(args[1]);
        user.setPassword(args[2]);
        user.setSex(args[3]);
        user.setSignature(args[4]);
        //...
        user.update(mContext, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                if (updateListener != null) {
                    updateListener.onUpdateSuccess();
                } else {
                    LogUtils.i(TAG, "update listener is null,you must set one!");
                }
            }

            @Override
            public void onFailure(int arg0, String msg) {
                // TODO Auto-generated method stub
                if (updateListener != null) {
                    updateListener.onUpdateFailure(msg);
                } else {
                    LogUtils.i(TAG, "update listener is null,you must set one!");
                }
            }
        });
    }

    public void setOnUpdateListener(IUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    /**
     * 改密
     */

    public void resetPassword(String psd) {
        BmobUser.resetPassword(mContext, psd, new ResetPasswordListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                if (resetPasswordListener != null) {
                    resetPasswordListener.onResetSuccess();
                } else {
                    LogUtils.i(TAG, "reset listener is null,you must set one!");
                }
            }

            @Override
            public void onFailure(int arg0, String msg) {
                // TODO Auto-generated method stub
                if (resetPasswordListener != null) {
                    resetPasswordListener.onResetFailure(msg);
                } else {
                    LogUtils.i(TAG, "reset listener is null,you must set one!");
                }
            }
        });
    }

    public void setOnResetPasswordListener(IResetPasswordListener resetPasswordListener) {
        this.resetPasswordListener = resetPasswordListener;
    }

    public interface ISignUpListener {
        void onSignUpSuccess();

        void onSignUpFailure(String msg);
    }

    public interface ILoginListener {
        void onLoginSuccess();

        void onLoginFailure(String msg);
    }

    public interface IUpdateListener {
        void onUpdateSuccess();

        void onUpdateFailure(String msg);
    }

    public interface IResetPasswordListener {
        void onResetSuccess();

        void onResetFailure(String msg);
    }

}
