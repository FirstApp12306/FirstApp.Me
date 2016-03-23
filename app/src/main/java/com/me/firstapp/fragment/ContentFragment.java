package com.me.firstapp.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.adapter.ContentPagerAdapter;
import com.me.firstapp.pager.BasePager;
import com.me.firstapp.pager.FindPager;
import com.me.firstapp.pager.FirstPager;
import com.me.firstapp.pager.MsgPager;
import com.me.firstapp.pager.PersonPager;
import com.me.firstapp.pager.TopicPager;
import com.me.firstapp.utils.DensityUtils;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class ContentFragment extends BaseFragment {

    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private TextView redCircle;
    private ArrayList<BasePager> mPagerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_content_viewpager);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.fragment_content_radio_group);
        redCircle = (TextView) view.findViewById(R.id.fragment_content_red_circle);
        return view;
    }

    @Override
    public void initData() {
        mRadioGroup.check(R.id.fragment_content_rb_first_page);//默认勾选首页
        // 初始化子页面
        mPagerList = new ArrayList<BasePager>();
        mPagerList.add(new FirstPager(mActivity));
        mPagerList.add(new TopicPager(mActivity));
        mPagerList.add(new MsgPager(mActivity));
        mPagerList.add(new FindPager(mActivity));
        mPagerList.add(new PersonPager(mActivity));

        mViewPager.setAdapter(new ContentPagerAdapter(mPagerList));
        // 监听RadioGroup的选择事件
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.fragment_content_rb_first_page:
                        mViewPager.setCurrentItem(0,false);
                        break;
                    case R.id.fragment_content_rb_topic:
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.fragment_content_rb_msg:
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.fragment_content_rb_find:
                        mViewPager.setCurrentItem(3,false);
                        break;
                    case R.id.fragment_content_rb_person:
                        mViewPager.setCurrentItem(4,false);
                        break;
                    default:
                        break;
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPagerList.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPagerList.get(0).initData();//初始化首页数据

        mRadioGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                FrameLayout.LayoutParams msgParams = new FrameLayout.LayoutParams(DensityUtils.dp2px(mActivity, 10), DensityUtils.dp2px(mActivity, 10));
                msgParams.leftMargin = (mRadioGroup.getChildAt(2).getLeft())+((mRadioGroup.getChildAt(3).getLeft())-(mRadioGroup.getChildAt(2).getLeft()))*3/5;
                redCircle.setLayoutParams(msgParams);
                redCircle.setGravity(Gravity.CENTER);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
