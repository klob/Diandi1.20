package com.diandi.ui.fragment;
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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.FeedAdapter;
import com.diandi.bussiness.db.DatabaseUtilC;
import com.diandi.model.diandi.DianDi;
import com.diandi.sync.UserHelper;
import com.diandi.ui.activity.CommentActivity;
import com.diandi.ui.activity.NewDiandiActivity;
import com.diandi.ui.activity.PlanActivity;
import com.diandi.ui.activity.Test;
import com.diandi.ui.activity.WritePlanActivity;
import com.diandi.util.CollectionUtils;
import com.diandi.util.OverridePendingUtil;
import com.diandi.view.ListviewButton;
import com.diandi.widget.dialog.ListDialog;
import com.diandi.widget.xlist.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.bmob.im.task.BRequest;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;


public class FeedFragment extends BaseFragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {
    private final static String FEED_LIST = "feed_list_";
    private BmobQuery<DianDi> query;
    private ArrayList<DianDi> mListItems;
    private FeedAdapter mAdapter;
    private XListView mListView;
    private TextView networkTips;
    private int mPageNum;
    private ListviewButton mListviewButton;

    public void setListView(XListView listView) {
        mListView = listView;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_channel, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        initView();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    }

    @Override
    public void findView() {
        mListView = (XListView) findViewById(R.id.fragment_diandi_listview);
        networkTips = (TextView) findViewById(R.id.fragment_dianndi_networktips);
        mListviewButton = (ListviewButton) findViewById(R.id.buttonFloat);
        mListviewButton.attachToListView(mListView);
        mListviewButton.setDrawableIcon(getResources().getDrawable(R.drawable.ic_action_new));

    }

    @Override
    public void initView() {
        mListItems = new ArrayList<DianDi>();
        mPageNum = 0;
        if (CustomApplication.getInstance().getCache().getAsObject(FEED_LIST) != null) {
            mListItems = (ArrayList<DianDi>) CustomApplication.getInstance().getCache().getAsObject(FEED_LIST);
            networkTips.setVisibility(View.GONE);
        }
        query = new BmobQuery<DianDi>();
        query.order("-createdAt");
        query.setLimit(BRequest.QUERY_LIMIT_COUNT);

        query.include("author");
        initXListView();
        bindEvent();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mListItems != null) {
            CustomApplication.getInstance().getCache().put(FEED_LIST, mListItems);

        }

    }

    private void initXListView() {

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        // mListView.pullRefreshing();

        mAdapter = new FeedAdapter(getActivity(), mListItems);
        mListView.setAdapter(mAdapter);
        initDiandyList(false);
    }

    @Override
    void bindEvent() {
        mListView.setOnItemClickListener(this);
        mListviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> list = new ArrayList<String>();
                list.add("记下点滴");
                list.add("打开格子");
                list.add("新建计划");
                //   list.add("发布匿名");
                final ListDialog listDialog = new ListDialog(getActivity(), "操作", list);
                listDialog.show();
                listDialog.setOnListItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            startAnimActivity(NewDiandiActivity.class);
                        }
                        if (i == 1) {
                            startAnimActivity(PlanActivity.class);
                        }
                        if (i == 2) {
                            startAnimActivity(WritePlanActivity.class);
                        }

                        if (i == 3) {
                            startAnimActivity(Test.class);

                        }
                        listDialog.dismiss();

                    }
                });
                OverridePendingUtil.in(getActivity());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CommentActivity.class);
        intent.putExtra("data", mListItems.get(position - 1));
        startAnimActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPageNum = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initDiandyList(true);
            }
        }, 700);
    }

    @Override
    public void onLoadMore() {
        query.count(getActivity(), DianDi.class, new CountListener() {
                    @Override
                    public void onSuccess(int i) {
                        if (i > mListItems.size()) {
                            mPageNum++;
                            query.setSkip(BRequest.QUERY_LIMIT_COUNT * (mPageNum));
                            query.findObjects(getActivity(), new FindListener<DianDi>() {
                                @Override
                                public void onSuccess(List<DianDi> list) {
                                    if (UserHelper.getCurrentUser() != null) {
                                        list = DatabaseUtilC.getInstance(getActivity()).setFav(list);
                                    }
                                    mListItems.addAll(list);
                                    mAdapter.setList(mListItems);
                                    refreshLoad();
                                }

                                @Override
                                public void onError(int arg0, String arg1) {
                                    BmobLog.i("查询错误:" + arg1);
                                    mListView.setPullLoadEnable(false);
                                    refreshLoad();
                                }
                            });
                        } else {
                            ShowToast("数据加载完成");
                            mListView.setPullLoadEnable(false);
                            refreshLoad();
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        refreshLoad();
                    }
                }
        );
    }

    private void initDiandyList(final boolean isUpdate) {
        query.addWhereLessThan("createdAt", new BmobDate(new Date(System.currentTimeMillis())));
        query.setSkip(BRequest.QUERY_LIMIT_COUNT * mPageNum);
        query.findObjects(getActivity(), new FindListener<DianDi>() {
            @Override
            public void onSuccess(List<DianDi> list) {
                networkTips.setVisibility(View.INVISIBLE);
                if (CollectionUtils.isNotNull(list)) {
                    if (isUpdate || mPageNum == 0) {
                        mListItems.clear();
                        mAdapter.setList(mListItems);
                    }
                    if (UserHelper.getCurrentUser() != null) {
                        list = DatabaseUtilC.getInstance(getActivity()).setFav(list);
                    }
                    mListItems.addAll(list);
                    mAdapter.setList(mListItems);
                    if (list.size() < BRequest.QUERY_LIMIT_COUNT) {
                        mListView.setPullLoadEnable(false);
                    } else {
                        mListView.setPullLoadEnable(true);
                    }
                } else {
                    BmobLog.i("查询成功:无返回值");
                    if (mListItems != null) {
                        mListItems.clear();
                    }
                }
                if (isUpdate) {
                    refreshPull();
                    //   progressDialog.dismiss();
                }
                //这样能保证每次查询都是从头开始
                mPageNum = 0;
            }

            @Override
            public void onError(int arg0, String arg1) {
                BmobLog.i("查询错误:" + arg1);
                mListView.setPullLoadEnable(false);
                refreshPull();
                //这样能保证每次查询都是从头开始
                mPageNum = 0;
                ShowToast(R.string.network_tips);
            }

        });

    }


    private void refreshLoad() {
        if (mListView.getPullLoading()) {
            mListView.stopLoadMore();
            networkTips.setVisibility(View.INVISIBLE);
        }
    }

    private void refreshPull() {
        if (mListView.getPullRefreshing()) {
            mListView.stopRefresh();
            networkTips.setVisibility(View.INVISIBLE);
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = formatter.format(new Date(System.currentTimeMillis()));
        return times;
    }
}

