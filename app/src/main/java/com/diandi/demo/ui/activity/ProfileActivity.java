package com.diandi.demo.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diandi.demo.CustomApplication;
import com.diandi.demo.R;
import com.diandi.demo.config.Constant;
import com.diandi.demo.model.User;
import com.diandi.demo.util.CacheUtils;
import com.diandi.demo.util.CollectionUtils;
import com.diandi.demo.util.ImageLoadOptions;
import com.diandi.demo.util.L;
import com.diandi.demo.util.OverridePendingUtil;
import com.diandi.demo.util.Sputil;
import com.diandi.demo.widget.dialog.DialogTips;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.MsgTag;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

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
public class ProfileActivity extends ActivityBase implements View.OnClickListener {
    public static final int UPDATE_NICK_CONTENT = 20;
    public static final int UPDATE_SIGNATURE_CONTENT = 21;
    public static final int UPDATE_SCHOOL_CONTENT = 22;
    static final int UPDATE_AVATAR = 11;
    static final int UPDATE_NICK = 12;
    static final int UPDATE_SEX = 13;
    static final int UPDATE_SIGN = 14;
    static final int EDIT_SIGN = 15;
    static final int GO_LOGIN = 16;
    private final static String USER_ID = "user_id_";
    protected Sputil mSputil;
    String[] sexs = new String[]{"女", "男"};
    String dateTime;
    AlertDialog albumDialog;
    private ImageView mAvatarImg;
    private TextView mNickText;
    private TextView mAccountText;
    private TextView mSignatureText;

    private Button mAddFriendBtn;
    private Button mLaunchChatBtn;
    private Button mAddBlackBtn;
    private Button mLogOutBtn;
    private RelativeLayout mAvatarLayout;
    private RelativeLayout mNickLayout;
    private RelativeLayout mAccountLayout;
    private RelativeLayout mSignatrueLayout;
    private String from = "";
    private String username = "";
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (from.equals("me")) {
            initMeInfo();
        } else {
            loadData(mUser);
        }
    }

    public void onStop() {
        super.onStop();
        if (mUser != null) {
            CustomApplication.getInstance().getCache().put(USER_ID + mUser.getUsername(), mUser);
        }
    }

    void findView() {
        setContentView(R.layout.activity_profile);

        mAvatarLayout = (RelativeLayout) findViewById(R.id.activity_setting_user_avatar_layout);
        mNickLayout = (RelativeLayout) findViewById(R.id.activity_setting_user_nick_layout);
        mAccountLayout = (RelativeLayout) findViewById(R.id.activity_setting_user_account_layout);

        mSignatrueLayout = (RelativeLayout) findViewById(R.id.activity_setting_user_sign_layout);

        mAvatarImg = (ImageView) findViewById(R.id.activity_setting_user_avatar_img);
        mNickText = (TextView) findViewById(R.id.activity_setting_user_nick_text);
        mAccountText = (TextView) findViewById(R.id.activity_setting_user_account_text);
        mSignatureText = (TextView) findViewById(R.id.activity_setting_user_sign_text);

        mAddFriendBtn = (Button) findViewById(R.id.activity_add_friend_btn);
        mLaunchChatBtn = (Button) findViewById(R.id.activity_setting_launch_chat_btn);
        mAddBlackBtn = (Button) findViewById(R.id.activity_setting_add_black_btn);
        mLogOutBtn = (Button) findViewById(R.id.activity_setting_logout_btn);

    }

    void initView() {
        initData();
        if (mSputil == null) {
            mSputil = new Sputil(this, Constant.PRE_NAME);
            mSputil.getInstance().registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

                }
            });
        }
        mLaunchChatBtn.setEnabled(false);
        mAddBlackBtn.setEnabled(false);
        mAvatarLayout.setOnClickListener(this);
        if (from.equals("me")) {
            initTopBarForLeft("个人资料");
            mNickLayout.setOnClickListener(this);
            mSignatrueLayout.setOnClickListener(this);
            mLogOutBtn.setOnClickListener(this);
            mAddFriendBtn.setVisibility(View.GONE);
            mLaunchChatBtn.setVisibility(View.GONE);
            mAddBlackBtn.setVisibility(View.GONE);
            mLogOutBtn.setVisibility(View.VISIBLE);
        } else {
            initTopBarForLeft("详细资料");
            if (from.equals("add")) {// 从附近的人列表添加好友--因为获取附近的人的方法里面有是否显示好友的情况，因此在这里需要判断下这个用户是否是自己的好友
                if (mApplication.getContactList().containsKey(username)) {// 是好友
                    mLaunchChatBtn.setVisibility(View.VISIBLE);
                    mAddBlackBtn.setVisibility(View.VISIBLE);
                    mLaunchChatBtn.setOnClickListener(this);
                    mAddBlackBtn.setOnClickListener(this);
                } else {
                    mLaunchChatBtn.setVisibility(View.GONE);
                    mAddBlackBtn.setVisibility(View.GONE);
                    mAddFriendBtn.setVisibility(View.VISIBLE);
                    mAddFriendBtn.setOnClickListener(this);
                }
            } else {// 查看他人
                mLaunchChatBtn.setVisibility(View.VISIBLE);
                mAddBlackBtn.setVisibility(View.VISIBLE);
                mLaunchChatBtn.setOnClickListener(this);
                mAddBlackBtn.setOnClickListener(this);
            }
            initOtherInfo(username);
        }

        mAddBlackBtn.setVisibility(View.GONE);
    }

    void initData() {
        from = getIntent().getStringExtra("from");
        username = getIntent().getStringExtra("username");
    }

    @Override
    void bindEvent() {
        super.bindEvent();
    }

    private void loadData(User user) {
        if (user != null) {
            mNickText.setText(user.getNick());
            mSignatureText.setText(user.getSignature());
            mAccountText.setText(user.getUsername());

            BmobFile avatarFile = user.getAvatarImg();
            if (avatarFile != null) {
                ImageLoader.getInstance().displayImage(user.getAvatar(), mAvatarImg, ImageLoadOptions.getOptions());
            } else {
                mAvatarImg.setImageResource(R.drawable.default_head_cry);
            }

            if (from.equals("other")) {
                if (BmobDB.create(this).isBlackUser(user.getUsername())) {
                    mAddBlackBtn.setVisibility(View.GONE);
                } else {
                    mAddBlackBtn.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void initMeInfo() {
        User user = mUserManager.getCurrentUser(User.class);
        initOtherInfo(user.getUsername());
    }

    private void initOtherInfo(String name) {
        if (name != null && CustomApplication.getInstance().getCache().getAsObject(USER_ID + name) != null) {
            mUser = (User) CustomApplication.getInstance().getCache().getAsObject(USER_ID + name);
            loadData(mUser);
        }
        mUserManager.queryUser(name, new FindListener<User>() {
            @Override
            public void onError(int arg0, String arg1) {
                LogE("onError onError:" + arg1);
            }

            @Override
            public void onSuccess(List<User> arg0) {
                // TODO Auto-generated method stub
                if (arg0 != null && arg0.size() > 0) {
                    mUser = arg0.get(0);
                    mLaunchChatBtn.setEnabled(true);
                    mAddFriendBtn.setEnabled(true);
                    mAddBlackBtn.setEnabled(true);
                    loadData(mUser);
                } else {
                    LogE("onSuccess 查无此人");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_setting_launch_chat_btn:// 发起聊天
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("user", mUser);
                startAnimActivity(intent);
                finish();
                break;
            case R.id.activity_setting_add_black_btn:// 黑名单
                showBlackDialog(mUser.getUsername());
                break;
            case R.id.activity_add_friend_btn:// 添加好友
                addFriend();
                break;
            case R.id.activity_setting_user_avatar_layout://修改头像
                if (from.equals("me")) {
                    showAlbumDialog();
                } else {
                    Intent intent2 = new Intent(mContext, ImageBrowserActivity.class);
                    ArrayList<String> photos = new ArrayList<String>();
                    photos.add(mUser.getAvatar());
                    intent2.putStringArrayListExtra("photos", photos);
                    intent2.putExtra("position", 0);
                    mContext.startActivity(intent2);
                }
                break;
            case R.id.activity_setting_user_nick_layout:  //修改昵称
                Intent intent1 = new Intent(ProfileActivity.this, UpdateInfoActivity.class);
                intent1.putExtra(Constant.UPDATE_ACTIONBAR_NAME, "修改昵称");
                intent1.putExtra(Constant.UPDATE_TEXT, "昵称");
                intent1.putExtra(Constant.UPDATE_EDIT_HINT, "请输入昵称");
                this.startActivityForResult(intent1, UPDATE_NICK_CONTENT);
                OverridePendingUtil.in(ProfileActivity.this);
                break;
            case R.id.activity_setting_user_sign_layout:  //修改签名
                Intent intent2 = new Intent(ProfileActivity.this, UpdateInfoActivity.class);
                intent2.putExtra(Constant.UPDATE_ACTIONBAR_NAME, "个性签名");
                intent2.putExtra(Constant.UPDATE_TEXT, "签名");
                intent2.putExtra(Constant.UPDATE_EDIT_HINT, "请输入签名");
                this.startActivityForResult(intent2, UPDATE_SIGNATURE_CONTENT);
                OverridePendingUtil.in(ProfileActivity.this);
                break;
            case R.id.activity_setting_logout_btn:
                CustomApplication.getInstance().logout();
                finish();
                startAnimActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case UPDATE_NICK_CONTENT:
                    Bundle b;
                    b = data.getExtras();
                    String nick = b.getString(Constant.UPDATE_BACK_CONTENT);
                    updateNick(nick);
                    break;
                case UPDATE_SIGNATURE_CONTENT:
                    Bundle c;
                    c = data.getExtras();
                    String signatrue = c.getString(Constant.UPDATE_BACK_CONTENT);
                    updateSign(signatrue);
                    break;
                case UPDATE_AVATAR:
                    initMeInfo();
                    mAvatarLayout.performClick();
                    break;
                case UPDATE_SIGN:
                    initMeInfo();
                    mAvatarLayout.performClick();
                case UPDATE_NICK:
                    initMeInfo();
                    mNickLayout.performClick();
                    break;
                case Constant.REQUESTCODE_UPLOADAVATAR_CAMERA:
                    String files = CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime;
                    File file = new File(files);
                    if (file.exists() && file.length() > 0) {
                        Uri uri = Uri.fromFile(file);
                        startPhotoZoom(uri);
                    } else {

                    }
                    break;
                case Constant.REQUESTCODE_UPLOADAVATAR_LOCATION:
                    if (data == null) {
                        return;
                    }
                    startPhotoZoom(data.getData());
                    break;
                case Constant.REQUESTCODE_UPLOADAVATAR_CROP:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            Bitmap bitmap = extras.getParcelable("data");
                            String iconUrl = saveToSdCard(bitmap);
                            mAvatarImg.setImageBitmap(bitmap);
                            updateAvatar(iconUrl);
                        }
                    }

                default:
                    break;
            }
        }
    }


    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 120);
        intent.putExtra("outputY", 120);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);

    }

    private void redictToLogin(int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivityForResult(intent, requestCode);
        OverridePendingUtil.in(ProfileActivity.this);
        ShowToast("请先登录。");
    }

    public void showAlbumDialog() {
        albumDialog = new AlertDialog.Builder(mContext).create();
        albumDialog.setCanceledOnTouchOutside(true);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_usericon, null);
        albumDialog.show();
        albumDialog.setContentView(v);
        albumDialog.getWindow().setGravity(Gravity.CENTER);


        TextView albumPic = (TextView) v.findViewById(R.id.album_pic);
        TextView cameraPic = (TextView) v.findViewById(R.id.camera_pic);
        albumPic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                albumDialog.dismiss();
                Date date1 = new Date(System.currentTimeMillis());
                dateTime = date1.getTime() + "";
                getAvataFromAlbum();
            }
        });
        cameraPic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                albumDialog.dismiss();
                Date date = new Date(System.currentTimeMillis());
                dateTime = date.getTime() + "";
                getAvataFromCamera();
            }
        });
    }

    private void getAvataFromCamera() {
        File f = new File(CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(f);
        Log.e("uri", uri + "");

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(camera, 1);
        OverridePendingUtil.in(ProfileActivity.this);
    }

    private void getAvataFromAlbum() {
        Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
        intent2.setType("image/*");
        startActivityForResult(intent2, 2);
        OverridePendingUtil.in(ProfileActivity.this);
    }


    private void addFriend() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("正在添加...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        // 发送tag请求
        BmobChatManager.getInstance(this).sendTagMessage(MsgTag.ADD_CONTACT,
                mUser.getObjectId(), new PushListener() {
                    @Override
                    public void onSuccess() {
                        progress.dismiss();
                        ShowToast("发送请求成功，等待对方验证！");
                    }

                    @Override
                    public void onFailure(int arg0, final String arg1) {
                        progress.dismiss();
                        ShowToast("发送请求成功，等待对方验证！");
                        LogE("发送请求失败:" + arg1);
                    }
                }
        );
    }

    private void showBlackDialog(final String username) {
        DialogTips dialog = new DialogTips(this, "加入黑名单",
                "加入黑名单，你将不再收到对方的消息，确定要继续吗？", "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                // 添加到黑名单列表
                mUserManager.addBlack(username, new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        ShowToast("黑名单添加成功!");
                        mAddBlackBtn.setVisibility(View.GONE);
                        // 重新设置下内存中保存的好友列表
                        CustomApplication.getInstance().setContactList(
                                CollectionUtils.list2map(BmobDB.create(
                                        ProfileActivity.this)
                                        .getContactList())
                        );
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        ShowToast("黑名单添加失败:" + arg1);
                    }
                });
            }
        });
        // 显示确认对话框
        dialog.show();
        dialog = null;
    }

    /**
     * 更新信息
     */
    private void updateAvatar(String avatarPath) {
        if (avatarPath != null) {
            final BmobFile file = new BmobFile(new File(avatarPath));
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("上传头像");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            file.upload(this, new UploadFileListener() {
                @Override
                public void onProgress(Integer integer) {
                    progressDialog.setMessage(integer + "");
                }

                @Override
                public void onSuccess() {
                    User currentUser = BmobUser.getCurrentUser(mContext, User.class);
                    currentUser.setAvatarImg(file);
                    currentUser.setAvatar(file.getFileUrl(mContext));
                    refreshAvatar(file.getFileUrl(mContext));
                    currentUser.update(mContext, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            ShowToast("更改头像成功。");
                        }

                        @Override
                        public void onFailure(int arg0, String arg1) {
                            ShowToast("更新头像失败。请检查网络~");
                            L.i(TAG, "更新失败2-->" + arg1);
                        }
                    });
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(int i, String s) {
                    ShowToast("上传头像失败。请检查网络~");
                    L.i(TAG, "上传文件失败。" + s);
                    progressDialog.dismiss();
                }
            });
        }

    }


    private void refreshAvatar(String avatar) {
        if (avatar != null && !avatar.equals("")) {
            ImageLoader.getInstance().displayImage(avatar, mAvatarImg, ImageLoadOptions.getOptions());
        }
    }


    private void updateNick(String nick) {
        User user = BmobUser.getCurrentUser(mContext, User.class);
        if (user != null) {
            user.setNick(nick);
            user.update(mContext, new UpdateListener() {

                @Override
                public void onSuccess() {
                    L.i(TAG, "更新信息成功。");
                }

                @Override
                public void onFailure(int arg0, String arg1) {
                    ShowToast("更新信息失败。请检查网络~");
                    L.i(TAG, "更新失败1-->" + arg1);
                }
            });
        } else {
            redictToLogin(UPDATE_NICK);
        }

    }

    private void updateSign(String signature) {
        User user = BmobUser.getCurrentUser(mContext, User.class);
        if (user != null) {
            user.setSignature(signature);
            user.update(mContext, new UpdateListener() {

                @Override
                public void onSuccess() {
                    L.i(TAG, "更新信息成功。");
                }

                @Override
                public void onFailure(int arg0, String arg1) {
                    // TODO Auto-generated method stub
                    ShowToast("更新信息失败。请检查网络~");
                    L.i(TAG, "更新失败1-->" + arg1);
                }
            });
        } else {
            redictToLogin(UPDATE_NICK);
        }
    }


    public String saveToSdCard(Bitmap bitmap) {
        String files = CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime + "_12";
        File file = new File(files);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        L.i(TAG, file.getAbsolutePath());
        return file.getAbsolutePath();
    }

   /* private void initPersonalInfo() {
        mUser = BmobUser.getCurrentUser(this, User.class);
        if (mUser != null) {
            mNickText.setText(mUser.getNick());
            mSignatureText.setText(mUser.getSignature());
            mAccountText.setText(mUser.getUsername());
            if (mUser.getSex().equals(Constant.SEX_FEMALE)) {
                mSexSwitch.setChecked(true);
                sputil.setValue("sex_settings", 0);
            } else {
                mSexSwitch.setChecked(false);
                sputil.setValue("sex_settings", 1);

            }
            BmobFile avatarFile = mUser.getAvatarImg();
            if (avatarFile != null) {
                ImageLoader.getInstance()
                        .displayImage(avatarFile.getFileUrl(), mAvatarImg,
                                CustomApplication.getInstance().getOptions(R.drawable.user_icon_default_main),
                                new SimpleImageLoadingListener() {

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view,
                                                                  Bitmap loadedImage) {
                                        // TODO Auto-generated method stub
                                        super.onLoadingComplete(imageUri, view, loadedImage);
                                    }

                                }
                        );

            }
        }
    }*/

}
