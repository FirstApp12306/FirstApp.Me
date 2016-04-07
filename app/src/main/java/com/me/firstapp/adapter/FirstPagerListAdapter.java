package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.activity.NoteDetailActivity;
import com.me.firstapp.activity.PersonInfoActivity;
import com.me.firstapp.activity.ScanImageActivity;
import com.me.firstapp.activity.TopicNoteActivity;
import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.Topic;
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
public class FirstPagerListAdapter extends BaseAdapter {

    private Context context;
    private Activity mActivity;
    private ArrayList<Note> notes;
    private ArrayList<User> users;
    private ArrayList<Topic> topics;
    private String loginUserID;

    public FirstPagerListAdapter(Context context, ArrayList<Note> notes, ArrayList<User> users, ArrayList<Topic> topics) {
        this.context = context;
        this.mActivity = (Activity) context;
        this.notes = notes;
        this.users = users;
        this.topics = topics;
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
    public void addMore(ArrayList<Note> notes, ArrayList<User> users, ArrayList<Topic> topics) {
        this.notes.addAll(notes);
        this.users.addAll(users);
        this.topics.addAll(topics);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.pager_friend_news_list_item, null);
            holder.btnPop = (ImageButton) convertView.findViewById(R.id.pager_friend_news_list_item_pop);
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.pager_friend_news_list_item_avatar);
            holder.tvTopicTitle = (TextView) convertView.findViewById(R.id.pager_friend_news_list_item_topic_title);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.pager_friend_news_list_item_username);
            holder.ivNoteImage = (ImageView) convertView.findViewById(R.id.pager_friend_news_list_item_note_image);
            holder.tvNoteContent = (TextView) convertView.findViewById(R.id.pager_friend_news_list_item_content);
            holder.tvTime = (TextView) convertView.findViewById(R.id.pager_friend_news_list_item_time);
            holder.btnComment = (Button) convertView.findViewById(R.id.pager_friend_news_list_item_btn_comment);
            holder.btnAgree = (Button) convertView.findViewById(R.id.pager_friend_news_list_item_btn_support);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final User user = users.get(position);
        final Note note = notes.get(position);
        final Topic topic = topics.get(position);

        ImageUtils.bindImageWithOptions(holder.ivAvatar,
                user.user_avatar, R.drawable.person_avatar_default_round,
                R.drawable.person_avatar_default_round);

        holder.ivNoteImage.setVisibility(View.VISIBLE);
        ImageUtils.bindImage(holder.ivNoteImage, note.image_key);

        holder.tvTopicTitle.setText(topic.topic_title);
        holder.tvUserName.setText(user.user_name);
        holder.tvNoteContent.setText(note.note_content);
        holder.tvTime.setText(note.time_stamp);
        holder.btnComment.setText(note.note_comment_counts + "");
        holder.btnAgree.setText(note.note_agree_counts + "");

        LogUtils.d("supportFlag", note.support_flag);

        if ("true".equals(note.support_flag)) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_post_like);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.btnAgree.setCompoundDrawables(null, null, drawable, null);
        } else {
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_post_unlike);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.btnAgree.setCompoundDrawables(null, null, drawable, null);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.pager_friend_news_list_item_topic_title:
                        intent = new Intent(mActivity, TopicNoteActivity.class);
                        intent.putExtra("topic_key", topic.topic_key);
                        intent.putExtra("topic_title", topic.topic_title);
                        mActivity.startActivity(intent);
                        break;
                    case R.id.pager_friend_news_list_item_pop:
                        break;
                    case R.id.pager_friend_news_list_item_avatar:
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
                    case R.id.pager_friend_news_list_item_note_image:
                        intent = new Intent(context, ScanImageActivity.class);
                        intent.putExtra("image_url", note.image_key);
                        context.startActivity(intent);
                        break;
                    case R.id.pager_friend_news_list_item_btn_comment:
                        intent = new Intent(context, NoteDetailActivity.class);
                        intent.putExtra("topic_key", note.topic_key);
                        intent.putExtra("topic_title", topic.topic_title);
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
                    case R.id.pager_friend_news_list_item_btn_support:
                        if (!"true".equals(note.support_flag)) {
                            addSupport(note);
                            sendSupportDataToServer(holder, note);
                        }
                        break;
                }
            }
        };
        holder.btnPop.setOnClickListener(listener);
        holder.ivAvatar.setOnClickListener(listener);
        holder.tvTopicTitle.setOnClickListener(listener);
        holder.ivNoteImage.setOnClickListener(listener);
        holder.btnComment.setOnClickListener(listener);
        holder.btnAgree.setOnClickListener(listener);
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

    private class ViewHolder {
        public ImageButton btnPop;
        public CircleImageView ivAvatar;
        public TextView tvTopicTitle;
        public TextView tvUserName;
        public ImageView ivNoteImage;
        public TextView tvNoteContent;
        public TextView tvTime;
        public Button btnComment;
        public Button btnAgree;
    }
}
