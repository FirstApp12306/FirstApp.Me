package com.me.firstapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.me.firstapp.R;
import com.me.firstapp.activity.settings.SettingsActivity;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.DialogUtils;
import com.me.firstapp.utils.SoftInputUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
@ContentView(R.layout.activity_complete_signup)
public class CompleteSignUpActivity extends BaseActivity {

    @ViewInject(R.id.activity_complete_regest_nickname)
    private EditText nameEditText;
    @ViewInject(R.id.activity_complete_regest_psd)
    private EditText psdEditText;
    @ViewInject(R.id.activity_complete_regest_sure_psd)
    private EditText sureEdiText;
    @ViewInject(R.id.activity_complete_regest_btn_submit)
    private Button btnCommit;
    private String phone;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phone = getIntent().getStringExtra("phone");

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //judgeEdit();
                Intent intent = new Intent(CompleteSignUpActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


    }

    /**
     * 判断输入的内容
     */
    private void judgeEdit(){

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(nameEditText.getText().toString().trim())){
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(psdEditText.getText().toString())){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sureEdiText.getText().toString())){
            Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!sureEdiText.getText().toString().equals(psdEditText.getText().toString())){
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        sendDataToServer();

    }

    private void sendDataToServer(){
        SoftInputUtils.hideSoftInputWindow(this);
        loadingDialog = DialogUtils.creatLoadingDialog(this, "请稍后...");
        loadingDialog.show();
        RequestParams params = new RequestParams(GlobalContants.REGEST_URL);
        //params.setSslSocketFactory();
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("password", psdEditText.getText().toString());
        params.addQueryStringParameter("name", nameEditText.getText().toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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
}
