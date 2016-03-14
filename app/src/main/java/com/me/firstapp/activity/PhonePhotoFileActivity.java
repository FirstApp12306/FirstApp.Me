package com.me.firstapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.adapter.PhonePotoFileGridAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_phone_photo_file)
public class PhonePhotoFileActivity extends BaseActivity {

    @ViewInject(R.id.activity_phone_photo_file_title)
    private TextView tvTitle;
    @ViewInject(R.id.activity_phone_photo_file_btn_cancel)
    private Button btnCancel;
    @ViewInject(R.id.activity_phone_photo_file_grid)
    private GridView mGridView;

    private PhonePotoFileGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PhonePotoFileGridAdapter(this);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 利用静态变量将数据传给GalleryActivity界面
                //GalleryActivity.dataList = ShowFilePhotoActivity.dataList;
                Intent intent = new Intent(PhonePhotoFileActivity.this, PhoneAlbumActivity.class);
                intent.putExtra("activityName", "PhonePhotoFileActivity");
                intent.putExtra("position", position);
                startActivity(intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清空选择的图片
                //ImageUtils.tempSelectedImg.clear();
                Intent intent = new Intent(PhonePhotoFileActivity.this, PhoneAlbumActivity.class);
                intent.putExtra("activityName", "SendNoteActivity");
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 清空选择的图片
            //ImageUtils.tempSelectedImg.clear();
            Intent intent = new Intent(PhonePhotoFileActivity.this, PhoneAlbumActivity.class);
            intent.putExtra("activityName", "SendNoteActivity");
            startActivity(intent);
            finish();
        }
        return false;
    }
}
