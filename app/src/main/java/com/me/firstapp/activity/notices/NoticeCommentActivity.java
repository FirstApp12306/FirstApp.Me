package com.me.firstapp.activity.notices;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.activity.BaseActivity;
import com.me.firstapp.activity.NoteDetailActivity;
import com.me.firstapp.adapter.NoticeCommentListAdapter;
import com.me.firstapp.entity.MyComment;
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
@ContentView(R.layout.activity_notice_comment)
public class NoticeCommentActivity extends BaseActivity {
    @ViewInject(R.id.activity_notice_comment_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_notice_comment_listview)
    private RefreshListView mListView;

    private String userID;
    private ArrayList<MyComment> myComments;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = PrefUtils.getString(this, "loginUser", null);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyComment myComment = (MyComment) parent.getAdapter().getItem(position);
                Intent intent = new Intent(NoticeCommentActivity.this, NoteDetailActivity.class);
                intent.putExtra("topic_key", myComment.topic_key);
                intent.putExtra("topic_title", myComment.topic_title);
                intent.putExtra("user_avatar", myComment.note_user_avatar);
                intent.putExtra("user_name", myComment.note_user_name);
                intent.putExtra("note_key", myComment.note_key);
                intent.putExtra("note_image", myComment.note_image);
                intent.putExtra("note_content", myComment.note_content);
                intent.putExtra("note_agree_counts", myComment.note_agree_counts);
                intent.putExtra("note_comment_counts", myComment.note_comment_counts);
                startActivity(intent);
            }
        });

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

        getDataFromServer(false);
    }

    private void getDataFromServer(final boolean isMore){
        RequestParams params = new RequestParams(GlobalContants.NOTICE_COMMENTS_LIST_URL);
        params.addQueryStringParameter("user_id", userID);
        params.addQueryStringParameter("page", page+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result, isMore);
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
        });

    }

    NoticeCommentListAdapter adapter;
    private void parseData(String result, boolean isMore){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if ("000000".equals(returnCode)){
                myComments = new ArrayList<>();
                JSONArray array = object1.getJSONArray("rows");
                JSONObject object = null;
                MyComment myComment = null;
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    myComment = gson.fromJson(object.toString(), MyComment.class);
                    myComments.add(myComment);
                    object = null;
                    myComment = null;
                }
                LogUtils.d("myComments", myComments.toString());
                if (!isMore){
                    if (myComments != null){
                        adapter = new NoticeCommentListAdapter(this, myComments);
                        mListView.setAdapter(adapter);
                    }
                }else{
                    adapter.addMore(myComments);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            mListView.onRefreshComplete(true);
        }
    }
}
