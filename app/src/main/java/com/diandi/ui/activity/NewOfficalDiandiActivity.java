package com.diandi.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.diandi.R;
import com.diandi.bean.OfficialDiandi;
import com.diandi.bean.User;
import com.diandi.util.factory.OverridePendingFactory;
import com.diandi.view.HeaderLayout;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

public class NewOfficalDiandiActivity extends ActivityBase {


    private EditText mContentEdit;
    private EditText mLinkEdit;
    private EditText mChannelEdit;

    private OfficialDiandi mDianDi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();

    }

    @Override
    void findView() {
        setContentView(R.layout.activity_new_offical_diandi);
        mContentEdit = (EditText) findViewById(R.id.activity_official_content);
        mLinkEdit = (EditText) findViewById(R.id.activity_official_link);
        mChannelEdit=(EditText)findViewById(R.id.activity_official_channel);

    }

    @Override
    void initView() {
        bindEvent();
        initTopBarForBoth("发布点滴", R.drawable.base_action_bar_true_bg_selector, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {

                String contentStr = mContentEdit.getText().toString().trim();
                String linkStr = mLinkEdit.getText().toString().trim();
                String channel =mChannelEdit.getText().toString().trim();
                if (TextUtils.isEmpty(contentStr)||TextUtils.isEmpty(channel)) {
                    ShowToast("内容不能为空");
                    return;
                } else {
                    publishWithoutFigure(contentStr, linkStr, channel, null);

                }
                finish();
                OverridePendingFactory.out(NewOfficalDiandiActivity.this);

            }
        });
    }

    @Override
    void bindEvent() {
    }

    private void publish(String contentStr, String linkStr) {
        mDianDi = new OfficialDiandi();
        mDianDi.setContent(contentStr);
        mDianDi.setLink(linkStr);
        mDianDi.setLove(0);
        mDianDi.setHate(0);
        mDianDi.setShare(0);
        mDianDi.setComment(0);


        mDianDi.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                ShowToast("发表成功");
            }

            @Override
            public void onFailure(int i, String s) {
                ShowToast(R.string.network_tips);
            }
        });
    }

    private void publishWithoutFigure(final String commitContent, final String commitLink, String channel, final BmobFile fingureFile) {
        User user = BmobUser.getCurrentUser(this, User.class);
        final OfficialDiandi dianDi = new OfficialDiandi();
        dianDi.setAuthor(user);
        dianDi.setContent(commitContent);
        if (fingureFile != null) {
            dianDi.setContentfigureurl(fingureFile);
        }
        dianDi.setChannel(channel);
        dianDi.setLove(0);
        dianDi.setHate(0);
        dianDi.setShare(0);
        dianDi.setComment(0);
        dianDi.setLink(commitLink);
        dianDi.setPass(true);
        dianDi.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                ShowToast("发表成功！");
                setResult(RESULT_OK);
            }

            @Override
            public void onFailure(int i, String s) {
                ShowToast(R.string.network_tips);
            }
        });
    }

}
