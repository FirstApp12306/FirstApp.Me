package com.me.firstapp.pager;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.activity.CreateTopicFirstActivity;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.adapter.TopicsAdapter;
import com.me.firstapp.entity.Topic;
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
public class TopicPager extends BasePager {

    private View view;
    private RefreshListView lvList;

    private ArrayList<Topic> mTopics;
    private TopicsAdapter topicsAdapter;
    private boolean isMoreNext;//加载下一页标志
    private long page = 1;//页数，默认为1

    public TopicPager(MainActivity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
        btnCreateTopic.setVisibility(View.VISIBLE);
        btnCreateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CreateTopicFirstActivity.class);
                mActivity.startActivity(intent);
            }
        });

    }

    @Override
    public void initData() {
        LogUtils.d("", "初始化Topic页面数据。。。。。。。");
        boolean isLogin = PrefUtils.getBoolean(mActivity, "login_flag", false);
        if (isLogin){
            view = View.inflate(mActivity, R.layout.pager_topic, null);
            lvList = (RefreshListView) view.findViewById(R.id.pager_topic_listview);
            refreshAndLoad();
        }else{
            view = View.inflate(mActivity, R.layout.pager_unlogin_tip, null);
        }

        flContent.removeAllViews();
        flContent.addView(view);// 向FrameLayout中动态添加布局

        initTopicData();

    }

    private void initTopicData(){
        String cache = CacheUtils.getCache(GlobalContants.TOPICS_LIST_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache,false);
        }
        getDataFromServer(false);
    }

    private void refreshAndLoad(){
        lvList.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getDataFromServer(false);
                isMoreNext = false;
            }

            @Override
            public void onLoadMore() {
                if (isMoreNext == false) {
                    page++;
                    getDataFromServer(true);
                } else {
                    Toast.makeText(mActivity, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete(false);// 收起加载更多的布局
                }
            }
        });
    }

    private void getDataFromServer(final boolean isMoreNext){
        RequestParams params = new RequestParams(GlobalContants.TOPICS_LIST_URL);
        params.addQueryStringParameter("page", page+"");
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result, isMoreNext);
                lvList.onRefreshComplete(true);
                if (page == 1){//缓存第一页的数据
                    CacheUtils.setCache(GlobalContants.TOPICS_LIST_URL, result, mActivity);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), "无法连接服务器", Toast.LENGTH_LONG).show();
                LogUtils.d("",ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "已取消", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                LogUtils.d("","访问服务器结束");
            }
        });
    }

    private void parseData(String result,boolean isMore){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if("000000".equals(returnCode)){
                mTopics = new ArrayList<>();
                JSONArray array = object1.getJSONArray("rows");
                LogUtils.d("array", array.toString());
                JSONObject object = null;
                Topic topic = null;
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    topic = gson.fromJson(object.toString(), Topic.class);
                    mTopics.add(topic);
                    object = null;
                    topic = null;
                }

                if (!isMore){
                    if (mTopics.size() != 0){
                        topicsAdapter = new TopicsAdapter(mActivity, mTopics);
                        lvList.setAdapter(topicsAdapter);
                    }
                }else{
                    ArrayList<Topic> moreTopicsList = mTopics;
                    if (!moreTopicsList.isEmpty()){
                        topicsAdapter.addMore(moreTopicsList);
                    }else{
                        isMoreNext = true;
                        Toast.makeText(mActivity, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                        lvList.onRefreshComplete(false);// 收起加载更多的布局
                    }
                }


            }else{
                Toast.makeText(x.app(), "数据异常，返回码：" + returnCode, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
