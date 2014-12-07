package com.diandi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.base.BaseListAdapter;
import com.diandi.bussiness.db.DatabaseUtil;
import com.diandi.model.User;
import com.diandi.model.diandi.DianDi;
import com.diandi.sync.UserHelper;
import com.diandi.sync.sns.TencentShare;
import com.diandi.sync.sns.TencentShareConstants;
import com.diandi.sync.sns.TencentShareEntity;
import com.diandi.ui.activity.CommentActivity;
import com.diandi.ui.activity.ImageBrowserActivity;
import com.diandi.ui.activity.LoginActivity;
import com.diandi.util.ActivityUtil;
import com.diandi.util.ImageLoadOptions;
import com.diandi.util.L;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;


public class PersonalAdapter extends BaseListAdapter<DianDi> {

    public static final String TAG = "AIContentAdapter";

    public PersonalAdapter(Context context, List<DianDi> list) {
        super(context, list);
    }


    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_personal_say, null);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.userLogo = (ImageView) convertView.findViewById(R.id.user_logo);
            viewHolder.contentText = (TextView) convertView.findViewById(R.id.content_text);
            viewHolder.contentImage = (ImageView) convertView.findViewById(R.id.content_image);
            viewHolder.love = (TextView) convertView.findViewById(R.id.item_action_love);
            viewHolder.share = (TextView) convertView.findViewById(R.id.item_action_share);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.item_action_comment);
            viewHolder.ll_action_comment = (LinearLayout) convertView.findViewById(R.id.ll_action_comment);
            viewHolder.ll_action_love = (LinearLayout) convertView.findViewById(R.id.ll_action_love);
            viewHolder.ll_action_share = (LinearLayout) convertView.findViewById(R.id.ll_action_share);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final DianDi entity = mDataList.get(position);
        User user = entity.getAuthor();
        String avatarUrl = null;
        if (user.getAvatar() != null) {
            avatarUrl = user.getAvatar();
        }

        //设置用户头像
        ImageLoader.getInstance().displayImage(avatarUrl, viewHolder.userLogo, ImageLoadOptions.getOptions());

        //设置用户姓名
        viewHolder.userName.setText(entity.getAuthor().getNick());
        L(entity.getAuthor() + "");
        if (entity.getAuthor().isV()) {
            L(entity.getAuthor() + "");
            viewHolder.userName.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            viewHolder.userName.setTextColor(mContext.getResources().getColor(R.color.dark_blue));

        }
        //设置内容
        viewHolder.contentText.setText(entity.getContent());
        //设置内容中的图片
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
                                    super.onLoadingComplete(imageUri, view, loadedImage);
                                    float[] cons = ActivityUtil.getBitmapConfiguration(loadedImage, viewHolder.contentImage, 1.0f);
                                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) cons[0], (int) cons[1]);
                                    layoutParams.addRule(RelativeLayout.BELOW, R.id.content_text);
                                    viewHolder.contentImage.setLayoutParams(layoutParams);
                                }

                            }
                    );

            //图片点击事件，跳转浏览页面
            viewHolder.contentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ImageBrowserActivity.class);
                    ArrayList<String> photos = new ArrayList<String>();
                    photos.add(entity.getContentfigureurl().getFileUrl());
                    intent.putStringArrayListExtra("photos", photos);
                    intent.putExtra("position", 0);
                    mContext.startActivity(intent);
                }
            });
        }

        //点赞设置
        viewHolder.love.setText(entity.getLove() + "");
        L.i("love", entity.getMyLove() + "..");
        if (entity.getMyLove()) {
            viewHolder.love.setTextColor(Color.parseColor("#D95555"));
        } else {
            viewHolder.love.setTextColor(Color.parseColor("#888888"));
        }
        if (DatabaseUtil.getInstance(mContext).isLoved(entity)) {
            entity.setMyLove(true);
            entity.setLove(entity.getLove());
            viewHolder.love.setTextColor(Color.parseColor("#D95555"));
            viewHolder.love.setText(entity.getLove() + "");
        }
        viewHolder.ll_action_love.setOnClickListener(new View.OnClickListener() {
            boolean oldFav = entity.getMyFav();

            @Override
            public void onClick(View v) {
                if (entity.getMyLove()) {
                    viewHolder.love.setTextColor(Color.parseColor("#D95555"));
                    return;
                }

                entity.setLove(entity.getLove() + 1);
                viewHolder.love.setTextColor(Color.parseColor("#D95555"));
                viewHolder.love.setText(entity.getLove() + "");

                entity.increment("love", 1);
                if (entity.getMyFav()) {
                    entity.setMyFav(false);
                }
                entity.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        entity.setMyLove(true);
                        entity.setMyFav(oldFav);
                        DatabaseUtil.getInstance(mContext).insertFav(entity);
                        L.i(TAG, "点赞成功~");
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        entity.setMyLove(true);
                        entity.setMyFav(oldFav);
                    }
                });
            }
        });

        //分享点击事件
        viewHolder.ll_action_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowToast("分享给好友看哦~");
                final TencentShare tencentShare = new TencentShare(CustomApplication.getInstance().getTopActivity(), getQQShareEntity(entity));
                tencentShare.shareToQQ();
            }
        });
        viewHolder.ll_action_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserHelper.getCurrentUser() == null) {
                    ShowToast("请先登录。");
                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginActivity.class);
                    CustomApplication.getInstance().getTopActivity().startActivity(intent);
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(CustomApplication.getInstance().getTopActivity(), CommentActivity.class);
                intent.putExtra("data", entity);
                startAnimActivity(intent);
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
        TencentShareEntity entity = new TencentShareEntity(title, img, TencentShareConstants.WEB, summary, comment);
        return entity;
    }

    public static class ViewHolder {
        public ImageView userLogo;
        public TextView userName;
        public TextView contentText;
        public ImageView contentImage;
        public LinearLayout ll_action_comment;
        public LinearLayout ll_action_love;
        public LinearLayout ll_action_share;

        public TextView love;
        public TextView share;
        public TextView comment;

    }
}








