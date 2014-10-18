package com.diandi.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.BlackListAdapter;
import com.diandi.util.CollectionUtils;
import com.diandi.view.dialog.DialogTips;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 黑名单列表
 */
public class BlackListActivity extends ActivityBase implements OnItemClickListener {

    ListView mBlackListView;
    BlackListAdapter mBlackListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    @Override
    void findView() {
        setContentView(R.layout.activity_blacklist);
        mBlackListView = (ListView) findViewById(R.id.activity_black_black_list);
        initTopBarForLeft("黑名单");
    }

    void initView() {
        initTopBarForLeft("黑名单");
        mBlackListAdapter = new BlackListAdapter(this, BmobDB.create(this).getBlackList());
        mBlackListView.setAdapter(mBlackListAdapter);
        bindEvent();
    }


    @Override
    void bindEvent() {
        mBlackListView.setOnItemClickListener(this);
    }

    public void showRemoveBlackDialog(final int position, final BmobChatUser user) {
        DialogTips dialog = new DialogTips(this, "移出黑名单",
                "你确定将" + user.getUsername() + "移出黑名单吗?", "确定", true, true);
        // 设置成功事件
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                mBlackListAdapter.remove(position);
                mUserManager.removeBlack(user.getUsername(), new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        ShowToast("移出黑名单成功");
                        //重新设置下内存中保存的好友列表
                        CustomApplication.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(getApplicationContext()).getContactList()));
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        ShowToast("移出黑名单失败:" + arg1);
                    }
                });

            }
        });
        // 显示确认对话框
        dialog.show();
        dialog = null;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        BmobChatUser invite = (BmobChatUser) mBlackListAdapter.getItem(arg2);
        showRemoveBlackDialog(arg2, invite);
    }


}
