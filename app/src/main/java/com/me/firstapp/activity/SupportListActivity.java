package com.me.firstapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.adapter.SupportListAdapter;
import com.me.firstapp.entity.Support;
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
@ContentView(R.layout.activity_support_list)
public class SupportListActivity extends BaseActivity {

    @ViewInject(R.id.activity_support_list_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_support_list_listview)
    private RefreshListView mListView;

    private String note_key;
    private String loginUserID;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        note_key = getIntent().getStringExtra("note_key");
        loginUserID = PrefUtils.getString(this, "loginUser", null);
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
                page = 1;
                getDataFromServer(false);
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
                Support support = (Support) parent.getAdapter().getItem(position);
                if (!support.user_id.equals(loginUserID)) {
                    Intent intent = new Intent(SupportListActivity.this, PersonInfoActivity.class);
                    intent.putExtra("user_id", support.user_id);
                    intent.putExtra("user_name", support.user_name);
                    intent.putExtra("user_avatar", support.user_avatar);
                    intent.putExtra("user_city", support.user_city);
                    intent.putExtra("signature", support.user_signature);
                    intent.putExtra("user_level", support.user_level);
                    intent.putExtra("user_phone", support.user_phone);
                    intent.putExtra("fans_flag", support.fans_flag);
                    startActivity(intent);
                }
            }
        });
    }

    private void getDataFromServer(final boolean isMore) {
        RequestParams params = new RequestParams(GlobalContants.SUPPORT_OF_NOTE_LIST_URL);
        params.addQueryStringParameter("note_key", note_key);
        params.addQueryStringParameter("user_id", loginUserID);
        params.addQueryStringParameter("page", page + "");

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

    private SupportListAdapter adapter;
    private ArrayList<Support> supports;

    private void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        try {
            JSONObject object = new JSONObject(result);
            String return_code = object.getString("return_code");
            if ("000000".equals(return_code)) {
                supports = new ArrayList<>();
                JSONArray array = object.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object2 = array.getJSONObject(i);
                    Support support = gson.fromJson(object2.toString(), Support.class);
                    supports.add(support);
                }
                LogUtils.d("supports", supports.toString());
                if (!isMore) {
                    if (supports != null) {
                        adapter = new SupportListAdapter(this, supports);
                        mListView.setAdapter(adapter);
                    }
                } else {
                    if (supports != null) {
                        adapter.addMore(supports);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            mListView.onRefreshComplete(true);
        }
    }
}
