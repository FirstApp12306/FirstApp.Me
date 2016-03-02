package com.me.firstapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.me.firstapp.manager.ActivityManager;

/**
 * Created by FirstApp.Me on 2016/3/1.
 */
public class BaseActivity extends Activity {
    private ActivityManager activityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
        activityManager = ActivityManager.getInstance();
        activityManager.pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityManager.popActivity(this);
    }
}
