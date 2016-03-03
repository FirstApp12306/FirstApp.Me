package com.me.firstapp.pager;

import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.utils.LogUtils;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class TopicPager extends BasePager {
    public TopicPager(MainActivity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public void initData() {
        LogUtils.d(FindPager.class, "初始化Topic页面数据。。。。。。。");
        TextView textView = new TextView(mActivity);
        textView.setText("Topic页面");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);

        flContent.removeAllViews();
        flContent.addView(textView);// 向FrameLayout中动态添加布局
    }
}
