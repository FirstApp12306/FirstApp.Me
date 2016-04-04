package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.List;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class ContactsListAdapter extends BaseAdapter {
    private Context context;
    private Activity mActivity;
    private ArrayList<User> users;
    private boolean isFans;
    private String loginUserID;

    public ContactsListAdapter(Context context, ArrayList<User> users, boolean isFans) {
        this.context = context;
        this.mActivity = (Activity) context;
        this.users = users;
        this.isFans = isFans;
        this.loginUserID = PrefUtils.getString(context, "loginUser", null);
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

    public void updateListView(ArrayList<User> users) {
        this.users = users;
        doNotify();
    }

    public void deleteListItem(User mUser){
        for (User user : users){
            if (user.user_id.equals(mUser.user_id)){
                users.remove(user);
                break;
            }
        }
        doNotify();
    }

    public void addListItem(User mUser){
        this.users.add(mUser);
        doNotify();
    }

    private void changeIVFollow(String user_id){
        for (User user : users){
            if (user_id.equals(user.user_id)){
                if ("true".equals(user.friend_flag)){
                    user.friend_flag = "false";
                }else{
                    user.friend_flag = "true";
                }
                break;
            }
        }
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.contacts_list_item, null);
            holder = new ViewHolder();
            holder.tvLetter = (TextView) convertView.findViewById(R.id.contacts_list_item_tv_catagory);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.contacts_list_item_tv_user_name);
            holder.tvUserID = (TextView) convertView.findViewById(R.id.contacts_list_item_tv_user_id);
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.contacts_list_item_avatar);
            holder.line = convertView.findViewById(R.id.contacts_list_item_line);
            holder.ivFollow = (ImageView) convertView.findViewById(R.id.contacts_list_item_follow);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final User user = users.get(position);
        holder.tvUserName.setText(user.user_name);
        holder.tvUserID.setText("ID:"+user.user_id);
        ImageUtils.bindImageWithOptions(holder.ivAvatar, user.user_avatar,
                R.drawable.person_avatar_default_round,
                R.drawable.person_avatar_default_round);
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(user.sortLetters);
            holder.line.setVisibility(View.GONE);
        }else{
            holder.tvLetter.setVisibility(View.GONE);
            holder.line.setVisibility(View.VISIBLE);
        }

        if (isFans){
            holder.ivFollow.setVisibility(View.VISIBLE);
            if ("true".equals(user.friend_flag)){
                holder.ivFollow.setImageResource(R.drawable.person_follow_eachother);
            }else{
                holder.ivFollow.setImageResource(R.drawable.person_follow);
            }
        }else{
            holder.ivFollow.setVisibility(View.GONE);
        }

        holder.ivFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer(user);
                changeIVFollow(user.user_id);
            }
        });

        return convertView;
    }

    public int getSectionForPosition(int position) {
        return this.users.get(position).sortLetters.charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = this.users.get(i).sortLetters;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    private class ViewHolder{
        TextView tvLetter;
        TextView tvUserName;
        TextView tvUserID;
        CircleImageView ivAvatar;
        View line;
        ImageView ivFollow;
    }

    private void sendDataToServer(final User user){
        RequestParams params;
        if ("true".equals(user.friend_flag)){
            params = new RequestParams(GlobalContants.DELETE_FRIEND_URL);
        }else{
            params = new RequestParams(GlobalContants.ADD_FRIEND_URL);
        }

        params.addQueryStringParameter("user_id", user.user_id);
        params.addQueryStringParameter("fans_id", loginUserID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                if ("true".equals(user.friend_flag)){
                    Toast.makeText(context, "已关注", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "已取消", Toast.LENGTH_SHORT).show();
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
