package com.me.firstapp.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.me.firstapp.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_blog)
public class BlogActivity extends BaseActivity {
    @ViewInject(R.id.activity_blog_title)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_blog_title)
    private TextView tvTitle;
    @ViewInject(R.id.activity_blog_webview)
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("blog_url");
        String title = getIntent().getStringExtra("blog_title");
        tvTitle.setText(title);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);// 表示支持js
        settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
        settings.setUseWideViewPort(true);// 支持双击缩放
        mWebView.loadUrl(url);
    }
}
