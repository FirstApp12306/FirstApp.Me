package com.me.firstapp.pager;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.activity.RegestActivity;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class PersonPager extends BasePager {

    private View view;
    private Button btnLogin;
    private Button btnRegest;
    public PersonPager(MainActivity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public void initData() {
        LogUtils.d(FindPager.class, "初始化Person页面数据。。。。。。。");
//        TextView textView = new TextView(mActivity);
//        textView.setText("Person页面");
//        textView.setTextColor(Color.RED);
//        textView.setTextSize(25);
//        textView.setGravity(Gravity.CENTER);

        boolean isLogin = PrefUtils.getBoolean(mActivity, "login_flag", false);
        if (isLogin){

        }else{
            view = View.inflate(mActivity, R.layout.pager_unlogin_tip, null);
            btnLogin = (Button) view.findViewById(R.id.pager_unlogin_btn_login);
            btnRegest = (Button) view.findViewById(R.id.pager_unlogin_btn_regest);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.pager_unlogin_btn_login:
                            break;
                        case R.id.pager_unlogin_btn_regest:
                            Intent intent = new Intent(mActivity, RegestActivity.class);
                            mActivity.startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            };
            btnLogin.setOnClickListener(listener);
            btnRegest.setOnClickListener(listener);
        }

        flContent.removeAllViews();
        flContent.addView(view);// 向FrameLayout中动态添加布局
    }
}
