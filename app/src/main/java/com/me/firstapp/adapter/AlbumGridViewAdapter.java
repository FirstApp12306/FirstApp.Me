package com.me.firstapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.me.firstapp.R;
import com.me.firstapp.entity.images.ImageItem;
import com.me.firstapp.utils.ImageUtils;

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
public class AlbumGridViewAdapter extends BaseAdapter {

    private ArrayList<ImageItem> imageItemList;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public AlbumGridViewAdapter( Context context, ArrayList<ImageItem> imageItemList) {
        this.imageItemList = imageItemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.phone_album_grid_view_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.phone_album_grid_view_item_image_view);
            viewHolder.chooseButton = (Button) convertView.findViewById(R.id.phone_album_grid_view_item_choose_btn);
            viewHolder.toggleButton = (ToggleButton) convertView.findViewById(R.id.phone_album_grid_view_item_toggle_button);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String path = "";
        if (imageItemList != null && position < imageItemList.size()) {
            path = imageItemList.get(position).imagePath;// 图片路径
        }
        ImageOptions imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(true)
                        // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                        //.setUseMemCache(false)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
        x.image().bind(viewHolder.imageView, path, imageOptions);

        viewHolder.toggleButton.setTag(position);
        viewHolder.chooseButton.setTag(position);
        viewHolder.toggleButton.setOnClickListener(new ToggleClickListener(viewHolder.chooseButton));

        if (ImageUtils.tempSelectedImg.contains(imageItemList.get(position))) {
            viewHolder.toggleButton.setChecked(true);
            viewHolder.chooseButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.toggleButton.setChecked(false);
            viewHolder.chooseButton.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ToggleClickListener implements View.OnClickListener {
        Button chooseBt;
        public ToggleClickListener(Button choosebt){
            this.chooseBt = choosebt;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) view;
                int position = (Integer) toggleButton.getTag();
                if (imageItemList != null && mOnItemClickListener != null && position < imageItemList.size()) {
                    mOnItemClickListener.onItemClick(toggleButton, position, toggleButton.isChecked(),chooseBt);
                }
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(ToggleButton toggleButton, int position, boolean isChecked,Button chooseBt);
    }

    private class ViewHolder {
        public ImageView imageView;
        public ToggleButton toggleButton;
        public Button chooseButton;
    }
}
