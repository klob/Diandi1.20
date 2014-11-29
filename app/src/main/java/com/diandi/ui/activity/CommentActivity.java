package com.diandi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.CommentAdapter;
import com.diandi.bean.Comment;
import com.diandi.bean.DianDi;
import com.diandi.bean.User;
import com.diandi.config.Constant;
import com.diandi.db.DatabaseUtil;
import com.diandi.sns.TencentShare;
import com.diandi.sns.TencentShareConstants;
import com.diandi.sns.TencentShareEntity;
import com.diandi.util.ActivityUtil;
import com.diandi.util.ImageLoadOptions;
import com.diandi.util.L;
import com.diandi.view.drop.CoverManager;
import com.diandi.view.drop.DropCover;
import com.diandi.view.drop.WaterDrop;
import com.diandi.view.gitonway.lee.niftynotification.Effects;
import com.diandi.view.gitonway.lee.niftynotification.NiftyNotificationView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
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

public class CommentActivity extends ActivityBase implements View.OnClickListener {

    private final static String COMMENT_ID = "comment_id_";
    public DianDi mDianDi;
    boolean isFav = false;
    private ListView commentList;
    private TextView footer;
    private EditText commentContent;
    private Button commentCommit;
    private TextView userName;
    private TextView commentItemContent;
    private ImageView commentItemImage;
    private ImageView userLogo;
    private ImageView myFav;
    private TextView comment;
    private TextView share;
    private TextView love;
    private TextView hate;
    private WaterDrop mWaterDrop;
    private String commentEdit = "";
    private CommentAdapter mAdapter;
    private List<Comment> comments = new ArrayList<Comment>();
    private int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    protected void findView() {
        setContentView(R.layout.activity_comment);
        commentList = (ListView) findViewById(R.id.comment_list);
        footer = (TextView) findViewById(R.id.loadmore);

        commentContent = (EditText) findViewById(R.id.comment_content);
        commentCommit = (Button) findViewById(R.id.comment_commit);

        userName = (TextView) findViewById(R.id.user_name);
        commentItemContent = (TextView) findViewById(R.id.content_text);
        commentItemImage = (ImageView) findViewById(R.id.content_image);

        userLogo = (ImageView) findViewById(R.id.user_logo);
        myFav = (ImageView) findViewById(R.id.item_action_fav);
        comment = (TextView) findViewById(R.id.item_action_comment);
        share = (TextView) findViewById(R.id.item_action_share);
        love = (TextView) findViewById(R.id.item_action_love);
        mWaterDrop = (WaterDrop) findViewById(R.id.item_drop);

    }

    protected void initView() {


        initTopBarForLeft("评论");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDianDi = (DianDi) getIntent().getSerializableExtra("data");//CustomApplication.getInstance().getCurrentDianDi();
        CustomApplication.getInstance().setCurrentDianDi(mDianDi);


        pageNum = 0;
        mAdapter = new CommentAdapter(CommentActivity.this, comments);
        commentList.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(commentList);
        commentList.setCacheColorHint(0);
        commentList.setScrollingCacheEnabled(false);
        commentList.setScrollContainer(false);
        commentList.setFastScrollEnabled(true);
        commentList.setSmoothScrollbarEnabled(true);
        onClickLoadMore();
        bindEvent();
        CoverManager.getInstance().init(this);
        CoverManager.getInstance().setMaxDragDistance(150);
        CoverManager.getInstance().setExplosionTime(150);
        initMoodView(mDianDi);
    }


    protected void bindEvent() {
        footer.setOnClickListener(this);
        commentCommit.setOnClickListener(this);
        userLogo.setOnClickListener(this);
        myFav.setOnClickListener(this);
        love.setOnClickListener(this);
        share.setOnClickListener(this);
        comment.setOnClickListener(this);
        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //     ShowToast("po" + position);
            }
        });
    }

    protected void fetchData() {
        fetchComment();
    }

    private void initMoodView(DianDi mood2) {
        if (mood2 == null) {
            return;
        }
        mWaterDrop.setText("new");
        mWaterDrop.setTextSize(13);
        if (Boolean.valueOf(CustomApplication.getInstance().getCache().getAsString(COMMENT_ID + mDianDi.getObjectId()))) {
            mWaterDrop.setVisibility(View.GONE);
        } else
            mWaterDrop.setVisibility(View.VISIBLE);
        mWaterDrop.setOnDragCompeteListener(new DropCover.OnDragCompeteListener() {
            @Override
            public void onDrag() {
                CustomApplication.getInstance().getCache().put(COMMENT_ID + mDianDi.getObjectId(), "true");
                //   Log.e(" ", CustomApplication.getInstance().getCache().getAsString(COMMENT_ID + mDianDi.getObjectId()));
            }
        });
        userName.setText(mDianDi.getAuthor().getNick());
        commentItemContent.setText(mDianDi.getContent());
        if (null == mDianDi.getContentfigureurl()) {
            commentItemImage.setVisibility(View.GONE);
        } else {
            commentItemImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance()
                    .displayImage(mDianDi.getContentfigureurl().getFileUrl() == null ? "" : mDianDi.getContentfigureurl().getFileUrl(), commentItemImage,
                            ImageLoadOptions.getOptions(R.drawable.bg_pic_loading),
                            new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri, View view,
                                                              Bitmap loadedImage) {
                                    super.onLoadingComplete(imageUri, view, loadedImage);
                                    float[] cons = ActivityUtil.getBitmapConfiguration(loadedImage, commentItemImage, 1.0f);
                                    RelativeLayout.LayoutParams layoutParams =
                                            new RelativeLayout.LayoutParams((int) cons[0], (int) cons[1]);
                                    layoutParams.addRule(RelativeLayout.BELOW, R.id.content_text);
                                    commentItemImage.setLayoutParams(layoutParams);
                                }
                            }
                    );
            commentItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ImageBrowserActivity.class);
                    ArrayList<String> photos = new ArrayList<String>();
                    photos.add(mDianDi.getContentfigureurl().getFileUrl());
                    intent.putStringArrayListExtra("photos", photos);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }
            });
        }
        love.setText(mDianDi.getLove() + "");
        if (mDianDi.getMyLove()) {
            love.setTextColor(Color.parseColor("#D95555"));
        } else {
            love.setTextColor(Color.parseColor("#000000"));
        }
        if (mDianDi.getMyFav()) {
            myFav.setImageResource(R.drawable.ic_action_fav_choose);
        } else {
            myFav.setImageResource(R.drawable.ic_action_fav_normal);
        }


        User user = mDianDi.getAuthor();
        ImageLoader.getInstance().displayImage(user.getAvatar(), userLogo,
                ImageLoadOptions.getOptions(),
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    }
                }
        );
    }

    private void fetchComment() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereRelatedTo("relation", new BmobPointer(mDianDi));
        query.include("user");
        query.order("createdAt");
        query.setLimit(Constant.NUMBERS_PER_PAGE);
        query.setSkip(Constant.NUMBERS_PER_PAGE * (pageNum++));
        query.findObjects(this, new FindListener<Comment>() {

            @Override
            public void onSuccess(List<Comment> data) {
                // TODO Auto-generated method stub
                L.i(TAG, "get comment success!" + data.size());
                if (data.size() != 0 && data.get(data.size() - 1) != null) {

                    if (data.size() < Constant.NUMBERS_PER_PAGE) {
                        //     ShowToast("已加载完所有评论~");
                        footer.setText("暂无更多评论~");
                    }

                    mAdapter.addAll(data);
                    mAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(commentList);
                    L.i(TAG, "refresh");
                } else {
                    //   ShowToast("暂无更多评论~");
                    footer.setText("暂无更多评论~");
                    pageNum--;
                }
            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub
                ShowToast("获取评论失败。请检查网络~~");
                pageNum--;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_logo:
                onClickUserLogo();
                break;
            case R.id.loadmore:
                onClickLoadMore();
                break;
            case R.id.comment_commit:
                onClickCommit();
                break;
            case R.id.item_action_fav:
                onClickFav(v);
                break;
            case R.id.item_action_love:
                onClickLove();
                break;
            case R.id.item_action_share:
                onClickShare();
                break;
            case R.id.item_action_comment:
                onClickComment();
                break;
            default:
                break;
        }
    }

    private void onClickUserLogo() {
        //跳转到个人信息界面
        User currentUser = BmobUser.getCurrentUser(this, User.class);
        if (currentUser != null) {//已登录
            Intent intent = new Intent();
            intent.setClass(CustomApplication.getInstance().getTopActivity(), PersonalActivity.class);
            CommentActivity.this.startAnimActivity(intent);
        } else {
            ShowToast("请先登录。");
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivityForResult(intent, Constant.GO_SETTINGS);

        }
    }

    private void onClickLoadMore() {
        fetchData();
    }

    private void onClickCommit() {
        // TODO Auto-generated method stub
        User currentUser = BmobUser.getCurrentUser(this, User.class);
        if (currentUser != null) {//已登录
            commentEdit = commentContent.getText().toString().trim();
            if (TextUtils.isEmpty(commentEdit)) {
                ShowToast("评论内容不能为空。");
                return;
            }
            //comment now
            publishComment(currentUser, commentEdit);
        } else {//未登录
            ShowToast("发表评论前请先登录。");
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivityForResult(intent, Constant.PUBLISH_COMMENT);
        }

    }

    private void publishComment(User user, String content) {

        commentCommit.setEnabled(false);
        final Comment comment = new Comment();
        comment.setUser(user);
        comment.setCommentContent(content);
        comment.save(this, new SaveListener() {


            @Override
            public void onSuccess() {
                ShowToast("评论成功。");
                if (mAdapter.getCount() < Constant.NUMBERS_PER_PAGE) {
                    mAdapter.add(comment);
                    mAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(commentList);
                    commentCommit.setEnabled(true);
                }
                commentContent.setText("");
                hideSoftInput();

                //将该评论与强语绑定到一起
                BmobRelation relation = new BmobRelation();
                relation.add(comment);
                mDianDi.setRelation(relation);
                mDianDi.update(CommentActivity.this, new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        L.i(TAG, "更新评论成功。");
//						fetchData();
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        L.i(TAG, "更新评论失败。" + arg1);
                    }
                });

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                commentCommit.setEnabled(true);
                ShowToast("评论失败。请检查网络~");
            }
        });
    }

    private void onClickFav(View v) {
        // TODO Auto-generated method stub

        User user = BmobUser.getCurrentUser(this, User.class);
        if (user != null && user.getSessionToken() != null) {
            BmobRelation favRelaton = new BmobRelation();
            mDianDi.setMyFav(!mDianDi.getMyFav());
            if (mDianDi.getMyFav()) {
                ((ImageView) v).setImageResource(R.drawable.ic_action_fav_choose);
                favRelaton.add(mDianDi);
                ShowToast("收藏成功。");
            } else {
                ((ImageView) v).setImageResource(R.drawable.ic_action_fav_normal);
                favRelaton.remove(mDianDi);
                ShowToast("取消收藏。");
            }

            user.setFavorite(favRelaton);
            user.update(this, new UpdateListener() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    L.i(TAG, "收藏成功。");
                    ShowToast("收藏成功。");
                    //try get fav to see if fav success
//					getMyFavourite();
                }

                @Override
                public void onFailure(int arg0, String arg1) {
                    // TODO Auto-generated method stub
                    L.i(TAG, "收藏失败。请检查网络~");
                    ShowToast("收藏失败。请检查网络~" + arg0);
                }
            });
        } else {
            //前往登录注册界面
            ShowToast("收藏前请先登录。");
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivityForResult(intent, Constant.SAVE_FAVOURITE);
        }

    }

    private void getMyFavourite() {
        User user = BmobUser.getCurrentUser(this, User.class);
        if (user != null) {
            BmobQuery<DianDi> query = new BmobQuery<DianDi>();
            query.addWhereRelatedTo("favorite", new BmobPointer(user));
            query.include("user");
            query.order("createdAt");
            query.setLimit(Constant.NUMBERS_PER_PAGE);
            query.findObjects(this, new FindListener<DianDi>() {

                @Override
                public void onSuccess(List<DianDi> data) {
                    // TODO Auto-generated method stub
                    L.i(TAG, "get fav success!" + data.size());
                    ShowToast("fav size:" + data.size());
                }

                @Override
                public void onError(int arg0, String arg1) {
                    // TODO Auto-generated method stub
                    ShowToast("获取收藏失败。请检查网络~");
                }
            });
        } else {
            //前往登录注册界面
            ShowToast("获取收藏前请先登录。");
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivityForResult(intent, Constant.GET_FAVOURITE);
        }
    }

    private void onClickLove() {
        // TODO Auto-generated method stub
        User user = BmobUser.getCurrentUser(this, User.class);
        if (user == null) {
            //前往登录注册界面
            ShowToast("请先登录。");
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        if (mDianDi.getMyLove()) {
            ShowToast("您已经赞过啦");
            return;
        }
        isFav = mDianDi.getMyFav();
        if (isFav) {
            mDianDi.setMyFav(false);
        }
        mDianDi.setLove(mDianDi.getLove() + 1);
        love.setTextColor(Color.parseColor("#D95555"));
        love.setText(mDianDi.getLove() + "");
        mDianDi.increment("love", 1);
        mDianDi.update(CommentActivity.this, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                mDianDi.setMyLove(true);
                mDianDi.setMyFav(isFav);
                DatabaseUtil.getInstance(CommentActivity.this).insertFav(mDianDi);

                ShowToast("点赞成功~");
            }

            @Override
            public void onFailure(int arg0, String arg1) {

            }
        });
    }


    private void onClickShare() {

        NiftyNotificationView.build(this, "分享给好友看哦", Effects.scale, R.id.mLyout).show();
        final TencentShare tencentShare = new TencentShare(CustomApplication.getInstance().getTopActivity(), getQQShareEntity(mDianDi));
        tencentShare.shareToQQ();
    }


    private void onClickComment() {
        // TODO Auto-generated method stub
        commentContent.requestFocus();

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(commentContent, 0);
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(commentContent.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.PUBLISH_COMMENT:
                    //登录完成
                    commentCommit.performClick();
                    break;
                case Constant.SAVE_FAVOURITE:
                    myFav.performClick();
                    break;
                case Constant.GET_FAVOURITE:

                    break;
                case Constant.GO_SETTINGS:
                    userLogo.performClick();
                    break;
                default:
                    break;
            }
        }

    }


    /**
     * 动态设置listview的高度
     * item 总布局必须是linearLayout
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + 15;
        listView.setLayoutParams(params);
    }

    private TencentShareEntity getQQShareEntity(DianDi qy) {
        String title = "这里好多美丽的风景";
        String comment = "来领略最美的风景吧";
        String img = null;
        if (qy.getContentfigureurl() != null) {
            img = qy.getContentfigureurl().getFileUrl();
        } else {
            img = TencentShareConstants.DEFAULT_IMG_URL;
        }
        String summary = qy.getContent();
        TencentShareEntity entity = new TencentShareEntity(title, img, TencentShareConstants.TARGET_URL, summary, comment);
        return entity;
    }

}
