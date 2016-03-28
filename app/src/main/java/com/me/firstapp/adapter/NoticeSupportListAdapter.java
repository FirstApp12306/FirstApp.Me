package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.MyComment;
import com.me.firstapp.entity.MySupport;
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
public class NoticeSupportListAdapter extends BaseAdapter {
    private Context context;
    private Activity mActivity;
    private ArrayList<MySupport> mySupports;
    public NoticeSupportListAdapter(Context context, ArrayList<MySupport> mySupports) {
        this.context = context;
        this.mActivity = (Activity) context;
        this.mySupports = mySupports;
    }

    @Override
    public int getCount() {
        return mySupports.size();
    }

    @Override
    public Object getItem(int position) {
        return mySupports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void doNotify(){
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void addMore(ArrayList<MySupport> moreSupports){
        this.mySupports.addAll(moreSupports);
        doNotify();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.notice_support_list_item, null);
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.notice_support_list_item_avatar);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.notice_support_list_item_user_name);
            holder.tvTime = (TextView) convertView.findViewById(R.id.notice_support_list_item_time);
            holder.ivNoteImage = (ImageView) convertView.findViewById(R.id.notice_support_list_item_note_image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        MySupport mySupport = mySupports.get(position);

        ImageOptions imageOptions1 = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(true)
                        // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                        //.setUseMemCache(false)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
        x.image().bind(holder.ivAvatar, mySupport.support_user_avatar, imageOptions1);
        holder.tvUserName.setText(mySupport.support_user_name);
        holder.tvTime.setText(mySupport.time_stamp);
        if ("#".equals(mySupport.note_image) || TextUtils.isEmpty(mySupport.note_image)){
            holder.ivNoteImage.setVisibility(View.GONE);
        }else {
            ImageOptions imageOptions2 = new ImageOptions.Builder()
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    // 默认自动适应大小
                    // .setSize(...)
                    .setIgnoreGif(true)
                            // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                            //.setUseMemCache(false)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
            x.image().bind(holder.ivNoteImage, mySupport.note_image, imageOptions2);
        }

        return convertView;
    }

    private class ViewHolder{
        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public TextView tvTime;
        public ImageView ivNoteImage;
    }
}
