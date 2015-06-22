package com.diandi.demo.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.diandi.demo.R;
import com.diandi.demo.sync.UserHelper;
import com.diandi.demo.ui.activity.MainActivity;
import com.diandi.demo.util.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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
public class DiandiFragment extends BaseFragment {


    private final static String TAG = "DiandiFragment";
    private View mContentView;
    private Button mFeedBtn;
    private Button mChannelBtn;
    private ImageView mUserAvatarImg;
    private FeedFragment mFeedFragment;
    private ChannelFragment mChannelFragment;

    public DiandiFragment() {
    }

    public ImageView getUserAvatarImg() {
        return mUserAvatarImg;
    }

    public void setUserAvatarImg(ImageView userAvatarImg) {
        mUserAvatarImg = userAvatarImg;
    }

    @Override
    void initView() {
        bindEvent();
        mFeedBtn.performClick();
    }

    @Override
    void findView() {
        mUserAvatarImg = (ImageView) mContentView.findViewById(R.id.fragment_diandi_user_avatar_img);
        mFeedBtn = (Button) mContentView.findViewById(R.id.fragment_diandi_feed_btn);
        mChannelBtn = (Button) mContentView.findViewById(R.id.fragment_diandi_channel_btn);
    }

    @Override
    void bindEvent() {
        mFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFeedBtn.isEnabled()) {
                    mFeedBtn.setEnabled(false);
                    mChannelBtn.setEnabled(true);
                }
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (mFeedFragment == null) {
                    mFeedFragment = new FeedFragment();
                }
                ft.replace(R.id.fragmet_diandi_container, mFeedFragment, TAG);
                ft.commit();
            }
        });

        mChannelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChannelBtn.isEnabled()) {
                    mChannelBtn.setEnabled(false);
                    mFeedBtn.setEnabled(true);
                }

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (mChannelFragment == null) {
                    mChannelFragment = new ChannelFragment();
                }
                ft.replace(R.id.fragmet_diandi_container, mChannelFragment, TAG);
                ft.commit();
            }
        });
        mUserAvatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).getDragLayout().open();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_diandi_father, null);
        findView();
        initView();
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void onResume() {
        super.onResume();
        ImageLoader.getInstance().displayImage(UserHelper.getCurrentUser().getAvatar(), mUserAvatarImg, ImageLoadOptions.getOptions());

    }

}
