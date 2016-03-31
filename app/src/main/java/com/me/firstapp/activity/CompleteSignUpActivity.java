package com.me.firstapp.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.DatabaseUtils;
import com.me.firstapp.utils.DialogUtils;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.utils.SoftInputUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.greenrobot.event.EventBus;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
@ContentView(R.layout.activity_complete_signup)
public class CompleteSignUpActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.activity_complete_regest_nickname)
    private EditText nameEditText;
    @ViewInject(R.id.activity_complete_regest_psd)
    private EditText psdEditText;
    @ViewInject(R.id.activity_complete_regest_sure_psd)
    private EditText sureEdiText;
    @ViewInject(R.id.activity_complete_regest_btn_submit)
    private Button btnOK;
    @ViewInject(R.id.activity_regest_returnback)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_complete_regest_name_clear)
    private ImageButton btnNameClear;
    @ViewInject(R.id.activity_complete_regest_new_psd_clear)
    private ImageButton btnNewPsdClear;
    @ViewInject(R.id.activity_complete_regest_sure_psd_clear)
    private ImageButton btnSurePsdClear;

    private String phone;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone = getIntent().getStringExtra("phone");

    }

    @Override
    protected void onStart() {
        super.onStart();

        btnOK.setTextColor(Color.parseColor("#bfbfbf"));
        btnOK.setClickable(false);

        btnOK.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnNameClear.setOnClickListener(this);
        btnNewPsdClear.setOnClickListener(this);
        btnSurePsdClear.setOnClickListener(this);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0){
                    btnNameClear.setVisibility(View.GONE);
                }else{
                    btnNameClear.setVisibility(View.VISIBLE);
                }

                if (s.length()<2){
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                }else{
                    btnOK.setTextColor(Color.parseColor("#435356"));
                    btnOK.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!com.me.firstapp.utils.TextUtils.validateUserName(s.toString())){
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                }
                if (android.text.TextUtils.isEmpty(psdEditText.getText().toString())) {
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                }
            }
        });
        psdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    btnNewPsdClear.setVisibility(View.GONE);
                } else {
                    btnNewPsdClear.setVisibility(View.VISIBLE);
                }
                if (s.length() < 6) {
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                } else {
                    btnOK.setTextColor(Color.parseColor("#435356"));
                    btnOK.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (android.text.TextUtils.isEmpty(sureEdiText.getText().toString())) {
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                }
            }
        });
        sureEdiText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    btnSurePsdClear.setVisibility(View.GONE);
                } else {
                    btnSurePsdClear.setVisibility(View.VISIBLE);
                }
                if (s.length() < 6) {
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                } else {
                    btnOK.setTextColor(Color.parseColor("#435356"));
                    btnOK.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!sureEdiText.getText().toString().equals(psdEditText.getText().toString())){
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                }else {
                    btnOK.setTextColor(Color.parseColor("#435356"));
                    btnOK.setClickable(true);
                }
            }
        });
    }

    private void sendDataToServer(){
        SoftInputUtils.hideSoftInputWindow(this);
        loadingDialog = DialogUtils.creatLoadingDialog(this, "请稍后...");
        loadingDialog.show();
        RequestParams params = new RequestParams(GlobalContants.SIGN_UP_URL);
        //params.setSslSocketFactory();
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("password", psdEditText.getText().toString());
        params.addQueryStringParameter("name", nameEditText.getText().toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), "无法连接服务器", Toast.LENGTH_LONG).show();
                LogUtils.d("",ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "已取消", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                LogUtils.d("","访问服务器结束");
                loadingDialog.cancel();
            }
        });

    }

    /**
     * 解析服务器JSON数据
     * @param result
     */
    private void parseData(String result){
        try {
            Gson gson = new Gson();
            JSONObject object1 = new JSONObject(result);
            JSONObject object2 = object1.getJSONObject("resultMap");
            String returnCode = object2.getString("return_code");
            if("000000".equals(returnCode)){
                JSONObject object3 = object2.getJSONObject("user");
                LogUtils.d("object3", object3.toString());
                final User user = gson.fromJson(object3.toString(),User.class);
                LogUtils.d("user", user.toString());
                saveUserData(user);
                PrefUtils.setBoolean(this, "login_flag", true);//记录登陆状态
                LogUtils.d("user_iduser_id", user.user_id);
                PrefUtils.setString(this, "loginUser", user.user_id);//记录登陆用户
                EventBus.getDefault().post(new Event.SignUpEvent(user));
                final Set<String> tags =  new HashSet<String>();
                tags.add("common");
                JMessageClient.register(phone, psdEditText.getText().toString(), new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        LogUtils.d("register", "状态码：" + i + "描述: " + s);
                        if (i == 0) {
                            UserInfo userInfo = JMessageClient.getMyInfo();
                            userInfo.setNickname(user.user_name);
                            JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i == 0) {
                                        JMessageClient.login(user.user_phone, user.password, new BasicCallback() {
                                            @Override
                                            public void gotResult(int i, String s) {
                                                LogUtils.d("register", "状态码：" + i + "描述: " + s);
                                                if (i == 0) {
                                                    JPushInterface.setAliasAndTags(CompleteSignUpActivity.this, user.user_phone, tags, new TagAliasCallback() {
                                                        @Override
                                                        public void gotResult(int i, String s, Set<String> set) {
                                                            LogUtils.d("register", "状态码：" + i + "描述: " + s);
                                                            if (i == 0) {
                                                                finish();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });

                        }
                    }
                });

            }else{
                Toast.makeText(x.app(), "数据异常，返回码："+returnCode, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存用户数据
     * @param user
     */
    private void saveUserData(User user){
        ContentValues cv = new ContentValues();
        cv.put("id", user.user_id);
        cv.put("name", user.user_name);
        cv.put("phone", user.user_phone);
        cv.put("avatar", user.user_avatar);
        cv.put("signature", user.user_signature);
        cv.put("sex", user.user_sex);
        cv.put("level", user.user_level);
        cv.put("points", user.user_points);
        cv.put("sts", user.sts);
        cv.put("login_sts", "01");
        cv.put("follow", "0");
        cv.put("fans", "0");
        cv.put("city", user.user_city);
        new DatabaseUtils(this).insert("user", cv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_complete_regest_btn_submit :
                sendDataToServer();
                break;
            case R.id.activity_regest_returnback :
                finish();
                break;
            case R.id.activity_complete_regest_name_clear :
                nameEditText.setText("");
                btnNameClear.setVisibility(View.GONE);
                btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                btnOK.setClickable(false);
                break;
            case R.id.activity_complete_regest_new_psd_clear :
                psdEditText.setText("");
                btnNewPsdClear.setVisibility(View.GONE);
                btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                btnOK.setClickable(false);
                break;
            case R.id.activity_complete_regest_sure_psd_clear :
                sureEdiText.setText("");
                btnSurePsdClear.setVisibility(View.GONE);
                btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                btnOK.setClickable(false);
                break;
        }
    }
}
