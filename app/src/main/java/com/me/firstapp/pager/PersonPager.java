package com.me.firstapp.pager;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.activity.MainActivity;
import com.me.firstapp.activity.profile.ProfileActivity;
import com.me.firstapp.activity.settings.SettingsActivity;
import com.me.firstapp.adapter.FirstPagerListAdapter;
import com.me.firstapp.adapter.PersonPagerViewAdapter;
import com.me.firstapp.adapter.TopicsAdapter;
import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.Topic;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.DatabaseUtils;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.CircleImageView;
import com.me.firstapp.view.HoveringScrollview;
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
public class PersonPager extends BasePager implements HoveringScrollview.OnScrollListener {

    private View view;

    private HoveringScrollview hoveringScrollview;
    private int searchLayoutTop;
    private LinearLayout hoveringLayout;
    private LinearLayout search01;
    private LinearLayout search02;
    private LinearLayout llayout;
    private ViewPager mViewPager;
    private View topicView;
    private View noteView;
    private RadioGroup nRadioGroup;
    private RefreshListView topicListView;
    private RefreshListView noteListView;
    private LinearLayout vpLinearLayout;
    private RelativeLayout pRelativeLayout;

    private CircleImageView ivUserAvatar;
    private TextView tvUserName;
    private TextView tvUserLevel;
    private TextView tvUserID;
    private TextView tvUserCity;
    private TextView tvUserFollow;
    private TextView tvUserFans;
    private Button btnFav;
    private TextView tvSignature;
    private Button btnEdit;


    private ArrayList<View> views = new ArrayList<>();
    private String userID;

    private DatabaseUtils databaseUtils;

    public PersonPager(MainActivity activity) {
        super(activity);
        userID = PrefUtils.getString(mActivity, "loginUser", null);
        databaseUtils = new DatabaseUtils(mActivity);
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

    float rgHigh;//viewpager顶部位置距离根布局顶部的高度
    float pHigh;//根布局的高度

    @Override
    public void initData() {
        LogUtils.d("", "初始化Person页面数据。。。。。。。");

        view = View.inflate(mActivity, R.layout.pager_person, null);
        init(view);
        setClick();

        //现在本地获取，再在服务器更新最新数据
        if (!TextUtils.isEmpty(userID)) {
            User user = databaseUtils.queryUser(userID);
            LogUtils.d("useruser", user.toString());
            setUserInfo(user);
        }

        topicView = View.inflate(mActivity, R.layout.view_pager_person_topic, null);
        noteView = View.inflate(mActivity, R.layout.view_pager_person_note, null);
        topicListView = (RefreshListView) topicView.findViewById(R.id.view_pager_person_topic_listview);
        noteListView = (RefreshListView) noteView.findViewById(R.id.view_pager_person_note_listview);
        topicListView.setPullRefreshAble(false);
        noteListView.setPullRefreshAble(false);

        hoveringScrollview.setOnScrollListener(this);// set Listener
        nRadioGroup.check(R.id.pager_person_rbtn_topic);

        nRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, SettingsActivity.class));
            }
        });

        //动态设置viewpager的高度--开始
        search02.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rgHigh = search02.getBottom();//viewpager顶部位置距离根布局顶部的高度
//                    LogUtils.d("rgHigh", rgHigh + "");
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (pHigh - rgHigh));//这里的单位是px，不是dp
                vpLinearLayout.setLayoutParams(params);
            }
        });

        pRelativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pHigh = pRelativeLayout.getHeight();//根布局的高度
//                    LogUtils.d("pHigh", pHigh + "");
            }
        });
        //动态设置viewpager的高度--结束

        views.add(topicView);
        views.add(noteView);
        mViewPager.setAdapter(new PersonPagerViewAdapter(views));

        getDataFromServer();

        flContent.removeAllViews();
        flContent.addView(view);// 向FrameLayout中动态添加布局
    }

    private void init(View view) {
        hoveringLayout = (LinearLayout) view.findViewById(R.id.hoveringLayout);
        hoveringScrollview = (HoveringScrollview) view.findViewById(R.id.pager_person_scrollview);
        search01 = (LinearLayout) view.findViewById(R.id.search01);
        search02 = (LinearLayout) view.findViewById(R.id.search02);
        llayout = (LinearLayout) view.findViewById(R.id.pager_person_ll_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.pager_person_view_pager);
        nRadioGroup = (RadioGroup) view.findViewById(R.id.pager_person_radiogroup);
        vpLinearLayout = (LinearLayout) view.findViewById(R.id.vpLinearLayout);
        pRelativeLayout = (RelativeLayout) view.findViewById(R.id.person_rllayout);

        ivUserAvatar = (CircleImageView) view.findViewById(R.id.person_pager_avatar);
        tvUserName = (TextView) view.findViewById(R.id.person_pager_nickname);
        tvUserLevel = (TextView) view.findViewById(R.id.person_pager_level);
        tvUserID = (TextView) view.findViewById(R.id.person_pager_id);
        tvUserCity = (TextView) view.findViewById(R.id.person_pager_location);
        tvUserFollow = (TextView) view.findViewById(R.id.person_pager_attention);
        tvUserFans = (TextView) view.findViewById(R.id.person_pager_fans);
        btnFav = (Button) view.findViewById(R.id.person_pager_btn_favourite);
        tvSignature = (TextView) view.findViewById(R.id.person_pager_sign);
        btnEdit = (Button) view.findViewById(R.id.person_pager_btn_edit_data);
    }

    private void setClick() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.person_pager_btn_favourite:
                        break;
                    case R.id.person_pager_btn_edit_data:
                        Intent intent = new Intent(mActivity, ProfileActivity.class);
                        intent.putExtra("user_id", userID);
                        mActivity.startActivity(intent);
                        break;
                    case R.id.pager_base_btn_setting:
                        break;
                }
            }
        };
        btnFav.setOnClickListener(listener);
        btnEdit.setOnClickListener(listener);
        btnSetting.setOnClickListener(listener);
    }

    private void setUserInfo(User mUser) {

        if (mUser != null) {
            ImageUtils.bindImageWithOptions(ivUserAvatar,
                    mUser.user_avatar, R.drawable.person_avatar_default_round,
                    R.drawable.person_avatar_default_round);
            tvUserName.setText(mUser.user_name);
            tvUserLevel.setText("等级:" + mUser.user_level);
            tvUserID.setText("ID:" + mUser.user_id);
            tvUserCity.setText(mUser.user_city);
            tvSignature.setText(mUser.user_signature);
            tvUserFollow.setText(mUser.follow);
            tvUserFans.setText(mUser.fans);
        }


    }


    @Override
    public void onScroll(int scrollY) {
        searchLayoutTop = llayout.getBottom();
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

    private void getDataFromServer() {
        RequestParams params = new RequestParams(GlobalContants.MY_TOPICS_LIST_URL);
        params.addQueryStringParameter("user_id", userID);
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

    private ArrayList<Topic> topics;

    private ArrayList<Note> notes;
    private ArrayList<User> users;
    private ArrayList<Topic> noteTopics;

    private void parseData(String result) {
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
                if (topics != null) {
                    topicListView.setAdapter(new TopicsAdapter(mActivity, topics));
                }

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

                if (notes != null && users != null && noteTopics != null) {
                    noteListView.setAdapter(new FirstPagerListAdapter(mActivity, notes, users, noteTopics));
                }

                JSONObject object3 = object1.getJSONObject("user");
                User user1 = gson.fromJson(object3.toString(), User.class);
                LogUtils.d("user1", user1.toString());
                setUserInfo(user1);

                //更新本地user信息
                updateLocalUser(user1);
            } else {
                Toast.makeText(x.app(), "数据异常，返回码：" + returnCode, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
//            mListView.onRefreshComplete(false);// 收起加载更多的布局
        }

    }

    private void updateLocalUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put("id", user.user_id);
        cv.put("name", user.user_name);
        cv.put("phone", user.user_phone);
        cv.put("avatar", user.user_avatar);
        cv.put("signature", user.user_signature);
        cv.put("sex", user.user_sex);
        cv.put("level", user.user_level);
        cv.put("points", user.user_points);
        cv.put("sts", user.sts);
        cv.put("login_sts", "01");
        cv.put("city", user.user_city);
        cv.put("fans", user.fans);
        cv.put("follow", user.follow);
        databaseUtils.updateTable("user", cv, "id = ?", new String[]{user.user_id});
    }
}
