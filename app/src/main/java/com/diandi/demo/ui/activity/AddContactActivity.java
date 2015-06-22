package com.diandi.demo.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.diandi.demo.R;
import com.diandi.demo.adapter.AddFriendAdapter;
import com.diandi.demo.util.CollectionUtils;
import com.diandi.demo.widget.xlist.XListView;
import com.diandi.demo.widget.xlist.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.task.BRequest;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.CountListener;
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
public class AddContactActivity extends ActivityBase implements OnClickListener, IXListViewListener, AdapterView.OnItemClickListener {


    private EditText mmSearchNameEdit;
    private Button mSearchBtn;
    private List<BmobChatUser> mUsers = new ArrayList<BmobChatUser>();
    private XListView mSearcgListView;
    private AddFriendAdapter mAddFriendAdapter;

    private int mCurPage = 0;
    private String mSearchName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    @Override
    void findView() {
        setContentView(R.layout.activity_add_contact);
        mmSearchNameEdit = (EditText) findViewById(R.id.activity_add_contact_search_name_edit);
        mSearchBtn = (Button) findViewById(R.id.activity_add_contact_search_btn);
        mSearcgListView = (XListView) findViewById(R.id.activity_add_contact_search_listview);
    }

    @Override
    void initView() {
        initTopBarForLeft("查找好友");
        initXListView();
        bindEvent();
    }

    @Override
    void bindEvent() {
        mSearchBtn.setOnClickListener(this);
    }

    private void initXListView() {
        // 首先不允许加载更多
        mSearcgListView.setPullLoadEnable(false);
        // 不允许下拉
        mSearcgListView.setPullRefreshEnable(false);
        // 设置监听器
        mSearcgListView.setXListViewListener(this);
        mSearcgListView.pullRefreshing();
        mAddFriendAdapter = new AddFriendAdapter(this, mUsers);
        mSearcgListView.setAdapter(mAddFriendAdapter);
        mSearcgListView.setOnItemClickListener(this);
    }

    private void initSearchList(final boolean isUpdate) {
        final ProgressDialog progressDialog = new ProgressDialog(AddContactActivity.this);
        if (!isUpdate) {
            progressDialog.setMessage("正在搜索...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
        }
        mUserManager.queryUserByPage(isUpdate, 0, mSearchName, new FindListener<BmobChatUser>() {

            @Override
            public void onError(int arg0, String arg1) {
                BmobLog.i("查询错误:" + arg1);
                if (mUsers != null) {
                    mUsers.clear();
                }
                ShowToast("用户不存在");
                mSearcgListView.setPullLoadEnable(false);
                refreshPull();
                //这样能保证每次查询都是从头开始
                mCurPage = 0;
            }

            @Override
            public void onSuccess(List<BmobChatUser> arg0) {
                if (CollectionUtils.isNotNull(arg0)) {
                    if (isUpdate) {
                        mUsers.clear();
                    }
                    mAddFriendAdapter.addAll(arg0);
                    if (arg0.size() < BRequest.QUERY_LIMIT_COUNT) {
                        mSearcgListView.setPullLoadEnable(false);
                        ShowToast("用户搜索完成!");
                    } else {
                        mSearcgListView.setPullLoadEnable(true);
                    }
                } else {
                    BmobLog.i("查询成功:无返回值");
                    if (mUsers != null) {
                        mUsers.clear();
                    }
                    ShowToast("用户不存在");
                }
                if (!isUpdate) {
                    progressDialog.dismiss();
                } else {
                    refreshPull();
                }
                //这样能保证每次查询都是从头开始
                mCurPage = 0;
            }
        });

    }

    private void queryMoreSearchList(int page) {
        mUserManager.queryUserByPage(true, page, mSearchName, new FindListener<BmobChatUser>() {
            @Override
            public void onSuccess(List<BmobChatUser> arg0) {
                if (CollectionUtils.isNotNull(arg0)) {
                    mAddFriendAdapter.addAll(arg0);
                }
                refreshLoad();
            }

            @Override
            public void onError(int arg0, String arg1) {
                LogE("搜索更多用户出错:" + arg1);
                mSearcgListView.setPullLoadEnable(false);
                refreshLoad();
            }

        });
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        BmobChatUser user = mAddFriendAdapter.getItem(position - 1);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("from", "add");
        intent.putExtra("username", user.getUsername());
        startAnimActivity(intent);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.activity_add_contact_search_btn://搜索
                mUsers.clear();
                mSearchName = mmSearchNameEdit.getText().toString();
                if (mSearchName != null && !mSearchName.equals("")) {
                    initSearchList(false);
                } else {
                    ShowToast("请输入用户名");
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        mUserManager.querySearchTotalCount(mSearchName, new CountListener() {
            @Override
            public void onSuccess(int arg0) {
                if (arg0 > mUsers.size()) {
                    mCurPage++;
                    queryMoreSearchList(mCurPage);
                } else {
                    ShowToast("数据加载完成");
                    mSearcgListView.setPullLoadEnable(false);
                    refreshLoad();
                }
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                LogE("查询附近的人总数失败" + arg1);
                refreshLoad();
            }
        });
    }

    private void refreshLoad() {
        if (mSearcgListView.getPullLoading()) {
            mSearcgListView.stopLoadMore();
        }
    }

    private void refreshPull() {
        if (mSearcgListView.getPullRefreshing()) {
            mSearcgListView.stopRefresh();
        }
    }

}
