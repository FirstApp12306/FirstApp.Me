package com.me.firstapp.pager;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.activity.TopicNoteActivity;
import com.me.firstapp.adapter.TopicsAdapter;
import com.me.firstapp.application.MyApplication;
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
 * 描述:
 */
public class TopicsNewPager extends TopicsBasePager {

    private ArrayList<Topic> mTopics;
    private TopicsAdapter topicsAdapter;
    private long page = 1;//页数，默认为1

    public TopicsNewPager(MainActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public void initData() {
        initTopicData();
        refreshAndLoad();
        listItemClick();
    }

    private void initTopicData(){
        String cache = CacheUtils.getCache(GlobalContants.NEW_TOPICS_LIST_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache,false);
        }
        boolean refreshFlag = PrefUtils.getBoolean(mActivity, MyApplication.NEW_TOPICS_REFRESH_FLAG, false);
        if (refreshFlag == false){
            getDataFromServer(false);
        }
    }

    private void listItemClick(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic topic = (Topic) parent.getAdapter().getItem(position);
                LogUtils.d("topic_det", topic.topic_title);
                LogUtils.d("position", position + "");

                handleItemClick(topic.topic_key);//请求网络本身就是异步了

                Intent intent = new Intent(mActivity, TopicNoteActivity.class);
                intent.putExtra("topic_key", topic.topic_key);
                intent.putExtra("topic_title", topic.topic_title);
                mActivity.startActivity(intent);

            }
        });
    }

    private void handleItemClick(String topic_key){
        RequestParams params = new RequestParams(GlobalContants.TOPIC_BROWSE_COUNT_URL);
        params.addQueryStringParameter("topic_key", topic_key);

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);

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

    private void refreshAndLoad(){
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
    }

    private void getDataFromServer(final boolean isMore){
        RequestParams params = new RequestParams(GlobalContants.NEW_TOPICS_LIST_URL);
        params.addQueryStringParameter("page", page + "");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtils.d("++++result", result);
                afterHttp(result);
                PrefUtils.setBoolean(mActivity, MyApplication.NEW_TOPICS_REFRESH_FLAG, true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), "无法连接服务器", Toast.LENGTH_LONG).show();
                LogUtils.d("", ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "已取消", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                LogUtils.d("", "访问服务器结束");

            }

            public void afterHttp(String result) {
                parseData(result, isMore);
                if (page == 1) {//缓存第一页的数据
                    CacheUtils.setCache(GlobalContants.NEW_TOPICS_LIST_URL, result, mActivity);
                }
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
                        mListView.setAdapter(topicsAdapter);
                    }
                }else{
                    ArrayList<Topic> moreTopicsList = mTopics;
                    if (!moreTopicsList.isEmpty()){
                        topicsAdapter.addMore(moreTopicsList);
                    }
                }
            }else{
                Toast.makeText(x.app(), "数据异常，返回码：" + returnCode, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            mListView.onRefreshComplete(false);// 收起加载更多的布局
        }
    }
}
