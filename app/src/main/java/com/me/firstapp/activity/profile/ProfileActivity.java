package com.me.firstapp.activity.profile;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.activity.BaseActivity;
import com.me.firstapp.activity.PhoneAlbumActivity;
import com.me.firstapp.application.MyApplication;
import com.me.firstapp.entity.User;
import com.me.firstapp.entity.images.ImageItem;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.DatabaseUtils;
import com.me.firstapp.utils.DialogUtils;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.view.CircleImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */

@ContentView(R.layout.activity_profile)
public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.activity_profile_root_layout)
    private LinearLayout parentView;
    @ViewInject(R.id.activity_profile_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_profile_ll_avatar)
    private LinearLayout llAvatar;
    @ViewInject(R.id.activity_profile_ll_name)
    private LinearLayout llName;
    @ViewInject(R.id.activity_profile_ll_sex)
    private LinearLayout llSex;
    @ViewInject(R.id.activity_profile_ll_city)
    private LinearLayout llCity;
    @ViewInject(R.id.activity_profile_ll_signature)
    private LinearLayout llSignature;
    @ViewInject(R.id.activity_profile_ll_pwd)
    private LinearLayout llPwd;
    @ViewInject(R.id.activity_profile_ll_logout)
    private LinearLayout llLogout;

    @ViewInject(R.id.activity_profile_avatar)
    private CircleImageView ivAvatar;
    @ViewInject(R.id.activity_profile_user_name)
    private TextView tvUserName;
    @ViewInject(R.id.activity_profile_user_sex)
    private TextView tvUserSex;
    @ViewInject(R.id.activity_profile_user_city)
    private TextView tvUserCity;
    @ViewInject(R.id.activity_profile_user_sign)
    private TextView tvSignature;

    private PopupWindow pop;
    private LinearLayout llPopUp;
    private Dialog loadingDialog;

    private File imageFile;
    private String userID;
    private User user;
    private DatabaseUtils databaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnBack.setOnClickListener(this);
        llAvatar.setOnClickListener(this);
        llName.setOnClickListener(this);
        llSex.setOnClickListener(this);
        llCity.setOnClickListener(this);
        llSignature.setOnClickListener(this);
        llPwd.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        showPopUpWindow();

        databaseUtils = new DatabaseUtils(this);
        userID = getIntent().getStringExtra("user_id");
        LogUtils.d("user_id", userID);
        user = databaseUtils.queryUser(userID);
        LogUtils.d("user", user.toString());

        if (user != null){
            x.image().bind(ivAvatar, user.user_avatar);
            tvUserName.setText(user.user_name);
            if ("01".equals(user.user_sex)){
                tvUserSex.setText("男");
            }
            if ("02".equals(user.user_sex)){
                tvUserSex.setText("女");
            }
            if ("03".equals(user.user_sex)){
                tvUserSex.setText("未知");
            }
            tvUserCity.setText(user.user_city);
            tvSignature.setText(user.user_signature);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_profile_btn_back :
                finish();
                break;
            case R.id.activity_profile_ll_avatar :
                llPopUp.startAnimation(AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.activity_translate_in));
                pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.activity_profile_ll_name :
                Intent intent1 = new Intent(this, AlterNameActivity.class);
                intent1.putExtra("user_id", userID);
                intent1.putExtra("user_name", tvUserName.getText().toString());
                startActivity(intent1);
                break;
            case R.id.activity_profile_ll_sex :
                Intent intent2 = new Intent(this, AlterSexActivity.class);
                intent2.putExtra("user_id", userID);
                if ("男".equals(tvUserSex.getText().toString())){
                    intent2.putExtra("user_sex", "01");
                }else if ("女".equals(tvUserSex.getText().toString())){
                    intent2.putExtra("user_sex", "02");
                }else {
                    intent2.putExtra("user_sex", "03");
                }
                startActivity(intent2);
                break;
            case R.id.activity_profile_ll_city :
                break;
            case R.id.activity_profile_ll_signature :
                break;
            case R.id.activity_profile_ll_pwd :
                break;
            case R.id.activity_profile_ll_logout :
                break;
        }
    }

    private void showPopUpWindow() {
        View view = View.inflate(this, R.layout.pub_note_pop_window, null);
        pop = new PopupWindow(this);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        llPopUp = (LinearLayout) view.findViewById(R.id.pub_note_pop_window_ll);
        RelativeLayout mRl = (RelativeLayout) view.findViewById(R.id.pub_note_pop_window_rl);
        Button btnCamera = (Button) view.findViewById(R.id.pub_note_pop_window_btn_camera);
        Button btnPhoto = (Button) view.findViewById(R.id.pub_note_pop_window_photo);
        Button btnCancel = (Button) view.findViewById(R.id.pub_note_pop_window_cancel);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.pub_note_pop_window_rl :
                        pop.dismiss();
                        llPopUp.clearAnimation();
                        break;
                    case R.id.pub_note_pop_window_btn_camera :
                        imageFile = ImageUtils.takePhoto(ProfileActivity.this, ImageUtils.AVATAR_IMG_DIR);
                        LogUtils.d("imageFile", imageFile.getPath());
                        pop.dismiss();
                        llPopUp.clearAnimation();
                        break;
                    case R.id.pub_note_pop_window_photo :
                        Intent intent = new Intent(ProfileActivity.this, PhoneAlbumActivity.class);
                        intent.putExtra("activityName", "ProfileActivity");
                        startActivity(intent);
                        pop.dismiss();
                        llPopUp.clearAnimation();
                        break;
                    case R.id.pub_note_pop_window_cancel :
                        pop.dismiss();
                        llPopUp.clearAnimation();
                        break;
                }

            }
        };
        mRl.setOnClickListener(listener);
        btnCamera.setOnClickListener(listener);
        btnPhoto.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);
    }

    //完成头像选择事件消息订阅
    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onUserEvent(Event.CompleteAvatarSelectEvent event) {
        String imagePath = ImageUtils.tempSelectedImg.get(0).imagePath;
        LogUtils.d("imagePath", imagePath);
        if (!TextUtils.isEmpty(imagePath)){
            imageFile = new File(imagePath);
            cropImageUri(Uri.fromFile(imageFile), 800, 600, MyApplication.CROP_BIG_PICTURE);
        }
    }

    //完成用户名修改事件消息订阅
    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onUserEvent(Event.CompleteAlterNameEvent event) {
        final String name = event.getName();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvUserName.setText(name);
            }
        });
    }

    //完成用户性别修改事件消息订阅
    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onUserEvent(Event.CompleteAlterSexEvent event) {
        final String sex = event.getSex();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ("01".equals(sex)){
                    tvUserSex.setText("男");
                }
                if ("02".equals(sex)){
                    tvUserSex.setText("女");
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MyApplication.REQUEST_CODE_TAKE_PHOTO:
                if (imageFile != null){
                    cropImageUri(Uri.fromFile(imageFile), 800, 600, MyApplication.CROP_BIG_PICTURE);
                }
                break;
            case MyApplication.CROP_BIG_PICTURE :
                //上传头像
                if (imageFile != null){
                    getToken(imageFile.getPath());
                }
                break;

        }
    }

    //获取token
    private void getToken(final String imagePath){
        loadingDialog = DialogUtils.creatLoadingDialog(this, "请稍后...");
        loadingDialog.show();
        RequestParams params = new RequestParams(GlobalContants.GET_TOKEN_URL);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result_token", result);
                try {
                    JSONObject object = new JSONObject(result);
                    String token = object.getString("token");
                    if (!TextUtils.isEmpty(token)) {
                        upLoadFile(token, imagePath);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("token_error", "获取token失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //上传图片
    private void upLoadFile(String token, String imagePath){
        LogUtils.d("token", token);
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(imagePath, null, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //  res 包含hash、key等信息，具体字段取决于上传策略的设置。
                        LogUtils.d("qiniu", res.toString());
                        try {
                            String image_key = res.getString("key");
                            if (!TextUtils.isEmpty(image_key)) {
                                LogUtils.d("ivAvatar", GlobalContants.FILE_URL + image_key);
                                //更新服务器用户信息
                                updateServerUser(image_key);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
    }

    //更新服务器用户信息
    private void updateServerUser(final String imgKey){
        RequestParams params = new RequestParams(GlobalContants.UPDATE_AVATAR_URL);
        params.addQueryStringParameter("user_id", userID);
        params.addQueryStringParameter("image_key", imgKey);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                x.image().bind(ivAvatar, GlobalContants.FILE_URL + imgKey);
                loadingDialog.cancel();
                updateLocalUser(GlobalContants.FILE_URL + imgKey);
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

    //更新极光推送头像
    private void updateAvatarInJPush(){
        //暂时先不做
    }

    //更新本地用户信息
    private void updateLocalUser(String avatar){
        ContentValues cv = new ContentValues();
        if (user != null){
            user.user_avatar = avatar;
            cv.put("avatar", user.user_avatar);
            databaseUtils.updateTable("user", cv, "id = ?", new String[]{user.user_id});
        }
    }

//    //截图
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageUtils.tempSelectedImg.clear();
        EventBus.getDefault().unregister(this);
    }
}
