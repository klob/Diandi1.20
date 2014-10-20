package com.diandi.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.diandi.R;
import com.ecloud.pulltozoomview.PullToZoomScrollView;

public class Test extends Activity {
    PullToZoomScrollView mPullToZoomScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mPullToZoomScrollView=(PullToZoomScrollView)findViewById(R.id.scroll_view);
    }



}
