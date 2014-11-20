package com.diandi.view.residemenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diandi.R;
import com.diandi.util.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * User: special
 * Date: 13-12-10
 * Time: 下午11:05
 * Mail: specialcyci@gmail.com
 */
public class ResideMenuItem extends LinearLayout {

    public static final int TYPE_HEAD = -6546546;
    public static final int TYPE_CONTENT = -6546545;
    /**
     * menu item  icon
     */
    private ImageView iv_icon;
    /**
     * menu item  title
     */
    private TextView tv_title;

    public ResideMenuItem(Context context, int type) {
        super(context);
        initViews(context, type);
    }

    public ResideMenuItem(Context context, int icon, int title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    public ResideMenuItem(Context context, int icon, String title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    public ResideMenuItem(Context context, String iconUrl, String title, int type) {
        super(context);
        initViews(context, type);
        ImageLoader.getInstance().displayImage(iconUrl, iv_icon,
                ImageLoadOptions.getOptions());
        tv_title.setText(title);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_item, this);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    private void initViews(Context context, int type) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (type == TYPE_CONTENT) {
            inflater.inflate(R.layout.residemenu_item, this);
        } else {
            inflater.inflate(R.layout.residemenu_item_head, this);
        }
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    /**
     * set the icon color;
     *
     * @param icon
     */
    public void setIcon(int icon) {
        iv_icon.setImageResource(icon);
    }


    public void setIcon(String iconUrl) {
        ImageLoader.getInstance().displayImage(iconUrl, iv_icon,
                ImageLoadOptions.getOptions());

    }

    /**
     * set the title with resource
     * ;
     *
     * @param title
     */
    public void setTitle(int title) {
        tv_title.setText(title);
    }

    /**
     * set the title with string;
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }


}
