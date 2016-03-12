package com.me.firstapp.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.me.firstapp.pager.TopicsBasePager;
import com.me.firstapp.view.RefreshListView;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class TopicsViewPagerAdapter extends PagerAdapter {

    private ArrayList<TopicsBasePager> mPagerList;

    public TopicsViewPagerAdapter(ArrayList<TopicsBasePager> mPagerList){
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
        TopicsBasePager pager = mPagerList.get(position);
        container.addView(pager.mRootView);
        return pager.mRootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
