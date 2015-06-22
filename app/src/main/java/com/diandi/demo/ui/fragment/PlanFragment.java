package com.diandi.demo.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.diandi.demo.R;
import com.diandi.demo.adapter.PlanAdapter;
import com.diandi.demo.db.PlanDao;
import com.diandi.demo.model.Plan;
import com.diandi.demo.ui.activity.NoteActivity;
import com.diandi.demo.ui.activity.RadialProgressActivity;
import com.diandi.demo.ui.activity.WritePlanActivity;
import com.diandi.demo.widget.dialog.DialogTips;
import com.diandi.demo.widget.dialog.ListDialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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

public class PlanFragment extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private PlanDao mPlanDao;
    private ListView mPlanListView;
    private View mView;
    private PlanAdapter mPlanAdapter;
    private List<Plan> mPlans;
    private String mPlanCategory;

    public String getPlanCategory() {
        return mPlanCategory;
    }

    public void setPlanCategory(String planCategory) {
        mPlanCategory = planCategory;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        mView = layoutInflater.inflate(R.layout.activity_my_list, null);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    public void onRefresh() {
        loadData();
        mPlanAdapter.setList(mPlans);
    }

    @Override
    void findView() {
        mPlanListView = (ListView) mView.findViewById(android.R.id.list);
    }


    @Override
    void initView() {
        mPlanDao = new PlanDao(getActivity());
        loadData();
        mPlanAdapter = new PlanAdapter(getActivity(), mPlans);
        mPlanListView.setAdapter(mPlanAdapter);
        bindEvent();
    }


    private void loadData() {
        try {
            mPlans = mPlanDao.query("category", mPlanCategory);
            mPlanDao.sortPlans(mPlans);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    void bindEvent() {
        mPlanListView.setOnItemClickListener(this);
        mPlanListView.setOnItemLongClickListener(this);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        final Plan plan = mPlanAdapter.getItem(position);
        DialogTips dialog = new DialogTips(getActivity(), plan.getTitle(), "删除计划", "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPlanDao.deletePlan(plan);
                onRefresh();
            }
        });
        dialog.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final Plan plan = mPlanAdapter.getItem(position);
        final ArrayList<String> list = new ArrayList<String>();
        list.add("设置进度");
        list.add("修改计划");
        list.add("标记完成");
        list.add("修改笔记");
        final ListDialog listDialog = new ListDialog(getActivity(), "操作", list);
        listDialog.show();
        listDialog.setOnListItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(getActivity(), RadialProgressActivity.class);
                    intent.putExtra(Plan.PLAN_ID, plan.get_id());
                    startAnimActivity(intent);
                    listDialog.dismiss();
                }
                if (i == 1) {
                    Intent intent = new Intent(getActivity(), WritePlanActivity.class);
                    intent.putExtra(Plan.PLAN_ID, plan.get_id());
                    startAnimActivity(intent);
                    listDialog.dismiss();
                }
                if (i == 2) {
                    plan.setProgress(100);
                    plan.setTop("false");
                    mPlanDao.createPlan(plan);
                    onRefresh();
                    listDialog.dismiss();
                }
                if (i == 3) {
                    Intent intent = new Intent(getActivity(), NoteActivity.class);
                    intent.putExtra(Plan.PLAN_ID, plan.get_id());
                    startAnimActivity(intent);
                    listDialog.dismiss();
                }
            }
        });
    }

}
