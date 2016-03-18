package com.me.firstapp.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.adapter.NoteDetailListAdapter;
import com.me.firstapp.adapter.NoteDetailPagerAdapter;
import com.me.firstapp.entity.Comment;
import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.Support;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.utils.SoftInputUtils;
import com.me.firstapp.view.CircleImageView;
import com.viewpagerindicator.TabPageIndicator;

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
@ContentView(R.layout.activity_note_detail)
public class NoteDetailActivity extends Activity implements View.OnLayoutChangeListener {

    @ViewInject(R.id.activity_note_detail_rootview)
    private LinearLayout mRootView;
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

    private ListView agreeListview;
    private ListView commentListView;

    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private View agreeView;
    private View commentView;

    private ArrayList<View> views = new ArrayList<>();
    private  String[] titles = new String[2];
    private String topic_key;
    private String note_key;
    private ArrayList<Support> supports;
    private ArrayList<Comment> comments;
    private Comment comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;

        initLocalData();
        initServerData();
        setViewClick();
        agreeView = View.inflate(this, R.layout.note_detail_view_pager_agree, null);
        commentView = View.inflate(this, R.layout.note_detail_view_pager_comment, null);
        agreeListview = (ListView) agreeView.findViewById(R.id.note_detail_view_pager_agree_listview);
        commentListView = (ListView) commentView.findViewById(R.id.note_detail_view_pager_comment_listview);
        views.add(agreeView);
        views.add(commentView);

        mViewPager.setAdapter(new NoteDetailPagerAdapter(views, titles));
        mIndicator.setViewPager(mViewPager);

        agreeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("OnItemClick", "item被点击");
            }
        });
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("OnItemClick", "item被点击");
                comment = (Comment) parent.getAdapter().getItem(position);
                mEditText.setHint("回复:"+comment.user_name);
                SoftInputUtils.showSoftInputWindow(NoteDetailActivity.this);
            }
        });

    }

    private void sendCommentDataToServer(){
        btnPub.setText("....");
        String user_id = PrefUtils.getString(this, "loginUser", null);
        RequestParams params = new RequestParams(GlobalContants.NOTE_COMMENT_ADD_URL);
        params.addQueryStringParameter("content", mEditText.getText().toString());
        params.addQueryStringParameter("topic_key", topic_key);
        params.addQueryStringParameter("note_key", note_key);
        params.addQueryStringParameter("user_id", user_id);
        if (comment != null){
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
    private void initLocalData(){
        topic_key = getIntent().getStringExtra("topic_key");
        String topicTitle = getIntent().getStringExtra("topic_title");
        String user_avatar = getIntent().getStringExtra("user_avatar");
        String user_name = getIntent().getStringExtra("user_name");
        note_key = getIntent().getStringExtra("note_key");
        String note_image = getIntent().getStringExtra("note_image");
        String note_content = getIntent().getStringExtra("note_content");
        long  note_agree_counts = getIntent().getLongExtra("note_agree_counts", 0);
        long note_comment_counts = getIntent().getLongExtra("note_comment_counts", 0);
        titles[0] = note_agree_counts + " 赞";
        titles[1] = note_comment_counts + " 评论";

        tvTitle.setText(topicTitle);
        tvUserName.setText(user_name);
        ImageOptions imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(true)
                        // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                        //.setUseMemCache(false)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
        x.image().bind(ivAvatar, user_avatar, imageOptions);
        if (TextUtils.isEmpty(note_image)){
            ivNoteImage.setVisibility(View.GONE);
        }else{
            ivNoteImage.setVisibility(View.VISIBLE);
            ImageOptions imageOptions2 = new ImageOptions.Builder()
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    // 默认自动适应大小
                    // .setSize(...)
                    .setIgnoreGif(true)
                            // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                            //.setUseMemCache(false)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
            x.image().bind(ivNoteImage, note_image, imageOptions2);
        }
        if (TextUtils.isEmpty(note_content)){
            tvNoteContent.setVisibility(View.GONE);
        }else{
            tvNoteContent.setVisibility(View.VISIBLE);
            tvNoteContent.setText(note_content);
        }

        boolean agreeFlag = PrefUtils.getBoolean(this, "agree_flag_" + note_key, false);
        if (agreeFlag == true ){
            btnAgree.setClickable(false);
            btnAgree.setImageResource(R.drawable.icon_post_like);
        }else{
            btnAgree.setClickable(true);
        }
    }

    private void initServerData(){
        RequestParams params = new RequestParams(GlobalContants.NOTE_SUPPORT_COMMENT_LIST_URL);
        params.addQueryStringParameter("note_key", note_key);
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

    private void parseData(String result){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if ("000000".equals(returnCode)){
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
                setListView(supports, comments);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListView(ArrayList<Support> supports, ArrayList<Comment> comments){
        if (supports != null){
            agreeListview.setAdapter(new NoteDetailListAdapter(this, supports));
        }
        if (comments != null){
            commentListView.setAdapter(new NoteDetailListAdapter(this, comments));
        }
    }

    private void setViewClick(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.activity_note_detail_btn_back :
                        finish();
                        break;
                    case R.id.activity_note_detail_avatar :
                        break;
                    case R.id.activity_note_detail_btn_agree :
                        btnAgree.setClickable(false);
                        btnAgree.setImageResource(R.drawable.icon_post_like);
                        sendSupportDataToServer();
                        break;
                    case R.id.activity_note_detail_btn_pub :
                        boolean loginFlag = PrefUtils.getBoolean(NoteDetailActivity.this, "login_flag", false);
                        if (loginFlag == false){
                            //未登陆
                            return;
                        }
                        if (TextUtils.isEmpty(mEditText.getText().toString())){
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
    }

    private void sendSupportDataToServer(){
        RequestParams params = new RequestParams(GlobalContants.NOTE_SUPPORT_ADD_URL);
        String userID = PrefUtils.getString(this,"loginUser", null);
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
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
            //软键盘弹起
            btnAgree.setVisibility(View.GONE);
            btnPub.setVisibility(View.VISIBLE);
            String temp_edit_comment = PrefUtils.getString(this, "temp_edit_comment", null);
            mEditText.setText(temp_edit_comment);
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
            //软件盘关闭
            btnAgree.setVisibility(View.VISIBLE);
            btnPub.setVisibility(View.GONE);
            PrefUtils.setString(this, "temp_edit_comment", mEditText.getText().toString());
            mEditText.setText(null);
        }
    }
}
