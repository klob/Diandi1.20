package com.diandi.demo.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.diandi.demo.R;
import com.diandi.demo.util.ImageLoadOptions;
import com.diandi.view.ActionSheet;
import com.diandi.demo.widget.CustomViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

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
public class ImageBrowserActivity extends FragmentActivity implements OnPageChangeListener, ActionSheet.ActionSheetListener {

    private LinearLayout layout_image;
    private CustomViewPager mSvpPager;
    private ImageBrowserAdapter mAdapter;
    private Button mSaveBtn;
    private int mPosition;

    private ArrayList<String> mPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpicture);
        findView();
        initView();
    }

    void findView() {
        mSvpPager = (CustomViewPager) findViewById(R.id.pagerview);
        mSaveBtn = (Button) findViewById(R.id.activity_imagebrower_more);
    }

    void initView() {
        mPhotos = getIntent().getStringArrayListExtra("photos");
        mPosition = getIntent().getIntExtra("position", 0);

        mAdapter = new ImageBrowserAdapter(this);
        mSvpPager.setAdapter(mAdapter);
        mSvpPager.setCurrentItem(mPosition, false);
        mSvpPager.setOnPageChangeListener(this);
    }

    void bindEvent() {
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionSheet();
            }
        });

    }

    public void showActionSheet() {
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("保存")
                .setCancelableOnTouchOutside(true)
                .setListener(this).show();
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        switch (index) {
            case 0:
                Toast.makeText(getApplicationContext(), "click item index = " + index, Toast.LENGTH_SHORT).show();
                break;
        }

    }


    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
        Toast.makeText(getApplicationContext(), "dismissed isCancle = " + isCancle, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        mPosition = arg0;
    }

    private class ImageBrowserAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public ImageBrowserAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mPhotos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            View imageLayout = inflater.inflate(R.layout.item_show_picture,
                    container, false);
            final PhotoView photoView = (PhotoView) imageLayout
                    .findViewById(R.id.photoview);
            final ProgressBar progress = (ProgressBar) imageLayout.findViewById(R.id.progress);

            final String imgUrl = mPhotos.get(position);
            ImageLoader.getInstance().displayImage(imgUrl, photoView, ImageLoadOptions.getOption(), new SimpleImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view,
                                            FailReason failReason) {
                    progress.setVisibility(View.GONE);

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progress.setVisibility(View.GONE);

                }
            });

            container.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


}
