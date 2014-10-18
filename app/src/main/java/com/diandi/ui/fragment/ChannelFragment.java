package com.diandi.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.ChannelAdapter;
import com.diandi.bean.OfficialDiandi;
import com.diandi.util.CollectionUtils;
import com.diandi.view.dialog.ActionItem;
import com.diandi.view.dialog.TitlePop;
import com.diandi.view.xlist.XListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.im.task.BRequest;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChannelFragment extends BaseFragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private XListView mListView;
    private ArrayList<OfficialDiandi> mListItems;
    private ChannelAdapter mAdapter;
    private BmobQuery<OfficialDiandi> mQuery;
    private TextView mNetworkTips;
    private int mPageNum;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        initView();
        bindEvent();
    }

    public void setCurrentChannel(String channel) {
        mChannel = channel;
    }

    private String mChannel;

    private final static String CHANNEL_LIST = "channel_list_";

    public ChannelFragment() {
    }


    @Override
    void initView() {
        mPageNum = 0;
        mListItems = new ArrayList<OfficialDiandi>();
        setCurrentChannel("华科");
        if (CustomApplication.getInstance().getCache().getAsObject(CHANNEL_LIST + mChannel) != null) {
            mListItems = (ArrayList<OfficialDiandi>) CustomApplication.getInstance().getCache().getAsObject(CHANNEL_LIST + mChannel);
            mNetworkTips.setVisibility(View.INVISIBLE);
        }
        mQuery = new BmobQuery<OfficialDiandi>();
        mQuery.order("-createdAt");
        mQuery.addWhereEqualTo(OfficialDiandi.CHANNEL, "华科");
        mQuery.setLimit(BRequest.QUERY_LIMIT_COUNT);
        mQuery.include("author");
        initXListView();
    }

    public void changeChannel(String channel) {
        setCurrentChannel(channel);
        mListItems = new ArrayList<OfficialDiandi>();
        mPageNum = 0;
        if (CustomApplication.getInstance().getCache().getAsObject(CHANNEL_LIST) != null) {
            mListItems = (ArrayList<OfficialDiandi>) CustomApplication.getInstance().getCache().getAsObject(CHANNEL_LIST + mChannel);
            mNetworkTips.setVisibility(View.GONE);
        }
        mQuery = new BmobQuery<OfficialDiandi>();
        mQuery.order("-createdAt");
        mQuery.setLimit(BRequest.QUERY_LIMIT_COUNT);
        mQuery.addWhereEqualTo(OfficialDiandi.CHANNEL, channel);
        mQuery.include("author");
        initXListView();
    }

    @Override
    void findView() {
        mListView = (XListView) findViewById(R.id.fragment_diandi_listview);
        mNetworkTips = (TextView) findViewById(R.id.fragment_dianndi_networktips);
    }

    @Override
    void bindEvent() {
        mListView.setOnItemClickListener(this);
        DiandiFragment.getMoreBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         /*       ChannelPopWindow channelPopWindow = new ChannelPopWindow(getActivity());
                channelPopWindow.showPopupWindow(mMoreBtn);*/
                TitlePop titlePop = new TitlePop(getActivity(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                titlePop.addAction(new ActionItem(getActivity(), "华科", R.drawable.hust));
           //     titlePop.addAction(new ActionItem(getActivity(), "网阅", R.drawable.internet));
         //       titlePop.addAction(new ActionItem(getActivity(), "好书", R.drawable.book));
                titlePop.addAction(new ActionItem(getActivity(), "应用", R.drawable.apk));
             //   titlePop.addAction(new ActionItem(getActivity(), "娱乐", R.drawable.happy));

                titlePop.show(view);
                titlePop.setItemOnClickListener(new TitlePop.OnItemOnClickListener() {
                    @Override
                    public void onItemClick(ActionItem item, int position) {
                        {
                            if (item.getTitle() != null) {
                                changeChannel(item.getTitle());
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mListItems != null) {
            CustomApplication.getInstance().getCache().put(CHANNEL_LIST + mChannel, mListItems);
        }
    }

    private void initXListView() {

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        //  mListView.pullRefreshing();

        mAdapter = new ChannelAdapter(getActivity(), mListItems);
        mListView.setAdapter(mAdapter);
        initChannelList(false);
    }

    private void initChannelList(final boolean isUpdate) {
        mQuery.addWhereLessThan("createdAt", new BmobDate(new Date(System.currentTimeMillis())));
        mQuery.setSkip(mPageNum * BRequest.QUERY_LIMIT_COUNT);
        mQuery.findObjects(getActivity(), new FindListener<OfficialDiandi>() {
            @Override
            public void onSuccess(List<OfficialDiandi> list) {
                mNetworkTips.setVisibility(View.INVISIBLE);
                if (CollectionUtils.isNotNull(list)) {
                    if (isUpdate || mPageNum == 0) {
                        mListItems.clear();
                        mAdapter.setList(mListItems);
                    }

                    mListItems.addAll(list);
                    mAdapter.setList(mListItems);
                    if (list.size() < BRequest.QUERY_LIMIT_COUNT) {
                        mListView.setPullLoadEnable(false);
                    } else {
                        mListView.setPullLoadEnable(true);
                    }
                } else {
                    if (mListItems != null) {
                        mListItems.clear();
                    }
                }
                if (isUpdate) {
                    refreshPull();
                }
                mPageNum = 0;

            }

            @Override
            public void onError(int i, String s) {
                // ShowToast(R.string.network_tips);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_channel, container, false);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


    }

    private void refreshLoad() {
        if (mListView.getPullLoading()) {
            mListView.stopLoadMore();
            mNetworkTips.setVisibility(View.INVISIBLE);
        }
    }

    private void refreshPull() {
        if (mListView.getPullRefreshing()) {
            mListView.stopRefresh();
            mNetworkTips.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        mPageNum = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initChannelList(true);
            }
        }, 700);
    }

    @Override
    public void onLoadMore() {
        mQuery.count(getActivity(), OfficialDiandi.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (i > mListItems.size()) {
                    mPageNum++;
                    mQuery.setSkip(BRequest.QUERY_LIMIT_COUNT * mPageNum);
                    mQuery.findObjects(getActivity(), new FindListener<OfficialDiandi>() {
                        @Override
                        public void onSuccess(List<OfficialDiandi> list) {
                            mListItems.addAll(list);
                            mAdapter.setList(mListItems);
                            refreshLoad();
                        }

                        @Override
                        public void onError(int i, String s) {
                            BmobLog.i("查询错误:" + s);
                            mListView.setPullLoadEnable(false);
                            refreshLoad();
                        }
                    });
                } else {
                    ShowToast("暂无更多数据");
                    mListView.setPullLoadEnable(false);
                    refreshLoad();
                }
            }

            @Override
            public void onFailure(int i, String s) {
                refreshLoad();
            }
        });


    }
}
