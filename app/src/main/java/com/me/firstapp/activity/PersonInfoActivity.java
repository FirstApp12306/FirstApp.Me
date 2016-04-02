package com.me.firstapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.adapter.FirstPagerListAdapter;
import com.me.firstapp.adapter.PersonPagerViewAdapter;
import com.me.firstapp.adapter.TopicsAdapter;
import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.Topic;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.CacheUtils;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.RefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
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
@ContentView(R.layout.activity_person_info)
public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.activity_person_info_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_person_info_user_name)
    private TextView tvUserName;
    @ViewInject(R.id.activity_person_info_avatar)
    private ImageView ivAvatar;
    @ViewInject(R.id.activity_person_info_user_level)
    private TextView tvUserLevel;
    @ViewInject(R.id.activity_person_info_user_id)
    private TextView tvUserID;
    @ViewInject(R.id.activity_person_info_user_city)
    private TextView tvUserCity;
    @ViewInject(R.id.activity_person_info_btn_follow)
    private ImageButton btnFollow;
    @ViewInject(R.id.activity_person_info_btn_msg)
    private ImageButton btnMsg;
    @ViewInject(R.id.activity_person_info_signature)
    private TextView tvSignature;
    @ViewInject(R.id.activity_person_info_viewpager)
    private ViewPager mViewPager;
    @ViewInject(R.id.activity_person_info_rg)
    private RadioGroup mRadioGroup;

    private View topicView;
    private View noteView;
    private RefreshListView topicListView;
    private RefreshListView noteListView;

    private ArrayList<View> views = new ArrayList<>();

    private String user_phone;
    private String user_name;
    private String fans_flag;
    private String loginUserID;
    private String user_id;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        topicView = View.inflate(this, R.layout.view_pager_person_topic, null);
        noteView = View.inflate(this, R.layout.view_pager_person_note, null);
        topicListView = (RefreshListView) topicView.findViewById(R.id.view_pager_person_topic_listview);
        noteListView = (RefreshListView) noteView.findViewById(R.id.view_pager_person_note_listview);
        views.add(noteView);
        views.add(topicView);

        mViewPager.setAdapter(new PersonPagerViewAdapter(views));

        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        user_id = intent.getStringExtra("user_id");
        String user_avatar = intent.getStringExtra("user_avatar");
        long user_level = intent.getLongExtra("user_level", 0);
        String user_city = intent.getStringExtra("user_city");
        String signature = intent.getStringExtra("signature");
        user_phone = intent.getStringExtra("user_phone");
        fans_flag = intent.getStringExtra("fans_flag");

        tvUserName.setText(user_name);
        tvSignature.setText(signature);
        tvUserCity.setText(user_city);
        tvUserID.setText("ID:" + user_id);
        tvUserLevel.setText("等级:" + user_level);

        ImageUtils.bindImageWithOptions(ivAvatar, user_avatar,
                R.drawable.person_avatar_default_round,
                R.drawable.person_avatar_default_round);

        String cache = CacheUtils.getCache(GlobalContants.MY_TOPICS_LIST_URL, this);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache, false);
        }
        getDataFromServer(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loginUserID = PrefUtils.getString(this, "loginUser", null);

        btnBack.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        btnMsg.setOnClickListener(this);

        if ("true".equals(fans_flag)) {
            btnFollow.setBackgroundResource(R.drawable.icon_user_unfollow);
        } else {
            btnFollow.setBackgroundResource(R.drawable.icon_user_follow);
        }

        mRadioGroup.check(R.id.activity_person_info_rbt_note);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.activity_person_info_rbt_note:
                        mViewPager.setCurrentItem(0, true);
                        break;
                    case R.id.activity_person_info_rbt_topic:
                        mViewPager.setCurrentItem(1, true);
                        break;
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mRadioGroup.check(R.id.activity_person_info_rbt_note);
                        break;
                    case 1:
                        mRadioGroup.check(R.id.activity_person_info_rbt_topic);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        topicListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
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
        noteListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_person_info_btn_back:
                finish();
                break;
            case R.id.activity_person_info_btn_follow:
                sendFansDataToServer();
                break;
            case R.id.activity_person_info_btn_msg:
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("targetID", user_phone);
                intent.putExtra("user_name", user_name);
                startActivity(intent);
                break;
        }
    }

    private void sendFansDataToServer() {
        RequestParams params;
        if ("true".equals(fans_flag)) {
            //取消粉丝
            params = new RequestParams(GlobalContants.DELETE_FRIEND_URL);
            btnFollow.setBackgroundResource(R.drawable.icon_user_follow);
            fans_flag = "false";
        } else {
            // 新增粉丝
            params = new RequestParams(GlobalContants.ADD_FRIEND_URL);
            btnFollow.setBackgroundResource(R.drawable.icon_user_unfollow);
            fans_flag = "true";
        }

        params.addQueryStringParameter("user_id", user_id);
        params.addQueryStringParameter("fans_id", loginUserID);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                if ("true".equals(fans_flag)) {

                } else {

                }
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
        RequestParams params = new RequestParams(GlobalContants.MY_TOPICS_LIST_URL);
        params.addQueryStringParameter("user_id", user_id);
        params.addQueryStringParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result, isMore);
                if (page == 1) {
                    CacheUtils.setCache(GlobalContants.MY_TOPICS_LIST_URL, result, PersonInfoActivity.this);
                }
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

    private ArrayList<Topic> topics;

    private ArrayList<Note> notes;
    private ArrayList<User> users;
    private ArrayList<Topic> noteTopics;

    private void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if ("000000".equals(returnCode)) {
                topics = new ArrayList<>();
                JSONArray array = object1.getJSONArray("topic_rows");
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
                LogUtils.d("topics", topics.toString());

                notes = new ArrayList<>();
                users = new ArrayList<>();
                noteTopics = new ArrayList<>();
                JSONArray array2 = object1.getJSONArray("note_rows");
                JSONObject object2;
                Note note;
                User user;
                Topic noteTopic;
                for (int i = 0; i < array2.length(); i++) {
                    object2 = array2.getJSONObject(i);
                    note = gson.fromJson(object2.toString(), Note.class);
                    notes.add(note);

                    user = gson.fromJson(object2.toString(), User.class);
                    users.add(user);

                    noteTopic = gson.fromJson(object2.toString(), Topic.class);
                    noteTopics.add(noteTopic);

                    object2 = null;
                    note = null;
                    user = null;
                    noteTopic = null;
                }
                LogUtils.d("notes", notes.toString());
                LogUtils.d("users", users.toString());
                LogUtils.d("noteTopic", noteTopics.toString());

                setListView(isMore);
            } else {
                Toast.makeText(x.app(), "数据异常，返回码：" + returnCode, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            noteListView.onRefreshComplete(false);// 收起加载更多的布局
            topicListView.onRefreshComplete(false);// 收起加载更多的布局
        }

    }

    private TopicsAdapter topicsAdapter;
    private FirstPagerListAdapter firstPagerListAdapter;

    private void setListView(boolean isMore) {
        if (!isMore) {
            if (topics != null) {
                topicsAdapter = new TopicsAdapter(this, topics);
                topicListView.setAdapter(topicsAdapter);
            }

            if (notes != null && users != null && noteTopics != null) {
                firstPagerListAdapter = new FirstPagerListAdapter(this, notes, users, noteTopics);
                noteListView.setAdapter(firstPagerListAdapter);
            }
        } else {
            if (topics != null && topicsAdapter != null) {
                topicsAdapter.addMore(topics);
            }
            if (notes != null && users != null && noteTopics != null && firstPagerListAdapter != null) {
                firstPagerListAdapter.addMore(notes, users, noteTopics);
            }
        }

    }

}
