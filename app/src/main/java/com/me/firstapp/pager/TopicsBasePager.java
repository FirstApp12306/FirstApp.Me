package com.me.firstapp.pager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.application.MyApplication;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.RefreshListView;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class TopicsBasePager {
    public MainActivity mActivity;
    public View mRootView;// 布局对象
    public RefreshListView mListView;

    public TopicsBasePager(MainActivity mActivity){
        this.mActivity = mActivity;
        initViews();
    }

    public void initViews(){
        mRootView = View.inflate(mActivity, R.layout.pager_base_topic, null);
        mListView = (RefreshListView) mRootView.findViewById(R.id.pager_base_topic_listview);
    }

    public void initData() {

    }
}
