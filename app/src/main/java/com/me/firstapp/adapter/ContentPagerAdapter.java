package com.me.firstapp.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.me.firstapp.pager.BasePager;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class ContentPagerAdapter extends PagerAdapter {

    private ArrayList<BasePager> mPagerList;

    public ContentPagerAdapter(ArrayList<BasePager> mPagerList){
        this.mPagerList = mPagerList;
    }

    @Override
    public int getCount() {
        return mPagerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager pager = mPagerList.get(position);
        container.addView(pager.mRootView);
        // pager.initData();// 初始化数据.... 不要放在此处初始化数据, 否则会预加载下一个页面
        return pager.mRootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
