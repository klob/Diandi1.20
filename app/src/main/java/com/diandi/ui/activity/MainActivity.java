package com.diandi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.diandi.CustomApplication;
import com.diandi.MyMessageReceiver;
import com.diandi.R;
import com.diandi.bean.User;
import com.diandi.ui.fragment.ContactFragment;
import com.diandi.ui.fragment.DiandiFragment;
import com.diandi.ui.fragment.RecentFragment;
import com.diandi.util.factory.OverridePendingUtil;
import com.diandi.view.residemenu.ResideMenu;
import com.diandi.view.residemenu.ResideMenuItem;
import com.umeng.fb.FeedbackAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;

public class MainActivity extends ActivityBase implements View.OnClickListener, EventListener {

    public final static int INFOR_REFREFLASH = 100;
    private static long firstTime;
    private ResideMenu resideMenu;
    private ResideMenuItem itemFav;
    private ResideMenuItem itemAbout;
    private ResideMenuItem itemFeedback;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemHead;
    private ResideMenuItem itemPlanBox;
    private ImageView iv_recent_tips, iv_contact_tips, iv_diandi_tips;//消息提示
    private Button[] mTabs;
    private Button mDiandiBtn, mRecentBtn, mContanctBtn;
    private View currentButton;
    private DiandiFragment diandiFragment;
    private ContactFragment contactFragment;
    private RecentFragment recentFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private View.OnClickListener diandiOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            DiandiFragment newsFatherFragment=null;
            if(newsFatherFragment==null)
                newsFatherFragment= new DiandiFragment();
            ft.replace(R.id.fragment_container, newsFatherFragment, TAG);
            ft.commit();
            setButton(view);
        }
    };
    private View.OnClickListener recentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            RecentFragment newsFatherFragment=null;
            if(newsFatherFragment==null)
                newsFatherFragment= new RecentFragment();
            ft.replace(R.id.fragment_container, newsFatherFragment, TAG);
            ft.commit();
            setButton(view);
        }
    };
    private View.OnClickListener contanctOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ContactFragment newsFatherFragment = new ContactFragment();
            ft.replace(R.id.fragment_container, newsFatherFragment, TAG);
            ft.commit();
            setButton(view);
        }
    };
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
        }

        @Override
        public void closeMenu() {
            //     Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INFOR_REFREFLASH) {
            itemHead.setIcon(CustomApplication.getInstance().getCurrentUser().getAvatar());
            itemHead.setTitle(CustomApplication.getInstance().getCurrentUser().getNick());
        }
    }

    @Override
    void findView() {
        setContentView(R.layout.activity_main);
        mDiandiBtn = (Button) findViewById(R.id.btn_diandi);
        mRecentBtn = (Button) findViewById(R.id.btn_message);
        mContanctBtn = (Button) findViewById(R.id.btn_contract);
        iv_diandi_tips = (ImageView) findViewById(R.id.iv_diandi_tips);
        iv_recent_tips = (ImageView) findViewById(R.id.iv_recent_tips);
        iv_contact_tips = (ImageView) findViewById(R.id.iv_contact_tips);
    }

    @Override
    void initView() {
        initMenu();
        bindEvent();
        mDiandiBtn.performClick();
    }

    @Override
    void bindEvent() {
        mDiandiBtn.setOnClickListener(diandiOnClickListener);
        mRecentBtn.setOnClickListener(recentOnClickListener);
        mContanctBtn.setOnClickListener(contanctOnClickListener);
    }

    private void setButton(View v) {
        if (currentButton != null && currentButton.getId() != v.getId()) {
            currentButton.setEnabled(true);
            currentButton.setSelected(false);
        }
        v.setEnabled(false);
        v.setSelected(true);
        currentButton = v;
    }

    private void initMenu() {

        List<ResideMenuItem> menuItems = new ArrayList<ResideMenuItem>();
        resideMenu = new ResideMenu(this);
        resideMenu.attachToActivity(this);
        resideMenu.setBackground(R.drawable.menu_background);

        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        itemFeedback = new ResideMenuItem(this, R.drawable.icon_feedback_selector, "反馈");
        itemAbout = new ResideMenuItem(this, R.drawable.icon_home_selector, "关于");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_setting_selector, "设置");
        itemPlanBox = new ResideMenuItem(this, ResideMenuItem.TYPE_CONTENT);
        itemPlanBox.setTitle("计划格子");

        User user = CustomApplication.getInstance().getCurrentUser();
        if (user != null) {
            itemHead = new ResideMenuItem(this, user.getAvatar(), user.getNick(), ResideMenuItem.TYPE_HEAD);
            itemHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("from", "me");
                    startActivityForResult(intent, INFOR_REFREFLASH);
                    OverridePendingUtil.in(MainActivity.this);
                }
            });
            menuItems.add(itemHead);
        }


        menuItems.add(itemFeedback);
        menuItems.add(itemAbout);
        menuItems.add(itemSettings);
        resideMenu.setMenuItems(menuItems, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemPlanBox, ResideMenu.DIRECTION_RIGHT);
        itemPlanBox.setOnClickListener(this);
        itemAbout.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemFeedback.setOnClickListener(this);


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemAbout) {
            startAnimActivity(AboutActivity.class);
        } else if (view == itemSettings) {
            startAnimActivity(SettingActivity.class);
        } else if (view == itemFeedback) {
            FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
            agent.startFeedbackActivity();
            OverridePendingUtil.in(MainActivity.this);
        } else if (view == itemPlanBox) {
            startAnimActivity(PlanActivity.class);
        }
    }

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //小圆点提示
        if (BmobDB.create(this).hasUnReadMsg()) {
            iv_recent_tips.setVisibility(View.VISIBLE);
        } else {
            iv_recent_tips.setVisibility(View.GONE);
        }
        if (BmobDB.create(this).hasNewInvite()) {
            iv_contact_tips.setVisibility(View.VISIBLE);
        } else {
            iv_contact_tips.setVisibility(View.GONE);
        }
        iv_diandi_tips.setVisibility(View.GONE);
        MyMessageReceiver.ehList.add(this);// 监听推送的消息
        //清空
        MyMessageReceiver.mNewNum = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyMessageReceiver.ehList.remove(this);
    }

    @Override
    public void onMessage(BmobMsg message) {
        // 声音提示
        boolean isAllow = CustomApplication.getInstance().getSpUtil().isAllowVoice();
        if (isAllow) {
            CustomApplication.getInstance().getMediaPlayer().start();
        }
        iv_recent_tips.setVisibility(View.VISIBLE);
        //保存接收到的消息-并发送已读回执给对方
        BmobChatManager.getInstance(this).saveReceiveMessage(true, message);
        if (currentTabIndex == 0) {
            //当前页面如果为会话页面，刷新此页面
            if (recentFragment != null) {
                recentFragment.refresh();
            }
        }

    }

    @Override
    public void onNetChange(boolean isNetConnected) {
        // TODO Auto-generated method stub
        if (isNetConnected) {
            ShowToast(R.string.network_tips);
        }
    }

    @Override
    public void onAddUser(BmobInvitation message) {
        // 声音提示
        boolean isAllow = CustomApplication.getInstance().getSpUtil().isAllowVoice();
        if (isAllow) {
            CustomApplication.getInstance().getMediaPlayer().start();
        }
        iv_contact_tips.setVisibility(View.VISIBLE);
        if (currentTabIndex == 1) {
            if (contactFragment != null) {
                contactFragment.refresh();
            }
        } else {
            String tickerText = message.getFromname() + "请求添加好友";
            boolean isAllowVibrate = CustomApplication.getInstance().getSpUtil().isAllowVibrate();
            BmobNotifyManager.getInstance(this).showNotify(isAllow, isAllowVibrate, R.drawable.icc_launcher, tickerText, message.getFromname(), tickerText, NewFriendActivity.class);
        }
    }

    @Override
    public void onOffline() {
        showOfflineDialog(this);
    }

    @Override
    public void onReaded(String conversionId, String msgTime) {
        // TODO Auto-generated method stub
    }

    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        if (firstTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            ShowToast("再按一次退出程序");
        }
        firstTime = System.currentTimeMillis();
    }

}
