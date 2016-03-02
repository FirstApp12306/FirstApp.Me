package com.me.firstapp.application;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

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
    }
}
