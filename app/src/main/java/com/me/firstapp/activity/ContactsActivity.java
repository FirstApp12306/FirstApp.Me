package com.me.firstapp.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.adapter.CommonViewPagerAdapter;
import com.me.firstapp.adapter.ContactsListAdapter;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PinyinUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_contacts)
public class ContactsActivity extends BaseActivity {
    @ViewInject(R.id.activity_contacts_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_contacts_rg)
    private RadioGroup mRadioGroup;
    @ViewInject(R.id.activity_contacts_vp)
    private ViewPager mViewPager;

    private View followView;
    private View fansView;
    private EditText etFollowSearch;
    private ListView lvFollowContacts;
    private TextView tvFollowCenter;
    private SideBar followSlideBar;
    private ListView followListView;

    private ArrayList<View> views = new ArrayList<>();
    private ContactsListAdapter followAdapter;
    private String loginUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginUserID = PrefUtils.getString(this, "loginUser", null);

        followView = View.inflate(this, R.layout.view_activity_contacts_follow, null);
        fansView = View.inflate(this, R.layout.view_activity_contacts_fans, null);
        initViews();
        views.add(0, followView);
        views.add(1, fansView);
        mViewPager.setAdapter(new CommonViewPagerAdapter(views));
        getDataFromServer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        followSlideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                 int position = followAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    followListView.setSelection(position + 1);
                }
            }
        });
    }

    private void initViews(){
        etFollowSearch = (EditText) followView.findViewById(R.id.view_activity_contacts_follow_et);
        lvFollowContacts = (ListView) followView.findViewById(R.id.view_activity_contacts_follow_listview);
        tvFollowCenter = (TextView) followView.findViewById(R.id.view_activity_contacts_follow_center_text);
        followSlideBar = (SideBar) followView.findViewById(R.id.view_activity_contacts_follow_sidebar);
        followListView = (ListView) followView.findViewById(R.id.view_activity_contacts_follow_listview);
        followSlideBar.setTextView(tvFollowCenter);
    }

    private void getDataFromServer(){
        RequestParams params = new RequestParams(GlobalContants.CONTACTS_LIST_URL);
        params.addQueryStringParameter("user_id", loginUserID);
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

    private ArrayList<User> followUsers;
    private ArrayList<User> fansUsers;
    private void parseData(String result){
        Gson gson = new Gson();
        try {
            JSONObject object1 = new JSONObject(result);
            String return_code = object1.getString("return_code");
            if ("000000".equals(return_code)){
                JSONArray array1 = object1.getJSONArray("follow_rows");
                followUsers = new ArrayList<>();
                for (int i = 0; i < array1.length(); i++) {
                    JSONObject object2 = array1.getJSONObject(i);
                    User user = gson.fromJson(object2.toString(), User.class);
                    followUsers.add(user);
                    user = null;
                }
                LogUtils.d("followUsers", followUsers.toString());
                followUsers = setUserLetters(followUsers);

                JSONArray array2 = object1.getJSONArray("fans_rows");
                fansUsers = new ArrayList<>();
                for (int i = 0; i < array2.length(); i++) {
                    JSONObject object3 = array2.getJSONObject(i);
                    User user = gson.fromJson(object3.toString(), User.class);
                    fansUsers.add(user);
                    user = null;
                }
                LogUtils.d("fansUsers", fansUsers.toString());
                fansUsers = setUserLetters(fansUsers);

                setAdapter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<User> setUserLetters(ArrayList<User> users){
        ArrayList<String> indexString = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            String pinyin = PinyinUtils.getPingYin(users.get(i).user_name);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                users.get(i).sortLetters = sortString.toUpperCase();
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
        }
        Collections.sort(indexString);
        followSlideBar.setIndexText(indexString);
        return users;
    }

    private void setAdapter() {
        Collections.sort(followUsers, new PinyinUtils.PinyinComparator());
        followAdapter = new ContactsListAdapter(this, followUsers);
        followListView.setAdapter(followAdapter);
    }
}
