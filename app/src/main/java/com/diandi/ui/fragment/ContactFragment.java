package com.diandi.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.ContactAdapter;
import com.diandi.model.User;
import com.diandi.ui.activity.AddContactActivity;
import com.diandi.ui.activity.NearPeopleActivity;
import com.diandi.ui.activity.NewFriendActivity;
import com.diandi.ui.activity.ProfileActivity;
import com.diandi.util.CharacterParser;
import com.diandi.util.CollectionUtils;
import com.diandi.util.PinyinComparator;
import com.diandi.widget.ClearEditText;
import com.diandi.widget.HeaderLayout.onRightImageButtonClickListener;
import com.diandi.widget.LetterView;
import com.diandi.widget.LetterView.OnTouchingLetterChangedListener;
import com.diandi.widget.dialog.DialogTips;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;

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
@SuppressLint("DefaultLocale")
public class ContactFragment extends BaseFragment implements OnItemClickListener, OnItemLongClickListener {

    LinearLayout mNewFriendLayout;
    LinearLayout mNearPeopleLayout;
    private ClearEditText mClearEditText;
    private TextView mTextDialog;
    private ListView mFriendListView;
    private LetterView mRightLetters;
    private ImageView mMsgTipImg;
    private TextView mNewNameText;
    private ContactAdapter mUserAdapter;

    private List<User> mFriends = new ArrayList<User>();


    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator mPinyinComparator;
    private boolean mHidden;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        initView();
    }

    @Override
    void findView() {
        mClearEditText = (ClearEditText) findViewById(R.id.et_msg_search);
        mFriendListView = (ListView) findViewById(R.id.list_friends);
        RelativeLayout headView = (RelativeLayout) mInflater.inflate(R.layout.include_new_friend, null);
        mMsgTipImg = (ImageView) headView.findViewById(R.id.iv_msg_tips);
        mNewFriendLayout = (LinearLayout) headView.findViewById(R.id.layout_new);
        mNearPeopleLayout = (LinearLayout) headView.findViewById(R.id.layout_near);
        mFriendListView.addHeaderView(headView);
        mRightLetters = (LetterView) findViewById(R.id.right_letter);
        mTextDialog = (TextView) findViewById(R.id.text_dialog);
        mRightLetters.setTextView(mTextDialog);
    }

    public void initView() {
        bindEvent();
        initTopBarForRight("联系人", R.drawable.base_action_bar_add_bg_selector,
                new onRightImageButtonClickListener() {
                    @Override
                    public void onClick() {
                        startAnimActivity(AddContactActivity.class);
                    }
                }
        );
        mCharacterParser = CharacterParser.getInstance();
        mPinyinComparator = new PinyinComparator();
        mUserAdapter = new ContactAdapter(getActivity(), mFriends);
        mFriendListView.setAdapter(mUserAdapter);
    }


    @Override
    void bindEvent() {
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mNewFriendLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), NewFriendActivity.class);
                intent.putExtra("from", "contact");
                startAnimActivity(intent);
            }
        });
        mNearPeopleLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), NearPeopleActivity.class);
                startAnimActivity(intent);
            }
        });

        mFriendListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getActivity().getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });
        mFriendListView.setOnItemClickListener(this);
        mFriendListView.setOnItemLongClickListener(this);
        mRightLetters.setOnTouchingLetterChangedListener(new LetterListViewListener());
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<User> filterDateList = new ArrayList<User>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mFriends;
        } else {
            filterDateList.clear();
            for (User sortModel : mFriends) {
                String name = sortModel.getUsername();
                if (name != null) {
                    if (name.indexOf(filterStr.toString()) != -1
                            || mCharacterParser.getSelling(name).startsWith(
                            filterStr.toString())) {
                        filterDateList.add(sortModel);
                    }
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, mPinyinComparator);
        mUserAdapter.updateListView(filterDateList);
    }

    private void filledData(List<BmobChatUser> datas) {
        mFriends.clear();
        int total = datas.size();
        for (int i = 0; i < total; i++) {
            BmobChatUser user = datas.get(i);
            User sortModel = new User();
            sortModel.setAvatar(user.getAvatar());
            sortModel.setNick(user.getNick());
            sortModel.setUsername(user.getUsername());
            sortModel.setObjectId(user.getObjectId());
            sortModel.setContacts(user.getContacts());
            // 汉字转换成拼音
            String username = sortModel.getUsername();
            // 若没有username
            if (username != null) {
                String pinyin = mCharacterParser.getSelling(sortModel.getUsername());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
            } else {
                sortModel.setSortLetters("#");
            }
            mFriends.add(sortModel);
        }
        // 根据a-z进行排序
        Collections.sort(mFriends, mPinyinComparator);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            queryMymFriends();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void queryMymFriends() {
        //是否有新的好友请求
        if (BmobDB.create(getActivity()).hasNewInvite()) {
            mMsgTipImg.setVisibility(View.VISIBLE);
        } else {
            mMsgTipImg.setVisibility(View.GONE);
        }
        Map<String, BmobChatUser> users = CustomApplication.getInstance().getContactList();
        //组装新的User
        filledData(CollectionUtils.map2list(users));
        if (mUserAdapter == null) {
            mUserAdapter = new ContactAdapter(getActivity(), mFriends);
            mFriendListView.setAdapter(mUserAdapter);
        } else {
            mUserAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.mHidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mHidden) {
            refresh();
        }
    }

    public void refresh() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    queryMymFriends();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        // TODO Auto-generated method stub
        User user = (User) mUserAdapter.getItem(position - 1);
        //先进入好友的详细资料页面
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("from", "other");
        intent.putExtra("username", user.getUsername());
        startAnimActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
                                   long arg3) {
        // TODO Auto-generated method stub
        User user = (User) mUserAdapter.getItem(position - 1);
        showDeleteDialog(user);
        return true;
    }

    public void showDeleteDialog(final User user) {
        DialogTips dialog = new DialogTips(getActivity(), user.getUsername(), "删除联系人", "确定", true, true);
        // 设置成功事件
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                deleteContact(user);
            }
        });
        // 显示确认对话框
        dialog.show();
        dialog = null;
    }

    /**
     * 删除联系人
     * deleteContact
     *
     * @return void
     * @throws
     */
    private void deleteContact(final User user) {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("正在删除...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        userManager.deleteContact(user.getObjectId(), new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                ShowToast("删除成功");
                //删除内存
                CustomApplication.getInstance().getContactList().remove(user.getUsername());
                //更新界面
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        progress.dismiss();
                        mUserAdapter.remove(user);
                    }
                });
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                ShowToast("删除失败：" + arg1);
                progress.dismiss();
            }
        });
    }

    private class LetterListViewListener implements
            OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(String s) {
            // 该字母首次出现的位置
            int position = mUserAdapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                mFriendListView.setSelection(position);
            }
        }
    }

}
