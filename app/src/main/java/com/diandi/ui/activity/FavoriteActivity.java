package com.diandi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.FeedAdapter;
import com.diandi.adapter.base.BaseListAdapter;
import com.diandi.bean.DianDi;
import com.diandi.bean.User;
import com.diandi.config.Constant;
import com.diandi.db.DatabaseUtil;
import com.diandi.util.LogUtils;
import com.diandi.view.xlist.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

public class FavoriteActivity extends BaseActivity implements XListView.IXListViewListener, AdapterView.OnItemClickListener {
    private final String FAVORITE = "favorite_";
    private TextView networkTips;
    private XListView mListView;
    private ArrayList<DianDi> mListItems;
    private BaseListAdapter<DianDi> mAdapter;
    private int pageNum;
    private ProgressBar progressbar;
    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
        loadData();
    }

    public void onResume() {
        super.onResume();
        onRefresh();
    }

    public void onDestroy() {
        super.onDestroy();
        if (mListItems != null) {
            CustomApplication.getInstance().getCache().put(FAVORITE + mUser.getObjectId(), mListItems);
        }
    }

    @Override
    void findView() {
        setContentView(R.layout.activity_favorite);
        mListView = (XListView) findViewById(R.id.fragment_diandi_listview);
        networkTips = (TextView) findViewById(R.id.activity_favorite_networkTips);
        progressbar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    void initView() {
        pageNum = 0;
        mUser = BmobUser.getCurrentUser(mContext, User.class);
        initTopBarForLeft("收藏");
        mListItems = new ArrayList<DianDi>();
        if (CustomApplication.getInstance().getCache().getAsObject(FAVORITE + mUser.getObjectId()) != null) {
            mListItems = (ArrayList<DianDi>) CustomApplication.getInstance().getCache().getAsObject(FAVORITE + mUser.getObjectId());
        }
        initXListView();
        bindEvent();
    }

    private void initXListView() {
        mListView.setOnItemClickListener(this);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        mListView.pullRefreshing();
        mAdapter = new FeedAdapter(this, mListItems);
        mListView.setAdapter(mAdapter);
    }


    @Override
    void bindEvent() {

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent();
        intent.setClass(this, CommentActivity.class);
        intent.putExtra("data", mListItems.get(position - 1));
        startAnimActivity(intent);
    }

    public void loadData() {
        //  progressbar.setVisibility(View.VISIBLE);

        BmobQuery<DianDi> query = new BmobQuery<DianDi>();
        query.addWhereRelatedTo("favorite", new BmobPointer(mUser));
        query.order("-createdAt");
        query.setLimit(Constant.NUMBERS_PER_PAGE);
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("createdAt", date);
        query.setSkip(Constant.NUMBERS_PER_PAGE * (pageNum++));
        query.include("author");
        query.findObjects(FavoriteActivity.this, new FindListener<DianDi>() {

            @Override
            public void onSuccess(List<DianDi> list) {
                if (list.size() != 0 && list.get(list.size() - 1) != null) {
                    {
                        mListItems.clear();
                    }
                    if (list.size() < Constant.NUMBERS_PER_PAGE) {
                        ShowToast("已加载完所有数据~");
                    }
                    if (CustomApplication.getInstance().getCurrentUser() != null) {
                        list = DatabaseUtil.getInstance(mContext).setFav(list);
                    }
                    mListItems.addAll(list);
                    mAdapter.notifyDataSetChanged();

                } else {
                    ShowToast("暂无更多数据~");
                    if (list.size() == 0 && mListItems.size() == 0) {

                        networkTips.setText("暂无收藏。快去首页收藏几个把~");
                        pageNum--;

                        Log.i(TAG, "SIZE:" + list.size() + "ssssize" + mListItems.size());
                        return;
                    }
                    pageNum--;
                }
                refreshPull();
            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub
                LogUtils.i(TAG, "find failed." + arg1);
                pageNum--;
                refreshPull();
            }
        });

    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    private void refreshPull() {
        if (mListView.getPullRefreshing()) {
            mListView.stopRefresh();
            networkTips.setVisibility(View.INVISIBLE);
        }
        //progressbar.setVisibility(View.GONE);
        networkTips.setVisibility(View.GONE);
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date(System.currentTimeMillis()));
    }
}
