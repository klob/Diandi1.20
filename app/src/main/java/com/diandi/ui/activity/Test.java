package com.diandi.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.PersonalAdapter;
import com.diandi.model.User;
import com.diandi.model.diandi.DianDi;
import com.diandi.util.ImageLoadOptions;
import com.diandi.util.L;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

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
public class Test extends Activity {

    private final static String PERSON_LIST = "person_list";


    private ArrayList<DianDi> mDianDis;
    private User mUser;
    private PersonalAdapter mAdapter;
    private ImageView item_info_head_img;
    private TextView item_info_nick_text;
    private TextView item_info_signature_text;
    private TextView tv_says_count;
    private TextView tv_follow_count;
    private ListView personal_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findView();
        initView();
    }


    private void findView() {
        item_info_head_img = (ImageView) findViewById(R.id.item_info_head_img);
        item_info_nick_text = (TextView) findViewById(R.id.item_info_nick_text);
        item_info_signature_text = (TextView) findViewById(R.id.item_info_signature_text);
        tv_says_count = (TextView) findViewById(R.id.tv_says_count);
        tv_follow_count = (TextView) findViewById(R.id.tv_follow_count);
        personal_list = (ListView) findViewById(R.id.personal_list);
    }

    private void initView() {
        mDianDis = new ArrayList<DianDi>();
        mUser = CustomApplication.getInstance().getCurrentDianDi().getAuthor();
        if (CustomApplication.getInstance().getCache().getAsObject(PERSON_LIST + mUser.getObjectId()) != null) {
            mDianDis = (ArrayList<DianDi>) CustomApplication.getInstance().getCache().getAsObject(PERSON_LIST + mUser.getObjectId());
        }
        mAdapter = new PersonalAdapter(this, mDianDis);
        updatePersonalInfo(mUser);
        personal_list.setAdapter(mAdapter);
    }

    private void updatePersonalInfo(User user) {
        item_info_nick_text.setText(user.getNick());
        item_info_signature_text.setText(user.getSignature());
        tv_says_count.setText(mAdapter.getCount() + "");
        ImageLoader.getInstance().displayImage(user.getAvatar(), item_info_head_img,
                ImageLoadOptions.getOptions(),
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        // TODO Auto-generated method stub
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    }

                }
        );
    }

}
