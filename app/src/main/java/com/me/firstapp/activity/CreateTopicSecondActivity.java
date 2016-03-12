package com.me.firstapp.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.me.firstapp.R;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.DialogUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
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
 * 描述:
 */
@ContentView(R.layout.activity_create_topic_second)
public class CreateTopicSecondActivity extends BaseActivity {

    @ViewInject(R.id.activity_create_topic_second_topic_title)
    private EditText etTopicTitle;
    @ViewInject(R.id.activity_create_topic_second_topic_detail)
    private EditText etTopicDetail;
    @ViewInject(R.id.activity_create_topic_second_topic_count)
    private TextView tvCount;
    @ViewInject(R.id.activity_create_topic_second_topic_btn_pub)
    private Button btnPub;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etTopicDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = s.length();
                LogUtils.d("count", count + "");
                tvCount.setText(count + "");
            }
        });

        btnPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeEdit();
            }
        });
    }

    private void judgeEdit(){
        if (TextUtils.isEmpty(etTopicTitle.getText().toString())){
            Toast.makeText(this, "话题名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(etTopicDetail.getText().toString())){
            Toast.makeText(this, "话题描述不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        sendDataToServer();
    }

    private void sendDataToServer(){
        SoftInputUtils.hideSoftInputWindow(this);
        loadingDialog = DialogUtils.creatLoadingDialog(this, "请稍后...");
        loadingDialog.show();

        RequestParams params = new RequestParams(GlobalContants.PUB_TOPIC_URL);
        params.addQueryStringParameter("userID", PrefUtils.getString(this, "loginUser", null));
        params.addQueryStringParameter("topicTitle", etTopicTitle.getText().toString());
        params.addQueryStringParameter("topicDetail", etTopicDetail.getText().toString());

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                Toast.makeText(x.app(), "话题发布成功", Toast.LENGTH_LONG).show();
                finish();
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
}
