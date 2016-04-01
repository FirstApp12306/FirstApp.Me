package com.me.firstapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_person_info)
public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.activity_person_info_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_person_info_user_name)
    private TextView tvUserName;
    @ViewInject(R.id.activity_person_info_avatar)
    private ImageView ivAvatar;
    @ViewInject(R.id.activity_person_info_user_level)
    private TextView tvUserLevel;
    @ViewInject(R.id.activity_person_info_user_id)
    private TextView tvUserID;
    @ViewInject(R.id.activity_person_info_user_city)
    private TextView tvUserCity;
    @ViewInject(R.id.activity_person_info_btn_follow)
    private ImageButton btnFollow;
    @ViewInject(R.id.activity_person_info_btn_msg)
    private ImageButton btnMsg;
    @ViewInject(R.id.activity_person_info_signature)
    private TextView tvSignature;

    private String  user_phone;
    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_name = getIntent().getStringExtra("user_name");
        String user_id = getIntent().getStringExtra("user_id");
        String user_avatar = getIntent().getStringExtra("user_avatar");
        long user_level = getIntent().getLongExtra("user_level", 0);
        String user_city = getIntent().getStringExtra("user_city");
        String signature = getIntent().getStringExtra("signature");
        user_phone = getIntent().getStringExtra("user_phone");

        tvUserName.setText(user_name);
        tvSignature.setText(signature);
        tvUserCity.setText(user_city);
        tvUserID.setText("ID:"+user_id);
        tvUserLevel.setText("等级:"+user_level);

        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(true)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setFailureDrawableId(R.drawable.person_avatar_default_round)
                .setLoadingDrawableId(R.drawable.person_avatar_default_round)
                .build();
        x.image().bind(ivAvatar, user_avatar, imageOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnBack.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        btnMsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_person_info_btn_back :
                finish();
                break;
            case R.id.activity_person_info_btn_follow :

                break;
            case R.id.activity_person_info_btn_msg :
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("targetID", user_phone);
                intent.putExtra("user_name", user_name);
                startActivity(intent);
                break;
        }
    }
}
