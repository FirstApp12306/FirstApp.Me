package com.me.firstapp.activity.notices;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.activity.BaseActivity;
import com.me.firstapp.adapter.NoticeCommentListAdapter;
import com.me.firstapp.entity.MyComment;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;

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
    private ListView mListView;

    private String userID;
    private ArrayList<MyComment> myComments = new ArrayList<>();

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
        getDataFromServer();
    }

    private void getDataFromServer(){
        RequestParams params = new RequestParams(GlobalContants.NOTICE_COMMENTS_LIST_URL);
        params.addQueryStringParameter("user_id", userID);
        params.addQueryStringParameter("rows", 999999999 + "");//将条数设置很大，意思是让服务器不要分页
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result);
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

    private void parseData(String result){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String returnCode = object1.getString("return_code");
            if ("000000".equals(returnCode)){
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
                if (myComments != null){
                    mListView.setAdapter(new NoticeCommentListAdapter(this, myComments));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
