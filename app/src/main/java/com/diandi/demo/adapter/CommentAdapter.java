package com.diandi.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diandi.demo.R;
import com.diandi.demo.adapter.base.BaseListAdapter;
import com.diandi.demo.model.diandi.Comment;
import com.diandi.demo.util.L;

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
public class CommentAdapter extends BaseListAdapter<Comment> {

    public CommentAdapter(Context context, List<Comment> list) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.comment_item, null);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName_comment);
            viewHolder.commentContent = (TextView) convertView.findViewById(R.id.content_comment);
            viewHolder.index = (TextView) convertView.findViewById(R.id.index_comment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Comment comment = mDataList.get(position);
        if (comment.getUser() != null) {
            viewHolder.userName.setText(comment.getUser().getNick());
            L.i("CommentActivity", "NAME:" + comment.getUser().getUsername());
        } else {
            viewHolder.userName.setText("墙友");
        }
        viewHolder.index.setText((position + 1) + "楼");
        viewHolder.commentContent.setText(comment.getCommentContent());
        return convertView;
    }

    public static class ViewHolder {
        public TextView userName;
        public TextView commentContent;
        public TextView index;
    }
}
