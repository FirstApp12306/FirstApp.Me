package com.me.firstapp.application;

import android.app.Application;
import android.util.Log;

import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.receiver.MsgNotificationClickEventReceiver;
import com.me.firstapp.utils.PrefUtils;

import org.xutils.BuildConfig;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.smssdk.SMSSDK;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class MyApplication extends Application {

    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    public static final String FIND_TOPICS_REFRESH_FLAG = "FIND_TOPICS_REFRESH_FLAG";//刷新发现话题页面的标志
    public static final String NEW_TOPICS_REFRESH_FLAG = "NEW_TOPICS_REFRESH_FLAG";//刷新最新话题页面的标志
    public static final String FIRST_PAGER_REFRESH_FLAG = "FIRST_PAGER_REFRESH_FLAG";//首页刷新标志
    public static final String FIND_PAGER_REFRESH_FLAG = "FIND_PAGER_REFRESH_FLAG";//精选页刷新标志

    @Override
    public void onCreate() {
        super.onCreate();

        //xUtils初始化
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志

        //Mob短信SDK初始化
        SMSSDK.initSDK(this, "10035105a5291", "3fd5b3308ebc6ab7cf1f617b90b88eca");

        //JMessage初始化
        JMessageClient.init(getApplicationContext());
        JPushInterface.setDebugMode(true);

        new MsgNotificationClickEventReceiver(this);
    }
}
