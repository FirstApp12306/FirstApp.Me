package com.me.firstapp.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.me.firstapp.R;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.view.RoundProgressBar;
import com.me.firstapp.zoom.PhotoView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_scan_image)
public class ScanImageActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.activity_scan_image_photoview)
    private PhotoView mPhotoView;
    @ViewInject(R.id.activity_scan_image_btn_save)
    private ImageButton btnSave;
    @ViewInject(R.id.activity_scan_image_btn_cancel)
    private ImageButton btnCancle;
    @ViewInject(R.id.activity_scan_image_roundProgressBar)
    private RoundProgressBar mRoundProgressBar;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        imageUrl = getIntent().getStringExtra("image_url");
        LogUtils.d("imageUrl", imageUrl);
        if (!TextUtils.isEmpty(imageUrl)){
            x.image().bind(mPhotoView, imageUrl);
        }
        btnSave.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.activity_scan_image_btn_save :
                RequestParams params = new RequestParams(imageUrl);
                String path = Environment.getExternalStorageDirectory()+"/FirstApp/pictures/NoteImages/"+imageUrl.substring(24, imageUrl.length())+".jpg";
                LogUtils.d("path", path);
                params.setSaveFilePath(path);
                x.http().get(params, new Callback.ProgressCallback<File>() {
                    @Override
                    public void onSuccess(File result) {
                        LogUtils.d("result", result.getPath());
                        Toast.makeText(ScanImageActivity.this, "已保存至/FirstApp/pictures/NoteImages/", Toast.LENGTH_SHORT).show();
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

                    @Override
                    public void onWaiting() {

                    }

                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isDownloading) {
                        LogUtils.d("current", current+"");
                        LogUtils.d("total", total+"");
                        LogUtils.d("progress", (double)current/(double)total*100+"");
                        LogUtils.d("isDownloading", isDownloading+"");
                        if (((double)current/(double)total) != 1){
                            btnSave.setClickable(true);
                            mRoundProgressBar.setVisibility(View.VISIBLE);
                            mRoundProgressBar.setProgress(((double)current/(double)total)*100);
                        }else{
                            mRoundProgressBar.setVisibility(View.GONE);
                            btnSave.setClickable(false);
                        }

                    }
                });

                break;
            case R.id.activity_scan_image_btn_cancel :
                finish();
                break;
        }
    }
}
