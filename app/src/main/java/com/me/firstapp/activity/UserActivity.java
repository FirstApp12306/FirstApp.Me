package com.me.firstapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.adapter.SearchUserListAdapter;
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
@ContentView(R.layout.activity_user)
public class UserActivity extends BaseActivity {
    @ViewInject(R.id.activity_user_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_user_list_view)
    private RefreshListView mListView;

    private String search;
    private String userID;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search = getIntent().getStringExtra("search");
        userID = PrefUtils.getString(this, "loginUser", null);

        mListView.setPullRefreshAble(false);

        getDataFromServer(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                User user = (User) parent.getAdapter().getItem(position);
                Intent intent = new Intent(UserActivity.this, PersonInfoActivity.class);
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

    }

    private void getDataFromServer(final boolean isMore){
        RequestParams params = new RequestParams(GlobalContants.USER_SEARCH_URL);
        params.addQueryStringParameter("search", search);
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
    private void parseData(String result, boolean isMore){
        Gson gson = new Gson();
        try {
            JSONObject object = new JSONObject(result);
            String return_code = object.getString("return_code");
            if ("000000".equals(return_code)){
                users = new ArrayList<>();
                JSONArray array = object.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object2 = (JSONObject) array.get(i);
                    User user = gson.fromJson(object2.toString(), User.class);
                    users.add(user);
                }
                LogUtils.d("users", users.toString());

                if (users != null){
                    setAdapter(users, isMore);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            mListView.onRefreshComplete(true);
        }
    }


    private SearchUserListAdapter userAdapter;
    private void setAdapter(ArrayList<User> users, boolean isMore){
        if (!isMore){
            userAdapter = new SearchUserListAdapter(this, users);
            mListView.setAdapter(userAdapter);
        }else {
            if (userAdapter != null){
                userAdapter.addMore(users);
            }
        }
    }
}
