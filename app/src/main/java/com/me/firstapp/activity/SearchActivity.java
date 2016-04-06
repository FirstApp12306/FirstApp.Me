package com.me.firstapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.adapter.SearchGridViewAdapter;
import com.me.firstapp.adapter.TopicsAdapter;
import com.me.firstapp.entity.Topic;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.RefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.activity_search_et)
    private EditText mEditText;
    @ViewInject(R.id.activity_search_btn_cancel)
    private Button btnCancel;
    @ViewInject(R.id.activity_search_btn_clear)
    private ImageButton btnClear;
    @ViewInject(R.id.activity_search_listview)
    private RefreshListView mListView;

    private View headerView;
    private GridView mGridView;
    private LinearLayout llMore;


    private String loginUserID;
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginUserID = PrefUtils.getString(this, "loginUser", null);
        headerView = View.inflate(this, R.layout.search_list_header_view, null);
        mGridView = (GridView) headerView.findViewById(R.id.search_list_header_view_gv);
        llMore = (LinearLayout) headerView.findViewById(R.id.search_list_header_view_ll_more);
        mListView.addHeaderView(headerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnCancel.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        llMore.setOnClickListener(this);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    page = 1;
                    getDataFromServer(false);
                } else {
                    if (userAdapter != null) {
                        userAdapter.clearList();
                        topicAdapter.clearList();
                    }
                }

                if (users != null) {
                    if (users.isEmpty()) {
                        llMore.setVisibility(View.GONE);
                    } else {
                        llMore.setVisibility(View.VISIBLE);
                    }
                }

                if (TextUtils.isEmpty(s.toString())){
                    btnClear.setVisibility(View.GONE);
                }else{
                    btnClear.setVisibility(View.VISIBLE);
                }
            }
        });

        mListView.setPullRefreshAble(false);

        mListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                page++;
                getDataFromServer(true);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic topic = (Topic) parent.getAdapter().getItem(position);
                LogUtils.d("topic_det", topic.topic_title);
                LogUtils.d("position", position + "");

                handleItemClick(topic.topic_key);//请求网络本身就是异步了

                Intent intent = new Intent(SearchActivity.this, TopicNoteActivity.class);
                intent.putExtra("topic_key", topic.topic_key);
                intent.putExtra("topic_title", topic.topic_title);
                startActivity(intent);
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getAdapter().getItem(position);
                Intent intent = new Intent(SearchActivity.this, PersonInfoActivity.class);
                intent.putExtra("user_name", user.user_name);
                intent.putExtra("user_id", user.user_id);
                intent.putExtra("user_avatar", user.user_avatar);
                intent.putExtra("user_level", user.user_level);
                intent.putExtra("user_city", user.user_city);
                intent.putExtra("signature", user.user_signature);
                intent.putExtra("user_phone", user.user_phone);
                intent.putExtra("fans_flag", user.fans_flag);
                startActivity(intent);

            }
        });

        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int colNum = mGridView.getNumColumns();
                LogUtils.d("colNum", colNum + "");
                if (users.size() <= colNum) {
                    llMore.setVisibility(View.GONE);
                }
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

    private void getDataFromServer(final boolean isMore) {
        RequestParams params = new RequestParams(GlobalContants.SEARCH_URL);
        params.addQueryStringParameter("user_id", loginUserID);
        params.addQueryStringParameter("search", mEditText.getText().toString());
        params.addQueryStringParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result, isMore);
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

    private ArrayList<User> users;
    private ArrayList<Topic> topics;
    private TopicsAdapter topicAdapter;
    private SearchGridViewAdapter userAdapter;

    private void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String return_code = object1.getString("return_code");
            if ("000000".equals(return_code)) {
                users = new ArrayList<>();
                JSONArray user_rows = object1.getJSONArray("user_rows");
                for (int i = 0; i < user_rows.length(); i++) {
                    JSONObject object2 = user_rows.getJSONObject(i);
                    User user = gson.fromJson(object2.toString(), User.class);
                    users.add(user);
                }
                LogUtils.d("users", users.toString());

                topics = new ArrayList<>();
                JSONArray topic_rows = object1.getJSONArray("topic_rows");
                for (int i = 0; i < topic_rows.length(); i++) {
                    JSONObject object3 = topic_rows.getJSONObject(i);
                    Topic topic = gson.fromJson(object3.toString(), Topic.class);
                    topics.add(topic);
                }
                LogUtils.d("topics", topics.toString());

                if (!isMore) {
                    if (topics != null) {
                        topicAdapter = new TopicsAdapter(this, topics);
                        mListView.setAdapter(topicAdapter);
                    }
                    if (users != null) {
                        userAdapter = new SearchGridViewAdapter(this, users);
                        mGridView.setAdapter(userAdapter);
                        if (users.isEmpty()){
                            llMore.setVisibility(View.GONE);
                        }else{
                            llMore.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    if (topics != null) {
                        topicAdapter.addMore(topics);
                    }
//                    if (users != null) {
//                        userAdapter.addMore(users);
//                    }
                }



            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            mListView.onRefreshComplete(true);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_search_btn_clear :
                mEditText.setText("");
                btnClear.setVisibility(View.GONE);
                break;
            case R.id.activity_search_btn_cancel :
                finish();
                break;
            case R.id.search_list_header_view_ll_more :
                Intent intent = new Intent(this, UserActivity.class);
                intent.putExtra("search", mEditText.getText().toString());
                startActivity(intent);
                break;
        }
    }
}
