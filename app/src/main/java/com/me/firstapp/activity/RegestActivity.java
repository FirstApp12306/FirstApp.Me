package com.me.firstapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.me.firstapp.R;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.TimeCountUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import cn.smssdk.utils.SMSLog;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
@ContentView(R.layout.activity_regest)
public class RegestActivity extends BaseActivity {

    @ViewInject(R.id.activity_regest_returnback)
    private ImageButton btnReturnBack;
    @ViewInject(R.id.activity_regest_phone)
    private EditText phonEditText;
    @ViewInject(R.id.activity_regest_btn_getcode)
    private Button codeButton;
    @ViewInject(R.id.activity_regest_verify_code)
    private EditText codeEditText;
    @ViewInject(R.id.activity_regest_btn_submit)
    private Button btnSubmit;

    public String phString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);

        //发送验证码
        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(phonEditText.getText().toString())) {
                    SMSSDK.getVerificationCode("86", phonEditText.getText().toString());
                    phString = phonEditText.getText().toString();
                    TimeCountUtil timeCountUtil = new TimeCountUtil(RegestActivity.this, 30000, 1000, codeButton);
                    timeCountUtil.start();
                } else {
                    Toast.makeText(RegestActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //提交验证码
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暂时不用提交，直接跳到完成注册页面
//                if (!TextUtils.isEmpty(codeEditText.getText().toString())) {
//                    SMSSDK.submitVerificationCode("86", phString, codeEditText.getText().toString());
//                }else{
//                    Toast.makeText(RegestActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
//                }

                    Intent intent = new Intent(RegestActivity.this, CompleteRegestActivity.class);
                    intent.putExtra("phone",phonEditText.getText().toString());
                    RegestActivity.this.startActivity(intent);
                    RegestActivity.this.finish();

            }
        });

        btnReturnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    static class MyHandler extends  Handler{
//        WeakReference<RegestActivity> mActivity;
//        MyHandler(RegestActivity activity){
//            mActivity = new WeakReference<RegestActivity>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            RegestActivity theActivity = mActivity.get();
//            int event = msg.arg1;
//            int result = msg.arg2;
//            Object data = msg.obj;
//            if (result == SMSSDK.RESULT_COMPLETE) {
//                System.out.println("--------result"+event);
//                //短信注册成功后，返回MainActivity,然后提示新好友
//                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
//                    Toast.makeText(theActivity, "提交验证码成功", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(theActivity, CompleteRegestActivity.class);
//                    theActivity.startActivity(intent);
//                    theActivity.finish();
//                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
//                    //已经验证
//                    Toast.makeText(theActivity, "验证码已经发送", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }else{
//                try {
//                    int status = 0;
//                    ((Throwable) data).printStackTrace();
//                    Throwable throwable = (Throwable) data;
//                    JSONObject object = new JSONObject(throwable.getMessage());
//                    String des = object.optString("detail");
//                    status = object.optInt("status");
//                    Log.d("detail","状态:"+status+" 描述:"+des);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg) {

            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            LogUtils.e(Handler.class, "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result"+event);
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(RegestActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegestActivity.this, CompleteRegestActivity.class);
                    startActivity(intent);
                    finish();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //已经验证
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();


                }

            } else {
                Log.d("loveyyyy","qqqqqqqq");
//				((Throwable) data).printStackTrace();
//				Toast.makeText(MainActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//					Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    Log.d("detail","状态:"+status+" 描述:"+des);
//                    if (!TextUtils.isEmpty(des)) {
//                        Toast.makeText(RegestActivity.this, des, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }


        };
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
