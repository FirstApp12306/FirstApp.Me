package com.me.firstapp.activity.notices;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.activity.BaseActivity;
import com.me.firstapp.adapter.NoticeFansListAdapter;
import com.me.firstapp.adapter.NoticeSupportListAdapter;
import com.me.firstapp.entity.MySupport;
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
@ContentView(R.layout.activity_notice_fans)
public class NoticeFansActivity extends BaseActivity {

    @ViewInject(R.id.activity_notice_fans_btn_back)
    private ImageButton btnReturn;
    @ViewInject(R.id.activity_notice_fans_listview)
    private RefreshListView mListView;

    private String userID;
    private ArrayList<User> users;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = PrefUtils.getString(this, "loginUser", null);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        RequestParams params = new RequestParams(GlobalContants.NOTICE_FANS_LIST_URL);
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

    NoticeFansListAdapter adapter;
    private void parseData(String result, boolean isMore){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if ("000000".equals(returnCode)){
                users = new ArrayList<>();
                JSONArray array = object1.getJSONArray("rows");
                JSONObject object = null;
                User user = null;
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    user = gson.fromJson(object.toString(), User.class);
                    users.add(user);
                    object = null;
                    user = null;
                }
                LogUtils.d("users", users.toString());
                if (!isMore){
                    if (users != null){
                        adapter = new NoticeFansListAdapter(this, users);
                        mListView.setAdapter(adapter);
                    }
                }else{
                    adapter.addMore(users);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            mListView.onRefreshComplete(true);
        }
    }
}
