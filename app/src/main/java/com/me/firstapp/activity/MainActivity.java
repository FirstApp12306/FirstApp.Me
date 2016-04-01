package com.me.firstapp.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.me.firstapp.R;
import com.me.firstapp.application.MyApplication;
import com.me.firstapp.fragment.ContentFragment;
import com.me.firstapp.manager.ActivityManager;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

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
    OnReceiveMsgListener mReceiveMsgListener;
    OnReceiveNewCommentListener mReceiveNewCommentListener;
    OnRefreshConvListener mOnRefreshConvListener;
    OnResetNewMsgListener mOnResetNewMsgListener;
    OnReceiveSupportListener mOnReceiveSupportListener;
    OnReceiveFansListener mOnReceiveFansListener;
    OnRefreshTopicsListener mOnRefreshTopicsListener;

    protected ActivityManager activityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManager = ActivityManager.getInstance();
        activityManager.pushActivity(this);
        x.view().inject(this);
        initFragment();
        JMessageClient.registerEventReceiver(this);
        EventBus.getDefault().register(this);
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
        PrefUtils.setBoolean(this, MyApplication.FIND_TOPICS_REFRESH_FLAG, false);
        PrefUtils.setBoolean(this, MyApplication.NEW_TOPICS_REFRESH_FLAG, false);
        PrefUtils.setBoolean(this, MyApplication.FIRST_PAGER_REFRESH_FLAG, false);
        PrefUtils.setBoolean(this, MyApplication.FIND_PAGER_REFRESH_FLAG, false);
        activityManager.popActivity(this);
    }


    private boolean isExit = false;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK :
                exit();
               break;
        }
        return false;
    }

    private void exit() {
        if(!isExit) {
            isExit = true;
            Toast.makeText(this, "再次点击将退出程序", Toast.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
        }
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

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onUserEvent(Event.RefreshTopicsEvent event){
        if (mOnRefreshTopicsListener != null){
            mOnRefreshTopicsListener.refreshTopics();
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

    //刷新话题监听
    public void setOnRefreshTopicsListener(OnRefreshTopicsListener listener){
        mOnRefreshTopicsListener = listener;
    }

    public interface OnRefreshTopicsListener {
        void refreshTopics();
    }
}
