package com.me.firstapp.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.me.firstapp.R;
import com.me.firstapp.application.MyApplication;
import com.me.firstapp.fragment.ContentFragment;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    private static final String FRAGMENT_CONTENT = "fragment_content";
    private OnMyWindowFocusChanged onMyWindowFocusChanged;
    OnReceiveMsgListener mReceiveMsgListener;
    OnReceiveNewCommentListener mReceiveNewCommentListener;
    OnRefreshConvListener mOnRefreshConvListener;
    OnResetNewMsgListener mOnResetNewMsgListener;
    OnReceiveSupportListener mOnReceiveSupportListener;
    OnReceiveFansListener mOnReceiveFansListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initFragment();
        JMessageClient.registerEventReceiver(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (onMyWindowFocusChanged != null){
            onMyWindowFocusChanged.onWindowFocusChanged(hasFocus);
        }
    }

    public interface  OnMyWindowFocusChanged{
        void onWindowFocusChanged(boolean hasFocus);
    }

    public void setOnMyWindowFocusChangedListener(OnMyWindowFocusChanged onMyWindowFocusChanged){
        this.onMyWindowFocusChanged = onMyWindowFocusChanged;
    }

    /**
     * 初始化fragment, 将fragment数据填充给布局文件
     */
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();// 开启事务
        transaction.replace(R.id.fl_content, new ContentFragment(), FRAGMENT_CONTENT);
        transaction.commit();// 提交事务
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
        EventBus.getDefault().unregister(this);
//        PrefUtils.setBoolean(this, "topic_pager_init_flag0", false);
//        PrefUtils.setBoolean(this, "topic_pager_init_flag1", false);
        PrefUtils.setBoolean(this, MyApplication.FIND_TOPICS_REFRESH_FLAG, false);
        PrefUtils.setBoolean(this, MyApplication.NEW_TOPICS_REFRESH_FLAG, false);
        PrefUtils.setBoolean(this, MyApplication.FIRST_PAGER_REFRESH_FLAG, false);

    }

    public void onEvent(MessageEvent event) {
        LogUtils.d("MessageEvent", "MessageEvent");
        if (mReceiveMsgListener != null){
            mReceiveMsgListener.receiveMsg(event.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onUserEvent(Event.NewCommentEvent event) {
        if (mReceiveNewCommentListener != null){
            mReceiveNewCommentListener.receiveComment(event.getExtraMsg(), event.getExtraExtra());
        }
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onUserEvent(Event.NewSupportEvent event){
        if (mOnReceiveSupportListener != null){
            mOnReceiveSupportListener.receiveSupport(event.getExtraMsg(), event.getExtraExtra());
        }
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onUserEvent(Event.UpdateConvEvent event){
        if (mOnRefreshConvListener != null){
            mOnRefreshConvListener.refreshConv();
        }
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onUserEvent(Event.ResetNewMsgNumEvent event){
        if (mOnResetNewMsgListener != null){
            mOnResetNewMsgListener.resetNewMsgNum(event.getConv());
        }
    }

    public void onUserEvent(Event.NewFansEvent event){
        if (mOnReceiveFansListener != null){
            mOnReceiveFansListener.receiveFans(event.getExtraMsg(), event.getExtraExtra());
        }
    }

    //接受聊天消息监听

    public void setOnReceiveMsgListener(OnReceiveMsgListener listener){
        mReceiveMsgListener = listener;
    }

    public interface OnReceiveMsgListener {
        void receiveMsg(Message msg);
    }

    //接收评论监听
    public void setOnReceiveNewCommentListener(OnReceiveNewCommentListener listener){
        mReceiveNewCommentListener = listener;
    }

    public interface  OnReceiveNewCommentListener {
        void receiveComment(String extraMsg, String extraExtra);
    }

    //刷新会话列表监听
    public void setOnRefreshConvListener(OnRefreshConvListener listener){
        mOnRefreshConvListener = listener;
    }

    public interface OnRefreshConvListener {
        void refreshConv();
    }

    //重置新消息数量监听
    public void setOnResetNewMsgListener(OnResetNewMsgListener listener){
        mOnResetNewMsgListener = listener;
    }

    public interface OnResetNewMsgListener {
        void resetNewMsgNum(Conversation conv);
    }

    //接收点赞监听
    public void setOnReceiveSupportListener(OnReceiveSupportListener listener){
        mOnReceiveSupportListener = listener;
    }

    public interface OnReceiveSupportListener {
        void receiveSupport(String extraMsg, String extraExtra);
    }

    //接收新粉丝监听
    public void setOnReceiveFansListener(OnReceiveFansListener listener){
        mOnReceiveFansListener = listener;
    }

    public interface OnReceiveFansListener {
        void receiveFans(String extraMsg, String extraExtra);
    }
}
