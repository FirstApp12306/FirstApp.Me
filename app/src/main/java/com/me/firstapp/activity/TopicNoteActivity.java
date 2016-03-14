package com.me.firstapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.adapter.TopicNotesViewAdapter;
import com.me.firstapp.manager.ActivityManager;
import com.me.firstapp.utils.LogUtils;
import com.viewpagerindicator.TabPageIndicator;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_topic_notes)
public class TopicNoteActivity extends Activity implements ViewPager.OnPageChangeListener {
    @ViewInject(R.id.activity_topic_note_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_topic_note_tab_title)
    private TextView tvTitle;
    @ViewInject(R.id.activity_topic_note_tab_indicator)
    private TabPageIndicator mIndicator;
    @ViewInject(R.id.activity_topic_note_viewpager)
    private ViewPager mViewPager;
    @ViewInject(R.id.activity_topic_notes_img_btn)
    private ImageButton mImageButton;
    @ViewInject(R.id.activity_topic_notes_tv_put)
    private TextView mTextView;
    @ViewInject(R.id.activity_topic_notes_btn_send_note)
    private Button btnSendNote;

    private ActivityManager activityManager;

    private ArrayList<View> views = new ArrayList<>();;
    private String topicKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        activityManager = ActivityManager.getInstance();
        activityManager.pushActivity(this);

        topicKey = getIntent().getStringExtra("topic_key");
        LogUtils.d("topicKey", topicKey);
        //mIndicator.setOnPageChangeListener(this);


        views.add(View.inflate(this, R.layout.view_topic_notes_pager_new, null));
        views.add(View.inflate(this, R.layout.view_topic_notes_pager_hot, null));
        LogUtils.d("views", views.size() + "");

        mViewPager.setAdapter(new TopicNotesViewAdapter(views));
        mIndicator.setViewPager(mViewPager);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.activity_topic_note_btn_back :
                        break;
                    case R.id.activity_topic_notes_img_btn :
                        break;
                    case R.id.activity_topic_notes_tv_put :
                        Intent intent = new Intent(TopicNoteActivity.this, SendNoteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.activity_topic_notes_btn_send_note :
                        break;
                }
            }
        };
        btnBack.setOnClickListener(listener);
        mImageButton.setOnClickListener(listener);
        mTextView.setOnClickListener(listener);
        btnSendNote.setOnClickListener(listener);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityManager.popActivity(this);
    }
}
