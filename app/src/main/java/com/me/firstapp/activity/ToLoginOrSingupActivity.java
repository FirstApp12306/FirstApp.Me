package com.me.firstapp.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.me.firstapp.R;
import com.me.firstapp.view.FullScreenVideoView;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_to_login_or_signup)
public class ToLoginOrSingupActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.activity_to_login_or_signup_video)
    private FullScreenVideoView mVideoView;
    @ViewInject(R.id.activity_to_login_or_signup_btn_login)
    private Button btnLogin;
    @ViewInject(R.id.activity_to_login_or_signup_btn_signup)
    private Button btnSignUp;
    @ViewInject(R.id.activity_to_login_or_signup_viewpager)
    private ViewPager mViewPager;
    @ViewInject(R.id.activity_to_login_or_signup_indicator)
    private CirclePageIndicator mIndicator;

    private String uri;
    private ArrayList<View> views = new ArrayList<>();
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();

    }

    private void initViews(){
        View view0 = View.inflate(this, R.layout.view_to_login_view_pager, null);
        TextView textViewTitle0 = (TextView) view0.findViewById(R.id.view_to_login_view_pager_title);
        TextView textViewText0 = (TextView) view0.findViewById(R.id.view_to_login_view_pager_text);
        textViewTitle0.setText("舒舒服服vvv生生世世事实上烦烦烦");
        textViewText0.setText("是多少带宽坎坎坷坷坎坎坷坷坎坎坷坷靠靠靠靠靠");
        views.add(0, view0);

        View view1 = View.inflate(this, R.layout.view_to_login_view_pager, null);
        TextView textViewTitle1 = (TextView) view1.findViewById(R.id.view_to_login_view_pager_title);
        TextView textViewText1 = (TextView) view1.findViewById(R.id.view_to_login_view_pager_text);
        textViewTitle1.setText("世世事实上烦烦烦");
        textViewText1.setText("是多少带宽坎坎坷坷坎坎坷");
        views.add(0, view1);

        View view2 = View.inflate(this, R.layout.view_to_login_view_pager, null);
        TextView textViewTitle2 = (TextView) view2.findViewById(R.id.view_to_login_view_pager_title);
        TextView textViewText2 = (TextView) view2.findViewById(R.id.view_to_login_view_pager_text);
        textViewTitle2.setText("实上烦烦烦");
        textViewText2.setText("是多少带宽坎坎坷，坷坎坎坷坷坎，坎坷坷靠靠靠靠靠");
        views.add(0, view2);

        View view3 = View.inflate(this, R.layout.view_to_login_view_pager, null);
        TextView textViewTitle3 = (TextView) view3.findViewById(R.id.view_to_login_view_pager_title);
        TextView textViewText3 = (TextView) view3.findViewById(R.id.view_to_login_view_pager_text);
        textViewTitle3.setText("对方的方法");
        textViewText3.setText("是多少带宽坎坎坷？坷坎坎坷坷坎坎坷坷靠靠靠靠靠？");
        views.add(0, view3);

        mViewPager.setAdapter(new ViewPagerAdapter());
        mIndicator.setViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        uri = "android.resource://" + getPackageName() + "/" + R.raw.to_login_art;
        mVideoView.setVideoURI(Uri.parse(uri));
        mVideoView.start();

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.setVideoURI(Uri.parse(uri));
                mVideoView.start();
            }
        });

        // 自动轮播条显示
        if (mHandler == null) {
            mHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    int currentItem = mViewPager.getCurrentItem();

                    if (currentItem < views.size() - 1) {
                        currentItem++;
                    } else {
                        currentItem = 0;
                    }

                    mViewPager.setCurrentItem(currentItem);// 切换到下一个页面
                    mHandler.sendEmptyMessageDelayed(0, 3000);// 继续延时3秒发消息,
                    // 形成循环
                };
            };
            mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒后发消息
        }

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_to_login_or_signup_btn_login :
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.activity_to_login_or_signup_btn_signup :
                Intent intent2 = new Intent(this, SignUpActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    private class ViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
