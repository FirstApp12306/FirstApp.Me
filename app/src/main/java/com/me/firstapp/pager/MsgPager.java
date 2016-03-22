package com.me.firstapp.pager;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.me.firstapp.R;
import com.me.firstapp.activity.ChatActivity;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.adapter.ConversationListAdapter;
import com.me.firstapp.adapter.MsgViewPagerAdapter;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.SortConvList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class MsgPager extends BasePager {
    private View view;
    private ViewPager mViewPager;
    private ArrayList<View> mViewList;
    private ListView convListView;

    private List<Conversation> convDatas = new ArrayList<Conversation>();//私信列表数据
    private ConversationListAdapter convAdapter;

    public MsgPager(MainActivity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public void initData() {
        LogUtils.d("", "初始化Msg页面数据。。。。。。。");
        view = View.inflate(mActivity, R.layout.pager_message, null);
        mViewPager = (ViewPager) view.findViewById(R.id.pager_message_viewpager);
        mViewList = new ArrayList<>();
        View convView = View.inflate(mActivity, R.layout.view_pager_message_conversation, null);
        View noticeView = View.inflate(mActivity, R.layout.view_pager_message_notice, null);
        convListView = (ListView) convView.findViewById(R.id.view_pager_message_conv_listview);
        mViewList.add(convView);
        mViewList.add(noticeView);
        mViewPager.setAdapter(new MsgViewPagerAdapter(mViewList));
        mViewPager.addOnPageChangeListener(new ViewPagerListener());
        mRadioGroup.setOnCheckedChangeListener(new RadioCheckedChangeListener());

        initConvListAdapter();
        convListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, ChatActivity.class);
                intent.putExtra("targetID", ((UserInfo) convDatas.get(position).getTargetInfo()).getUserName());
                mActivity.startActivity(intent);
            }
        });


        flContent.removeAllViews();
        flContent.addView(view);// 向FrameLayout中动态添加布局
    }

    // 得到会话列表
    @SuppressWarnings("unchecked")
    private void initConvListAdapter() {
        convDatas = JMessageClient.getConversationList();
        //对会话列表进行时间排序
        if (convDatas.size() > 1) {
            SortConvList sortList = new SortConvList();
            Collections.sort(convDatas, sortList);
        }
        convAdapter = new ConversationListAdapter(mActivity, convDatas);
        convListView.setAdapter(convAdapter);
    }

    class RadioCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.pager_base_rbtn_find :
                    mViewPager.setCurrentItem(0,false);
                    break;
                case R.id.pager_base_rbtn_new :
                    mViewPager.setCurrentItem(1,false);
                    break;
            }
        }
    }

    class ViewPagerListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            LogUtils.d("position", position+"");
            switch (position){
                case 0 :
                    mRadioGroup.check(R.id.pager_base_rbtn_find);
                    break;
                case 1 :
                    mRadioGroup.check(R.id.pager_base_rbtn_new);
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
