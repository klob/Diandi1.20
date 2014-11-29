package com.diandi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.base.BaseListAdapter;
import com.diandi.bean.DianDi;
import com.diandi.bean.User;
import com.diandi.config.Constant;
import com.diandi.sns.TencentShare;
import com.diandi.sns.TencentShareConstants;
import com.diandi.sns.TencentShareEntity;
import com.diandi.ui.activity.CommentActivity;
import com.diandi.util.ActivityUtil;
import com.diandi.util.ImageLoadOptions;
import com.diandi.util.L;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
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
public class PersonCenterAdapter extends BaseListAdapter<DianDi> {

    public static final String TAG = "AIContentAdapter";
    public static final int SAVE_FAVOURITE = 2;

    public PersonCenterAdapter(Context context, List<DianDi> list) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_feed, null);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.userLogo = (ImageView) convertView.findViewById(R.id.user_logo);
            viewHolder.favMark = (ImageView) convertView.findViewById(R.id.item_action_fav);
            viewHolder.contentText = (TextView) convertView.findViewById(R.id.content_text);
            viewHolder.contentImage = (ImageView) convertView.findViewById(R.id.content_image);
            viewHolder.love = (TextView) convertView.findViewById(R.id.item_action_love);
            viewHolder.share = (TextView) convertView.findViewById(R.id.item_action_share);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.item_action_comment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final DianDi entity = mDataList.get(position);
        L.i("user", entity.toString());
        User user = entity.getAuthor();
        if (user == null) {
            L.i("user", "USER IS NULL");
        }
        if (user.getAvatar() == null) {
            L.i("user", "USER avatar IS NULL");
        }
        String avatarUrl = null;
        if (user.getAvatarImg() != null) {
            avatarUrl = user.getAvatar();
        }
        ImageLoader.getInstance()
                .displayImage(avatarUrl, viewHolder.userLogo,
                        ImageLoadOptions.getOptions(),
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view,
                                                          Bitmap loadedImage) {
                                // TODO Auto-generated method stub
                                super.onLoadingComplete(imageUri, view, loadedImage);
                            }

                        });
        viewHolder.userLogo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CustomApplication.getInstance().setCurrentDianDi(entity);
//				User currentUser = BmobUser.getCurrentUser(mContext,User.class);
//				if(currentUser != null){//已登录
//					Intent intent = new Intent();
//					intent.setClass(CustomApplication.getInstance().getTopActivity(), PersonalActivity.class);
//					mContext.startActivity(intent);
//				}else{//未登录
//					ActivityUtil.show(mContext, "请先登录。");
//					Intent intent = new Intent();
//					intent.setClass(CustomApplication.getInstance().getTopActivity(), RegisterAndLoginActivity.class);
//					CustomApplication.getInstance().getTopActivity().startActivityForResult(intent, Constant.GO_SETTINGS);
//				}
            }
        });
        viewHolder.userName.setText(entity.getAuthor().getNick());
        viewHolder.contentText.setText(entity.getContent());
        if (null == entity.getContentfigureurl()) {
            viewHolder.contentImage.setVisibility(View.GONE);
        } else {
            viewHolder.contentImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance()
                    .displayImage(entity.getContentfigureurl().getFileUrl() == null ? "" : entity.getContentfigureurl().getFileUrl(), viewHolder.contentImage,
                            ImageLoadOptions.getOptions(R.drawable.bg_pic_loading),
                            new SimpleImageLoadingListener() {

                                @Override
                                public void onLoadingComplete(String imageUri, View view,
                                                              Bitmap loadedImage) {
                                    // TODO Auto-generated method stub
                                    super.onLoadingComplete(imageUri, view, loadedImage);
                                    float[] cons = ActivityUtil.getBitmapConfiguration(loadedImage, viewHolder.contentImage, 1.0f);
                                    RelativeLayout.LayoutParams layoutParams =
                                            new RelativeLayout.LayoutParams((int) cons[0], (int) cons[1]);
                                    layoutParams.addRule(RelativeLayout.BELOW, R.id.content_text);
                                    viewHolder.contentImage.setLayoutParams(layoutParams);
                                }

                            });
        }
        viewHolder.love.setText(entity.getLove() + "");
        L.i("love", entity.getMyLove() + "..");
        if (entity.getMyLove()) {
            viewHolder.love.setTextColor(Color.parseColor("#D95555"));
        } else {
            viewHolder.love.setTextColor(Color.parseColor("#000000"));
        }
        viewHolder.hate.setText(entity.getHate() + "");
        viewHolder.love.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (entity.getMyLove()) {
                    return;
                }
                entity.setLove(entity.getLove() + 1);
                viewHolder.love.setTextColor(Color.parseColor("#D95555"));
                viewHolder.love.setText(entity.getLove() + "");
                entity.setMyLove(true);
                entity.increment("love", 1);
                entity.update(mContext, new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        L.i(TAG, "点赞成功~");
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        // TODO Auto-generated method stub

                    }
                });
            }
        });
        viewHolder.hate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                entity.setHate(entity.getHate() + 1);
                viewHolder.hate.setText(entity.getHate() + "");
                entity.increment("hate", 1);
                entity.update(mContext, new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        ShowToast("点踩成功~");
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        // TODO Auto-generated method stub

                    }
                });
            }
        });
        viewHolder.share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //share to sociaty
                ShowToast("分享给好友看哦~");
                final TencentShare tencentShare = new TencentShare(CustomApplication.getInstance().getTopActivity(), getQQShareEntity(entity));
                tencentShare.shareToQQ();
            }
        });
        viewHolder.comment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //评论
                Intent intent = new Intent();
                intent.putExtra("data", entity);
                intent.setClass(mContext, CommentActivity.class);
                startAnimActivity(intent);
            }
        });

        if (entity.getMyFav()) {
            viewHolder.favMark.setImageResource(R.drawable.ic_action_fav_choose);
        } else {
            viewHolder.favMark.setImageResource(R.drawable.ic_action_fav_normal);
        }
        viewHolder.favMark.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //收藏
                ShowToast("收藏");
                onClickFav(v, entity);

            }
        });
        return convertView;
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

        String targetUrl = "http://yuanquan.bmob.cn";
        TencentShareEntity entity = new TencentShareEntity(title, img, targetUrl, summary, comment);
        return entity;
    }

    private void onClickFav(View v, DianDi DianDi) {
        // TODO Auto-generated method stub
        User user = BmobUser.getCurrentUser(mContext, User.class);
        if (user != null && user.getSessionToken() != null) {
            BmobRelation favRelaton = new BmobRelation();

            DianDi.setMyFav(!DianDi.getMyFav());
            if (DianDi.getMyFav()) {
                ((ImageView) v).setImageResource(R.drawable.ic_action_fav_choose);
                favRelaton.add(DianDi);
                ShowToast("收藏成功。");
            } else {
                ((ImageView) v).setImageResource(R.drawable.ic_action_fav_normal);
                favRelaton.remove(DianDi);
                ShowToast("取消收藏。");
            }

            user.setFavorite(favRelaton);
            user.update(mContext, new UpdateListener() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    L.i(TAG, "收藏成功。");
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
        }
    }

    private void getMyFavourite() {
        User user = BmobUser.getCurrentUser(mContext, User.class);
        if (user != null) {
            BmobQuery<DianDi> query = new BmobQuery<DianDi>();
            query.addWhereRelatedTo("favorite", new BmobPointer(user));
            query.include("user");
            query.order("createdAt");
            query.setLimit(Constant.NUMBERS_PER_PAGE);
            query.findObjects(mContext, new FindListener<DianDi>() {

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
        }
    }

    public static class ViewHolder {
        public ImageView userLogo;
        public TextView userName;
        public TextView contentText;
        public ImageView contentImage;

        public ImageView favMark;
        public TextView love;
        public TextView hate;
        public TextView share;
        public TextView comment;
    }
}
