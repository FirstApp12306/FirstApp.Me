package com.me.firstapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.me.firstapp.R;
import com.me.firstapp.utils.ImageUtils;

import org.xutils.x;


/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class PubNoteGridViewAdapter extends BaseAdapter {
    private int selectedPosition = -1;
    private Context context;

    public PubNoteGridViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (ImageUtils.tempSelectedImg.size() == ImageUtils.MAX_PIC_NUM) {
            return ImageUtils.MAX_PIC_NUM;
        }
        return ImageUtils.tempSelectedImg.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.pub_note_grid_view_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.pub_note_grid_view_item_image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == ImageUtils.tempSelectedImg.size()) {
            holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
            if (position == ImageUtils.MAX_PIC_NUM) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            x.image().bind(holder.image, ImageUtils.tempSelectedImg.get(position).imagePath);
        }
        return convertView;
    }

    private class ViewHolder {
        public ImageView image;
    }
}
