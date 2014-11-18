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
import com.diandi.db.DatabaseUtil;
import com.diandi.sns.TencentShare;
import com.diandi.sns.TencentShareConstants;
import com.diandi.sns.TencentShareEntity;
import com.diandi.ui.activity.CommentActivity;
import com.diandi.ui.activity.ImageBrowserActivity;
import com.diandi.ui.activity.LoginActivity;
import com.diandi.ui.activity.PersonalActivity;
import com.diandi.util.ActivityUtil;
import com.diandi.util.L;
import com.diandi.view.drop.WaterDrop;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-24
 * TODO
 */

public class FeedAdapter extends BaseListAdapter<DianDi> {

    public static final String TAG = "AIContentAdapter";
    public static final int SAVE_FAVOURITE = 2;
    public static final int DIANDI_ALL = 1;
    private int mDiandiType = DIANDI_ALL;
    public static final int DIANDI_PERSON = 2;
    public static final int DIANDI_FAV = 3;
    private static final String VIEW_ID = "view_id_";

    public FeedAdapter(Context context, List<DianDi> list) {
        super(context, list);
    }

    public FeedAdapter(Context context, List<DianDi> list, int diandiType) {
        super(context, list);
        this.mDiandiType = diandiType;
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
            viewHolder.waterDrop = (WaterDrop) convertView.findViewById(R.id.item_drop);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

      /*  viewHolder.waterDrop.setText("new");
        viewHolder.waterDrop.setTextSize(13);
                if (Boolean.valueOf(CustomApplication.getInstance().getCache().getAsString(VIEW_ID + entity.getObjectId()))) {
            viewHolder.waterDrop.setVisibility(View.GONE);
        } else
            viewHolder.waterDrop.setVisibility(View.VISIBLE);
        viewHolder.waterDrop.setOnDragCompeteListener(new DropCover.OnDragCompeteListener() {
            @Override
            public void onDrag() {
                CustomApplication.getInstance().getCache().put(VIEW_ID + entity.getObjectId(), "true");
                Log.e(" ", CustomApplication.getInstance().getCache().getAsString(VIEW_ID + entity.getObjectId()));
            }
        });*/

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
        if (user.getAvatar() != null) {
            avatarUrl = user.getAvatar();
        }
        ImageLoader.getInstance().displayImage(avatarUrl, viewHolder.userLogo, CustomApplication.getInstance().getOptions());
        if (mDiandiType == 1 || mDiandiType == 3) {
            viewHolder.userLogo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
             /*   if (CustomApplication.getInstance().getCurrentUser() == null) {
                    ShowToast("请先登录。");
                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginActivity.class);
                    CustomApplication.getInstance().getTopActivity().startActivity(intent);
                    return;
                }
                */
                    CustomApplication.getInstance().setCurrentDianDi(entity);
                    startAnimActivity(PersonalActivity.class);
                }
            });
        }
        viewHolder.userName.setText(entity.getAuthor().getNick());
        L(entity.getAuthor() + "");
        if (entity.getAuthor().isV()) {
            L(entity.getAuthor() + "");
            viewHolder.userName.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            viewHolder.userName.setTextColor(mContext.getResources().getColor(R.color.dark_blue));

        }

        viewHolder.contentText.setText(entity.getContent());
        if (null == entity.getContentfigureurl()) {
            viewHolder.contentImage.setVisibility(View.GONE);
        } else {
            viewHolder.contentImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance()
                    .displayImage(entity.getContentfigureurl().getFileUrl() == null ? "" : entity.getContentfigureurl().getFileUrl(), viewHolder.contentImage,
                            CustomApplication.getInstance().getOptions(R.drawable.bg_pic_loading),
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
            viewHolder.contentImage.setOnClickListener(new OnClickListener() {
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
        viewHolder.love.setOnClickListener(new OnClickListener() {
            boolean oldFav = entity.getMyFav();

            @Override
            public void onClick(View v) {
                if (entity.getMyLove()) {
                    viewHolder.love.setTextColor(Color.parseColor("#D95555"));
                    return;
                }
      /*          if (DatabaseUtil.getInstance(mContext).isLoved(entity)) {
                    entity.setMyLove(true);
                    entity.setLove(entity.getLove());
                    viewHolder.love.setTextColor(Color.parseColor("#D95555"));
                    viewHolder.love.setText(entity.getLove() + "");
                    return;
                }*/
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
        viewHolder.share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowToast("分享给好友看哦~");
                final TencentShare tencentShare = new TencentShare(CustomApplication.getInstance().getTopActivity(), getQQShareEntity(entity));
                tencentShare.shareToQQ();
            }
        });
        viewHolder.comment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CustomApplication.getInstance().getCurrentUser() == null) {
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

        if (entity.getMyFav()) {
            viewHolder.favMark.setImageResource(R.drawable.ic_action_fav_choose);
        } else {
            viewHolder.favMark.setImageResource(R.drawable.ic_action_fav_normal);
        }
        viewHolder.favMark.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
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
        TencentShareEntity entity = new TencentShareEntity(title, img, TencentShareConstants.WEB, summary, comment);
        return entity;
    }

    private void onClickFav(View v, final DianDi DianDi) {
        User user = BmobUser.getCurrentUser(mContext, User.class);
        if (user != null && user.getSessionToken() != null) {
            BmobRelation favRelaton = new BmobRelation();
            DianDi.setMyFav(!DianDi.getMyFav());
            if (DianDi.getMyFav()) {
                ((ImageView) v).setImageResource(R.drawable.ic_action_fav_choose);
                favRelaton.add(DianDi);
                user.setFavorite(favRelaton);
                ShowToast("收藏成功。");
                user.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        DatabaseUtil.getInstance(mContext).insertFav(DianDi);
                        L.i(TAG, "收藏成功。");
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        L.i(TAG, "收藏失败。请检查网络~");
                        ShowToast("收藏失败。请检查网络~" + arg0);
                    }
                });

            } else {
                ((ImageView) v).setImageResource(R.drawable.ic_action_fav_normal);
                favRelaton.remove(DianDi);
                user.setFavorite(favRelaton);
                ShowToast("取消收藏。");
                user.update(mContext, new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        DatabaseUtil.getInstance(mContext).deleteFav(DianDi);
                        L.i(TAG, "取消收藏。");
                        //try get fav to see if fav success
//						getMyFavourite();
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        L.i(TAG, "取消收藏失败。请检查网络~");
                        ShowToast("取消收藏失败。请检查网络~" + arg0);
                    }
                });
            }
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
                    L.i(TAG, "get fav success!" + data.size());
                    ShowToast("fav size:" + data.size());
                }

                @Override
                public void onError(int arg0, String arg1) {
                    ShowToast("获取收藏失败。请检查网络~");
                }
            });
        } else {
            //前往登录注册界面
            ShowToast("获取收藏前请先登录。");
            Intent intent = new Intent();
            intent.setClass(mContext, LoginActivity.class);
            CustomApplication.getInstance().getTopActivity().startActivityForResult(intent, Constant.GET_FAVOURITE);
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

        private WaterDrop waterDrop;
    }
}
