package com.me.firstapp.pager;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.activity.CreateTopicFirstActivity;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.adapter.TopicsAdapter;
import com.me.firstapp.entity.Topic;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;

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
    private ListView lvList;

    private ArrayList<Topic> mTopics = new ArrayList<>();
    private TopicsAdapter topicsAdapter;

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
            lvList = (ListView) view.findViewById(R.id.pager_topic_listview);
        }else{
            view = View.inflate(mActivity, R.layout.pager_unlogin_tip, null);
        }

        flContent.removeAllViews();
        flContent.addView(view);// 向FrameLayout中动态添加布局

        getDataFromServer();
    }

    private void getDataFromServer(){
        RequestParams params = new RequestParams(GlobalContants.TOPICS_LIST_URL);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result, true);
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

    private void parseData(String result,boolean isMore){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if("000000".equals(returnCode)){
                JSONArray array = object1.getJSONArray("rows");
                LogUtils.d("array", array.toString());
                for (int i = 0; i < array.length(); i++) {
                    mTopics.add((Topic) array.get(i));
                }
                LogUtils.d("mTopics", mTopics.toString());
                topicsAdapter = new TopicsAdapter(mActivity, mTopics);
                lvList.setAdapter(topicsAdapter);
            }else{
                Toast.makeText(x.app(), "数据异常，返回码：" + returnCode, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
