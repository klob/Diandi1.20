package com.diandi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.diandi.R;
import com.diandi.config.Constant;
import com.diandi.util.factory.OverridePendingUtil;
import com.diandi.view.HeaderLayout.onRightImageButtonClickListener;

public class UpdateInfoActivity extends ActivityBase {
    private EditText mEditText;
    private TextView mTextView;
    private String mFromStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    void findView() {
        setContentView(R.layout.activity_set_updateinfo);
        mEditText = (EditText) findViewById(R.id.activity_updateinfo_edittext);
        mTextView = (TextView) findViewById(R.id.activity_updateinfo_text);
    }

    void initView() {
        Intent intent = getIntent();
        String mFromStr = intent.getStringExtra(Constant.UPDATE_ACTIONBAR_NAME);
        mTextView.setText(intent.getStringExtra(Constant.UPDATE_TEXT));
        mEditText.setHint(intent.getStringExtra(Constant.UPDATE_EDIT_HINT));
        initTopBarForBoth(mFromStr, R.drawable.base_action_bar_true_bg_selector,
                new onRightImageButtonClickListener() {
                    @Override
                    public void onClick() {
                        String backStr = mEditText.getText().toString();
                        if (backStr.equals("")) {
                            ShowToast("不能为空");
                            return;
                        }
                        Intent intentBack = new Intent();
                        intentBack.putExtra(Constant.UPDATE_BACK_CONTENT, backStr);
                        setResult(RESULT_OK, intentBack);
                        finish();
                        OverridePendingUtil.out(UpdateInfoActivity.this);
                    }
                }
        );
        if (mFromStr.equals("修改昵称")) {
            bindEvent();
        }
    }

    @Override
    void bindEvent() {

        TextWatcher textWatcher = new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable s) {
                editStart = mEditText.getSelectionStart();
                editEnd = mEditText.getSelectionEnd();
                // mTextView.setText(" + temp.length() + "个字符");
                if (temp.length() > 6) {
                    ShowToast("你输入的字数已经超过了限制！");
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mEditText.setText(s);
                    mEditText.setSelection(tempSelection);
                }
            }
        };

        mEditText.addTextChangedListener(textWatcher);

    }
}
