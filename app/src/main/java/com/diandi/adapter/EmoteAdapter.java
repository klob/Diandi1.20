package com.diandi.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diandi.R;
import com.diandi.adapter.base.BaseArrayListAdapter;
import com.diandi.bean.FaceText;

import java.util.List;

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
public class EmoteAdapter extends BaseArrayListAdapter {

    public EmoteAdapter(Context context, List<FaceText> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_face_text, null);
            holder = new ViewHolder();
            holder.mIvImage = (ImageView) convertView
                    .findViewById(R.id.v_face_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FaceText faceText = (FaceText) getItem(position);
        String key = faceText.text.substring(1);
        Drawable drawable = mContext.getResources().getDrawable(mContext.getResources().getIdentifier(key, "drawable", mContext.getPackageName()));
        holder.mIvImage.setImageDrawable(drawable);
        return convertView;
    }

    class ViewHolder {
        ImageView mIvImage;
    }
}
