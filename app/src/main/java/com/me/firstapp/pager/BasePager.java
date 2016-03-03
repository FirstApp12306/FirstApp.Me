package com.me.firstapp.pager;

import android.view.View;
import android.widget.FrameLayout;

import com.me.firstapp.R;
import com.me.firstapp.activity.MainActivity;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class BasePager {
    public MainActivity mActivity;
    public View mRootView;// 布局对象
    public FrameLayout flContent;

    public BasePager(MainActivity activity) {
        mActivity = activity;
        initViews();
    }

    /**
     * 初始化布局
     */
    public void initViews() {
        mRootView = View.inflate(mActivity, R.layout.pager_base, null);
        flContent = (FrameLayout) mRootView.findViewById(R.id.pager_base_content);
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }
}
