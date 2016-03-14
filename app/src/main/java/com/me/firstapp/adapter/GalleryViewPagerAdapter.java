package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class GalleryViewPagerAdapter extends PagerAdapter {

    private Context context;
    private Activity mActivity;
    private ArrayList<View> viewList;

    public GalleryViewPagerAdapter(Context context, ArrayList<View> viewList) {
        this.context = context;
        this.mActivity = (Activity)context;
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    //需要重写这个方法，才能删除并刷新item
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void removeItem(int position){
        viewList.remove(position);
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}
