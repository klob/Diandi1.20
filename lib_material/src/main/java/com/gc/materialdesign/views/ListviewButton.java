package com.gc.materialdesign.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;

import com.gc.materialdesign.listener.ScrollDirectionDetector;
import com.gc.materialdesign.listener.ScrollDirectionListener;

/**
 * *******************************************************************************
 * *********    Author : klob(kloblic@gmail.com) .
 * *********    Date : 2014-11-28  .
 * *********    Time : 18:39 .
 * *********    Project name : Diandi1.18 .
 * *********    Version : 1.0
 * *********    Copyright @ 2014, klob, All Rights Reserved
 * *******************************************************************************
 */
public class ListviewButton extends ButtonFloat {

    private static final int TRANSLATE_DURATION_MILLIS = 200;
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    protected AbsListView mListView;
    private boolean mVisible;
    private FabOnScrollListener mOnScrollListener;

    public ListviewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        mVisible = true;
    }


    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof MarginLayoutParams) {
            marginBottom = ((MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    protected AbsListView.OnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        toggle(true, animate, false);
    }

    public void hide(boolean animate) {
        toggle(false, animate, false);
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height + getMarginBottom();
            if (animate) {
                animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                setTranslationY(translationY);
            }
        }
    }

    public void attachToListView(AbsListView listView) {
        attachToListView(listView, new FabOnScrollListener());
    }

    public void attachToListView(AbsListView listView, FabOnScrollListener onScrollListener) {
        mListView = listView;
        mOnScrollListener = onScrollListener;
        onScrollListener.setFloatingActionButton(this);
        onScrollListener.setListView(listView);
        mListView.setOnScrollListener(onScrollListener);
    }

    public static class FabOnScrollListener extends ScrollDirectionDetector {
        private ListviewButton mListviewButton;

        public FabOnScrollListener() {
            setScrollDirectionListener(new ScrollDirectionListener() {
                @Override
                public void onScrollDown() {
                    mListviewButton.show();
                }

                @Override
                public void onScrollUp() {
                    mListviewButton.hide();
                }
            });
        }

        public void setFloatingActionButton(ListviewButton listviewButton) {
            mListviewButton = listviewButton;
        }
    }

}
