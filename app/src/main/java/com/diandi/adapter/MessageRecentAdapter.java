package com.diandi.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.diandi.R;
import com.diandi.adapter.base.ViewHolder;
import com.diandi.util.FaceTextUtils;
import com.diandi.util.ImageLoadOptions;
import com.diandi.util.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.config.BmobConfig;
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
public class MessageRecentAdapter extends ArrayAdapter<BmobRecent> implements Filterable {

    private LayoutInflater inflater;
    private List<BmobRecent> mData;
    private Context mContext;

    public MessageRecentAdapter(Context context, int textViewResourceId, List<BmobRecent> objects) {
        super(context, textViewResourceId, objects);
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        mData = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final BmobRecent item = mData.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_conversation, parent, false);
        }
        ImageView iv_recent_avatar = ViewHolder.get(convertView, R.id.iv_recent_avatar);
        TextView tv_recent_name = ViewHolder.get(convertView, R.id.tv_recent_name);
        TextView tv_recent_msg = ViewHolder.get(convertView, R.id.tv_recent_msg);
        TextView tv_recent_time = ViewHolder.get(convertView, R.id.tv_recent_time);
        TextView tv_recent_unread = ViewHolder.get(convertView, R.id.tv_recent_unread);

        //填充数据
        String avatar = item.getAvatar();
        if (avatar != null && !avatar.equals("")) {
            ImageLoader.getInstance().displayImage(avatar, iv_recent_avatar, ImageLoadOptions.getOptions());
        } else {
            iv_recent_avatar.setImageResource(R.drawable.default_head_cry);
        }

        tv_recent_name.setText(item.getNick());
        tv_recent_time.setText(TimeUtil.getChatTime(item.getTime()));
        int num = BmobDB.create(mContext).getUnreadCount(item.getTargetid());
        //显示内容
        if (item.getType() == BmobConfig.TYPE_TEXT) {
            SpannableString spannableString = FaceTextUtils.toSpannableString(mContext, item.getMessage());
            tv_recent_msg.setText(spannableString);
        } else if (item.getType() == BmobConfig.TYPE_IMAGE) {
            tv_recent_msg.setText("[图片]");
        } else if (item.getType() == BmobConfig.TYPE_LOCATION) {
            String all = item.getMessage();
            if (all != null && !all.equals("")) {//位置类型的信息组装格式：地理位置&维度&经度
                String address = all.split("&")[0];
                tv_recent_msg.setText("[位置]" + address);
            }
        } else if (item.getType() == BmobConfig.TYPE_VOICE) {
            tv_recent_msg.setText("[语音]");
        }

        if (num > 0) {
            tv_recent_unread.setVisibility(View.VISIBLE);
            tv_recent_unread.setText(num + "");
        } else {
            tv_recent_unread.setVisibility(View.GONE);
        }
        return convertView;
    }

}
