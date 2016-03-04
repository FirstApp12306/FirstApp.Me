package com.me.firstapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.me.firstapp.R;
import com.me.firstapp.utils.DialogUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
@ContentView(R.layout.activity_complete_regest)
public class CompleteRegestActivity extends BaseActivity {

    @ViewInject(R.id.activity_complete_regest_nickname)
    private EditText nameEditText;
    @ViewInject(R.id.activity_complete_regest_psd)
    private EditText psdEditText;
    @ViewInject(R.id.activity_complete_regest_sure_psd)
    private EditText sureEdiText;
    @ViewInject(R.id.activity_complete_regest_btn_submit)
    private Button btnCommit;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phone = getIntent().getStringExtra("phone");

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeEdit();

            }
        });


    }

    /**
     * 判断输入的内容
     */
    private void judgeEdit(){
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

        DialogUtils.creatLoadingDialog(this, "请稍后...").show();
    }
}
