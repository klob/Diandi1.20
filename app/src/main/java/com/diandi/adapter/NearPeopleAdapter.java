package com.diandi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diandi.CustomApplication;
import com.diandi.R;
import com.diandi.adapter.base.BaseListAdapter;
import com.diandi.adapter.base.ViewHolder;
import com.diandi.bean.User;
import com.diandi.util.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * 附近的人
 *
 * @author smile
 * @ClassName: BlackListAdapter
 * @Description: TODO
 * @date 2014-6-24 下午5:27:14
 */
public class NearPeopleAdapter extends BaseListAdapter<User> {

    private static final double EARTH_RADIUS = 6378137;

    public NearPeopleAdapter(Context context, List<User> list) {
        super(context, list);
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离：单位为米
     */
    public static double DistanceOfTwoPoints(double lat1, double lng1,
                                             double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_near_people, null);
        }
        final User contract = getList().get(position);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_distance = ViewHolder.get(convertView, R.id.tv_distance);
        TextView tv_logintime = ViewHolder.get(convertView, R.id.tv_logintime);
        ImageView iv_avatar = ViewHolder.get(convertView, R.id.iv_avatar);
        String avatar = contract.getAvatar();
        ImageLoader.getInstance().displayImage(avatar, iv_avatar,
                ImageLoadOptions.getOptions());

        BmobGeoPoint location = contract.getLocation();
        String currentLat = CustomApplication.getInstance().getLatitude();
        String currentLong = CustomApplication.getInstance().getLongtitude();
        if (location != null && !currentLat.equals("") && !currentLong.equals("")) {
            double distance = DistanceOfTwoPoints(Double.parseDouble(currentLat), Double.parseDouble(currentLong), contract.getLocation().getLatitude(),
                    contract.getLocation().getLongitude());
            tv_distance.setText(String.valueOf(distance) + "米");
        } else {
            tv_distance.setText("未知");
        }
        String userNick = contract.getNick();
        if (userNick != null) {
            tv_name.setText(contract.getNick());
        } else {
            tv_name.setText("无名氏" + position + "号");

        }
        tv_logintime.setText("最近登录时间:" + contract.getUpdatedAt());
        return convertView;
    }

}
