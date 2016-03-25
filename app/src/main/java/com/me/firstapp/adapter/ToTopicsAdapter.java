package com.me.firstapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.me.firstapp.R;
import com.me.firstapp.entity.Topic;
import com.me.firstapp.utils.LogUtils;

import org.xutils.common.util.DensityUtil;
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
public class ToTopicsAdapter extends PagerAdapter {
    private ArrayList<Topic> mTopTopics;
    private Context context;
    private ImageOptions imageOptions;
    public ToTopicsAdapter(Context context, ArrayList<Topic> mTopTopics) {
        this.context = context;
        this.mTopTopics = mTopTopics;
    }

    @Override
    public int getCount() {
        return mTopTopics.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        imageOptions = new ImageOptions.Builder()
//                .setSize(DensityUtil.dip2px(300), DensityUtil.dip2px(200))
//                .setRadius(DensityUtil.dip2px(5))
//                .setLoadingDrawableId(R.drawable.common_bg_image_loading)
//                .setFailureDrawableId(R.drawable.common_bg_image_loadfail)
//                .build();
        ImageOptions imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(true)
                        // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                        //.setUseMemCache(false)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
        ImageView image = new ImageView(context);
//        image.setScaleType(ImageView.ScaleType.FIT_XY);// 基于控件大小填充图片
//        LogUtils.d("mTopTopicsimage_url", mTopTopics.get(position).image_url);
        x.image().bind(image, mTopTopics.get(position).image_url, imageOptions);
        container.addView(image);
        return image;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
