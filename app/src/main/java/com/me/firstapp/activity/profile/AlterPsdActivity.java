package com.me.firstapp.activity.profile;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.me.firstapp.R;
import com.me.firstapp.activity.BaseActivity;
import com.me.firstapp.activity.ToLoginOrSingupActivity;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.DatabaseUtils;
import com.me.firstapp.utils.DialogUtils;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.utils.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.greenrobot.event.EventBus;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_alter_psd)
public class AlterPsdActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.activity_alter_psd_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_alter_psd_btn_ok)
    private Button btnOK;
    @ViewInject(R.id.activity_alter_psd_old_edit)
    private EditText etOldPsd;
    @ViewInject(R.id.activity_alter_psd_new_edit)
    private EditText etNewPsd;
    @ViewInject(R.id.activity_alter_psd_sure_edit)
    private EditText etSurePsd;
    @ViewInject(R.id.activity_alter_psd_btn_old_clear)
    private ImageButton btnOldClear;
    @ViewInject(R.id.activity_alter_psd_btn_new_clear)
    private ImageButton btnNewClear;
    @ViewInject(R.id.activity_alter_psd_sure_clear)
    private ImageButton btnSureClear;

    private String userID;
    private Dialog loadingDialog;
    private DatabaseUtils databaseUtils;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = getIntent().getStringExtra("user_id");
        databaseUtils = new DatabaseUtils(this);
        user = databaseUtils.queryUser(userID);
        LogUtils.d("user", user.toString());

        btnOK.setTextColor(Color.parseColor("#bfbfbf"));
        btnOK.setClickable(false);

        btnBack.setOnClickListener(this);
        btnOldClear.setOnClickListener(this);
        btnNewClear.setOnClickListener(this);
        btnSureClear.setOnClickListener(this);
        btnOK.setOnClickListener(this);

        etOldPsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    btnOldClear.setVisibility(View.GONE);
                } else {
                    btnOldClear.setVisibility(View.VISIBLE);
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
                if (android.text.TextUtils.isEmpty(etNewPsd.getText().toString()) || android.text.TextUtils.isEmpty(etSurePsd.getText().toString())) {
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                }
            }
        });
        etNewPsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    btnNewClear.setVisibility(View.GONE);
                } else {
                    btnNewClear.setVisibility(View.VISIBLE);
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
                if (android.text.TextUtils.isEmpty(etSurePsd.getText().toString())) {
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                }
            }
        });
        etSurePsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    btnSureClear.setVisibility(View.GONE);
                } else {
                    btnSureClear.setVisibility(View.VISIBLE);
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
                if (!etSurePsd.getText().toString().equals(etNewPsd.getText().toString())){
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                }else {
                    btnOK.setTextColor(Color.parseColor("#435356"));
                    btnOK.setClickable(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_alter_psd_btn_back :
                finish();
                break;
            case R.id.activity_alter_psd_btn_ok :
                sendDataToServer();
                break;
            case R.id.activity_alter_psd_btn_old_clear :
                etOldPsd.setText("");
                btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                btnOK.setClickable(false);
                btnOldClear.setVisibility(View.GONE);
                break;
            case R.id.activity_alter_psd_btn_new_clear :
                etNewPsd.setText("");
                btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                btnOK.setClickable(false);
                btnNewClear.setVisibility(View.GONE);
                break;
            case R.id.activity_alter_psd_sure_clear :
                etSurePsd.setText("");
                btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                btnOK.setClickable(false);
                btnSureClear.setVisibility(View.GONE);
                break;
        }
    }

    //更新服务器信息
    private void sendDataToServer(){
        loadingDialog = DialogUtils.creatLoadingDialog(this, "请稍后...");
        loadingDialog.show();
        RequestParams params = new RequestParams(GlobalContants.UPDATE_USER_PASSWORD_URL);
        params.addQueryStringParameter("user_id", userID);
        params.addQueryStringParameter("old_password", etOldPsd.getText().toString());
        params.addQueryStringParameter("new_password", etNewPsd.getText().toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String return_code = jsonObject.getString("return_code");
                    if ("000000".equals(return_code)){
                        updatePasswordInJPush();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {


            }
        });
    }

    //更新极光
    private void updatePasswordInJPush(){
        JMessageClient.updateUserPassword(etOldPsd.getText().toString(), etNewPsd.getText().toString(), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                updateLocalUser();
            }
        });
    }

    //更新本地信息
    private void updateLocalUser(){
        ContentValues cv = new ContentValues();
        if (user != null){
            user.password = etNewPsd.getText().toString();
            cv.put("password", user.password);
            databaseUtils.updateTable("user", cv, "id = ?", new String[]{user.user_id});

            doLogout();
        }
    }

    private void doLogout(){
        PrefUtils.setBoolean(AlterPsdActivity.this, "login_flag", false);
        PrefUtils.setString(AlterPsdActivity.this, "loginUser", null);
        JMessageClient.logout();

        loadingDialog.cancel();

        activityManager.popAllActivity();
        Intent intent6 = new Intent(AlterPsdActivity.this, ToLoginOrSingupActivity.class);
        startActivity(intent6);
    }
}
