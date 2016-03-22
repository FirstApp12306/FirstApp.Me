package com.me.firstapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.me.firstapp.manager.ActivityManager;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class BaseActivity extends AppCompatActivity {
    private ActivityManager activityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        activityManager = ActivityManager.getInstance();
        activityManager.pushActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityManager.popActivity(this);
    }
}
