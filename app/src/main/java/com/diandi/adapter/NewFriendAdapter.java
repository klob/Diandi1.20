package com.diandi.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.base.BaseListAdapter;
import com.diandi.adapter.base.ViewHolder;
import com.diandi.util.CollectionUtils;
import com.diandi.util.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.UpdateListener;

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
public class NewFriendAdapter extends BaseListAdapter<BmobInvitation> {

    public NewFriendAdapter(Context context, List<BmobInvitation> list) {
        super(context, list);
    }
    @Override
    public View bindView(int arg0, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_add_friend, null);
        }
        final BmobInvitation msg = getList().get(arg0);

        ImageView avatarImg = ViewHolder.get(convertView, R.id.item_add_friend_avatar_img);
        TextView nameText = ViewHolder.get(convertView, R.id.item_add_friend_name_text);
        final Button addBtn = ViewHolder.get(convertView, R.id.item_add_friend_add_btn);

        String avatar = msg.getAvatar();
        if (avatar != null && !avatar.equals("")) {
            ImageLoader.getInstance().displayImage(avatar, avatarImg, ImageLoadOptions.getOptions());
        } else {
            avatarImg.setImageResource(R.drawable.default_head);
        }

        int status = msg.getStatus();
        if (status == BmobConfig.INVITE_ADD_NO_VALIDATION || status == BmobConfig.INVITE_ADD_NO_VALI_RECEIVED) {
//			btn_add.setText("同意");
//			btn_add.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_login_selector));
//			btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_white));
            addBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    BmobLog.i("点击同意按钮:" + msg.getFromid());
                    agressAdd(addBtn, msg);
                }
            });
        } else if (status == BmobConfig.INVITE_ADD_AGREE) {
            addBtn.setText("已同意");
            addBtn.setBackgroundDrawable(null);
            addBtn.setTextColor(mContext.getResources().getColor(R.color.base_color_text_black));
            addBtn.setEnabled(false);
        }
        nameText.setText(msg.getFromname());
        return convertView;
    }

    /**
     * 添加好友
     * agressAdd
     *
     * @param @param btn_add
     * @param @param msg
     * @return void
     * @throws
     * @Title: agressAdd
     * @Description: TODO
     */
    private void agressAdd(final Button btn_add, final BmobInvitation msg) {
        final ProgressDialog progress = new ProgressDialog(mContext);
        progress.setMessage("正在添加...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        try {
            //同意添加好友
            BmobUserManager.getInstance(mContext).agreeAddContact(msg, new UpdateListener() {
                @Override
                public void onSuccess() {
                    progress.dismiss();
                    btn_add.setText("已同意");
                    btn_add.setBackgroundDrawable(null);
                    btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_black));
                    btn_add.setEnabled(false);
                    //保存到application中方便比较
                    CustomApplication.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(mContext).getContactList()));
                }

                @Override
                public void onFailure(int arg0, final String arg1) {
                    progress.dismiss();
                    ShowToast("添加失败: " + arg1);
                }
            });
        } catch (final Exception e) {
            progress.dismiss();
            ShowToast("添加失败: " + e.getMessage());
        }
    }
}
