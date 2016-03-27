package com.me.firstapp.pager;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.utils.LogUtils;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class FirstPager extends  BasePager {

    public FirstPager(MainActivity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
        mRadioGroup.setVisibility(View.GONE);
        redCircle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("动态");
    }

    @Override
    public void initData() {
        LogUtils.d("", "初始化First页面数据。。。。。。。");
        TextView textView = new TextView(mActivity);
        textView.setText("First页面");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);

        flContent.removeAllViews();
        flContent.addView(textView);// 向FrameLayout中动态添加布局
    }
}
