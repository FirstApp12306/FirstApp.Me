package com.me.firstapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.me.firstapp.R;
import com.me.firstapp.adapter.AlbumGridViewAdapter;
import com.me.firstapp.entity.images.ImageBucket;
import com.me.firstapp.entity.images.ImageItem;
import com.me.firstapp.utils.AlbumHelper;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.view.OptimizeGridView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_phone_album)
public class PhoneAlbumActivity extends BaseActivity {

    @ViewInject(R.id.activity_phone_album_btn_back)
    private Button btnBack;
    @ViewInject(R.id.activity_phone_album_head_title)
    private TextView tvTitle;
    @ViewInject(R.id.activity_phone_album_btn_cancel)
    private Button btnCancel;
    @ViewInject(R.id.activity_phone_album_bt_preview)
    private Button btnPre;
    @ViewInject(R.id.activity_phone_album_btn_ok)
    private Button btnOk;
    @ViewInject(R.id.activity_phone_album_grid)
    private OptimizeGridView mGridView;
    @ViewInject(R.id.album_no_photo_Text)
    private TextView noText;

    private AlbumHelper helper;
    public static List<ImageBucket> contentList;
    private AlbumGridViewAdapter gridImageAdapter;
    private String activityName;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBtnListen();
        setBtnOk();
        // 获取图片数据
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        contentList = helper.getImagesBucketList(false);

        activityName = getIntent().getStringExtra("activityName");
        if("PhonePhotoFileActivity".equals(activityName)){
            position = getIntent().getIntExtra("position", 0);
            tvTitle.setText(contentList.get(position).bucketName);
            mGridView.setEmptyView(noText);
            gridImageAdapter = new AlbumGridViewAdapter(this, contentList.get(position).imageList);
            mGridView.setAdapter(gridImageAdapter);
            doOnItemClick(contentList.get(position).imageList);
        }else{
            ArrayList<ImageItem> imageItemList = new ArrayList();
            for (int i = 0; i < contentList.size(); i++) {
                imageItemList.addAll(contentList.get(i).imageList);
            }
            mGridView.setEmptyView(noText);
            gridImageAdapter = new AlbumGridViewAdapter(this,imageItemList);
            mGridView.setAdapter(gridImageAdapter);
            doOnItemClick(imageItemList);
        }
    }

    private void doOnItemClick(final ArrayList<ImageItem> imageList){
        gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(ToggleButton toggleButton, int position, boolean isChecked, Button chooseBt) {
                if (ImageUtils.tempSelectedImg.size() >= ImageUtils.MAX_PIC_NUM) {
                    toggleButton.setChecked(false);
                    chooseBt.setVisibility(View.GONE);
                    if (!removeImageItem(imageList.get(position))) {
                        Toast.makeText(PhoneAlbumActivity.this, "超出可选图片张数", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (isChecked) {
                    ImageUtils.tempSelectedImg.add(imageList.get(position));
                    chooseBt.setVisibility(View.VISIBLE);
                    btnOk.setText("完成" + "(" + ImageUtils.tempSelectedImg.size() + "/" + ImageUtils.MAX_PIC_NUM + ")");
                } else {
                    ImageUtils.tempSelectedImg.remove(imageList.get(position));
                    chooseBt.setVisibility(View.GONE);
                    btnOk.setText("完成" + "(" + ImageUtils.tempSelectedImg.size() + "/" + ImageUtils.MAX_PIC_NUM + ")");
                }
                setBtnOk();
            }
        });
    }

    private boolean removeImageItem (ImageItem imageItem) {
        if (ImageUtils.tempSelectedImg.contains(imageItem)) {
            ImageUtils.tempSelectedImg.remove(imageItem);
            btnOk.setText("完成" + "(" + ImageUtils.tempSelectedImg.size() + "/" + ImageUtils.MAX_PIC_NUM + ")");
            return true;
        }
        return false;
    }

    private void setBtnOk() {
        if (ImageUtils.tempSelectedImg.size() > 0) {
            btnOk.setText("完成" + "(" + ImageUtils.tempSelectedImg.size() + "/" + ImageUtils.MAX_PIC_NUM + ")");
            btnPre.setPressed(true);
            btnOk.setPressed(true);
            btnPre.setClickable(true);
            btnOk.setClickable(true);
            btnOk.setTextColor(Color.parseColor("#435356"));
            btnPre.setTextColor(Color.parseColor("#435356"));
        } else {
            btnOk.setText("完成" + "(" + ImageUtils.tempSelectedImg.size() + "/" + ImageUtils.MAX_PIC_NUM + ")");
            btnPre.setPressed(false);
            btnPre.setClickable(false);
            btnOk.setPressed(false);
            btnOk.setClickable(false);
            btnOk.setTextColor(Color.parseColor("#D1D1D1"));
            btnPre.setTextColor(Color.parseColor("#D1D1D1"));
        }
    }

    private void setBtnListen(){
        btnOk.setText("完成" + "(" + ImageUtils.tempSelectedImg.size() + "/" + ImageUtils.MAX_PIC_NUM + ")");
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.activity_phone_album_btn_back :
                        startActivity(new Intent(PhoneAlbumActivity.this, PhonePhotoFileActivity.class));
                        finish();
                        break;
                    case R.id.activity_phone_album_btn_cancel :
                        ImageUtils.tempSelectedImg.clear();
                        finish();
                        break;
                    case R.id.activity_phone_album_bt_preview :
                        if (ImageUtils.tempSelectedImg.size() > 0) {
                            Intent intent = new Intent(PhoneAlbumActivity.this, ImagesGalleryActivity.class);
                            intent.putExtra("activityName", "PhoneAlbumActivity");
                            startActivity(intent);
                            //finish();
                        }
                        break;
                    case R.id.activity_phone_album_btn_ok :
                        EventBus.getDefault().post(new Event.CompleteNoteAddimageEvent());
                        if ("ProfileActivity".equals(activityName)){
                            EventBus.getDefault().post(new Event.CompleteAvatarSelectEvent());
                        }
                        finish();
                        break;

                }
            }
        };
        btnBack.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);
        btnPre.setOnClickListener(listener);
        btnOk.setOnClickListener(listener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setBtnOk();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ImageUtils.tempSelectedImg.clear();
            finish();
        }
        return false;
    }
}
