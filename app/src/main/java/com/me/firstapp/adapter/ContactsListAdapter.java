package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class ContactsListAdapter extends BaseAdapter {
    private Context context;
    private Activity mActivity;
    private ArrayList<User> users;

    public ContactsListAdapter(Context context, ArrayList<User> users) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.contacts_list_item, null);
            holder = new ViewHolder();
            holder.tvLetter = (TextView) convertView.findViewById(R.id.contacts_list_item_tv_catagory);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.contacts_list_item_tv_user_name);
            holder.tvUserID = (TextView) convertView.findViewById(R.id.contacts_list_item_tv_user_id);
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.contacts_list_item_avatar);
            holder.line = convertView.findViewById(R.id.contacts_list_item_line);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        User user = users.get(position);
        holder.tvUserName.setText(user.user_name);
        holder.tvUserID.setText("ID:"+user.user_id);
        ImageUtils.bindImageWithOptions(holder.ivAvatar, user.user_avatar,
                R.drawable.person_avatar_default_round,
                R.drawable.person_avatar_default_round);
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(user.sortLetters);
            holder.line.setVisibility(View.GONE);
        }else{
            holder.tvLetter.setVisibility(View.GONE);
            holder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public int getSectionForPosition(int position) {
        return this.users.get(position).sortLetters.charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = this.users.get(i).sortLetters;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    private class ViewHolder{
        TextView tvLetter;
        TextView tvUserName;
        TextView tvUserID;
        CircleImageView ivAvatar;
        View line;
    }
}
