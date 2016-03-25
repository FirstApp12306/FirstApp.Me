package com.me.firstapp.pager;

import android.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.utils.DensityUtils;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class BasePager {
    public MainActivity mActivity;
    public View mRootView;// 布局对象
    public FrameLayout flContent;
    public RelativeLayout mRl;
    public Button btnCreateTopic;
    public RadioGroup mRadioGroup;
    public RadioButton rbtLeft;
    public RadioButton rbtRight;
    public TextView redCircle;

    public BasePager(MainActivity activity) {
        mActivity = activity;
        initViews();
    }

    /**
     * 初始化布局
     */
    public void initViews() {
        mRootView = View.inflate(mActivity, R.layout.pager_base, null);
        flContent = (FrameLayout) mRootView.findViewById(R.id.pager_base_content);
        mRl = (RelativeLayout) mRootView.findViewById(R.id.pager_base_rl);
        btnCreateTopic = (Button) mRootView.findViewById(R.id.pager_base_btn_create_topic);
        mRadioGroup = (RadioGroup) mRootView.findViewById(R.id.pager_base_rg);
        rbtLeft = (RadioButton) mRootView.findViewById(R.id.pager_base_rbtn_left);
        rbtRight = (RadioButton) mRootView.findViewById(R.id.pager_base_rbtn_right);
        redCircle = (TextView) mRootView.findViewById(R.id.pager_base_red_circle);
        mRadioGroup.check(R.id.pager_base_rbtn_left);

        mRadioGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                FrameLayout.LayoutParams msgParams = new FrameLayout.LayoutParams(DensityUtils.dp2px(mActivity, 10), DensityUtils.dp2px(mActivity, 10));
                msgParams.leftMargin = mRadioGroup.getChildAt(1).getLeft()*2;
////                msgParams.topMargin = mRadioGroup.getChildAt(1).getTop()*3/2;
                redCircle.setLayoutParams(msgParams);
                redCircle.setGravity(Gravity.CENTER_VERTICAL);
            }
        });
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }
}
