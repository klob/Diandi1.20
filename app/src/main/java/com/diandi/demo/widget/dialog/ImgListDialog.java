package com.diandi.demo.widget.dialog;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.diandi.demo.R;

import java.util.ArrayList;
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

public class ImgListDialog extends ListDialog {
    public ImgListDialog(Context context) {
        super(context);
    }

    public ImgListDialog(Context context, int theme) {
        super(context, theme);
    }

    public ImgListDialog(Context context, String title, ArrayList<String> list) {
        super(context, title, list);
    }

    @Override
    public void setListText(ArrayList<String> lists) {
        mListText.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_list_dialog, lists));
    }

    public class ImgListDialogAdapter extends ArrayAdapter<ActionItem> {
        public ImgListDialogAdapter(Context context, int resource, List<ActionItem> objects) {
            super(context, resource, objects);
        }
    }


}
