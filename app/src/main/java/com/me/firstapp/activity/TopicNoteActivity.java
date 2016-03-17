package com.me.firstapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.adapter.NotePagerListAdapter;
import com.me.firstapp.adapter.TopicNotesViewAdapter;
import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.manager.ActivityManager;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.view.RefreshListView;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
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
@ContentView(R.layout.activity_topic_notes)
public class TopicNoteActivity extends Activity implements ViewPager.OnPageChangeListener {
    @ViewInject(R.id.activity_topic_note_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_topic_note_tab_title)
    private TextView tvTitle;
    @ViewInject(R.id.activity_topic_note_tab_indicator)
    private TabPageIndicator mIndicator;
    @ViewInject(R.id.activity_topic_note_viewpager)
    private ViewPager mViewPager;
    @ViewInject(R.id.activity_topic_notes_img_btn)
    private ImageButton mImageButton;
    @ViewInject(R.id.activity_topic_notes_tv_put)
    private TextView mTextView;
    @ViewInject(R.id.activity_topic_notes_btn_send_note)
    private Button btnSendNote;

    private View noteNewView;
    private View noteHotView;
    private RefreshListView newListView;
    private RefreshListView hotListView;

    private ActivityManager activityManager;

    private ArrayList<View> views = new ArrayList<>();;
    private String topicKey;
    private String topicTitle;

    private HashMap<String, Object> dataMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        activityManager = ActivityManager.getInstance();
        activityManager.pushActivity(this);

        topicKey = getIntent().getStringExtra("topic_key");
        topicTitle = getIntent().getStringExtra("topic_title");
        LogUtils.d("topicKey", topicKey);
        mIndicator.setOnPageChangeListener(this);

        tvTitle.setText(topicTitle);

        noteNewView = View.inflate(this, R.layout.view_topic_notes_pager_new, null);
        noteHotView = View.inflate(this, R.layout.view_topic_notes_pager_hot, null);
        newListView = (RefreshListView) noteNewView.findViewById(R.id.view_topic_notes_pager_new_list);
        hotListView = (RefreshListView) noteHotView.findViewById(R.id.view_topic_notes_pager_hot_list);
        views.add(noteNewView);
        views.add(noteHotView);

        mViewPager.setAdapter(new TopicNotesViewAdapter(views));
        mIndicator.setViewPager(mViewPager);

        setViewClick();
        initData();
    }

    private void setViewClick(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.activity_topic_note_btn_back :
                        break;
                    case R.id.activity_topic_notes_img_btn :
                        break;
                    case R.id.activity_topic_notes_tv_put :
                        Intent intent = new Intent(TopicNoteActivity.this, SendNoteActivity.class);
                        intent.putExtra("topic_key", topicKey);
                        intent.putExtra("topic_title", topicTitle);
                        startActivity(intent);
                        break;
                    case R.id.activity_topic_notes_btn_send_note :
                        break;
                }
            }
        };
        btnBack.setOnClickListener(listener);
        mImageButton.setOnClickListener(listener);
        mTextView.setOnClickListener(listener);
        btnSendNote.setOnClickListener(listener);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityManager.popActivity(this);
    }

    private void initData(){
        RequestParams params = new RequestParams(GlobalContants.NEW_NOTES_LIST_URL);
        params.addQueryStringParameter("topic_key", topicKey);
        params.setCacheMaxAge(1000 * 60);
        x.http().get(params, new Callback.CacheCallback<String>() {
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

            @Override
            public boolean onCache(String result) {
                LogUtils.d("result", result);
                parseData(result);
                return true;
            }
        });
    }

    private void parseData(String result){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if ("000000".equals(returnCode)){
                ArrayList<Note> new_notes = new ArrayList<>();
                ArrayList<User> new_users = new ArrayList<>();

                JSONArray array = object1.getJSONArray("new_note_rows");
                JSONObject object2;
                Note note;
                User user;
                for (int i = 0; i < array.length(); i++) {
                    object2 = array.getJSONObject(i);
                    note = gson.fromJson(object2.toString(), Note.class);
                    new_notes.add(note);

                    user = gson.fromJson(object2.toString(), User.class);
                    new_users.add(user);

                    object2 = null;
                    note = null;
                    user = null;
                }
                dataMap.put("new_notes", new_notes);
                dataMap.put("new_users", new_users);
                LogUtils.d("new_notes", new_notes.toString());
                LogUtils.d("new_users", new_users.toString());
                new_notes = null;
                new_users = null;

                ArrayList<Note> hot_notes = new ArrayList<>();
                ArrayList<User> hot_users = new ArrayList<>();
                array = object1.getJSONArray("hot_note_rows");
                for (int i = 0; i < array.length(); i++) {
                    object2 = array.getJSONObject(i);
                    note = gson.fromJson(object2.toString(), Note.class);
                    hot_notes.add(note);

                    user = gson.fromJson(object2.toString(), User.class);
                    hot_users.add(user);

                    object2 = null;
                    note = null;
                    user = null;
                }
                dataMap.put("hot_notes", hot_notes);
                dataMap.put("hot_users", hot_users);
                LogUtils.d("hot_notes", hot_notes.toString());
                LogUtils.d("hot_users", hot_users.toString());
                hot_notes = null;
                hot_users = null;

                setListView(dataMap);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListView(HashMap<String, Object> dataMap){
        ArrayList<Note> new_notes = (ArrayList<Note>) dataMap.get("new_notes");
        ArrayList<User> new_users = (ArrayList<User>) dataMap.get("new_users");
        if (new_notes != null && new_users != null){
            newListView.setAdapter(new NotePagerListAdapter(this, new_notes, new_users));
        }

        ArrayList<Note> hot_notes = (ArrayList<Note>) dataMap.get("hot_notes");
        ArrayList<User> hot_users = (ArrayList<User>) dataMap.get("hot_users");
        if (hot_notes != null && hot_users != null){
            hotListView.setAdapter(new NotePagerListAdapter(this, hot_notes, hot_users));
        }
    }
}
