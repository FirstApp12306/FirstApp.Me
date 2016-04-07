package com.me.firstapp.pager;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.activity.BlogActivity;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.adapter.BlogPagerListAdapter;
import com.me.firstapp.adapter.FindPagerListAdapter;
import com.me.firstapp.adapter.FirstPagerListAdapter;
import com.me.firstapp.application.MyApplication;
import com.me.firstapp.entity.Blog;
import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.Topic;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.CacheUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.DropDownListView;
import com.me.firstapp.view.RefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class FindPager extends BasePager {

    private String loginUserID;
    private DropDownListView mListView;

    public FindPager(MainActivity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
        mRadioGroup.setVisibility(View.GONE);
        redCircle.setVisibility(View.GONE);
        btnSetting.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("博客");
    }

    @Override
    public void initData() {
        View view = View.inflate(mActivity, R.layout.pager_blog, null);
        mListView = (DropDownListView) view.findViewById(R.id.pager_blog_list_view);
        getDataFromServer();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Blog blog = (Blog) parent.getAdapter().getItem(position);
                Intent intent = new Intent(mActivity, BlogActivity.class);
                intent.putExtra("blog_title", blog.blog_title);
                intent.putExtra("blog_url", blog.blog_url);
                mActivity.startActivity(intent);

            }
        });
        flContent.removeAllViews();
        flContent.addView(view);// 向FrameLayout中动态添加布局
    }

    private void getDataFromServer(){
        RequestParams params = new RequestParams(GlobalContants.BLOG_LIST_URL);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result);
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

    private ArrayList<Blog> blogs;
    private BlogPagerListAdapter blogAdapter;
    private void parseData(String result){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String return_code = object1.getString("return_code");
            if ("000000".equals(return_code)){
                blogs = new ArrayList<>();
                JSONArray array = object1.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object2 = array.getJSONObject(i);
                    Blog blog = gson.fromJson(object2.toString(), Blog.class);
                    blogs.add(blog);
                }
                if (blogs != null){
                    blogAdapter = new BlogPagerListAdapter(mActivity, blogs);
                    mListView.setAdapter(blogAdapter);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
