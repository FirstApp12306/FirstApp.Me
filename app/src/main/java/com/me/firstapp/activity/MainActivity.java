package com.me.firstapp.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.me.firstapp.R;
import com.me.firstapp.fragment.ContentFragment;
import com.me.firstapp.utils.PrefUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    private static final String FRAGMENT_CONTENT = "fragment_content";
    private OnMyWindowFocusChanged onMyWindowFocusChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initFragment();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (onMyWindowFocusChanged != null){
            onMyWindowFocusChanged.onWindowFocusChanged(hasFocus);
        }
    }

    public interface  OnMyWindowFocusChanged{
        public  void onWindowFocusChanged(boolean hasFocus);
    }

    public void setOnMyWindowFocusChangedListener(OnMyWindowFocusChanged onMyWindowFocusChanged){
        this.onMyWindowFocusChanged = onMyWindowFocusChanged;
    }

    /**
     * 初始化fragment, 将fragment数据填充给布局文件
     */
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();// 开启事务
        transaction.replace(R.id.fl_content, new ContentFragment(),FRAGMENT_CONTENT);
        transaction.commit();// 提交事务
        // Fragment leftMenuFragment = fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
    }

//    // 获取主页面fragment
//    public ContentFragment getContentFragment() {
//        FragmentManager fm = getSupportFragmentManager();
//        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
//        return fragment;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PrefUtils.setBoolean(this, "topic_pager_init_flag0", false);
        PrefUtils.setBoolean(this, "topic_pager_init_flag1", false);
    }
}
