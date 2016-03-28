package com.me.firstapp.pager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.activity.LoginActivity;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.activity.SignUpActivity;
import com.me.firstapp.adapter.FindPagerListAdapter;
import com.me.firstapp.adapter.PersonPagerViewAdapter;
import com.me.firstapp.adapter.ToTopicsAdapter;
import com.me.firstapp.adapter.TopicsAdapter;
import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.Topic;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.HoveringScrollview;
import com.me.firstapp.view.MyScrollView;

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
public class PersonPager extends BasePager implements HoveringScrollview.OnScrollListener {

    private View view;
    private Button btnLogin;
    private Button btnSignUp;
    private MainActivity activity;

    private HoveringScrollview hoveringScrollview;
    private int searchLayoutTop;
    private LinearLayout hoveringLayout;
    private LinearLayout search01, search02;
    private LinearLayout llayout;
    private ViewPager mViewPager;
    private View topicView;
    private View noteView;
    private RadioGroup nRadioGroup;
    private RadioButton rbtnTopic;
    private RadioButton rbtnNote;
    private ListView topicListView;
    private ListView noteListView;

    private ArrayList<View> views = new ArrayList<>();
    private String userID;
    private boolean isLogin;

    public PersonPager(MainActivity activity) {
        super(activity);
        this.activity = activity;
        isLogin = PrefUtils.getBoolean(mActivity, "login_flag", false);
        userID = PrefUtils.getString(mActivity, "loginUser", null);
    }

    @Override
    public void initViews() {
        super.initViews();
        mRadioGroup.setVisibility(View.GONE);
        redCircle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        btnSetting.setVisibility(View.VISIBLE);
        tvTitle.setText("我");
    }

    @Override
    public void initData() {
        LogUtils.d("", "初始化Person页面数据。。。。。。。");

        if (isLogin){
            view = View.inflate(mActivity, R.layout.pager_person, null);
            hoveringLayout = (LinearLayout) view.findViewById(R.id.hoveringLayout);
            hoveringScrollview = (HoveringScrollview) view.findViewById(R.id.pager_person_scrollview);
            search01 = (LinearLayout) view.findViewById(R.id.search01);
            search02 = (LinearLayout) view.findViewById(R.id.search02);
            llayout = (LinearLayout) view.findViewById(R.id.pager_person_ll_layout);
            mViewPager = (ViewPager) view.findViewById(R.id.pager_person_view_pager);
            nRadioGroup = (RadioGroup) view.findViewById(R.id.pager_person_radiogroup);
            rbtnTopic = (RadioButton) view.findViewById(R.id.pager_person_rbtn_topic);
            rbtnNote = (RadioButton) view.findViewById(R.id.pager_person_rbtn_note);
            hoveringScrollview.setOnScrollListener(this);// set Listener
            nRadioGroup.check(R.id.pager_person_rbtn_topic);

            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.pager_person_rbtn_topic:
                            mViewPager.setCurrentItem(0, true);
                            break;
                        case R.id.pager_person_rbtn_note:
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
                            nRadioGroup.check(R.id.pager_person_rbtn_topic);
                            break;
                        case 1:
                            nRadioGroup.check(R.id.pager_person_rbtn_note);
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            topicView = View.inflate(mActivity, R.layout.view_pager_person_topic, null);
            noteView = View.inflate(mActivity, R.layout.view_pager_person_note, null);
            topicListView = (ListView) topicView.findViewById(R.id.view_pager_person_topic_listview);
            noteListView = (ListView) noteView.findViewById(R.id.view_pager_person_note_listview);

            views.add(topicView);
            views.add(noteView);
            mViewPager.setAdapter(new PersonPagerViewAdapter(views));

            getTopicDataFromServer();
            getNoteDataFromServer();

        }else{
            view = View.inflate(mActivity, R.layout.pager_unlogin_tip, null);
            btnLogin = (Button) view.findViewById(R.id.pager_unlogin_btn_login);
            btnSignUp = (Button) view.findViewById(R.id.pager_unlogin_btn_regest);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.pager_unlogin_btn_login:
                            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                            break;
                        case R.id.pager_unlogin_btn_regest:
                            mActivity.startActivity(new Intent(mActivity, SignUpActivity.class));
                            break;
                        default:
                            break;
                    }
                }
            };

            btnLogin.setOnClickListener(listener);
            btnSignUp.setOnClickListener(listener);
        }


        flContent.removeAllViews();
        flContent.addView(view);// 向FrameLayout中动态添加布局
    }


    @Override
    public void onScroll(int scrollY) {
        searchLayoutTop = llayout.getBottom();
        LogUtils.d("searchLayoutTop", searchLayoutTop + "");
        if (scrollY >= searchLayoutTop) {
            if (hoveringLayout.getParent() != search01) {
                search02.removeView(hoveringLayout);
                search01.addView(hoveringLayout);
                search01.setVisibility(View.VISIBLE);
            }
        } else {
            if (hoveringLayout.getParent() != search02) {
                search01.removeView(hoveringLayout);
                search02.addView(hoveringLayout);
                search01.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void getTopicDataFromServer(){
        RequestParams params = new RequestParams(GlobalContants.MY_TOPICS_LIST_URL);
        params.addQueryStringParameter("user_id", userID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseTopicData(result);
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
    private void parseTopicData(String result){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if("000000".equals(returnCode)){
                topics = new ArrayList<>();
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
                LogUtils.d("topics", topics.toString());
                if (topics != null){
                    topicListView.setAdapter(new TopicsAdapter(mActivity, topics));
                }

            }else{
                Toast.makeText(x.app(), "数据异常，返回码：" + returnCode, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
//            mListView.onRefreshComplete(false);// 收起加载更多的布局
        }

    }

    private void getNoteDataFromServer(){
        RequestParams params = new RequestParams(GlobalContants.MY_NOTES_LIST_URL);
        params.addQueryStringParameter("user_id", userID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("NOTEresult", result);
                parseNoteData(result);
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
    private ArrayList<Topic> noteTopics;
    private void parseNoteData(String result){
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
                    noteTopics.add(topic);

                    object2 = null;
                    note = null;
                    user = null;
                    topic = null;
                }

                LogUtils.d("notes", notes.toString());
                LogUtils.d("users", users.toString());
                LogUtils.d("topics", topics.toString());

                if(notes != null && users != null && topics != null){
                    noteListView.setAdapter(new FindPagerListAdapter(mActivity, notes, users, topics));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
//            mListView.onRefreshComplete(true);// 收起加载更多的布局
        }
    }
}
