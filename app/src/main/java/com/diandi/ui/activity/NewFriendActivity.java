package com.diandi.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.diandi.R;
import com.diandi.adapter.NewFriendAdapter;
import com.diandi.widget.HeaderLayout;
import com.diandi.widget.dialog.DialogTips;

import cn.bmob.im.bean.BmobInvitation;
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
public class NewFriendActivity extends ActivityBase implements OnItemLongClickListener {

    ListView listview;
    NewFriendAdapter adapter;
    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    void initView() {
        initTopBarForBoth("新朋友", R.drawable.icon_clear_selector, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                DialogTips dialog = new DialogTips(NewFriendActivity.this, "提示", "删除所有好友请求", "确定", true, true);
                // 设置成功事件
                dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int userId) {
                        deleteAllInvite();
                        adapter.removeAll();
                    }
                });
                // 显示确认对话框
                dialog.show();
                dialog = null;

            }
        });
        from = getIntent().getStringExtra("from");
        adapter = new NewFriendAdapter(this, BmobDB.create(this).queryBmobInviteList());
        listview.setAdapter(adapter);
        if (from == null) {//若来自通知栏的点击，则定位到最后一条
            listview.setSelection(adapter.getCount());
        }
        bindEvent();
    }

    @Override
    void findView() {
        setContentView(R.layout.activity_new_friend);
        listview = (ListView) findViewById(R.id.list_newfriend);

    }

    @Override
    void bindEvent() {
        listview.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
                                   long arg3) {
        BmobInvitation invite = adapter.getItem(position);
        showDeleteDialog(position, invite);
        return true;
    }

    public void showDeleteDialog(final int position, final BmobInvitation invite) {
        DialogTips dialog = new DialogTips(this, invite.getFromname(), "删除好友请求", "确定", true, true);
        // 设置成功事件
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                deleteInvite(position, invite);
            }
        });
        // 显示确认对话框
        dialog.show();
        dialog = null;
    }

    /**
     * 删除请求
     * deleteRecent
     *
     * @param @param recent
     * @return void
     * @throws
     */
    private void deleteInvite(int position, BmobInvitation invite) {
        adapter.remove(position);
        BmobDB.create(this).deleteInviteMsg(invite.getFromid(), Long.toString(invite.getTime()));
    }

    private void deleteAllInvite() {
        for (BmobInvitation invitation : adapter.getList()) {
            BmobDB.create(this).deleteInviteMsg(invitation.getFromid(), Long.toString(invitation.getTime()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (from == null) {
            startAnimActivity(MainActivity.class);
        }
    }


}
