package com.diandi.demo.adapter.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.diandi.demo.model.User;
import com.diandi.demo.sync.UserHelper;
import com.diandi.demo.util.L;
import com.diandi.demo.util.OverridePendingUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.im.util.BmobLog;

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
@SuppressLint("UseSparseArrays")
public abstract class BaseListAdapter<E> extends BaseAdapter {

    public String TAG = getClass().getSimpleName();
    public List<E> mDataList;
    public Context mContext;
    public LayoutInflater mInflater;
    // adapter中的内部点击事件
    public Map<Integer, onInternalClickListener> canClickItem;
    Toast mToast;

    public BaseListAdapter(Context context, List<E> list) {
        super();
        this.mContext = context;
        this.mDataList = list;
        mInflater = LayoutInflater.from(context);
    }

    public List<E> getList() {
        return mDataList;
    }

    public void setList(List<E> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }

    public void add(E e) {
        this.mDataList.add(e);
        notifyDataSetChanged();
    }

    public void addAll(List<E> list) {
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeAll() {
        this.mDataList.clear();
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.mDataList.remove(position);
        notifyDataSetChanged();
    }

    public void L(String str) {
        L.e(TAG, str);
    }

    public User getCurrentUser() {
        return UserHelper.getCurrentUser(mContext);
    }


    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public E getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = bindView(position, convertView, parent);
        // 绑定内部点击监听
        addInternalClickListener(convertView, position, mDataList.get(position));
        return convertView;
    }

    public abstract View bindView(int position, View convertView, ViewGroup parent);

    private void addInternalClickListener(final View itemV, final Integer position, final Object valuesMap) {
        if (canClickItem != null) {
            for (Integer key : canClickItem.keySet()) {
                View inView = itemV.findViewById(key);
                final onInternalClickListener inviewListener = canClickItem.get(key);
                if (inView != null && inviewListener != null) {
                    inView.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            inviewListener.OnClickListener(itemV, v, position,
                                    valuesMap);
                        }
                    });
                }
            }
        }
    }

    public void setOnInViewClickListener(Integer key,
                                         onInternalClickListener onClickListener) {
        if (canClickItem == null)
            canClickItem = new HashMap<Integer, onInternalClickListener>();
        canClickItem.put(key, onClickListener);
    }

    public void ShowToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            ((Activity) mContext).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(mContext, text,
                                Toast.LENGTH_SHORT);
                    } else {
                        mToast.setText(text);
                    }
                    mToast.show();
                }
            });

        }
    }

    public void ShowLog(String msg) {
        BmobLog.i(msg);
    }

    public void startAnimActivity(Class<?> cla) {
        mContext.startActivity(new Intent(mContext, cla));
        if (mContext instanceof Activity) {
            OverridePendingUtil.in((Activity) mContext);
        }
    }

    public void startAnimActivity(Intent intent) {
        mContext.startActivity(intent);
        if (mContext instanceof Activity) {
            OverridePendingUtil.in((Activity) mContext);
        }
    }

    public interface onInternalClickListener {
        public void OnClickListener(View parentV, View v, Integer position,
                                    Object values);
    }

}
