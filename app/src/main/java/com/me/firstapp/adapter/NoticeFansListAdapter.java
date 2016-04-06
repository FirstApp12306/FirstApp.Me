package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.User;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.view.CircleImageView;

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
    private Activity mActivity;
    private ArrayList<User> users;

    public NoticeFansListAdapter(Context context, ArrayList<User> users) {
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

    public void doNotify() {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void addMore(ArrayList<User> moreUsers) {
        this.users.addAll(moreUsers);
        doNotify();
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
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        User user = users.get(position);

        ImageUtils.bindImageWithOptions(holder.ivAvatar, user.user_avatar,
                R.drawable.person_avatar_default_round, R.drawable.person_avatar_default_round);
        holder.tvUserName.setText(user.user_name);
        holder.tvUserID.setText("ID:"+user.user_id);
        holder.tvUserCity.setText(user.user_city);


        return convertView;
    }

    private class ViewHolder {
        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public TextView tvUserID;
        public TextView tvUserCity;
        public ImageButton btnFollow;
        public ImageButton btnEachOther;
    }
}
