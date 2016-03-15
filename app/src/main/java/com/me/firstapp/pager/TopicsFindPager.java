package com.me.firstapp.pager;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.activity.TopicNoteActivity;
import com.me.firstapp.adapter.ToTopicsAdapter;
import com.me.firstapp.adapter.TopicsAdapter;
import com.me.firstapp.entity.Topic;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.CacheUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.RefreshListView;
import com.viewpagerindicator.LinePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class TopicsFindPager extends TopicsBasePager {

    private ArrayList<Topic> mTopics;
    private ArrayList<Topic> mTopTopics;
    private HashMap<String, Object> topicMap = new HashMap<>();
    private TopicsAdapter topicsAdapter;
    private boolean isMoreNext;//加载下一页标志
    private long page = 1;//页数，默认为1

    private ViewPager mViewPager;
    private LinePageIndicator mIndicator;
    private Handler mHandler;

    public TopicsFindPager(MainActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initViews() {
        super.initViews();
        View headerView = View.inflate(mActivity, R.layout.list_header_toptopics,null);
        mViewPager = (ViewPager) headerView.findViewById(R.id.list_header_toptopics_viewpager);
        mIndicator = (LinePageIndicator) headerView.findViewById(R.id.list_header_toptopics_indicator);
        mListView.addHeaderView(headerView);
    }

    @Override
    public void initData() {
        initTopicData();//初始化数据
        refreshAndLoad();//监听刷新和加载更多
        listItemClick();//item点击事件
    }

    private void initTopicData(){
        String cache = CacheUtils.getCache(GlobalContants.FIND_TOPICS_LIST_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache,false);
        }
        getDataFromServer(false, true, true);
    }

    private void listItemClick(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic topic = (Topic) parent.getAdapter().getItem(position);
                LogUtils.d("topic_det", topic.topic_title);
                LogUtils.d("position", position + "");
                handleItemClick(topic);
            }
        });
    }

    private void handleItemClick(final Topic topic){
        RequestParams params = new RequestParams(GlobalContants.TOPIC_BROWSE_COUNT_URL);
        params.addQueryStringParameter("topic_key", topic.topic_key);
        x.http().post(params, new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
//                Intent intent = new Intent(mActivity, TopicNoteActivity.class);
//                intent.putExtra("topic_key", topic.topic_key);
//                intent.putExtra("topic_title", topic.topic_title);
//                mActivity.startActivity(intent);
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
                getDataFromServer(false, false, false);
                isMoreNext = false;
            }

            @Override
            public void onLoadMore() {
                if (isMoreNext == false) {
                    page++;
                    getDataFromServer(true, false, false);
                } else {
                    Toast.makeText(mActivity, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                    mListView.onRefreshComplete(false);// 收起加载更多的布局
                }
            }
        });
    }

    private void getDataFromServer(final boolean isMoreNext, final boolean loadingFlag, final boolean isHttpCache){
        if (loadingFlag == true){
            loadingView.setVisibility(View.VISIBLE);
        }
        LogUtils.d("isHttpCache", isHttpCache+"");
        //下拉刷新和加载更多不设置缓存
        RequestParams params = new RequestParams(GlobalContants.FIND_TOPICS_LIST_URL);
        params.addQueryStringParameter("page", page+"");
        params.setCacheMaxAge(1000 * 60);
        x.http().get(params, new Callback.CacheCallback<String>() {

            @Override
            public boolean onCache(String result) {
                LogUtils.d("++++cache", result);
                //afterHttp(result);
                return isHttpCache;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.d("++++result", result);
                //afterHttp(result);
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
                if (loadingFlag == true){
                    loadingView.setVisibility(View.GONE);
                }
                LogUtils.d("", "访问服务器结束");

            }

            public void afterHttp(String result){
                parseData(result, isMoreNext);
                mListView.onRefreshComplete(true);
                if (page == 1) {//缓存第一页的数据
                    CacheUtils.setCache(GlobalContants.FIND_TOPICS_LIST_URL, result, mActivity);
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
                ArrayList<Topic> topics = new ArrayList<>();
                ArrayList<Topic>  topTopics = new ArrayList<>();
                JSONArray array = object1.getJSONArray("rows");
                LogUtils.d("array", array.toString());
                JSONObject object = null;
                Topic topic = null;
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    topic = gson.fromJson(object.toString(), Topic.class);
                    topics.add(topic);
                    object = null;
                    topic = null;
                }
                //遍历出顶部话题
                for (Topic thisTopic : topics) {
                    if ("Y".equals(thisTopic.top_topic_yn)){
                        topTopics.add(thisTopic);
                    }
                }

                topicMap.put("topics", topics);
                topicMap.put("topTopics", topTopics);

                topics = null;
                topTopics = null;

                if (!isMore){
                    mTopics = (ArrayList<Topic>) topicMap.get("topics");
                    mTopTopics = (ArrayList<Topic>) topicMap.get("topTopics");
                    if (mTopics != null){
                        topicsAdapter = new TopicsAdapter(mActivity, mTopics);
                        mListView.setAdapter(topicsAdapter);
                    }
                    if (mTopTopics != null){
                        mViewPager.setAdapter(new ToTopicsAdapter(mActivity, mTopTopics));
                        mIndicator.setViewPager(mViewPager);
                        //mIndicator.setSnap(true);// 支持快照显示
                        //mIndicator.setOnPageChangeListener(this);
                        mIndicator.onPageSelected(0);// 让指示器重新定位到第一个点
                    }
                }else{
                    ArrayList<Topic> moreTopics = (ArrayList<Topic>) topicMap.get("topics");
                    LogUtils.d("moreTopics", moreTopics.size()+"");
                    if (moreTopics.size() != 0){
                      //  mTopics.addAll(moreTopics);
                        topicsAdapter.addMore(moreTopics);
                    }else{
                        isMoreNext = true;
                        Toast.makeText(mActivity, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                        mListView.onRefreshComplete(false);// 收起加载更多的布局
                    }
                }
                // 自动轮播条显示
                if (mHandler == null) {
                    mHandler = new Handler() {
                        public void handleMessage(android.os.Message msg) {
                            int currentItem = mViewPager.getCurrentItem();

                            if (currentItem < mTopTopics.size() - 1) {
                                currentItem++;
                            } else {
                                currentItem = 0;
                            }

                            mViewPager.setCurrentItem(currentItem);// 切换到下一个页面
                            mHandler.sendEmptyMessageDelayed(0, 3000);// 继续延时3秒发消息,
                            // 形成循环
                        };
                    };
                    mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒后发消息
                }

            }else{
                Toast.makeText(x.app(), "数据异常，返回码：" + returnCode, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
