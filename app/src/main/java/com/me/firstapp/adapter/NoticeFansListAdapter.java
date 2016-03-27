package com.me.firstapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.MySupport;
import com.me.firstapp.entity.User;
import com.me.firstapp.view.CircleImageView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class NoticeFansListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<User> users;
    public NoticeFansListAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.notice_fans_listview_item, null);
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.notice_fans_listview_item_avatar);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.notice_fans_listview_item_user_name);
            holder.tvUserID = (TextView) convertView.findViewById(R.id.notice_fans_listview_item_user_id);
            holder.tvUserCity = (TextView) convertView.findViewById(R.id.notice_fans_listview_item_user_city);
            holder.btnFollow = (ImageButton) convertView.findViewById(R.id.notice_fans_listview_item_pserson_fallow);
            holder.btnEachOther = (ImageButton) convertView.findViewById(R.id.notice_fans_listview_item_pserson_fallow_eachother);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        User user = users.get(position);

        ImageOptions imageOptions1 = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(true)
                        // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                        //.setUseMemCache(false)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
        x.image().bind(holder.ivAvatar, user.user_avatar, imageOptions1);
        holder.tvUserName.setText(user.user_name);
        holder.tvUserID.setText(user.user_id);
        holder.tvUserCity.setText(user.user_city);



        return convertView;
    }

    private class ViewHolder{
        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public TextView tvUserID;
        public TextView tvUserCity;
        public ImageButton btnFollow;
        public ImageButton btnEachOther;
    }
}
