package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.me.firstapp.R;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.CircleImageView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class SearchUserListAdapter extends BaseAdapter {
    private Context context;
    private Activity mActivity;
    private ArrayList<User> users;
    private String loginUserID;

    public SearchUserListAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.mActivity = (Activity) context;
        this.users = users;
        loginUserID = PrefUtils.getString(context, "loginUser", null);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addMore(ArrayList<User> moreUsers){
        this.users.addAll(moreUsers);
        doNotify();
    }

    private void doNotify(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private void changeFollow(User mUser){
        for (User user : users){
            if (user.user_id.equals(mUser.user_id)){
                if ("true".equals(user.fans_flag)){
                    user.fans_flag = "false";
                }else{
                    user.fans_flag = "true";
                }
                break;
            }
        }
        doNotify();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.user_activity_list_item, null);
            holder = new ViewHolder();
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.user_activity_list_item_avatar);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.user_activity_list_item_user_name);
            holder.tvUserCity = (TextView) convertView.findViewById(R.id.user_activity_list_item_user_city);
            holder.tvUserID = (TextView) convertView.findViewById(R.id.user_activity_list_item_user_id);
            holder.ivFollow = (ImageView) convertView.findViewById(R.id.user_activity_list_item_follow);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final User user = users.get(position);
        ImageUtils.bindImageWithOptions(holder.ivAvatar, user.user_avatar,
                R.drawable.person_avatar_default_round,
                R.drawable.person_avatar_default_round);
        holder.tvUserName.setText(user.user_name);
        holder.tvUserID.setText("ID:"+user.user_id);
        holder.tvUserCity.setText(user.user_city);
        if ("true".equals(user.fans_flag)){
            holder.ivFollow.setImageResource(R.drawable.icon_recommend_unfollow);
        }else{
            holder.ivFollow.setImageResource(R.drawable.icon_recommend_follow);
        }

        holder.ivFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer(user);
                changeFollow(user);
            }
        });

        return convertView;
    }

    private class ViewHolder{
        CircleImageView ivAvatar;
        TextView tvUserName;
        TextView tvUserCity;
        TextView tvUserID;
        ImageView ivFollow;
    }

    private void sendDataToServer(final User user){

        RequestParams params;
        if ("true".equals(user.fans_flag)){//取消关注
            params = new RequestParams(GlobalContants.DELETE_FRIEND_URL);
        }else{//关注
            params = new RequestParams(GlobalContants.ADD_FRIEND_URL);
        }
        params.addQueryStringParameter("user_id", user.user_id);
        params.addQueryStringParameter("fans_id", loginUserID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                if ("true".equals(user.fans_flag)){
                    Toast.makeText(context, "已关注", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "取消关注", Toast.LENGTH_SHORT).show();
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
