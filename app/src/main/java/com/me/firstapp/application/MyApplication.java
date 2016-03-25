package com.me.firstapp.application;

import android.app.Application;
import android.util.Log;

import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.receiver.MsgNotificationClickEventReceiver;

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

    @Override
    public void onCreate() {
        super.onCreate();

        //xUtils初始化
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志

        //FirstApp服务初始化
//        firstAppInit();

        //Mob短信SDK初始化
        SMSSDK.initSDK(this, "10035105a5291", "3fd5b3308ebc6ab7cf1f617b90b88eca");

        //JMessage初始化
        JMessageClient.init(getApplicationContext());
        JPushInterface.setDebugMode(true);

        new MsgNotificationClickEventReceiver(this);
    }

//    private void firstAppInit(){
//        RequestParams params = new RequestParams(GlobalContants.SERVER_LOGIN_URL);
//        params.addQueryStringParameter("userno", GlobalContants.SERVER_USER_NO);
//        params.addQueryStringParameter("pwd", GlobalContants.SERVER_USER_PASSWORD);
//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Log.d("FirstApp.Me","success");
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//    }
}
