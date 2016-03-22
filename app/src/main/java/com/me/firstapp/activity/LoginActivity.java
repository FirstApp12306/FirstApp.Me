package com.me.firstapp.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
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
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.activity_login_back)
    private ImageButton btnReturn;
    @ViewInject(R.id.activity_login_phone)
    private EditText etPhone;
    @ViewInject(R.id.activity_login_psd)
    private EditText etPsd;
    @ViewInject(R.id.activity_login_btn_submit)
    private Button btnLogin;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View.OnClickListener listener = new View.OnClickListener() {
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
                }
            }
        };
        btnReturn.setOnClickListener(listener);
        btnLogin.setOnClickListener(listener);
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
                User user = gson.fromJson(object3.toString(),User.class);
                LogUtils.d("user", user.toString());
                updateUserData(user);
                PrefUtils.setBoolean(this, "login_flag", true);//记录登陆状态
                LogUtils.d("user_iduser_id", user.user_id);
                PrefUtils.setString(this, "loginUser", user.user_id);//记录登陆用户
                JMessageClient.login(user.user_phone, user.password, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        LogUtils.d("register", "状态码：" + i + "描述: " + s);
                        finish();
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
    private void updateUserData(User user){
        DatabaseUtils databaseUtils = new DatabaseUtils(this);
        User user1 = databaseUtils.queryUser(user.user_id);
        ContentValues cv = new ContentValues();
        if (user1 == null){

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
            cv.put("city", user.user_city);
            databaseUtils.insert("user", cv);
        }

        User user2 = databaseUtils.queryLoginUser(user.user_id, "02");
        if (user2 != null){
            cv.put("login_sts", "01");
            databaseUtils.updateTable("user", cv, "id = ?", new String[]{user.user_id});
        }
    }
}
