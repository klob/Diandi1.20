package com.diandi.bean;

import cn.bmob.v3.BmobObject;

/**
 * ************************************************************
 * *********    User : SuLinger(462679107@qq.com) .
 * *********    Date : 2014-08-29  .
 * *********    Time:  2014-08-29  .
 * *********    Project name :DD .
 * *********    Copyright @ 2014, SuLinger, All Rights Reserved
 * *************************************************************
 */

public class Comment extends BmobObject {

    public static final String TAG = "Comment";

    private User user;
    private String commentContent;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
