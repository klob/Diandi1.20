package com.diandi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diandi.R;
import com.diandi.adapter.base.BaseListAdapter;
import com.diandi.adapter.base.ViewHolder;
import com.diandi.util.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.bmob.im.bean.BmobChatUser;

/** 黑名单
 * @ClassName: BlackListAdapter
 * @Description: TODO
 * @author smile
 * @date 2014-6-24 下午5:27:14
 */
public class BlackListAdapter extends BaseListAdapter<BmobChatUser> {

    public BlackListAdapter(Context context, List<BmobChatUser> list) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_blacklist, null);
        }
        final BmobChatUser contract = getList().get(position);
        TextView tv_friend_name = ViewHolder.get(convertView, R.id.tv_friend_name);
        ImageView iv_avatar = ViewHolder.get(convertView, R.id.img_friend_avatar);
        String avatar = contract.getAvatar();
        if (avatar != null && !avatar.equals("")) {
            ImageLoader.getInstance().displayImage(avatar, iv_avatar, ImageLoadOptions.getOptions());
        } else {
            iv_avatar.setImageResource(R.drawable.default_head);
        }
        tv_friend_name.setText(contract.getUsername());
        return convertView;
    }

}
