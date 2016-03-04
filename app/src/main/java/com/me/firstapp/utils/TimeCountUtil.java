package com.me.firstapp.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class TimeCountUtil extends CountDownTimer {

    private Activity mActivity;
    private Button btn;

    public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, Button btn){
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn = btn;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);
        btn.setText(millisUntilFinished / 1000 + "秒后可重新发送");
        //倒计时时间显示为红色
        Spannable span = new SpannableString(btn.getText().toString());
        span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        btn.setText(span);
    }

    @Override
    public void onFinish() {
        btn.setText("重新获取验证码");
        btn.setClickable(true);
    }
}
