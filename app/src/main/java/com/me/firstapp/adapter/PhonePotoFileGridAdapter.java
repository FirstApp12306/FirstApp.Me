package com.me.firstapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.activity.PhoneAlbumActivity;
import com.me.firstapp.entity.images.ImageBucket;
import com.me.firstapp.entity.images.ImageItem;

import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class PhonePotoFileGridAdapter extends BaseAdapter {

    private Context context;

    public PhonePotoFileGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return PhoneAlbumActivity.contentList.size();
    }

    @Override
    public ImageBucket getItem(int position) {
        return PhoneAlbumActivity.contentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.phone_poto_file_grid_view_item, null);
            holder = new ViewHolder();
            holder.fileImage = (ImageView) convertView.findViewById(R.id.phone_poto_file_gridview_item_file_image);
            holder.folderName = (TextView) convertView.findViewById(R.id.phone_poto_file_gridview_item_file_name);
            holder.fileNum = (TextView) convertView.findViewById(R.id.phone_poto_file_gridview_item_file_num);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (PhoneAlbumActivity.contentList.get(position).imageList != null && !PhoneAlbumActivity.contentList.get(position).imageList.isEmpty()) {
            // 给folderName设置值为文件夹名称
            holder.folderName.setText(PhoneAlbumActivity.contentList.get(position).bucketName);
            // 给fileNum设置文件夹内图片数量
            holder.fileNum.setText("" + PhoneAlbumActivity.contentList.get(position).count);
            // 封面图片路径
            ImageItem item = PhoneAlbumActivity.contentList.get(position).imageList.get(0);// 封面显示第一张图片
            holder.fileImage.setTag(item.imagePath);
            //utils.display(holder.fileImage, item.imagePath);
            ImageOptions imageOptions = new ImageOptions.Builder()
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    // 默认自动适应大小
                    // .setSize(...)
                    .setIgnoreGif(true)
                            // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                            //.setUseMemCache(false)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
            x.image().bind(holder.fileImage, item.imagePath, imageOptions);
        } else {
            holder.fileImage.setImageResource(R.drawable.album_no_pictures);
        }

        return convertView;
    }

    private class ViewHolder {
        public ImageView fileImage;
        public TextView folderName;
        public TextView fileNum;
    }


}
