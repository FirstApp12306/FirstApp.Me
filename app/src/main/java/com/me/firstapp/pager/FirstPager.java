package com.me.firstapp.pager;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.adapter.FirstPagerListAdapter;
import com.me.firstapp.application.MyApplication;
import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.Topic;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.CacheUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
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
public class FirstPager extends  BasePager {

    private RefreshListView mListView;
    private String loginUserID;
    private long page = 1;//页数，默认为1
    private FirstPagerListAdapter  adapter;

    public FirstPager(MainActivity activity) {
        super(activity);
        loginUserID = PrefUtils.getString(mActivity, "loginUser", null);
    }

    @Override
    public void initViews() {
        super.initViews();
        mRadioGroup.setVisibility(View.GONE);
        redCircle.setVisibility(View.GONE);
        btnSetting.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("动态");
    }

    @Override
    public void initData() {
        LogUtils.d("111", "初始化First页面数据。。。。。。。");
        View view = View.inflate(mActivity, R.layout.pager_friend_news, null);
        mListView = (RefreshListView) view.findViewById(R.id.pager_friend_news_listview);
        mListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getDataFromServer(false);
            }

            @Override
            public void onLoadMore() {
                page++;
                getDataFromServer(true);
            }
        });

        String cache = CacheUtils.getCache(GlobalContants.FRIEND_NOTIES_LIST_URL, mActivity);
        LogUtils.d("cache", cache);
        if (!TextUtils.isEmpty(cache)){
            parseData(cache, false);
        }
        boolean refreshFlag = PrefUtils.getBoolean(mActivity, MyApplication.FIRST_PAGER_REFRESH_FLAG, false);
        if (refreshFlag == false){
            getDataFromServer(false);
        }

        flContent.removeAllViews();
        flContent.addView(view);// 向FrameLayout中动态添加布局
    }

    private void getDataFromServer(final boolean isMore){
        RequestParams params = new RequestParams(GlobalContants.FRIEND_NOTIES_LIST_URL);
        params.addQueryStringParameter("user_id", loginUserID);
        params.addQueryStringParameter("page", page+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result, isMore);
                if (page == 1){
                    CacheUtils.setCache(GlobalContants.FRIEND_NOTIES_LIST_URL, result, mActivity);
                }
                PrefUtils.setBoolean(mActivity, MyApplication.FIRST_PAGER_REFRESH_FLAG, true);
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

    private ArrayList<Note> notes;
    private ArrayList<User> users;
    private ArrayList<Topic> topics;
    private void parseData(String result, boolean isMore){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if ("000000".equals(returnCode)){
                notes = new ArrayList<>();
                users = new ArrayList<>();
                topics = new ArrayList<>();

                JSONArray array = object1.getJSONArray("rows");
                JSONObject object2;
                Note note;
                User user;
                Topic topic;
                for (int i = 0; i < array.length(); i++) {
                    object2 = array.getJSONObject(i);
                    note = gson.fromJson(object2.toString(), Note.class);
                    notes.add(note);

                    user = gson.fromJson(object2.toString(), User.class);
                    users.add(user);

                    topic = gson.fromJson(object2.toString(), Topic.class);
                    topics.add(topic);

                    object2 = null;
                    note = null;
                    user = null;
                    topic = null;
                }

                LogUtils.d("notes", notes.toString());
                LogUtils.d("users", users.toString());
                LogUtils.d("topics", topics.toString());

                if (!isMore){
                    if(notes != null && users != null && topics != null){
                        adapter = new FirstPagerListAdapter(mActivity, notes, users, topics);
                        mListView.setAdapter(adapter);
                    }
                }else{
                    if (notes != null && users != null && topics != null){
                        adapter.addMore(notes, users, topics);
                    }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            mListView.onRefreshComplete(true);// 收起加载更多的布局
        }
    }
}
