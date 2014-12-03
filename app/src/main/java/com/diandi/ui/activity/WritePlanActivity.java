package com.diandi.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diandi.R;
import com.diandi.bussiness.db.PlanDao;
import com.diandi.model.Plan;
import com.diandi.util.FormatUtil;
import com.diandi.widget.HeaderLayout;
import com.diandi.widget.googledatetimepicker.date.DatePickerDialog;
import com.diandi.widget.googledatetimepicker.time.RadialPickerLayout;
import com.diandi.widget.googledatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

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
public class WritePlanActivity extends BaseActivity {

    private final String TAG = "write";
    private final Calendar mCalendar = Calendar.getInstance();
    private Plan mPlan;
    private EditText mTitleEdit;
    private TextView mDateText;
    private RelativeLayout mDateLayout;
    private TextView mTimeText;
    private RelativeLayout mTimeLayout;
    private TextView mCategoryText;
    private RelativeLayout mCategoryLayout;
    private CheckBox mTopCheck;
    private EditText mNoteEdit;
    private int mTimeYear;
    private int mTimeMonth;
    private int mTimeDay;
    private int mTimeHour;
    private int mTimeMinute;
    private PlanDao mPlanDao;

    private int mPlanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();

    }

    void findView() {
        setContentView(R.layout.activity_write_plan);
        mTitleEdit = (EditText) findViewById(R.id.activity_write_plan_title_edit);
        mDateText = (TextView) findViewById(R.id.activity_write_plan_date_text);
        mDateLayout = (RelativeLayout) findViewById(R.id.activity_write_plan_date_rv);
        mTimeText = (TextView) findViewById(R.id.activity_write_plan_time_text);
        mTimeLayout = (RelativeLayout) findViewById(R.id.activity_write_plan_time_rv);
        mCategoryText = (TextView) findViewById(R.id.activity_write_plan_category_text);
        mCategoryLayout = (RelativeLayout) findViewById(R.id.activity_write_plan_category_rv);
        mTopCheck = (CheckBox) findViewById(R.id.activity_write_plan_top_chk);
        mNoteEdit = (EditText) findViewById(R.id.activity_write_plan_note_edit);

    }

    @Override
    void initView() {
        mPlanDao = new PlanDao(this);
        initTopBarForBoth("写计划", R.drawable.base_action_bar_true_bg_selector, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                if (mTitleEdit.getText().toString().equals("")) {
                    ShowToast("标题不能为空");
                    return;
                }
                mCalendar.set(mTimeYear, mTimeMonth, mTimeDay, mTimeHour,
                        mTimeMinute, 0);
                Date date = new Date(mCalendar.getTimeInMillis());
                mPlan.setTitle(mTitleEdit.getText().toString());
                mPlan.setPlanDate(date);
                mPlan.setCategory(mCategoryText.getText().toString());
                mPlan.setTop(String.valueOf(mTopCheck.isChecked()));
                mPlan.setNote(mNoteEdit.getText().toString());
                mPlanDao.createPlan(mPlan);
                Log.e(TAG, mPlan.toString());
                finish();
            }
        });
        mPlanId = getIntent().getIntExtra(Plan.PLAN_ID, -1);
        if (mPlanId == -1) {
            mPlan = new Plan();
        } else {
            mPlan = mPlanDao.getPlanById(mPlanId);
            mCalendar.setTime(mPlan.getPlanDate());
            mTitleEdit.setText(mPlan.getTitle());
            mCategoryText.setText(mPlan.getCategory());
            mTopCheck.setChecked(Boolean.valueOf(mPlan.getTop()));
            mNoteEdit.setText(mPlan.getNote());
        }


        mTimeYear = mCalendar.get(Calendar.YEAR);
        mTimeMonth = mCalendar.get(Calendar.MONTH);
        mTimeDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mTimeHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mTimeMinute = mCalendar.get(Calendar.MINUTE);

        mDateText.setText(getDateString());
        mTimeText.setText(getTimeString());
        bindEvent();

    }


    void bindEvent() {
        mDateLayout.setOnClickListener(new DateChangeListener());
        mTimeLayout.setOnClickListener(new TimeChangeListener());
        mCategoryLayout.setOnClickListener(new CategoryChangeListenr());
    }

    private void notifyDateTextView() {
        mCalendar.set(mTimeYear, mTimeMonth, mTimeDay);
        String date = getDateString();
        mDateText.setText(date);
    }

    private void notifyTimeTextView() {
        String time = getTimeString();
        mTimeText.setText(time);
    }

    private String getDateString() {
        String dateString = FormatUtil.pad(mTimeYear) + "-" + FormatUtil.pad(mTimeMonth + 1) + "-" + FormatUtil.pad(mTimeDay)
                + "  星期" + FormatUtil.week2String(mCalendar.get(Calendar.DAY_OF_WEEK));
        return dateString;
    }

    private String getTimeString() {
        String timeString = FormatUtil.padHour(mTimeHour) + ":" + FormatUtil.pad(mTimeMinute) + " "
                + FormatUtil.padAP(mTimeHour);
        return timeString;
    }

    private class DateChangeListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            DatePickerDialog datePickerDialog = new DatePickerDialog().newInstance(dateSetListener, mTimeYear, mTimeMonth,
                    mTimeDay);
            String tag = "";
            datePickerDialog.show(getSupportFragmentManager(), tag);
        }

        private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePickerDialog dialog, int year,
                                  int monthOfYear, int dayOfMonth) {
                mTimeYear = year;
                mTimeMonth = monthOfYear;
                mTimeDay = dayOfMonth;
                mDateText.setTextColor(getResources().getColor(
                        android.R.color.holo_blue_light));
                notifyDateTextView();
            }
        };


    }

    private class TimeChangeListener implements View.OnClickListener {


        private String tag = "";

        @Override
        public void onClick(View view) {
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                    timeSetListener, mTimeHour, mTimeMinute, false);
            timePickerDialog.show(getSupportFragmentManager(), tag);
        }

        private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay,
                                  int minute) {
                mTimeHour = hourOfDay;
                mTimeMinute = minute;
                mTimeText.setTextColor(getResources().getColor(
                        android.R.color.holo_blue_light));
                notifyTimeTextView();
            }
        };
    }

    private class CategoryChangeListenr implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final View view1 = LayoutInflater.from(WritePlanActivity.this).inflate(R.layout.category_dialog, null);
            final RadioGroup radioGroup = (RadioGroup) view1.findViewById(R.id.category_rdogp);
            final AlertDialog dialog = new AlertDialog.Builder(WritePlanActivity.this).setTitle("分类选择").setView(view1).create();
            dialog.show();
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int radioButtonId = radioGroup.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) view1.findViewById(radioButtonId);
                    String category = rb.getText().toString();
                    mCategoryText.setText(category);
                    dialog.dismiss();
                }
            });
        }
    }
}
