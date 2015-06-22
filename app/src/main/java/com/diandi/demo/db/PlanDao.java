package com.diandi.demo.db;

import android.content.Context;
import android.util.Log;

import com.diandi.demo.model.Plan;
import com.diandi.demo.util.Calculator;
import com.diandi.demo.util.DataSortUtil;
import com.diandi.demo.util.TimeUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

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


public class PlanDao {


    private final static String TAG = "DBUtils";
    public static Dao<Plan, Integer> planDao = null;

    public PlanDao(Context context) {
        if (planDao == null) {
            DataHelper dbHelper = new DataHelper(context);
            try {
                planDao = dbHelper.getDao(Plan.class);
            } catch (SQLException e) {
                Log.e(TAG, "得到Dao失败");
                e.printStackTrace();
            }
        }
    }

    public void createPlans(List<Plan> lists) {
        for (Plan plan : lists) {
            try {
                planDao.createOrUpdate(plan);
            } catch (SQLException e) {
                Log.e(TAG, "增加失败");
                e.printStackTrace();
            }
        }
    }

    public void createPlan(Plan plan) {
        try {
            planDao.createOrUpdate(plan);
        } catch (SQLException e) {
            Log.e(TAG, "增加失败");
            e.printStackTrace();
        }
    }

    public void deletePlan(Plan plan) {
        try {
            planDao.delete(plan);
        } catch (SQLException e) {
            Log.e(TAG, "删除失败");
            e.printStackTrace();
        }
    }

    public void deletePlanById(int id) {
        try {
            planDao.deleteById(id);
        } catch (SQLException e) {
            Log.e(TAG, "删除失败");
            e.printStackTrace();
        }
    }

    public List<Plan> getPlans() {
        List<Plan> plans = new ArrayList<Plan>();
        try {
            plans = planDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "查询失败");
            e.printStackTrace();
        }

        return plans;
    }

    public List<Plan> getSortPlans() {
        List<Plan> plans = new ArrayList<Plan>();
        try {
            plans = planDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "查询失败");
            e.printStackTrace();
        }
        sortPlans(plans);

        Log.v(TAG, "" + plans.toString());
        return plans;
    }

    public Plan getPlanById(int id) {
        Plan plan = null;
        try {
            plan = planDao.queryForId(id);
        } catch (SQLException e) {
            Log.e(TAG, "查询失败");
            e.printStackTrace();
        }
        return plan;
    }

    public List<Plan> query(String attributeName, String attributeValue) throws SQLException {
        QueryBuilder<Plan, Integer> queryBuilder = planDao.queryBuilder();
        queryBuilder.where().eq(attributeName, attributeValue);
        PreparedQuery<Plan> preparedQuery = queryBuilder.prepare();
        return query(preparedQuery);
    }

    public List<Plan> query(PreparedQuery<Plan> preparedQuery) throws SQLException {
        return planDao.query(preparedQuery);
    }

    public void updatePlan(Plan plan) {
        try {
            planDao.update(plan);
        } catch (SQLException e) {
            Log.e(TAG, "更新失败");
            e.printStackTrace();
        }

    }

    public void updatePlanById(int id) {
        Plan plan = null;
        try {
            plan = planDao.queryForId(id);
        } catch (SQLException e) {
            Log.e(TAG, "查询失败");
            e.printStackTrace();
        }
    }

    public void sortPlans(List<Plan> plans) {
        for (Plan plan : plans) {

            int m = plan.getProgress();
            if (plan.getTop().equals("true"))
                plan.setType(Plan.URGENT_TOP);
            else if (m == 100) {
                plan.setType(Plan.URGENT_FINISHED);
            } else {
                int k = plan.getProgress();
                int a = Calculator.calculate(TimeUtil.getDatetimeString(plan.getPlanDate()));
                Log.v(TAG, plan.getTitle() + "  " + a);
                if (k > 75)
                    plan.setType(Plan.URGENT_LOW);
                else if (k > 50)
                    plan.setType(Plan.URGENT_MIDDLE);
                else if (k > 25)
                    plan.setType(Plan.URGENT_HIGH);
                else
                    plan.setType(Plan.URGENT_EXTRA);
            }

        }
        DataSortUtil<Plan> dataSortUtil = new DataSortUtil<Plan>();
        dataSortUtil.Sort(plans, "getType", "des");
        createPlans(plans);


    }

}


