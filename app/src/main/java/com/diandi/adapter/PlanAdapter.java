package com.diandi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diandi.R;
import com.diandi.adapter.base.BaseListAdapter;
import com.diandi.adapter.base.ViewHolder;
import com.diandi.bean.Plan;
import com.diandi.io.MemoryCache;
import com.diandi.util.Calculator;
import com.diandi.util.TimeUtil;

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

public class PlanAdapter extends BaseListAdapter<Plan> {

    public PlanAdapter(Context context, List<Plan> list) {
        super(context, list);
    }

    @Override
    public void setList(List<Plan> list) {
        super.setList(list);
    }

    @Override
    public View bindView(int position, View contentView, ViewGroup parent) {
        LinearLayout backgroundLayout;
        TextView titleText;
        TextView timeText;
        ImageView headImg;
        TextView progressText;
        TextView dueText;
        TextView noteText;
        if (contentView == null) {
            contentView = mInflater.inflate(R.layout.item_plan, null);

        }
        titleText = ViewHolder.get(contentView, R.id.item_plan_title_text);
        timeText = ViewHolder.get(contentView, R.id.item_plan_time_text);
        headImg = ViewHolder.get(contentView, R.id.item_plan_head_img);
        progressText = ViewHolder.get(contentView, R.id.item_plan_progress_text);
        backgroundLayout = ViewHolder.get(contentView, R.id.item_plan_background);
        dueText = ViewHolder.get(contentView, R.id.item_plan_due_text);
        noteText = ViewHolder.get(contentView, R.id.item_plan_note_text);


        String title = mDataList.get(position).getTitle();

        String dueTime = TimeUtil.getDueTimeString(mDataList.get(position).getPlanDate());


        int day = Calculator.calculate(dueTime);

        progressText.setText(mDataList.get(position).getProgress() + "%");
        dueText.setText(dueTime);
        if (day < 0) {
            titleText.setText(title + "已经");
        } else {
            titleText.setText(title + "还剩");
        }
        timeText.setText(String.valueOf(Math.abs(day)) + "天");

        int imageResId;
        int t = mDataList.get(position).getType();
        switch (t) {
            case Plan.URGENT_TOP:
                imageResId = R.drawable.cover_bg4;
                backgroundLayout.setBackgroundResource(R.drawable.listitem_red);
                break;
            case Plan.URGENT_EXTRA:
                imageResId = R.drawable.cover_bg4;
                backgroundLayout.setBackgroundResource(R.drawable.listitem_red);
                break;
            case Plan.URGENT_HIGH:
                imageResId = R.drawable.cover_bg5;
                backgroundLayout.setBackgroundResource(R.drawable.listitem_yellow);
                break;
            case Plan.URGENT_MIDDLE:
                imageResId = R.drawable.cover_bg6;
                backgroundLayout.setBackgroundResource(R.drawable.listitem_blue);
                break;
            case Plan.URGENT_LOW:
                imageResId = R.drawable.cover_bg3;
                backgroundLayout.setBackgroundResource(R.drawable.listitem_green);
                break;
            default:
                imageResId = R.drawable.cover_bg1;
                backgroundLayout.setBackgroundResource(R.drawable.listitem_white);
                break;
        }
        backgroundLayout.setPadding(6, 8, 8, 8);

        MemoryCache memoryCache = new MemoryCache();
        Bitmap bitmap = memoryCache.getBitmapFromMemCache(imageResId);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
            memoryCache.addBitmapToMemoryCache(imageResId, bitmap);
        }
        headImg.setImageBitmap(bitmap);
        noteText.setText(mDataList.get(position).getNote());
        return contentView;
    }
}







