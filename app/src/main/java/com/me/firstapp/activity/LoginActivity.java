package com.me.firstapp.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import de.greenrobot.event.EventBus;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.activity_login_back)
    private ImageButton btnReturn;
    @ViewInject(R.id.activity_login_phone)
    private EditText etPhone;
    @ViewInject(R.id.activity_login_psd)
    private EditText etPsd;
    @ViewInject(R.id.activity_login_btn_submit)
    private Button btnLogin;
    @ViewInject(R.id.activity_login_phone_clear)
    private ImageButton btnPhoneClear;
    @ViewInject(R.id.activity_login_password_clear)
    private ImageButton btnPsdClear;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnReturn.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnPhoneClear.setOnClickListener(this);
        btnPsdClear.setOnClickListener(this);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    btnPhoneClear.setVisibility(View.GONE);
                } else {
                    btnPhoneClear.setVisibility(View.VISIBLE);
                }
            }
        });
        etPsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    btnPsdClear.setVisibility(View.GONE);
                } else {
                    btnPsdClear.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void sendDataToServer(){
        SoftInputUtils.hideSoftInputWindow(this);
        loadingDialog = DialogUtils.creatLoadingDialog(this, "请稍后...");
        loadingDialog.show();
        RequestParams params = new RequestParams(GlobalContants.LOGIN_URL);
        params.addQueryStringParameter("phone", etPhone.getText().toString());
        params.addQueryStringParameter("password", etPsd.getText().toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), "无法连接服务器", Toast.LENGTH_LONG).show();
                LogUtils.d("", ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "已取消", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                LogUtils.d("", "访问服务器结束");
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
            String returnCode = object1.getString("return_code");
            if("000000".equals(returnCode)){
                JSONObject object2 = object1.getJSONObject("user");
                User user = gson.fromJson(object2.toString(),User.class);
                LogUtils.d("user", user.toString());

                PrefUtils.setBoolean(this, "login_flag", true);//记录登陆状态
                PrefUtils.setString(this, "loginUser", user.user_id);//记录登陆用户

                setJPush(user);

            }else if ("0001".equals(returnCode)){
                loadingDialog.cancel();
                Toast.makeText(this, object1.getString("err_message"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setJPush(final User user){
        final Set<String> tags =  new HashSet<String>();
        tags.add("common");

        JMessageClient.login(user.user_phone, user.password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                LogUtils.d("register", "状态码：" + i + "描述: " + s);
                if (i == 0){
                    JPushInterface.setAliasAndTags(LoginActivity.this, user.user_phone, tags, new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            LogUtils.d("register", "状态码：" + i + "描述: " + s);
                            if (i == 0) {

                            }
                        }
                    });
                    updateUserData(user);
                }
            }
        });
    }

    /**
     * 保存用户数据
     * @param user
     */
    private void updateUserData(User user){
        LogUtils.d("avataravatar", user.user_avatar);
        DatabaseUtils databaseUtils = new DatabaseUtils(this);
        User oldUser = databaseUtils.queryUser(user.user_id);
        LogUtils.d("user1user1", oldUser.toString());
        ContentValues cv = new ContentValues();
        if (oldUser.user_id == null){//因为这里查询结果不是null,所以用id来判断
            cv.put("id", user.user_id);
            cv.put("name", user.user_name);
            cv.put("phone", user.user_phone);
            cv.put("password", user.password);
            cv.put("avatar", user.user_avatar);
            cv.put("signature", user.user_signature);
            cv.put("sex", user.user_sex);
            cv.put("level", user.user_level);
            cv.put("points", user.user_points);
            cv.put("city", user.user_city);
            cv.put("follow", user.follow);
            cv.put("fans", user.fans);
            databaseUtils.insert("user", cv);
        }else{
            databaseUtils.updateTable("user", cv, "id = ?", new String[]{user.user_id});
        }

        loadingDialog.cancel();
        activityManager.popAllActivity();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_back :
                finish();
                break;
            case R.id.activity_login_btn_submit :
                if (TextUtils.isEmpty(etPhone.getText().toString())){
                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPsd.getText().toString())){
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendDataToServer();
                break;
            case R.id.activity_login_phone_clear :
                etPhone.setText("");
                btnPhoneClear.setVisibility(View.GONE);
                break;
            case R.id.activity_login_password_clear :
                etPsd.setText("");
                btnPsdClear.setVisibility(View.GONE);
                break;
        }
    }
}
