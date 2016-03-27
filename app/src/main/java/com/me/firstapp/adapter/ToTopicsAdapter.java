package com.me.firstapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.me.firstapp.R;
import com.me.firstapp.activity.TopicNoteActivity;
import com.me.firstapp.entity.Topic;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.LogUtils;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
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
        final Topic topic = mTopTopics.get(position);
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
        x.image().bind(image, topic.image_url, imageOptions);
        container.addView(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleItemClick(topic.topic_key);
                Intent intent = new Intent(context, TopicNoteActivity.class);
                intent.putExtra("topic_key", topic.topic_key);
                intent.putExtra("topic_title", topic.topic_title);
                context.startActivity(intent);
            }
        });
        return image;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void handleItemClick(String topic_key){
        RequestParams params = new RequestParams(GlobalContants.TOPIC_BROWSE_COUNT_URL);
        params.addQueryStringParameter("topic_key", topic_key);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
