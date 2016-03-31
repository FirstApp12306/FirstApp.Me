package com.me.firstapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.me.firstapp.R;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.TimeCountUtil;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
@ContentView(R.layout.activity_signup)
public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.activity_regest_returnback)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_signup_phone)
    private EditText phoneEditText;
    @ViewInject(R.id.activity_signup_btn_getcode)
    private Button codeButton;
    @ViewInject(R.id.activity_signup_verify_code)
    private EditText codeEditText;
    @ViewInject(R.id.activity_signup_btn_submit)
    private Button btnSubmit;
    @ViewInject(R.id.activity_signup_clear)
    private ImageButton btnClear;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        //发送验证码
        codeButton.setOnClickListener(this);
        //提交验证码
        btnSubmit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()<1){
                    btnClear.setVisibility(View.GONE);
                }else{
                    btnClear.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg) {

            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            LogUtils.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result"+event);
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(SignUpActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, CompleteSignUpActivity.class);
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
//                        Toast.makeText(SignUpActivity.this, des, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_regest_returnback :
                finish();
                break;
            case R.id.activity_signup_btn_getcode :
                if (!TextUtils.isEmpty(phoneEditText.getText().toString())) {
                    SMSSDK.getVerificationCode("86", phoneEditText.getText().toString());
                    phString = phoneEditText.getText().toString();
                    TimeCountUtil timeCountUtil = new TimeCountUtil(SignUpActivity.this, 30000, 1000, codeButton);
                    timeCountUtil.start();
                } else {
                    Toast.makeText(SignUpActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.activity_signup_btn_submit :
                //暂时不用提交，直接跳到完成注册页面
//                if (!TextUtils.isEmpty(codeEditText.getText().toString())) {
//                    SMSSDK.submitVerificationCode("86", phString, codeEditText.getText().toString());
//                }else{
//                    Toast.makeText(SignUpActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
//                }

                if (TextUtils.isEmpty(phoneEditText.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(SignUpActivity.this, CompleteSignUpActivity.class);
                intent.putExtra("phone",phoneEditText.getText().toString());
                SignUpActivity.this.startActivity(intent);
                SignUpActivity.this.finish();
                break;
            case R.id.activity_signup_clear :
                phoneEditText.setText("");
                btnClear.setVisibility(View.GONE);
                break;
        }
    }
}
