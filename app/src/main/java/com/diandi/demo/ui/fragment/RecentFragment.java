package com.diandi.demo.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.diandi.demo.R;
import com.diandi.demo.adapter.MessageRecentAdapter;
import com.diandi.demo.ui.activity.ChatActivity;
import com.diandi.demo.widget.ClearEditText;
import com.diandi.demo.widget.dialog.DialogTips;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;

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
public class RecentFragment extends BaseFragment implements OnItemClickListener, OnItemLongClickListener {

    private ClearEditText mClearEditText;
    private ListView mRecentListview;
    private MessageRecentAdapter mMessageRecentAdapter;
    private boolean mHidden;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recent, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        findView();
        initView();
    }


    @Override
    void findView() {
        mRecentListview = (ListView) findViewById(R.id.fragment_recent_listview);
        mClearEditText = (ClearEditText) findViewById(R.id.fragment_recent_edit);
    }

    public void initView() {
        initTopBarForOnlyTitle("会话");
        mRecentListview.setOnItemClickListener(this);
        mRecentListview.setOnItemLongClickListener(this);
        mMessageRecentAdapter = new MessageRecentAdapter(getActivity(), R.layout.item_conversation, BmobDB.create(getActivity()).queryRecents());
        mRecentListview.setAdapter(mMessageRecentAdapter);
        bindEvent();

    }


    @Override
    void bindEvent() {
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mMessageRecentAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void deleteRecent(BmobRecent recent) {
        mMessageRecentAdapter.remove(recent);
        BmobDB.create(getActivity()).deleteRecent(recent.getTargetid());
        BmobDB.create(getActivity()).deleteMessages(recent.getTargetid());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
                                   long arg3) {
        // TODO Auto-generated method stub
        BmobRecent recent = mMessageRecentAdapter.getItem(position);
        showDeleteDialog(recent);
        return true;
    }

    public void showDeleteDialog(final BmobRecent recent) {
        DialogTips dialog = new DialogTips(getActivity(), recent.getUserName(), "删除会话", "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                deleteRecent(recent);
            }
        });
        dialog.show();
        dialog = null;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        // TODO Auto-generated method stub
        BmobRecent recent = mMessageRecentAdapter.getItem(position);
        //重置未读消息
        BmobDB.create(getActivity()).resetUnread(recent.getTargetid());
        //组装聊天对象
        BmobChatUser user = new BmobChatUser();
        user.setAvatar(recent.getAvatar());
        user.setNick(recent.getNick());
        user.setUsername(recent.getUserName());
        user.setObjectId(recent.getTargetid());
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("user", user);
        startAnimActivity(intent);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.mHidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    public void refresh() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    mMessageRecentAdapter = new MessageRecentAdapter(getActivity(), R.layout.item_conversation, BmobDB.create(getActivity()).queryRecents());
                    mRecentListview.setAdapter(mMessageRecentAdapter);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mHidden) {
            refresh();
        }
    }

}
