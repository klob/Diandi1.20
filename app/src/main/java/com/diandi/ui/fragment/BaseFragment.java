package com.diandi.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.bean.User;
import com.diandi.sync.UserHelper;
import com.diandi.util.factory.OverridePendingUtil;
import com.diandi.view.HeaderLayout;
import com.diandi.view.HeaderLayout.HeaderStyle;
import com.diandi.view.HeaderLayout.onLeftImageButtonClickListener;
import com.diandi.view.HeaderLayout.onRightImageButtonClickListener;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;

/**
 * Fragmenet 基类
 *
 * @author smile
 * @ClassName: FragmentBase
 * @Description: TODO
 * @date 2014-5-22 下午2:43:50
 */
public abstract class BaseFragment extends Fragment {
    public final String TAG = ((Object) this).getClass().getSimpleName();
    protected BmobUserManager userManager;
    protected BmobChatManager manager;
    protected CustomApplication mApplication;
    protected HeaderLayout mHeaderLayout;
    protected LayoutInflater mInflater;
    protected Toast mToast;
    private Handler handler = new Handler();

    public Object getCache(String cacheKey) {
        return CustomApplication.getInstance().getCache().getAsObject(cacheKey);
    }

    public void putCache(String cacheKey, Object... obj) {
        if (obj != null)
            CustomApplication.getInstance().getCache().put(cacheKey, obj);
    }

    abstract void initView();

    abstract void findView();

    abstract void bindEvent();

    public void runOnWorkThread(Runnable action) {
        new Thread(action).start();
    }

    public void runOnUiThread(Runnable action) {
        handler.post(action);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mApplication = CustomApplication.getInstance();
        userManager = BmobUserManager.getInstance(getActivity());
        manager = BmobChatManager.getInstance(getActivity());
        mInflater = LayoutInflater.from(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void LogE(Object object) {
        Log.e(TAG, object.toString());
    }

    public void LogE(String str) {
        Log.e(TAG, str);
    }

    public void ShowToast(final String text) {

        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mToast == null && getActivity() != null) {
                            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
                        } else {
                            mToast.setText(text);
                        }
                        mToast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public void ShowToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mToast == null) {
                        mToast = Toast.makeText(getActivity().getApplicationContext(), resId,
                                Toast.LENGTH_LONG);
                    } else {
                        mToast.setText(resId);
                    }
                    mToast.show();
                    Log.d(TAG, getString(resId));
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public View findViewById(int paramInt) {
        return getView().findViewById(paramInt);
    }

    public void initTopBarForOnlyTitle(String titleName) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(titleName);
    }

    public void initTopBarForBoth(String titleName, int rightDrawableId,
                                  onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
                listener);
    }

    public void initTopBarForLeft(String titleName) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
    }

    public void initTopBarForRight(String titleName, int rightDrawableId,
                                   onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
                listener);
    }

    public void startAnimActivity(Intent intent) {
        getActivity().startActivity(intent);
        OverridePendingUtil.in(getActivity());
    }

    public void startAnimActivity(Class<?> cla) {
        getActivity().startActivity(new Intent(getActivity(), cla));
        OverridePendingUtil.in(getActivity());
    }

    public User getCurrentUser() {
        return UserHelper.getCurrentUser(getActivity());
    }

    public class OnLeftButtonClickListener implements
            onLeftImageButtonClickListener {
        @Override
        public void onClick() {
            getActivity().finish();
        }
    }

}
