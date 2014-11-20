package com.diandi.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.diandi.R;
import com.diandi.sync.UserHelper;
import com.diandi.ui.activity.TestActivity;
import com.diandi.util.ImageLoadOptions;
import com.diandi.view.dialog.TitlePop;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiandiFragment extends BaseFragment {


    private final static String TAG = "DiandiFragment";
    private static ImageButton mMoreBtn;
    private static TitlePop mTitlePop;
    private View mContentView;
    private Button mFeedBtn;
    private Button mChannelBtn;
    private ImageView mUserAvatarImg;
    private ImageButton mNewDiandiBtn;
    private FeedFragment mFeedFragment;
    private ChannelFragment mChannelFragment;

    public DiandiFragment() {
    }

    public static ImageButton getMoreBtn() {
        return mMoreBtn;
    }

    public static TitlePop getmTitlePop() {
        return mTitlePop;
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
        mMoreBtn = (ImageButton) mContentView.findViewById(R.id.fragment_diandi_more_btn);
        mNewDiandiBtn = (ImageButton) mContentView.findViewById(R.id.fragment_diandi_new_diandi_btn);
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
       /*         mMoreBtn.setVisibility(View.VISIBLE);
                mNewDiandiBtn.setVisibility(View.GONE);*/
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
                if (getActivity() instanceof TestActivity) {
                    ((TestActivity) getActivity()).getDragLayout().open();
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
