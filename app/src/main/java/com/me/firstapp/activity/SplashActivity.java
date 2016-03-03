package com.me.firstapp.activity;

import android.content.Intent;
import android.os.Bundle;

import com.me.firstapp.R;
import com.me.firstapp.utils.PrefUtils;

import org.xutils.view.annotation.ContentView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                jumpNextPage();
            }
        };
        timer.schedule(task, 1000*3);
    }

    /**
     * 跳转下一个页面的方法
     */
    private void jumpNextPage() {
        // 判断之前有没有显示过新手引导
        boolean userGuide = PrefUtils.getBoolean(this, "is_user_guide_showed", false);
        if (!userGuide) {
            // 跳转到新手引导页
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
