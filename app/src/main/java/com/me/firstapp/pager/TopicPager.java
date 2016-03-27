package com.me.firstapp.pager;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.me.firstapp.R;
import com.me.firstapp.activity.CreateTopicFirstActivity;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.adapter.TopicsViewPagerAdapter;
import com.me.firstapp.utils.LogUtils;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class TopicPager extends BasePager {

    private View view;
    private ViewPager mViewPager;
    private ArrayList<TopicsBasePager> mPagerList;

    public TopicPager(MainActivity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
        btnCreateTopic.setVisibility(View.VISIBLE);
        btnCreateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CreateTopicFirstActivity.class);
                mActivity.startActivity(intent);
            }
        });

        rbtLeft.setText("发现");
        rbtRight.setText("最新");
    }

    @Override
    public void initData() {
        LogUtils.d("fff", "初始化Topic页面数据。。。。。。。");
        view = View.inflate(mActivity, R.layout.pager_topic, null);
        mViewPager = (ViewPager) view.findViewById(R.id.pager_topic_viewpager);
        mPagerList = new ArrayList<>();
        mPagerList.add(new TopicsFindPager(mActivity));
        mPagerList.add(new TopicsNewPager(mActivity));
        mViewPager.setAdapter(new TopicsViewPagerAdapter(mPagerList));
        mViewPager.addOnPageChangeListener(new TopicViewPagerListener());
        mRadioGroup.setOnCheckedChangeListener(new TopicRadioCheckedChangeListener());
        mPagerList.get(0).initData();//初始化首页数据

        flContent.removeAllViews();
        flContent.addView(view);// 向FrameLayout中动态添加布局

    }

    class TopicRadioCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.pager_base_rbtn_left :
                    mViewPager.setCurrentItem(0,false);
                    break;
                case R.id.pager_base_rbtn_right :
                    mViewPager.setCurrentItem(1,false);
                    break;
            }
        }
    }

    class TopicViewPagerListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            LogUtils.d("position", position+"");
            switch (position){
                case 0 :
                    mRadioGroup.check(R.id.pager_base_rbtn_left);
                    break;
                case 1 :
                    mRadioGroup.check(R.id.pager_base_rbtn_right);
                    break;
            }
            mPagerList.get(position).initData();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
