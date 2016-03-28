package com.me.firstapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.adapter.PubNoteGridViewAdapter;
import com.me.firstapp.application.MyApplication;
import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.User;
import com.me.firstapp.entity.images.ImageItem;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.DialogUtils;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.OptimizeGridView;
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
@ContentView(R.layout.activity_send_note)
public class SendNoteActivity extends BaseActivity {
    @ViewInject(R.id.activity_send_note_btn_cancel)
    private Button btnCancel;
    @ViewInject(R.id.activity_send_note_title)
    private TextView tvTitle;
    @ViewInject(R.id.activity_send_note_btn_pub)
    private Button btnPub;
    @ViewInject(R.id.activity_send_note_edit)
    private EditText mEditText;
    @ViewInject(R.id.activity_send_note_grid_view)
    private OptimizeGridView mGridView;

    private View parentView;
    private PopupWindow pop;
    private LinearLayout llPopUp;

    private File imageFile;

    private PubNoteGridViewAdapter gridAdapter;
    private Dialog loadingDialog;

    private String topicKey;
    private String topicTitle;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topicKey = getIntent().getStringExtra("topic_key");
        topicTitle = getIntent().getStringExtra("topic_title");
        userID = PrefUtils.getString(this, "loginUser", null);
        tvTitle.setText(topicTitle);
        parentView = View.inflate(this, R.layout.activity_send_note, null);
        btnListen();
        showPopUpWindow();
        EventBus.getDefault().register(this);
        gridAdapter = new PubNoteGridViewAdapter(this);
        mGridView.setAdapter(gridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == ImageUtils.tempSelectedImg.size()) {
                    llPopUp.startAnimation(AnimationUtils.loadAnimation(SendNoteActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(SendNoteActivity.this, ImagesGalleryActivity.class);
                    intent.putExtra("activityName", "SendNoteActivity");
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        });
    }

    private void btnListen(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.activity_send_note_btn_cancel :
                        finish();
                        break;
                    case R.id.activity_send_note_btn_pub :
                        if (TextUtils.isEmpty(mEditText.getText().toString()) && ImageUtils.tempSelectedImg.size() == 0){
                            Toast.makeText(SendNoteActivity.this, "发布的帖子不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sendNote();
                        break;
                }
            }
        };
        btnCancel.setOnClickListener(listener);
        btnPub.setOnClickListener(listener);
    }

    private void sendNote(){
        loadingDialog = DialogUtils.creatLoadingDialog(this, "请稍后...");
        loadingDialog.show();
        if (ImageUtils.tempSelectedImg.size() != 0){
            //获取token
            getToken();
        }else{
            sendDataToServer(null);
        }
    }

    //获取token
    private void getToken(){
        RequestParams params = new RequestParams(GlobalContants.GET_TOKEN_URL);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result_token", result);
                try {
                    JSONObject object = new JSONObject(result);
                    String token = object.getString("token");
                    if (!TextUtils.isEmpty(token)) {
                        upLoadFile(token);
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
    private void upLoadFile(String token){
            LogUtils.d("token", token);
            LogUtils.d("imagePath", ImageUtils.tempSelectedImg.get(0).imagePath + "#######");
            UploadManager uploadManager = new UploadManager();
            uploadManager.put(ImageUtils.tempSelectedImg.get(0).imagePath, null, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            //  res 包含hash、key等信息，具体字段取决于上传策略的设置。
                            LogUtils.d("qiniu", res.toString());
                            try {
                                String image_key = res.getString("key");
                                if (!TextUtils.isEmpty(image_key)) {
                                    sendDataToServer(image_key);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, null);
    }

    //发送数据到服务器
    private void sendDataToServer(String key){
        RequestParams params = new RequestParams(GlobalContants.SEND_NOTE_URL);
        params.addQueryStringParameter("image_key", key);//图片的key
        params.addQueryStringParameter("content", mEditText.getText().toString());
        params.addQueryStringParameter("topic_key", topicKey);
        params.addQueryStringParameter("user_id", userID);
        LogUtils.d("topic_key", topicKey);
        LogUtils.d("user_id", userID);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                Gson gson = new Gson();
                if (!TextUtils.isEmpty(result)){
                    try {
                        JSONObject object1 = new JSONObject(result);
                        JSONObject object2 = object1.getJSONObject("resultMap");
                        Note note = gson.fromJson(object2.toString(), Note.class);
                        User user = gson.fromJson(object2.toString(), User.class);
                        LogUtils.d("note", note.toString());
                        LogUtils.d("user", user.toString());
                        EventBus.getDefault().post(new Event.RefreshNotesEvent(note, user));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.d("error", ex.toString());
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
                        imageFile = ImageUtils.takePhoto(SendNoteActivity.this, ImageUtils.NOTE_IMG_DIR);
                        pop.dismiss();
                        llPopUp.clearAnimation();
                        break;
                    case R.id.pub_note_pop_window_photo :
                        Intent intent = new Intent(SendNoteActivity.this, PhoneAlbumActivity.class);
                        intent.putExtra("activityName", "SendNoteActivity");
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MyApplication.REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    LogUtils.d("REQUEST_CODE_TAKE_PHOTO", MyApplication.REQUEST_CODE_TAKE_PHOTO+"");
                    if (ImageUtils.tempSelectedImg.size() < ImageUtils.MAX_PIC_NUM && resultCode == RESULT_OK) {
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.imagePath = imageFile.getAbsolutePath();
                        ImageUtils.tempSelectedImg.add(takePhoto);
                        gridAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread) //在ui线程执行
    public void onUserEvent(Event.CompleteNoteAddimageEvent event) {
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageUtils.tempSelectedImg.clear();
        EventBus.getDefault().unregister(this);
    }
}
