package com.me.firstapp.pager;

import android.content.Intent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.activity.LoginActivity;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.activity.SignUpActivity;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.MyScrollView;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class PersonPager extends BasePager {

    private View view;
    private Button btnLogin;
    private Button btnSignUp;
    private Button btnSetting;
    private ImageView avatar;
    private Button btnEditData;
    private EditText nickName;
    private EditText level;
    private EditText userID;
    private EditText location;
    private TextView attention;
    private Button btnFav;
    private TextView fans;
    private TextView signature;

    //测试控件
    private TextView pager_person_test;
    private MyScrollView myScrollView;
    private LinearLayout pager_person_ll_one;
    private LinearLayout pager_person_ll_two;
    private LinearLayout pager_person_ll_layout;
    private LinearLayout pager_person_ll_three;
    private Button pager_person_btn_topic;
    private int height;

    public PersonPager(MainActivity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
        mRl.setVisibility(View.GONE);//让基础页面的标题栏消失
    }

    @Override
    public void initData() {
        LogUtils.d("", "初始化Person页面数据。。。。。。。");
        boolean isLogin = PrefUtils.getBoolean(mActivity, "login_flag", false);
        if (isLogin){
            view = View.inflate(mActivity, R.layout.pager_person, null);
            pager_person_test = (TextView) view.findViewById(R.id.pager_person_test);
            myScrollView = (MyScrollView) view.findViewById(R.id.pager_person_scrollview);
            pager_person_ll_one = (LinearLayout) view.findViewById(R.id.pager_person_ll_one);
            pager_person_ll_two = (LinearLayout) view.findViewById(R.id.pager_person_ll_two);
            pager_person_ll_layout = (LinearLayout) view.findViewById(R.id.pager_person_ll_layout);
//            pager_person_btn_topic = (Button) view.findViewById(R.id.pager_person_btn_topic);
//            pager_person_ll_three = (LinearLayout) view.findViewById(R.id.pager_person_ll_three);

            //height = pager_person_ll_layout.getBottom();

            mActivity.setOnMyWindowFocusChangedListener(new MainActivity.OnMyWindowFocusChanged() {
                @Override
                public void onWindowFocusChanged(boolean hasFocus) {
                    LogUtils.d("hasFocus",hasFocus+"");
                }
            });


        }else{
            view = View.inflate(mActivity, R.layout.pager_unlogin_tip, null);
            btnLogin = (Button) view.findViewById(R.id.pager_unlogin_btn_login);
            btnSignUp = (Button) view.findViewById(R.id.pager_unlogin_btn_regest);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.pager_unlogin_btn_login:
                            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                            break;
                        case R.id.pager_unlogin_btn_regest:
                            mActivity.startActivity(new Intent(mActivity, SignUpActivity.class));
                            break;
                        default:
                            break;
                    }
                }
            };
            btnLogin.setOnClickListener(listener);
            btnSignUp.setOnClickListener(listener);
        }

        flContent.removeAllViews();
        flContent.addView(view);// 向FrameLayout中动态添加布局
    }


}
