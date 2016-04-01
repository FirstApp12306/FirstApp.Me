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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import de.greenrobot.event.EventBus;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_alter_signature)
public class AlterSignatureActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.activity_alter_signature_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_alter_signature_btn_ok)
    private Button btnOK;
    @ViewInject(R.id.activity_alter_signature_edit)
    private EditText mEditText;
    @ViewInject(R.id.activity_alter_signature_btn_clear)
    private ImageButton btnClear;
    @ViewInject(R.id.activity_alter_signature_tv_num)
    private TextView tvNum;

    private String userID;
    private String userSignature;
    private Dialog loadingDialog;
    private DatabaseUtils databaseUtils;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = getIntent().getStringExtra("user_id");
        userSignature = getIntent().getStringExtra("signature");
        databaseUtils = new DatabaseUtils(this);
        user = databaseUtils.queryUser(userID);
        LogUtils.d("user", user.toString());
        mEditText.setText(userSignature);

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
                if (s.length() == 0) {
                    btnClear.setVisibility(View.GONE);
                } else {
                    btnClear.setVisibility(View.VISIBLE);
                }

                if (s.toString().equals(userSignature)) {
                    btnOK.setTextColor(Color.parseColor("#bfbfbf"));
                    btnOK.setClickable(false);
                } else {
                    btnOK.setTextColor(Color.parseColor("#435356"));
                    btnOK.setClickable(true);
                }

                tvNum.setText(s.length()+"/"+30);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_alter_signature_btn_back :
                finish();
                break;
            case R.id.activity_alter_signature_btn_ok :
                sendDataToServer();
                SoftInputUtils.hideSoftInputWindow(this);
                break;
            case R.id.activity_alter_signature_btn_clear :
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
        RequestParams params = new RequestParams(GlobalContants.UPDATE_USER_SIGNATURE_URL);
        params.addQueryStringParameter("user_id", userID);
        params.addQueryStringParameter("signature", mEditText.getText().toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                updateLocalUser();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                loadingDialog.cancel();

            }
        });
    }

    //更新极光
    private void updateNameInJPush(){
        //暂时先不做
    }

    //更新本地信息
    private void updateLocalUser(){
        ContentValues cv = new ContentValues();
        if (user != null){
            user.user_signature = mEditText.getText().toString();
            cv.put("signature", user.user_signature);
            databaseUtils.updateTable("user", cv, "id = ?", new String[]{user.user_id});
            finish();
        }
    }
}
