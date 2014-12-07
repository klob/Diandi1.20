package com.diandi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.FeedAdapter;
import com.diandi.config.Constant;
import com.diandi.model.User;
import com.diandi.model.diandi.DianDi;
import com.diandi.util.ImageLoadOptions;
import com.diandi.util.L;
import com.diandi.widget.xlist.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

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
public class PersonalActivity extends ActivityBase implements XListView.IXListViewListener, AdapterView.OnItemClickListener, View.OnClickListener {


    public static final int EDIT_USER = 1;
    private final static String PERSON_LIST = "person_list";
    private ImageView personalIcon;
    private TextView personalName;
    private TextView personalSign;
    private ImageView goSettings;
    private TextView personalTitle;
    private XListView mListView;
    private ArrayList<DianDi> mDianDis;
    private FeedAdapter mAdapter;
    private User mUser;
    private int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
        loadData();
    }

    void findView() {
        setContentView(R.layout.fragment_personal);
        personalIcon = (ImageView) findViewById(R.id.personal_icon);
        personalName = (TextView) findViewById(R.id.personl_name);
        personalSign = (TextView) findViewById(R.id.personl_signature);
        goSettings = (ImageView) findViewById(R.id.go_settings);
        personalTitle = (TextView) findViewById(R.id.personl_title);
        mListView = (XListView) findViewById(R.id.pull_refresh_list_personal);
    }

    void initView() {
        mDianDis = new ArrayList<DianDi>();
        mUser = CustomApplication.getInstance().getCurrentDianDi().getAuthor();
        updatePersonalInfo(mUser);

        if (CustomApplication.getInstance().getCache().getAsObject(PERSON_LIST + mUser.getObjectId()) != null) {
            mDianDis = (ArrayList<DianDi>) CustomApplication.getInstance().getCache().getAsObject(PERSON_LIST + mUser.getObjectId());
        }
        Log.e("mDianDis", mDianDis.toString());
        Log.e("size", mDianDis.size()+"");

        initTopBarForLeft("个人中心");
        initMyPublish();
        initXListView();
        bindEvent();
    }

    public void onStop() {
        super.onStop();
        if (mDianDis != null) {
            CustomApplication.getInstance().getCache().put(PERSON_LIST + mUser.getObjectId(), mDianDis);
        }
    }

    private void initXListView() {
        mListView.setOnItemClickListener(this);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        mListView.pullRefreshing();
        mAdapter = new FeedAdapter(this, mDianDis, FeedAdapter.DIANDI_PERSON);
        mListView.setAdapter(mAdapter);
    }

    private void initMyPublish() {
        if (isCurrentUser(mUser)) {
            personalTitle.setText("我发表过的");
            goSettings.setVisibility(View.INVISIBLE);
            User user = BmobUser.getCurrentUser(this, User.class);
            updatePersonalInfo(user);
        } else {
            goSettings.setVisibility(View.GONE);

            personalTitle.setText("ta发表过的");

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent();
        intent.setClass(PersonalActivity.this, CommentActivity.class);
        intent.putExtra("data", mDianDis.get(position - 1));
        startAnimActivity(intent);
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    private void refreshLoad() {
        if (mListView.getPullLoading()) {
            mListView.stopLoadMore();
        }
    }

    private void refreshPull() {
        if (mListView.getPullRefreshing()) {
            mListView.stopRefresh();
        }
    }

    private void updatePersonalInfo(User user) {
        personalName.setText(user.getNick());
        personalSign.setText(user.getSignature());
        ImageLoader.getInstance().displayImage(user.getAvatar(), personalIcon,
                ImageLoadOptions.getOptions(),
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        // TODO Auto-generated method stub
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        L.i(TAG, "load personal icon completed.");
                    }

                }
        );
    }

    //判断点击条目的用户是否是当前登录用户

    private boolean isCurrentUser(User user) {
        if (null != user) {
            User cUser = BmobUser.getCurrentUser(mContext, User.class);
            if (cUser != null && cUser.getObjectId().equals(user.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    protected void bindEvent() {
        // TODO Auto-generated method stub
        personalIcon.setOnClickListener(this);
        personalSign.setOnClickListener(this);
        personalTitle.setOnClickListener(this);
        goSettings.setOnClickListener(this);
    }

    protected void loadData() {
        BmobQuery<DianDi> query = new BmobQuery<DianDi>();
        query.setLimit(Constant.NUMBERS_PER_PAGE);
        query.setSkip(Constant.NUMBERS_PER_PAGE * (pageNum++));
        query.order("-createdAt");
        query.include("author");
        query.addWhereEqualTo("author", mUser);
        query.findObjects(this, new FindListener<DianDi>() {

                    @Override
                    public void onSuccess(List<DianDi> data) {

                        if (data.size() != 0 && data.get(data.size() - 1) != null) {
                            mDianDis.clear();
                            if (data.size() < Constant.NUMBERS_PER_PAGE) {
                                //     ShowToast("已加载完所有数据~");
                            }
                            mDianDis.addAll(data);
                            mAdapter.notifyDataSetChanged();

                        } else

                        {
                            ShowToast("暂无更多数据~");
                            pageNum--;
                        }

                        refreshPull();
                    }

                    @Override
                    public void onError(int arg0, String msg) {
                        // TODO Auto-generated method stub
                        L.i(TAG, "find failed." + msg);
                        pageNum--;
                        refreshPull();
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.personal_icon:
                Intent intent = new Intent(mContext, ProfileActivity.class);
                if (!isCurrentUser(mUser)) {
                    intent.putExtra("from", "add");
                    intent.putExtra("username", mUser.getUsername());
                } else {
                    intent.putExtra("from", "me");
                    intent.putExtra("username", mUser.getUsername());
                }
                startAnimActivity(intent);
                break;
            case R.id.personl_signature:
            case R.id.go_settings:
                if (isCurrentUser(mUser)) {
                    Intent intent2 = new Intent();
                    intent2.setClass(PersonalActivity.this, ProfileActivity.class);
                    startActivityForResult(intent2, EDIT_USER);
                    L.i(TAG, "current user edit...");
                }
                break;
            case R.id.personl_title:

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case EDIT_USER:
                    getCurrentUserInfo();
                    pageNum = 0;
                    loadData();
                    break;

                default:
                    break;
            }
        }
    }

    //  查询当前用户具体信息

    private void getCurrentUserInfo() {
        User user = BmobUser.getCurrentUser(PersonalActivity.this, User.class);
        L.i(TAG, "sign:" + user.getSignature());
        updatePersonalInfo(user);
        ShowToast("更新信息成功。");
    }


}

