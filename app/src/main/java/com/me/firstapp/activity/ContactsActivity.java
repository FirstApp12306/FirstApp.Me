package com.me.firstapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.firstapp.R;
import com.me.firstapp.adapter.CommonViewPagerAdapter;
import com.me.firstapp.adapter.ContactsListAdapter;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.CacheUtils;
import com.me.firstapp.utils.DialogUtils;
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
import java.util.List;
import java.util.Map;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_contacts)
public class ContactsActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.activity_contacts_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_contacts_rg)
    private RadioGroup mRadioGroup;
    @ViewInject(R.id.activity_contacts_vp)
    private ViewPager mViewPager;

    private View followView;
    private View fansView;
    private EditText etFollowSearch;
    private TextView tvFollowCenter;
    private SideBar followSlideBar;
    private ListView followListView;
    private ImageButton btnFollowClear;
    private EditText etFansSearch;
    private TextView tvFansCenter;
    private SideBar fansSlideBar;
    private ListView fansListView;
    private ImageButton btnFansClear;

    private ArrayList<View> views = new ArrayList<>();
    private ContactsListAdapter followAdapter;
    private ContactsListAdapter fansAdapter;
    private String loginUserID;
    private long fans_total = 0;
    private long follow_total = 0;

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

        String cache = CacheUtils.getCache(GlobalContants.CONTACTS_LIST_URL, this);
        if (!TextUtils.isEmpty(cache)){
            parseData(cache);
        }
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
        fansSlideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = fansAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    fansListView.setSelection(position + 1);
                }
            }
        });

        followListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getAdapter().getItem(position);
                setOnItemClick(user);
            }
        });

        fansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getAdapter().getItem(position);
                setOnItemClick(user);
            }
        });
        etFollowSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<User> mUserList = filterData(s.toString(), followUsers);
                followAdapter.updateListView(mUserList);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 1) {
                    btnFollowClear.setVisibility(View.GONE);
                } else {
                    btnFollowClear.setVisibility(View.VISIBLE);
                }
            }
        });
        etFansSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<User> mUserList = filterData(s.toString(), fansUsers);
                fansAdapter.updateListView(mUserList);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 1) {
                    btnFansClear.setVisibility(View.GONE);
                } else {
                    btnFansClear.setVisibility(View.VISIBLE);
                }
            }
        });
        btnBack.setOnClickListener(this);
        btnFollowClear.setOnClickListener(this);
        btnFansClear.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mRadioGroup.check(R.id.activity_contacts_rbt_follow);
                        break;
                    case 1:
                        mRadioGroup.check(R.id.activity_contacts_rbt_fans);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRadioGroup.check(R.id.activity_contacts_rbt_follow);
        mViewPager.setCurrentItem(0);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.activity_contacts_rbt_follow :
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.activity_contacts_rbt_fans :
                        mViewPager.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    Map map;
    Dialog dialog;
    ImageButton btnFollow;
    Intent intent;
    private void setOnItemClick(final User user){

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.contacts_list_item_click_dialog_btn_close :
                        dialog.cancel();
                        break;
                    case R.id.contacts_list_item_click_dialog_btn_follow :
                        sendDataToServer(user, btnFollow);
                        break;
                    case R.id.contacts_list_item_click_dialog_btn_chat :
                        intent = new Intent(ContactsActivity.this, ChatActivity.class);
                        intent.putExtra("targetID", user.user_phone);
                        intent.putExtra("user_name", user.user_name);
                        startActivity(intent);
                        break;
                    case R.id.contacts_list_item_click_dialog_btn_home :
                        intent = new Intent(ContactsActivity.this, PersonInfoActivity.class);
                        intent.putExtra("user_name", user.user_name);
                        intent.putExtra("user_id", user.user_id);
                        intent.putExtra("user_avatar", user.user_avatar);
                        intent.putExtra("user_level", user.user_level);
                        intent.putExtra("user_city", user.user_city);
                        intent.putExtra("signature", user.user_signature);
                        intent.putExtra("user_phone", user.user_phone);
                        intent.putExtra("fans_flag", "true");
                        startActivity(intent);
                        break;
                }
            }
        };
        map = DialogUtils.createContactsItemDialog(ContactsActivity.this, listener, user.user_avatar, user.user_name, user.user_id, user.friend_flag);
        dialog = (Dialog) map.get("dialog");
        btnFollow = (ImageButton) map.get("btnFollow");
        dialog.show();
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private ArrayList<User> filterData(String filterStr, ArrayList<User> users) {
        ArrayList<User> mUserList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mUserList = users;
        }else{
            mUserList.clear();
            for (User user : users){
                String name = user.user_name;
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mUserList.add(user);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mUserList, new PinyinUtils.PinyinComparator());
        return mUserList ;
    }

    private void initViews(){
        etFollowSearch = (EditText) followView.findViewById(R.id.view_activity_contacts_follow_et);
        tvFollowCenter = (TextView) followView.findViewById(R.id.view_activity_contacts_follow_center_text);
        followSlideBar = (SideBar) followView.findViewById(R.id.view_activity_contacts_follow_sidebar);
        followListView = (ListView) followView.findViewById(R.id.view_activity_contacts_follow_listview);
        btnFollowClear = (ImageButton) followView.findViewById(R.id.view_activity_contacts_follow_btn_clear);
        followSlideBar.setTextView(tvFollowCenter);

        etFansSearch = (EditText) fansView.findViewById(R.id.view_activity_contacts_fans_et_serch);
        fansListView = (ListView) fansView.findViewById(R.id.view_activity_contacts_fans_listview);
        tvFansCenter = (TextView) fansView.findViewById(R.id.view_activity_contacts_fans_tv_center);
        fansSlideBar = (SideBar) fansView.findViewById(R.id.view_activity_contacts_fans_sidebar);
        btnFansClear = (ImageButton) fansView.findViewById(R.id.view_activity_contacts_fans_btn_clear);
        fansSlideBar.setTextView(tvFansCenter);

    }

    private void getDataFromServer(){
        RequestParams params = new RequestParams(GlobalContants.CONTACTS_LIST_URL);
        params.addQueryStringParameter("user_id", loginUserID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                parseData(result);
                CacheUtils.setCache(GlobalContants.CONTACTS_LIST_URL, result, ContactsActivity.this);
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
            fans_total = object1.getLong("fans_total");
            follow_total = object1.getLong("follow_total");
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
                ArrayList<String> followIndexStrings = setIndex(followUsers);
                followSlideBar.setIndexText(followIndexStrings);

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
                ArrayList<String> fansIndexStrings = setIndex(fansUsers);
                fansSlideBar.setIndexText(fansIndexStrings);

                setAdapter();
            }
            etFollowSearch.setHint("搜索"+follow_total+"位关注人");
            etFansSearch.setHint("搜索" + fans_total + "位粉丝");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<User> setUserLetters(ArrayList<User> users){

        for (int i = 0; i < users.size(); i++) {
            String pinyin = PinyinUtils.getPingYin(users.get(i).user_name);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                users.get(i).sortLetters = sortString;
            }
        }
        return users;
    }

    private ArrayList<String> setIndex(ArrayList<User> users){
        ArrayList<String> indexStrings = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            String letter = users.get(i).sortLetters;
            if (!indexStrings.contains(letter)) {
                indexStrings.add(letter);
            }
        }
        Collections.sort(indexStrings);
        return indexStrings;
    }

    private void setAdapter() {
        if(followUsers != null){
            Collections.sort(followUsers, new PinyinUtils.PinyinComparator());
            followAdapter = new ContactsListAdapter(this, followUsers, false);
            followListView.setAdapter(followAdapter);
        }

        if (fansUsers != null){
            Collections.sort(fansUsers, new PinyinUtils.PinyinComparator());
            fansAdapter = new ContactsListAdapter(this, fansUsers, true);
            fansListView.setAdapter(fansAdapter);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_contacts_btn_back :
                finish();
                break;
            case R.id.view_activity_contacts_follow_btn_clear :
                etFollowSearch.setText("");
                btnFollowClear.setVisibility(View.GONE);
                break;
            case R.id.view_activity_contacts_fans_btn_clear :
                etFansSearch.setText("");
                btnFansClear.setVisibility(View.GONE);
                break;
        }
    }

    private User tempUser;//这里设置临时的user,是为了防止当前选择的user数据的串改，导致无法判断是关注用户还是取消关注
    private void sendDataToServer(final User user, final ImageButton btnFollow){
        tempUser = user;
        RequestParams params;
        if ("true".equals(tempUser.friend_flag)){//取消关注
            tempUser.friend_flag = "false";
            btnFollow.setImageResource(R.drawable.person_follow);
            followAdapter.deleteListItem(tempUser);
            params = new RequestParams(GlobalContants.DELETE_FRIEND_URL);
        }else{//关注
            tempUser.friend_flag = "true";
            btnFollow.setImageResource(R.drawable.person_follow_cancel);
            followAdapter.addListItem(tempUser);
            params = new RequestParams(GlobalContants.ADD_FRIEND_URL);
        }

        params.addQueryStringParameter("user_id", tempUser.user_id);
        params.addQueryStringParameter("fans_id", loginUserID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                if ("true".equals(tempUser.friend_flag)){
                    Toast.makeText(ContactsActivity.this, "已关注", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ContactsActivity.this, "已取消", Toast.LENGTH_SHORT).show();
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
}
