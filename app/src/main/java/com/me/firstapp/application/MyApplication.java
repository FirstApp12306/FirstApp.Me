package com.me.firstapp.application;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

import cn.smssdk.SMSSDK;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //xUtils初始化
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志

        //Mob短信SDK初始化
        SMSSDK.initSDK(this, "10035105a5291", "3fd5b3308ebc6ab7cf1f617b90b88eca");
    }
}
