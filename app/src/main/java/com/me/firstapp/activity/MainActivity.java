package com.me.firstapp.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.me.firstapp.R;
import com.me.firstapp.fragment.ContentFragment;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Message;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initFragment();
        JMessageClient.registerEventReceiver(this);
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
        transaction.replace(R.id.fl_content, new ContentFragment(),FRAGMENT_CONTENT);
        transaction.commit();// 提交事务
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
        PrefUtils.setBoolean(this, "topic_pager_init_flag0", false);
        PrefUtils.setBoolean(this, "topic_pager_init_flag1", false);
    }

    public void onEvent(MessageEvent event) {
        if (mReceiveMsgListener != null){
            mReceiveMsgListener.receiveMsg(event.getMessage());
        }
    }

    //接受聊天消息监听

    public void setOnReceiveMsgListener(OnReceiveMsgListener listener){
        mReceiveMsgListener = listener;
    }

    public interface OnReceiveMsgListener {
        void receiveMsg(Message msg);
    }
}
