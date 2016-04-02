package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.me.firstapp.R;
import com.me.firstapp.entity.Support;
import com.me.firstapp.utils.ImageUtils;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class SupportAvatarGridViewAdapter extends BaseAdapter {
    private Context context;
    private Activity mActivity;
    private ArrayList<Support> supports;

    public SupportAvatarGridViewAdapter(Context context, ArrayList<Support> supports) {
        this.context = context;
        this.mActivity = (Activity) context;
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
            convertView = View.inflate(context, R.layout.comment_list_header_grid_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.comment_list_header_grid_item_avatar);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Support support = supports.get(position);
        ImageUtils.bindImageWithOptions(holder.image, support.user_avatar,
                R.drawable.person_avatar_default_round, R.drawable.person_avatar_default_round);
        return convertView;
    }

    private class ViewHolder{
        public ImageView image;
    }

}
