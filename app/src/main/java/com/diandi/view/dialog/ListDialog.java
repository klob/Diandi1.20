package com.diandi.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.diandi.R;

import java.util.ArrayList;

/**
 * Created by sulinger  .
 * date:14-9-13 .
 * time:15:05 .
 * project:DianDi1.1.0 .
 * Copyright © sulinger .All Rights Reserved.
 */
public class ListDialog extends Dialog {

    private TextView mTitleText;
    protected ListView mListText;
    protected Context mContext;
    private String mTitle;
    private ArrayList<String> mListContent;

    public ListDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ListDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    public ListDialog(Context context, String title, ArrayList<String> list) {
        super(context, R.style.ListDialog);
        this.mContext = context;
        this.mTitle = title;
        this.mListContent = list;


    }

    @Override
    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        findView();
        initView();
    }

    private void findView() {
        setContentView(R.layout.dialog_list);
        mTitleText = (TextView) findViewById(R.id.list_dialog_title);
        mListText = (ListView) findViewById(R.id.list_dialog_list);
    }

    private void initView() {
        setTitleText(mTitle);
        setListText(mListContent);
        setCanceledOnTouchOutside(true);

    }

    public void setTitleText(String titleText) {
        mTitleText.setText(titleText);
    }

    public void setListText(ArrayList<String> lists) {
        mListText.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_list_dialog, lists));
    }

    public void setOnListItemClickListener(OnItemClickListener onListItemClickListener) {
        mListText.setOnItemClickListener(onListItemClickListener);
    }
    public ListView getList()
    {
        return mListText;
    }
}



