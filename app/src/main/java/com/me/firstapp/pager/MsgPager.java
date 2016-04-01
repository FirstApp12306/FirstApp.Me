package com.me.firstapp.pager;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.activity.ChatActivity;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.activity.notices.NoticeCommentActivity;
import com.me.firstapp.activity.notices.NoticeFansActivity;
import com.me.firstapp.activity.notices.NoticeSupportActivity;
import com.me.firstapp.adapter.ConversationListAdapter;
import com.me.firstapp.adapter.MsgViewPagerAdapter;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.utils.SortConvList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

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
    private LinearLayout llComment;
    private LinearLayout llLike;
    private LinearLayout llFans;
    private LinearLayout llFirstApp;
    private TextView tvNewCommentNum;
    private ImageView ivCommentArrow;
    private TextView tvNewSupportNum;
    private ImageView ivSupportArrow;
    private TextView tvNewFansNum;
    private ImageView ivFansArrow;
    private MainActivity activity;//必须用传过来的activity，不能用mActivity

    private List<Conversation> convDatas = new ArrayList<Conversation>();//私信列表数据
    private ConversationListAdapter convAdapter;

    public MsgPager(MainActivity activity) {
        super(activity);
        this.activity = activity;
        rbtLeft.setText("私信");
        rbtRight.setText("提醒");
    }

    @Override
    public void initViews() {
        super.initViews();
        btnSetting.setVisibility(View.GONE);
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

        llComment = (LinearLayout) noticeView.findViewById(R.id.view_pager_msg_notice_comment);
        llLike = (LinearLayout) noticeView.findViewById(R.id.view_pager_msg_notice_like);
        llFans = (LinearLayout) noticeView.findViewById(R.id.view_pager_msg_notice_fans);
        llFirstApp = (LinearLayout) noticeView.findViewById(R.id.view_pager_msg_notice_firstapp);

        tvNewCommentNum = (TextView) noticeView.findViewById(R.id.view_pager_msg_notice_comment_new_num);
        ivCommentArrow = (ImageView) noticeView.findViewById(R.id.view_pager_msg_notice_comment_arrow);
        tvNewSupportNum = (TextView) noticeView.findViewById(R.id.view_pager_msg_notice_support_new_num);
        ivSupportArrow = (ImageView) noticeView.findViewById(R.id.view_pager_msg_notice_support_arrow);
        tvNewFansNum = (TextView) noticeView.findViewById(R.id.view_pager_msg_notice_fans_new_num);
        ivFansArrow = (ImageView) noticeView.findViewById(R.id.view_pager_msg_notice_fans_arrow);

        setNum();

        mViewList.add(convView);
        mViewList.add(noticeView);
        mViewPager.setAdapter(new MsgViewPagerAdapter(mViewList));
        mViewPager.addOnPageChangeListener(new ViewPagerListener());
        mRadioGroup.check(R.id.pager_base_rbtn_left);//默认勾选第一个
        mRadioGroup.setOnCheckedChangeListener(new RadioCheckedChangeListener());

        initConvListAdapter();
        convListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (convAdapter != null) {
//                    convAdapter.setUnReadCount(convDatas.get(position));
//                }
                Intent intent = new Intent(mActivity, ChatActivity.class);
                intent.putExtra("targetID", ((UserInfo) convDatas.get(position).getTargetInfo()).getUserName());
                mActivity.startActivity(intent);
            }
        });

        //接收消息监听
        activity.setOnReceiveMsgListener(new MainActivity.OnReceiveMsgListener() {
            @Override
            public void receiveMsg(Message msg) {
                if (convAdapter != null) {
//                    convAdapter.setToTop(msg.getTargetID());
                    convDatas = JMessageClient.getConversationList();
                    convAdapter.refreshConv(convDatas);
                }
            }
        });
        //接收评论监听
        activity.setOnReceiveNewCommentListener(new MainActivity.OnReceiveNewCommentListener() {
            @Override
            public void receiveComment(String extraMsg, String extraExtra) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        redCircle.setVisibility(View.VISIBLE);
                        setNum();
                    }
                });
            }
        });

        //刷新会话列表监听
        activity.setOnRefreshConvListener(new MainActivity.OnRefreshConvListener() {
            @Override
            public void refreshConv() {
                if (convAdapter != null) {
                    convDatas = JMessageClient.getConversationList();
                    convAdapter.refreshConv(convDatas);
                }
            }
        });

        //重置消息数量监听
        activity.setOnResetNewMsgListener(new MainActivity.OnResetNewMsgListener() {
            @Override
            public void resetNewMsgNum(Conversation conv) {
                if (convAdapter != null) {
                    convAdapter.setUnReadCount(conv);
                }
            }
        });

        //接收点赞监听
        activity.setOnReceiveSupportListener(new MainActivity.OnReceiveSupportListener() {
            @Override
            public void receiveSupport(String extraMsg, String extraExtra) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        redCircle.setVisibility(View.VISIBLE);
                        setNum();
                    }
                });
            }
        });

        //接收新粉丝监听
        activity.setOnReceiveFansListener(new MainActivity.OnReceiveFansListener() {
            @Override
            public void receiveFans(String extraMsg, String extraExtra) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        redCircle.setVisibility(View.VISIBLE);
                        setNum();
                    }
                });
            }
        });

        setClick();
        flContent.removeAllViews();
        flContent.addView(view);// 向FrameLayout中动态添加布局
    }

    //设置各个提示的数量
    private void setNum(){
        String newCommentNum = PrefUtils.getString(mActivity, "new_comment_num", "0");
        String newSupportNum = PrefUtils.getString(mActivity, "new_support_num", "0");
        String newFansNum = PrefUtils.getString(mActivity, "new_fans_num", "0");
        if ("0".equals(newCommentNum)){
            tvNewCommentNum.setVisibility(View.GONE);
            ivCommentArrow.setVisibility(View.VISIBLE);
        }else {
            ivCommentArrow.setVisibility(View.GONE);
            tvNewCommentNum.setText(newCommentNum);
            tvNewCommentNum.setVisibility(View.VISIBLE);
        }
        if ("0".equals(newSupportNum)){
            tvNewSupportNum.setVisibility(View.GONE);
            ivSupportArrow.setVisibility(View.VISIBLE);
        }else{
            ivSupportArrow.setVisibility(View.GONE);
            tvNewSupportNum.setText(newSupportNum);
            tvNewSupportNum.setVisibility(View.VISIBLE);
        }
        if ("0".equals(newFansNum)){
            tvNewFansNum.setVisibility(View.GONE);
            ivFansArrow.setVisibility(View.VISIBLE);
        }else{
            ivFansArrow.setVisibility(View.GONE);
            tvNewFansNum.setText(newFansNum);
            tvNewFansNum.setVisibility(View.VISIBLE);
        }

        if ("0".equals(newCommentNum) && "0".equals(newSupportNum) && "0".equals(newFansNum)){
            redCircle.setVisibility(View.GONE);
        }
    }

    private void setClick(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.view_pager_msg_notice_comment :
                        mActivity.startActivity(new Intent(mActivity, NoticeCommentActivity.class));
                        PrefUtils.setString(mActivity, "new_comment_num", "0");
                        setNum();
                        break;
                    case R.id.view_pager_msg_notice_like :
                        mActivity.startActivity(new Intent(mActivity, NoticeSupportActivity.class));
                        PrefUtils.setString(mActivity, "new_support_num", "0");
                        setNum();
                        break;
                    case R.id.view_pager_msg_notice_fans :
                        mActivity.startActivity(new Intent(mActivity, NoticeFansActivity.class));
                        PrefUtils.setString(mActivity, "new_fans_num", "0");
                        setNum();
                        break;
                    case R.id.view_pager_msg_notice_firstapp :
                        break;
                }
            }
        };
        llComment.setOnClickListener(listener);
        llLike.setOnClickListener(listener);
        llFans.setOnClickListener(listener);
        llFirstApp.setOnClickListener(listener);
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
                case R.id.pager_base_rbtn_left :
                    mViewPager.setCurrentItem(0,true);
                    break;
                case R.id.pager_base_rbtn_right :
                    mViewPager.setCurrentItem(1,true);
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
                    mRadioGroup.check(R.id.pager_base_rbtn_left);
                    break;
                case 1 :
                    mRadioGroup.check(R.id.pager_base_rbtn_right);
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
