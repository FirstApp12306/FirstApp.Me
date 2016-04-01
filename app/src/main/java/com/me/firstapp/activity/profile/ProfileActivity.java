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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.activity.BaseActivity;
import com.me.firstapp.activity.PhoneAlbumActivity;
import com.me.firstapp.activity.ToLoginOrSingupActivity;
import com.me.firstapp.application.MyApplication;
import com.me.firstapp.entity.User;
import com.me.firstapp.entity.images.ImageItem;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.DatabaseUtils;
import com.me.firstapp.utils.DialogUtils;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.CircleImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
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

        showPopUpWindow();
        EventBus.getDefault().register(this);

        databaseUtils = new DatabaseUtils(this);
        userID = getIntent().getStringExtra("user_id");
        LogUtils.d("user_id", userID);

    }

    @Override
    protected void onResume() {
        super.onResume();

        user = databaseUtils.queryUser(userID);
        LogUtils.d("user", user.toString());

        if (user != null){
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setIgnoreGif(true)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setFailureDrawableId(R.drawable.person_avatar_default_round)
                    .setLoadingDrawableId(R.drawable.person_avatar_default_round)
                    .build();
            x.image().bind(ivAvatar, user.user_avatar, imageOptions);

            tvUserSex.setText(user.user_sex);
            tvUserName.setText(user.user_name);
            tvUserCity.setText(user.user_city);
            tvSignature.setText(user.user_signature);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnBack.setOnClickListener(this);
        llAvatar.setOnClickListener(this);
        llName.setOnClickListener(this);
        llSex.setOnClickListener(this);
        llCity.setOnClickListener(this);
        llSignature.setOnClickListener(this);
        llPwd.setOnClickListener(this);
        llLogout.setOnClickListener(this);

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
                Intent intent5 = new Intent(this, AlterCityActivity.class);
                intent5.putExtra("user_id", userID);
                startActivity(intent5);
                break;
            case R.id.activity_profile_ll_signature :
                Intent intent3 = new Intent(this, AlterSignatureActivity.class);
                intent3.putExtra("user_id", userID);
                intent3.putExtra("signature", tvSignature.getText().toString());
                startActivity(intent3);
                break;
            case R.id.activity_profile_ll_pwd :
                Intent intent4 = new Intent(this, AlterPsdActivity.class);
                intent4.putExtra("user_id", userID);
                startActivity(intent4);
                break;
            case R.id.activity_profile_ll_logout :
                showLogoutTipDialog();
                break;
        }
    }

    private Dialog mDialog;
    private void showLogoutTipDialog(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.dialog_base_with_button_cancel_btn:
                        mDialog.dismiss();
                        break;
                    case R.id.dialog_base_with_button_commit_btn:
                        mDialog.dismiss();
                        PrefUtils.setBoolean(ProfileActivity.this, "login_flag", false);
                        PrefUtils.setString(ProfileActivity.this, "loginUser", null);
                        JMessageClient.logout();

                        activityManager.popAllActivity();

                        Intent intent6 = new Intent(ProfileActivity.this, ToLoginOrSingupActivity.class);
                        startActivity(intent6);

                        break;
                }
            }
        };
        mDialog = DialogUtils.createCommonDialog(this, listener, "您确定退出当前账户吗？");
        mDialog.show();
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
                updateAvatarInJPush(imgKey);
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
    private void updateAvatarInJPush(final String imgKey){
        JMessageClient.updateUserAvatar(imageFile, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                LogUtils.d("updateUserAvatar","状态码："+i+"描述："+s);
                if (i == 0){
                    updateLocalUser(imgKey);
                }
            }
        });
    }

    //更新本地用户信息
    private void updateLocalUser(String imageKey){
        ContentValues cv = new ContentValues();
        if (user != null){
            user.user_avatar = GlobalContants.FILE_URL + imageKey;
            cv.put("avatar", user.user_avatar);
            databaseUtils.updateTable("user", cv, "id = ?", new String[]{user.user_id});
            x.image().bind(ivAvatar, user.user_avatar);
            loadingDialog.cancel();
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
