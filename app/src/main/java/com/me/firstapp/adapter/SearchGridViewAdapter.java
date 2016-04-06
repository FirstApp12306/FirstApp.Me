package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.activity.PersonInfoActivity;
import com.me.firstapp.entity.User;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.view.CircleImageView;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class SearchGridViewAdapter extends BaseAdapter {
    private Context context;
    private Activity mActivity;
    private ArrayList<User> users;

    public SearchGridViewAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.mActivity = (Activity) context;
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

    public void addMore(ArrayList<User> moreUsers){
        this.users.addAll(moreUsers);
        doNotify();
    }

    public void doNotify(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void clearList(){
        this.users.clear();
        doNotify();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.search_grid_view_item, null);
            holder = new ViewHolder();
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.search_grid_view_item_avatar);
            holder.ivShadow = (CircleImageView) convertView.findViewById(R.id.search_grid_view_item_shadow);
            holder.ivArrow = (ImageView) convertView.findViewById(R.id.search_grid_view_item_arrow);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.search_grid_view_item_user_name);
            holder.tvUserID = (TextView) convertView.findViewById(R.id.search_grid_view_item_user_id);
            holder.tvMore = (TextView) convertView.findViewById(R.id.search_grid_view_item_user_more);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final User user = users.get(position);
        ImageUtils.bindImageWithOptions(holder.ivAvatar, user.user_avatar,
                R.drawable.person_avatar_default_round,
                R.drawable.person_avatar_default_round);
        holder.tvUserName.setText(user.user_name);
        holder.tvUserID.setText("ID:"+user.user_id);

        return convertView;
    }

    private class ViewHolder{
        CircleImageView ivAvatar;
        CircleImageView ivShadow;
        ImageView ivArrow;
        TextView tvUserName;
        TextView tvUserID;
        TextView tvMore;
    }
}
