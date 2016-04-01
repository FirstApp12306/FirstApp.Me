package com.me.firstapp.activity.profile;

import android.app.Dialog;
import android.content.ContentValues;
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
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.DatabaseUtils;
import com.me.firstapp.utils.DialogUtils;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.SoftInputUtils;
import com.me.firstapp.utils.TextUtils;

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
@ContentView(R.layout.activity_alter_name)
public class AlterNameActivity extends BaseActivity implements View.OnClickListener {

    private String userID;
    private String userName;

    @ViewInject(R.id.activity_alter_name_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_alter_name_btn_ok)
    private Button btnOK;
    @ViewInject(R.id.activity_alter_name_edit)
    private EditText mEditText;
    @ViewInject(R.id.activity_alter_name_btn_clear)
    private ImageButton btnClear;

    private Dialog loadingDialog;
    private DatabaseUtils databaseUtils;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = getIntent().getStringExtra("user_id");
        userName = getIntent().getStringExtra("user_name");
        databaseUtils = new DatabaseUtils(this);
        user = databaseUtils.queryUser(userID);
        LogUtils.d("user", user.toString());
        mEditText.setText(userName);

        btnOK.setTextColor(Color.parseColor("#bfbfbf"));
        btnOK.setClickable(false);

        btnBack.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnOK.setOnClickListener(this);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0){
                    btnClear.setVisibility(View.GONE);
                }else{
                    btnClear.setVisibility(View.VISIBLE);
                }

                if (s.toString().equals(userName)){
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                }else{
                    if (s.length()<2){
                        btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                        btnOK.setClickable(false);
                    }else{
                        btnOK.setTextColor(Color.parseColor("#435356"));
                        btnOK.setClickable(true);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.validateUserName(s.toString())){
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_alter_name_btn_back :
                finish();
                break;
            case R.id.activity_alter_name_btn_ok :
                sendDataToServer();
                SoftInputUtils.hideSoftInputWindow(this);
                break;
            case R.id.activity_alter_name_btn_clear :
                btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                btnOK.setClickable(false);
                mEditText.setText("");
                btnClear.setVisibility(View.GONE);
                break;
        }
    }

    //更新服务器信息
    private void sendDataToServer(){
        loadingDialog = DialogUtils.creatLoadingDialog(this, "请稍后...");
        loadingDialog.show();
        RequestParams params = new RequestParams(GlobalContants.UPDATE_USER_NAME_URL);
        params.addQueryStringParameter("user_id", userID);
        params.addQueryStringParameter("user_name", mEditText.getText().toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                updateNameInJPush();
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
    private void updateNameInJPush(){
        UserInfo userInfo = JMessageClient.getMyInfo();
        userInfo.setNickname(mEditText.getText().toString());
        JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0){
                    updateLocalUser();
                }
            }
        });
    }

    //更新本地信息
    private void updateLocalUser(){
        ContentValues cv = new ContentValues();
        if (user != null){
            user.user_name = mEditText.getText().toString();
            cv.put("name", user.user_name);
            databaseUtils.updateTable("user", cv, "id = ?", new String[]{user.user_id});
            loadingDialog.cancel();
            finish();
        }
    }
}
