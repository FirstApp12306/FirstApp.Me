package com.me.firstapp.activity.notices;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.activity.BaseActivity;
import com.me.firstapp.activity.NoteDetailActivity;
import com.me.firstapp.adapter.NoticeCommentListAdapter;
import com.me.firstapp.adapter.NoticeSupportListAdapter;
import com.me.firstapp.entity.MyComment;
import com.me.firstapp.entity.MySupport;
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
@ContentView(R.layout.activity_notice_support)
public class NoticeSupportActivity extends BaseActivity {

    @ViewInject(R.id.activity_notice_support_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_notice_support_listview)
    private RefreshListView mListView;

    private String userID;
    private ArrayList<MySupport> mySupports;
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
                MySupport mySupport = (MySupport) parent.getAdapter().getItem(position);
                Intent intent = new Intent(NoticeSupportActivity.this, NoteDetailActivity.class);
                intent.putExtra("topic_key", mySupport.topic_key);
                intent.putExtra("topic_title", mySupport.topic_title);
                intent.putExtra("user_avatar", mySupport.note_user_avatar);
                intent.putExtra("user_name", mySupport.note_user_name);
                intent.putExtra("note_key", mySupport.note_key);
                intent.putExtra("note_image", mySupport.note_image);
                intent.putExtra("note_content", mySupport.note_content);
                intent.putExtra("note_agree_counts", mySupport.note_agree_counts);
                intent.putExtra("note_comment_counts", mySupport.note_comment_counts);
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
        RequestParams params = new RequestParams(GlobalContants.NOTICE_SUPPORTS_LIST_URL);
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

    NoticeSupportListAdapter adapter;
    private void parseData(String result, boolean isMore){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if ("000000".equals(returnCode)){
                mySupports = new ArrayList<>();
                JSONArray array = object1.getJSONArray("rows");
                JSONObject object = null;
                MySupport mySupport = null;
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    mySupport = gson.fromJson(object.toString(), MySupport.class);
                    mySupports.add(mySupport);
                    object = null;
                    mySupport = null;
                }
                LogUtils.d("mySupports", mySupports.toString());
                if (!isMore){
                    if (mySupports != null){
                        adapter = new NoticeSupportListAdapter(this, mySupports);
                        mListView.setAdapter(adapter);
                    }
                }else{
                    adapter.addMore(mySupports);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            mListView.onRefreshComplete(true);
        }
    }
}
