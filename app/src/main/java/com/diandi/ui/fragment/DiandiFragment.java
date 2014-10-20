package com.diandi.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.bean.Plan;
import com.diandi.bean.User;
import com.diandi.ui.activity.MainActivity;
import com.diandi.ui.activity.NewDiandiActivity;
import com.diandi.ui.activity.NewOfficalDiandiActivity;
import com.diandi.ui.activity.NoteActivity;
import com.diandi.ui.activity.PlanActivity;
import com.diandi.ui.activity.RadialProgressActivity;
import com.diandi.ui.activity.Test;
import com.diandi.ui.activity.WritePlanActivity;
import com.diandi.util.L;
import com.diandi.util.factory.OverridePendingFactory;
import com.diandi.view.dialog.ActionItem;
import com.diandi.view.dialog.ListDialog;
import com.diandi.view.dialog.TitlePop;
import com.diandi.view.residemenu.ResideMenu;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiandiFragment extends BaseFragment {


    private final static String TAG = "DiandiFragment";

    private View mContentView;
    private Button mFeedBtn;
    private Button mChannelBtn;
    private ImageView mUserAvatarImg;

    public static ImageButton getMoreBtn() {
        return mMoreBtn;
    }


    private static ImageButton mMoreBtn;
    private ImageButton mNewDiandiBtn;
    private FeedFragment mFeedFragment;
    private ChannelFragment mChannelFragment;

    public static TitlePop getmTitlePop() {
        return mTitlePop;
    }

    private static TitlePop mTitlePop;


    public DiandiFragment() {
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
                mNewDiandiBtn.setVisibility(View.VISIBLE);
                mMoreBtn.setVisibility(View.GONE);
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
                mMoreBtn.setVisibility(View.VISIBLE);
                mNewDiandiBtn.setVisibility(View.GONE);
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
            public void onClick(View view) {
                ((MainActivity) getActivity()).getResideMenu().openMenu(ResideMenu.DIRECTION_LEFT);
                ((MainActivity) getActivity()).getResideMenu().setMenuListener(new ResideMenu.OnMenuListener() {
                    @Override
                    public void openMenu() {
                        //      mUserIconImg.setVisibility(View.GONE);
                    }

                    @Override
                    public void closeMenu() {
                        //     mUserIconImg.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        mNewDiandiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = mApplication.getCurrentUser();
            /*    if (user.isOfficial()) {
                    startAnimActivity(NewOfficalDiandiActivity.class);
                    L.e(TAG, user.toString());
                } else {
                    startAnimActivity(NewDiandiActivity.class);
                    L.e(TAG, user.toString() + "    ");
                }*/
                final ArrayList<String> list = new ArrayList<String>();
                list.add("记下点滴");
                list.add("发布公众");
                list.add("发布匿名");
                list.add("打开格子");
                final ListDialog listDialog = new ListDialog(getActivity(), "操作", list);
                listDialog.show();
                listDialog.setOnListItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            startAnimActivity(Test.class);
                            listDialog.dismiss();
                        }
                        if (i == 1) {
                            startAnimActivity(NewOfficalDiandiActivity.class);
                            listDialog.dismiss();
                        }
                        if (i == 2) {
                            startAnimActivity(NewDiandiActivity.class);
                            listDialog.dismiss();
                        }
                        if (i == 3) {
                            startAnimActivity(PlanActivity.class);
                            listDialog.dismiss();
                        }

                    }
                });
                OverridePendingFactory.in(getActivity());
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
        ImageLoader.getInstance().displayImage(CustomApplication.getInstance().getCurrentUser().getAvatar(), mUserAvatarImg, CustomApplication.getInstance().getOptions());

    }

}
