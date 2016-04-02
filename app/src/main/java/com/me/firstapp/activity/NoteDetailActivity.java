package com.me.firstapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.adapter.NoteDetailListAdapter;
import com.me.firstapp.adapter.NoteDetailPagerAdapter;
import com.me.firstapp.entity.Comment;
import com.me.firstapp.entity.Support;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.CacheUtils;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.utils.SoftInputUtils;
import com.me.firstapp.view.CircleImageView;
import com.me.firstapp.view.HoveringScrollview;
import com.me.firstapp.view.NoteDetailListView;
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

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_note_detail)
public class NoteDetailActivity extends Activity implements View.OnLayoutChangeListener ,HoveringScrollview.OnScrollListener {

    @ViewInject(R.id.activity_note_detail_rootview)
    private FrameLayout mRootView;
    @ViewInject(R.id.activity_note_detail_indicator)
    private TabPageIndicator mIndicator;
    @ViewInject(R.id.activity_note_detail_viewpager)
    private ViewPager mViewPager;
    @ViewInject(R.id.activity_note_detail_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_note_detail_topic_title)
    private TextView tvTitle;
    @ViewInject(R.id.activity_note_detail_avatar)
    private CircleImageView ivAvatar;
    @ViewInject(R.id.activity_note_detail_user_name)
    private TextView tvUserName;
    @ViewInject(R.id.activity_note_detail_note_image)
    private ImageView ivNoteImage;
    @ViewInject(R.id.activity_note_detail_note_content)
    private TextView tvNoteContent;
    @ViewInject(R.id.activity_note_detail_edit)
    private EditText mEditText;
    @ViewInject(R.id.activity_note_detail_btn_agree)
    private ImageButton btnAgree;
    @ViewInject(R.id.activity_note_detail_btn_pub)
    private Button btnPub;
    @ViewInject(R.id.activity_note_detail_sv)
    private HoveringScrollview mScrollView;

    private NoteDetailListView agreeListview;
    private NoteDetailListView commentListView;

    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private View agreeView;
    private View commentView;

    private ArrayList<View> views = new ArrayList<>();
    private String[] titles = new String[2];
    private String topic_key;
    private String note_key;
    private String note_image;
    private ArrayList<Support> supports;
    private ArrayList<Comment> comments;
    private Comment comment;
    private long page = 1;
    private String loginUserID;
    private String user_id;
    private String fans_flag;
    private String user_name;
    private String user_avatar;
    private String user_city;
    private String signature;
    private String user_level;
    private String user_phone;

    private int searchLayoutTop;
    @ViewInject(R.id.activity_note_detail_search01)
    private LinearLayout search01;
    @ViewInject(R.id.activity_note_detail_search02)
    private LinearLayout search02;
    @ViewInject(R.id.activity_note_detail_hoveringLayout)
    private LinearLayout hoveringLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        agreeView = View.inflate(this, R.layout.note_detail_view_pager_agree, null);
        commentView = View.inflate(this, R.layout.note_detail_view_pager_comment, null);
        agreeListview = (NoteDetailListView) agreeView.findViewById(R.id.note_detail_view_pager_agree_listview);
        commentListView = (NoteDetailListView) commentView.findViewById(R.id.note_detail_view_pager_comment_listview);

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;

        initLocalData();
        String cache = PrefUtils.getString(this, GlobalContants.NOTE_SUPPORT_COMMENT_LIST_URL + note_key, null);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache, false);
        }
        initServerData(false);
        setViewClick();

        views.add(agreeView);
        views.add(commentView);


        mViewPager.setAdapter(new NoteDetailPagerAdapter(views, titles));
        mIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(1);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loginUserID = PrefUtils.getString(this, "loginUser", null);
        mScrollView.setOnScrollListener(this);
        agreeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("OnItemClick", "item被点击");
            }
        });
        commentListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mScrollView.smoothScrollTo(0, 20);
            }
        });
        agreeListview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mScrollView.smoothScrollTo(0,20);
            }
        });
//        agreeListview.setOnRefreshListener(new NoteDetailListView.OnRefreshListener() {
//            @Override
//            public void onLoadMore() {
//                page++;
//                initServerData(true);
//
//            }
//        });
//        commentListView.setOnRefreshListener(new NoteDetailListView.OnRefreshListener() {
//            @Override
//            public void onLoadMore() {
//                page++;
//                initServerData(true);
//            }
//        });
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("OnItemClick", "item被点击");
                comment = (Comment) parent.getAdapter().getItem(position);
                mEditText.setHint("回复:" + comment.user_name);
                SoftInputUtils.showSoftInputWindow(NoteDetailActivity.this);
            }
        });

    }

    private void sendCommentDataToServer() {
        btnPub.setText("....");

        RequestParams params = new RequestParams(GlobalContants.NOTE_COMMENT_ADD_URL);
        params.addQueryStringParameter("content", mEditText.getText().toString());
        params.addQueryStringParameter("topic_key", topic_key);
        params.addQueryStringParameter("note_key", note_key);
        params.addQueryStringParameter("user_id", loginUserID);
        if (comment != null) {
            params.addQueryStringParameter("to_user_id", comment.user_id);
        }

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                btnPub.setText("发布");
                Toast.makeText(NoteDetailActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                mEditText.setText(null);
                SoftInputUtils.hideSoftInputWindow(NoteDetailActivity.this);

                Gson gson = new Gson();
                try {
                    JSONObject object1 = new JSONObject(result);
                    JSONObject object2 = object1.getJSONObject("resultMap");
                    Comment comment = gson.fromJson(object2.toString(), Comment.class);
                    LogUtils.d("comment", comment.toString());
                    if (commentAdapter != null) {
                        commentAdapter.addNewCom(comment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    //初始化本地数据
    private void initLocalData() {
        Intent intent = getIntent();
        topic_key = intent.getStringExtra("topic_key");
        user_id = intent.getStringExtra("user_id");
        user_city = intent.getStringExtra("user_city");
        signature = intent.getStringExtra("signature");
        user_level = intent.getStringExtra("user_level");
        user_phone = intent.getStringExtra("user_phone");
        fans_flag = intent.getStringExtra("fans_flag");
        String topicTitle = intent.getStringExtra("topic_title");
        user_avatar = intent.getStringExtra("user_avatar");
        user_name = intent.getStringExtra("user_name");
        String support_flag = intent.getStringExtra("support_flag");
        note_key = intent.getStringExtra("note_key");
        note_image = intent.getStringExtra("note_image");
        String note_content = intent.getStringExtra("note_content");
        long note_agree_counts = intent.getLongExtra("note_agree_counts", 0);
        long note_comment_counts = intent.getLongExtra("note_comment_counts", 0);
        titles[0] = note_agree_counts + " 赞";
        titles[1] = note_comment_counts + " 评论";

        tvTitle.setText(topicTitle);
        tvUserName.setText(user_name);
        ImageUtils.bindImageWithOptions(ivAvatar, user_avatar, R.drawable.person_avatar_default_round,
                R.drawable.person_avatar_default_round);
        if (TextUtils.isEmpty(note_image)) {
            ivNoteImage.setVisibility(View.GONE);
        } else {
            ivNoteImage.setVisibility(View.VISIBLE);
            ImageUtils.bindImage(ivNoteImage, note_image);
        }
        if (TextUtils.isEmpty(note_content)) {
            tvNoteContent.setVisibility(View.GONE);
        } else {
            tvNoteContent.setVisibility(View.VISIBLE);
            tvNoteContent.setText(note_content);
        }

        if ("true".equals(support_flag)) {
            btnAgree.setClickable(false);
            btnAgree.setImageResource(R.drawable.icon_post_like);
        } else {
            btnAgree.setClickable(true);
        }
    }

    private void initServerData(final boolean isMore) {
        RequestParams params = new RequestParams(GlobalContants.NOTE_SUPPORT_COMMENT_LIST_URL);
        params.addQueryStringParameter("note_key", note_key);
        params.addQueryStringParameter("page", page + "");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result, isMore);
                if (page == 1) {//缓存第一页的数据
                    CacheUtils.setCache(GlobalContants.NOTE_SUPPORT_COMMENT_LIST_URL + note_key, result, NoteDetailActivity.this);
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

    private void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if ("000000".equals(returnCode)) {
                supports = new ArrayList<>();
                JSONArray array = object1.getJSONArray("support_rows");
                JSONObject object2;
                Support support;
                for (int i = 0; i < array.length(); i++) {
                    object2 = array.getJSONObject(i);
                    support = gson.fromJson(object2.toString(), Support.class);
                    supports.add(support);
                    object2 = null;
                    support = null;
                }
                LogUtils.d("supports", supports.toString());

                comments = new ArrayList<>();
                Comment comment;
                array = object1.getJSONArray("comment_rows");
                for (int i = 0; i < array.length(); i++) {
                    object2 = array.getJSONObject(i);
                    comment = gson.fromJson(object2.toString(), Comment.class);
                    comments.add(comment);
                    object2 = null;
                    comment = null;
                }
                LogUtils.d("comments", comments.toString());
                setListView(supports, comments, isMore);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private NoteDetailListAdapter supportAdapter;
    private NoteDetailListAdapter commentAdapter;

    private void setListView(ArrayList<Support> supports, ArrayList<Comment> comments, boolean isMore) {
        if (!isMore) {
            if (supports != null) {
                supportAdapter = new NoteDetailListAdapter(this, supports);
                agreeListview.setAdapter(supportAdapter);
            }
            if (comments != null) {
                commentAdapter = new NoteDetailListAdapter(this, comments);
                commentListView.setAdapter(commentAdapter);
            }
        } else {
            if (supports.size() != 0) {
                supportAdapter.addMoreSup(supports);
            }
            if (comments.size() != 0) {
                commentAdapter.addMoreCom(comments);
            }
        }
//        commentListView.onRefreshComplete(true);
//        agreeListview.onRefreshComplete(true);
    }

    private void setViewClick() {

        View.OnClickListener listener = new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.activity_note_detail_btn_back:
                        finish();
                        break;
                    case R.id.activity_note_detail_note_image:
                        intent = new Intent(NoteDetailActivity.this, ScanImageActivity.class);
                        intent.putExtra("image_url", note_image);
                        startActivity(intent);
                        break;
                    case R.id.activity_note_detail_avatar:
                        if (!user_id.equals(loginUserID)) {
                            intent = new Intent(NoteDetailActivity.this, PersonInfoActivity.class);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("user_name", user_name);
                            intent.putExtra("user_avatar", user_avatar);
                            intent.putExtra("user_city", user_city);
                            intent.putExtra("signature", signature);
                            intent.putExtra("user_level", user_level);
                            intent.putExtra("user_phone", user_phone);
                            intent.putExtra("fans_flag", fans_flag);
                            startActivity(intent);
                        }
                        break;
                    case R.id.activity_note_detail_btn_agree:
                        PrefUtils.setBoolean(NoteDetailActivity.this, "agree_flag_" + note_key, true);
                        btnAgree.setImageResource(R.drawable.icon_post_like);
                        btnAgree.setClickable(false);
                        sendSupportDataToServer();
                        break;
                    case R.id.activity_note_detail_btn_pub:
                        if (TextUtils.isEmpty(mEditText.getText().toString())) {
                            Toast.makeText(NoteDetailActivity.this, "没发现任何内容哦", Toast.LENGTH_LONG).show();
                            return;
                        }

                        sendCommentDataToServer();
                        break;
                }
            }
        };
        btnBack.setOnClickListener(listener);
        ivAvatar.setOnClickListener(listener);
        btnAgree.setOnClickListener(listener);
        btnPub.setOnClickListener(listener);
        ivNoteImage.setOnClickListener(listener);
    }

    private void sendSupportDataToServer() {
        RequestParams params = new RequestParams(GlobalContants.NOTE_SUPPORT_ADD_URL);
        String userID = PrefUtils.getString(this, "loginUser", null);
        params.addQueryStringParameter("user_id", userID);
        params.addQueryStringParameter("note_key", note_key);
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

    @Override
    protected void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        mRootView.addOnLayoutChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PrefUtils.setString(this, "temp_edit_comment", null);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            //软键盘弹起
            LogUtils.d("qqq", "软键盘弹起");
            mEditText.requestFocus();
            btnAgree.setVisibility(View.GONE);
            btnPub.setVisibility(View.VISIBLE);
            String temp_edit_comment = PrefUtils.getString(this, "temp_edit_comment", null);
            mEditText.setText(temp_edit_comment);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            //软件盘关闭
            LogUtils.d("wwww", "软件盘关闭");
            btnAgree.setVisibility(View.VISIBLE);
            btnPub.setVisibility(View.GONE);
            PrefUtils.setString(this, "temp_edit_comment", mEditText.getText().toString());
            mEditText.setText(null);
        }
    }

    @Override
    public void onScroll(int scrollY) {
        searchLayoutTop = tvNoteContent.getBottom();
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
}
