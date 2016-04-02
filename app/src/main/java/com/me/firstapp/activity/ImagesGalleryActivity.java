package com.me.firstapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.me.firstapp.R;
import com.me.firstapp.adapter.GalleryViewPagerAdapter;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.zoom.PhotoView;
import com.me.firstapp.zoom.ViewPagerFixed;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_images_gallery)
public class ImagesGalleryActivity extends BaseActivity {

    @ViewInject(R.id.activity_images_gallery_btn_back)
    private Button btnBack;
    @ViewInject(R.id.activity_images_gallery_btn_del)
    private Button btnDele;
    @ViewInject(R.id.activity_images_gallery_vp)
    private ViewPagerFixed mViewPager;
    @ViewInject(R.id.activity_images_gallery_btn_ok)
    private Button btnOK;

    private String activityName = null;
    private int position = 0;
    private ArrayList<View> listViews = null;
    private GalleryViewPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityName = getIntent().getStringExtra("activityName");
        position = getIntent().getIntExtra("position", 0);
        setBtnOK();
        setBtnClick();
        for (int i = 0; i < ImageUtils.tempSelectedImg.size(); i++) {
            initListViews( ImageUtils.tempSelectedImg.get(i).imagePath);
        }
        mAdapter = new GalleryViewPagerAdapter(this,listViews);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        mViewPager.setCurrentItem(position);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int arg0) {
            position = arg0;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void initListViews(String imagePath) {
        if (listViews == null){
            listViews = new ArrayList();
        }
        PhotoView img = new PhotoView(this);
        img.setBackgroundColor(0xff000000);
        x.image().bind(img, imagePath);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    private void setBtnOK(){
        if (ImageUtils.tempSelectedImg.size() > 0) {
            btnOK.setText("完成"+"(" + ImageUtils.tempSelectedImg.size() + "/"+ImageUtils.MAX_PIC_NUM+")");
            btnOK.setClickable(true);
            btnOK.setTextColor(Color.parseColor("#435356"));
        } else {
            btnOK.setClickable(false);
            btnOK.setTextColor(Color.parseColor("#bfbfbf"));
        }
    }

    private void setBtnClick(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.activity_images_gallery_btn_back :
                        finish();
                        break;
                    case R.id.activity_images_gallery_btn_del :

                        break;
                    case R.id.activity_images_gallery_btn_ok :
                        EventBus.getDefault().post(new Event.CompleteNoteAddimageEvent());
                        finish();
                        break;
                }
            }
        };
        btnBack.setOnClickListener(listener);
        btnDele.setOnClickListener(listener);
        btnOK.setOnClickListener(listener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }
}
