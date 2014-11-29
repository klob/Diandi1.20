package com.diandi.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.diandi.R;
import com.diandi.adapter.base.BaseListAdapter;
import com.diandi.adapter.base.ViewHolder;
import com.diandi.util.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.inteface.MsgTag;
import cn.bmob.v3.listener.PushListener;

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
public class AddFriendAdapter extends BaseListAdapter<BmobChatUser> {

    public AddFriendAdapter(Context context, List<BmobChatUser> list) {
        super(context, list);
    }

    @Override
    public View bindView(int arg0, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_add_friend, null);
        }
        final BmobChatUser contract = getList().get(arg0);

        ImageView avatarImg = ViewHolder.get(convertView, R.id.item_add_friend_avatar_img);
        TextView nameText = ViewHolder.get(convertView, R.id.item_add_friend_name_text);
        Button addBtn = ViewHolder.get(convertView, R.id.item_add_friend_add_btn);

        String avatar = contract.getAvatar();
        ImageLoader.getInstance().displayImage(avatar, avatarImg, ImageLoadOptions.getOptions());
        nameText.setText(contract.getUsername());
        addBtn.setText("添加");
        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final ProgressDialog progress = new ProgressDialog(mContext);
                progress.setMessage("正在添加...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();
                //发送tag请求
                BmobChatManager.getInstance(mContext).sendTagMessage(MsgTag.ADD_CONTACT, contract.getObjectId(), new PushListener() {
                    @Override
                    public void onSuccess() {
                        progress.dismiss();
                        ShowToast("发送请求成功，等待对方验证!");
                    }

                    @Override
                    public void onFailure(int arg0, final String arg1) {
                        progress.dismiss();
                        ShowToast("发送请求失败，请重新添加!");
                        ShowLog("发送请求失败:" + arg1);
                    }
                });
            }
        });
        return convertView;
    }

}
