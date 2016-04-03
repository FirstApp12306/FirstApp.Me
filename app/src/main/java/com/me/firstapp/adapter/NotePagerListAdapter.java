package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.activity.ChatActivity;
import com.me.firstapp.activity.NoteDetailActivity;
import com.me.firstapp.activity.PersonInfoActivity;
import com.me.firstapp.activity.ScanImageActivity;
import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.view.CircleImageView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class NotePagerListAdapter extends BaseAdapter {
    private Context context;
    private Activity mActivity;
    private ArrayList<Note> notes;
    private ArrayList<User> users;
    private String topicTitle;
    private String loginUserID;

    public NotePagerListAdapter(Context context, ArrayList<Note> notes, ArrayList<User> users, String topicTitle) {
        this.context = context;
        this.mActivity = (Activity) context;
        this.notes = notes;
        this.users = users;
        this.topicTitle = topicTitle;
        loginUserID = PrefUtils.getString(context, "loginUser", null);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void doNotify() {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    //加载更多
    public void addMore(ArrayList<Note> notes, ArrayList<User> users) {
        this.notes.addAll(notes);
        this.users.addAll(users);
        doNotify();
    }

    //新增帖子
    public void addNew(Note note, User user) {
        this.notes.add(0, note);
        this.users.add(0, user);
        doNotify();
    }

    //新增点赞
    public void addSupport(Note note) {
        for (Note mNote : notes) {
            if (mNote.note_key.equals(note.note_key)) {
                mNote.note_agree_counts++;
                mNote.support_flag = "true";
                break;
            }
        }
        doNotify();
    }

    //新增粉丝
    public void addFans(User user){
        for (User mUser : users){
            if (mUser.user_id.equals(user.user_id)){
                mUser.fans_flag = "true";
                break;
            }
        }
        doNotify();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.notes_pager_list_item, null);
            holder.btnAddFriend = (ImageButton) convertView.findViewById(R.id.notes_pager_list_item_btn_add_friend);
            holder.btnPop = (ImageButton) convertView.findViewById(R.id.notes_pager_list_item_pop);
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.notes_pager_list_item_avatar);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.notes_pager_list_item_username);
            holder.ivNoteImage = (ImageView) convertView.findViewById(R.id.notes_pager_list_item_note_image);
            holder.tvNoteContent = (TextView) convertView.findViewById(R.id.notes_pager_list_item_content);
            holder.tvTime = (TextView) convertView.findViewById(R.id.notes_pager_list_item_time);
            holder.btnComment = (Button) convertView.findViewById(R.id.notes_pager_list_item_comment);
            holder.btnAgree = (Button) convertView.findViewById(R.id.notes_pager_list_item_agree);
            holder.tvAddedFriend = (TextView) convertView.findViewById(R.id.notes_pager_list_item_added_friend);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final User user = users.get(position);
        final Note note = notes.get(position);
        holder.tvUserName.setText(user.user_name);
        holder.tvTime.setText(note.time_stamp);
        ImageUtils.bindImageWithOptions(holder.ivAvatar, user.user_avatar,
                R.drawable.person_avatar_default_round, R.drawable.person_avatar_default_round);
        if (TextUtils.isEmpty(note.note_content)) {
            holder.tvNoteContent.setVisibility(View.GONE);
        } else {
            holder.tvNoteContent.setVisibility(View.VISIBLE);
            holder.tvNoteContent.setText(note.note_content);
        }

        if (TextUtils.isEmpty(note.image_key)){
            holder.ivNoteImage.setVisibility(View.GONE);
        }else{
            holder.ivNoteImage.setVisibility(View.VISIBLE);
            ImageUtils.bindImage(holder.ivNoteImage, note.image_key);
        }


        holder.btnComment.setText(note.note_comment_counts + "");
        holder.btnAgree.setText(note.note_agree_counts + "");

        if ("true".equals(note.support_flag)) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_post_like);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.btnAgree.setCompoundDrawables(null, null, drawable, null);
        } else {
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_post_unlike);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.btnAgree.setCompoundDrawables(null, null, drawable, null);
        }

        if (user.user_id.equals(loginUserID)) {
            holder.btnAddFriend.setVisibility(View.GONE);
            holder.tvAddedFriend.setVisibility(View.INVISIBLE);
        } else {
            if ("true".equals(user.fans_flag)) {
                holder.btnAddFriend.setVisibility(View.GONE);
                holder.tvAddedFriend.setVisibility(View.VISIBLE);
            } else {
                holder.tvAddedFriend.setVisibility(View.GONE);
                holder.btnAddFriend.setVisibility(View.VISIBLE);
            }
        }

        View.OnClickListener listener = new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.notes_pager_list_item_btn_add_friend:
                        if (!"true".equals(user.fans_flag)) {
                            addFans(user);
                            sendAddFansDataToServer(user);
                        }
                        break;
                    case R.id.notes_pager_list_item_pop:
                        break;
                    case R.id.notes_pager_list_item_avatar:
                        if (!user.user_id.equals(loginUserID)){
                            intent = new Intent(context, PersonInfoActivity.class);
                            intent.putExtra("user_id", user.user_id);
                            intent.putExtra("user_name", user.user_name);
                            intent.putExtra("user_avatar", user.user_avatar);
                            intent.putExtra("user_city", user.user_city);
                            intent.putExtra("signature", user.user_signature);
                            intent.putExtra("user_level", user.user_level);
                            intent.putExtra("user_phone", user.user_phone);
                            intent.putExtra("fans_flag", user.fans_flag);
                            context.startActivity(intent);
                        }
                        break;
                    case R.id.notes_pager_list_item_note_image:
                        intent = new Intent(context, ScanImageActivity.class);
                        intent.putExtra("image_url", note.image_key);
                        context.startActivity(intent);
                        break;
                    case R.id.notes_pager_list_item_comment:
                        intent = new Intent(context, NoteDetailActivity.class);
                        intent.putExtra("topic_key", note.topic_key);
                        intent.putExtra("topic_title", topicTitle);
                        intent.putExtra("user_avatar", user.user_avatar);
                        intent.putExtra("user_name", user.user_name);
                        intent.putExtra("user_city", user.user_city);
                        intent.putExtra("user_phone", user.user_phone);
                        intent.putExtra("user_level", user.user_level+"");
                        intent.putExtra("signature", user.user_signature);
                        intent.putExtra("user_id", user.user_id);
                        intent.putExtra("fans_flag", user.fans_flag);
                        intent.putExtra("note_key", note.note_key);
                        intent.putExtra("note_image", note.image_key);
                        intent.putExtra("note_content", note.note_content);
                        intent.putExtra("note_agree_counts", note.note_agree_counts);
                        intent.putExtra("note_comment_counts", note.note_comment_counts);
                        intent.putExtra("support_flag", note.support_flag);
                        context.startActivity(intent);
                        break;
                    case R.id.notes_pager_list_item_agree:
                        if (!"true".equals(note.support_flag)) {
                            addSupport(note);
                            sendSupportDataToServer(holder, note);
                        }
                        break;
                }
            }
        };
        holder.btnAddFriend.setOnClickListener(listener);
        holder.btnPop.setOnClickListener(listener);
        holder.btnComment.setOnClickListener(listener);
        holder.btnAgree.setOnClickListener(listener);
        holder.ivAvatar.setOnClickListener(listener);
        holder.ivNoteImage.setOnClickListener(listener);

        return convertView;
    }

    private void sendSupportDataToServer(final ViewHolder holder, final Note note) {
        RequestParams params = new RequestParams(GlobalContants.NOTE_SUPPORT_ADD_URL);
        String userID = PrefUtils.getString(context, "loginUser", null);
        params.addQueryStringParameter("user_id", userID);
        params.addQueryStringParameter("note_key", note.note_key);
        params.addQueryStringParameter("topic_key", note.topic_key);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                doNotify();
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

    private void sendAddFansDataToServer(User user) {
        RequestParams params = new RequestParams(GlobalContants.ADD_FRIEND_URL);
        params.addQueryStringParameter("user_id", user.user_id);
        params.addQueryStringParameter("fans_id", loginUserID);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                doNotify();
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


    private class ViewHolder {
        public ImageButton btnAddFriend;
        public ImageButton btnPop;
        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public ImageView ivNoteImage;
        public TextView tvNoteContent;
        public TextView tvTime;
        public Button btnComment;
        public Button btnAgree;
        public TextView tvAddedFriend;
    }
}
