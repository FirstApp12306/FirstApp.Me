package com.me.firstapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.Support;
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
public class NoteDetailListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Support> supports;
    private boolean typeFlag;//判断出入的是support数据还是comment数据

    public NoteDetailListAdapter(Context context, ArrayList<Support> supports, boolean typeFlag) {
        this.context = context;
        this.supports = supports;
        this.typeFlag = typeFlag;
    }

    @Override
    public int getCount() {
        if (typeFlag == true){
            return supports.size();
        }else{
            return  0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (typeFlag == true){
            return supports.get(position);
        }else{
            return  null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SupportViewHolder supportViewHolder = null;
        if (typeFlag == true){
            if (convertView == null){
                supportViewHolder = new SupportViewHolder();
                convertView = View.inflate(context, R.layout.note_detail_agree_pager_item, null);
                supportViewHolder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.note_detail_agree_pager_item_avatar);
                supportViewHolder.tvUserName = (TextView) convertView.findViewById(R.id.note_detail_agree_pager_item_user_name);
                supportViewHolder.tvUserID = (TextView) convertView.findViewById(R.id.note_detail_agree_pager_item_user_id);
                supportViewHolder.tvCity = (TextView) convertView.findViewById(R.id.note_detail_agree_pager_item_city);
                convertView.setTag(supportViewHolder);
            }else {
                supportViewHolder = (SupportViewHolder) convertView.getTag();
            }
        }else{

        }

        if (typeFlag == true){
            Support support = supports.get(position);
            ImageOptions imageOptions = new ImageOptions.Builder()
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    // 默认自动适应大小
                    // .setSize(...)
                    .setIgnoreGif(true)
                            // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                            //.setUseMemCache(false)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
            x.image().bind(supportViewHolder.ivAvatar, support.user_avatar, imageOptions);
            supportViewHolder.tvUserName.setText(support.user_name);
            supportViewHolder.tvUserID.setText("ID:"+support.user_id);
            supportViewHolder.tvCity.setText(support.user_city);
        }
        return convertView;
    }

    private class SupportViewHolder{
        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public TextView tvUserID;
        public TextView tvCity;
    }
}
