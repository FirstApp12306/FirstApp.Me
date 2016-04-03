package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.Support;
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
public class SupportListAdapter extends BaseAdapter {
    private Context context;
    private Activity mActivity;
    private ArrayList<Support> supports;

    public SupportListAdapter(Context context, ArrayList<Support> supports) {
        this.context = context;
        mActivity = (Activity) context;
        this.supports = supports;
    }

    @Override
    public int getCount() {
        return supports.size();
    }

    @Override
    public Object getItem(int position) {
        return supports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addMore(ArrayList<Support> moreSupports){
        this.supports.addAll(moreSupports);
        doNotify();
    }

    private void doNotify(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.activity_support_list_item, null);
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.activity_support_list_item_avatar);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.activity_support_list_item_user_name);
            holder.tvUserID = (TextView) convertView.findViewById(R.id.activity_support_list_item_user_id);
            holder.tvUserCity = (TextView) convertView.findViewById(R.id.activity_support_list_item_city);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Support support = supports.get(position);
        ImageUtils.bindImageWithOptions(holder.ivAvatar, support.user_avatar,
                R.drawable.person_avatar_default_round, R.drawable.person_avatar_default_round);
        holder.tvUserName.setText(support.user_name);
        holder.tvUserID.setText(support.user_id);
        holder.tvUserCity.setText(support.user_city);
        return convertView;
    }

    private class ViewHolder{
        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public TextView tvUserID;
        public TextView tvUserCity;
    }
}
