package com.diandi.dragmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.diandi.dragmenu.DragLayout.Status;

public class DragMainLayout extends RelativeLayout {
    private final static String TAG = " DragMainLayout";
    private DragLayout dl;

    public DragMainLayout(Context context) {
        super(context);
    }

    public DragMainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragMainLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDragLayout(DragLayout dl) {
        this.dl = dl;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (dl.getStatus() != Status.Close) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dl.getStatus() != Status.Close) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                dl.close();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

}
