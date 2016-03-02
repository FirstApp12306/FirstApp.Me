package com.me.firstapp.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by FirstApp.Me on 2016/3/1.
 */
public class NewUserGuideAdapter extends PagerAdapter{
    private int[] mImageIds;
    private ArrayList<ImageView> mImageViewList;

    public NewUserGuideAdapter(int[] mImageIds, ArrayList<ImageView> mImageViewList){
        this.mImageIds = mImageIds;
        this.mImageViewList = mImageViewList;
    }

    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mImageViewList.get(position));
        return mImageViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
