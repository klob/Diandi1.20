package com.diandi.view.dialog;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diandi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ************************************************************
 * *********    User : SuLinger(462679107@qq.com) .
 * *********    Date : 2014-11-08  .
 * *********    Time:  2014-11-08  .
 * *********    Project name :Diandi1.18 .
 * *********    Copyright @ 2014, SuLinger, All Rights Reserved
 * *************************************************************
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

    public class ImgListDialogAdapter extends ArrayAdapter<ActionItem>
    {
        public ImgListDialogAdapter(Context context, int resource, List<ActionItem> objects) {
            super(context, resource, objects);
        }
    }


}
