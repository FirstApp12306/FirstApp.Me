package com.me.firstapp.activity.profile;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.me.firstapp.R;
import com.me.firstapp.activity.BaseActivity;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.DatabaseUtils;
import com.me.firstapp.utils.DialogUtils;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.LogUtils;

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
@ContentView(R.layout.activity_alter_sex)
public class AlterSexActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.activity_alter_sex_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_alter_sex_ll_man)
    private LinearLayout llMan;
    @ViewInject(R.id.activity_alter_sex_ll_woman)
    private LinearLayout llWoman;
    @ViewInject(R.id.activity_alter_sex_iv_man)
    private ImageView ivMan;
    @ViewInject(R.id.activity_alter_sex_iv_woman)
    private ImageView ivWoman;

    private String sex;
    private String userID;
    private String userSex;
    private Dialog loadingDialog;
    private DatabaseUtils databaseUtils;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = getIntent().getStringExtra("user_id");
        userSex = getIntent().getStringExtra("user_sex");
        databaseUtils = new DatabaseUtils(this);
        user = databaseUtils.queryUser(userID);
        LogUtils.d("user", user.toString());

        btnBack.setOnClickListener(this);
        llMan.setOnClickListener(this);
        llWoman.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_alter_sex_btn_back :
                finish();
                break;
            case R.id.activity_alter_sex_ll_man :
                ivMan.setVisibility(View.VISIBLE);
                ivWoman.setVisibility(View.INVISIBLE);
                userSex = "01";
                sendDataToServer();
                break;
            case R.id.activity_alter_sex_ll_woman :
                ivMan.setVisibility(View.INVISIBLE);
                ivWoman.setVisibility(View.VISIBLE);
                userSex = "02";
                sendDataToServer();
                break;
        }
    }

    private void sendDataToServer(){
        loadingDialog = DialogUtils.creatLoadingDialog(this, "请稍后...");
        loadingDialog.show();
        RequestParams params = new RequestParams(GlobalContants.UPDATE_USER_SEX_URL);
        params.addQueryStringParameter("user_id", userID);
        params.addQueryStringParameter("user_sex", userSex);
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
            user.user_sex = userSex;
            cv.put("user_sex", user.user_sex);
            databaseUtils.updateTable("user", cv, "id = ?", new String[]{user.user_id});
            EventBus.getDefault().post(new Event.CompleteAlterSexEvent(userSex));
            finish();
        }
    }
}
