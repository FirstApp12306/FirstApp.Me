package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.User;
import com.me.firstapp.view.CircleImageView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class NotePagerListAdapter extends BaseAdapter {
    private HashMap<String, Object> dataMap;
    private Context context;
    private Activity mActivity;
    private ArrayList<Note> notes;
    private ArrayList<User> users;

    public NotePagerListAdapter(HashMap<String, Object> dataMap, Context context) {
        this.dataMap = dataMap;
        this.context = context;
        this.mActivity = (Activity) context;
        notes = (ArrayList<Note>) dataMap.get("notes");
        users = (ArrayList<User>) dataMap.get("users");
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.notes_pager_list_item, null);
            holder.btnAddFriend = (Button) convertView.findViewById(R.id.notes_pager_list_item_btn_add_friend);
            holder.btnPop = (Button) convertView.findViewById(R.id.notes_pager_list_item_pop);
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.notes_pager_list_item_avatar);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.notes_pager_list_item_username);
            holder.ivNoteImage = (ImageView) convertView.findViewById(R.id.notes_pager_list_item_note_image);
            holder.tvNoteContent = (TextView) convertView.findViewById(R.id.notes_pager_list_item_content);
            holder.tvTime = (TextView) convertView.findViewById(R.id.notes_pager_list_item_time);
            holder.btnForward = (Button) convertView.findViewById(R.id.notes_pager_list_item_foward);
            holder.btnComment = (Button) convertView.findViewById(R.id.notes_pager_list_item_comment);
            holder.btnAgree = (Button) convertView.findViewById(R.id.notes_pager_list_item_agree);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        User user = users.get(position);
        Note note = notes.get(position);
        holder.tvUserName.setText(user.user_name);
        if ("#".equals(user.user_avatar) || TextUtils.isEmpty(user.user_avatar)){

        }else{
            ImageOptions imageOptions = new ImageOptions.Builder()
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    // 默认自动适应大小
                    // .setSize(...)
                    .setIgnoreGif(true)
                            // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                            //.setUseMemCache(false)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
            x.image().bind(holder.ivAvatar, user.user_avatar, imageOptions);
        }
        if (TextUtils.isEmpty(note.note_content)){
            holder.tvNoteContent.setVisibility(View.GONE);
        }else{
            holder.tvNoteContent.setVisibility(View.VISIBLE);
            holder.tvNoteContent.setText(note.note_content);
        }
        if ("#".equals(note.image_key) || note.image_key == null){
            holder.ivNoteImage.setVisibility(View.GONE);
        }else{
            holder.ivNoteImage.setVisibility(View.VISIBLE);
            ImageOptions imageOptions = new ImageOptions.Builder()
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    // 默认自动适应大小
                    // .setSize(...)
                    .setIgnoreGif(true)
                            // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                            //.setUseMemCache(false)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
            x.image().bind(holder.ivNoteImage, note.image_key, imageOptions);
        }
        holder.btnComment.setText(note.note_comment_counts+"");
        holder.btnAgree.setText(note.note_agree_counts+"");
        holder.btnForward.setText(note.note_forward_counts+"");

        return convertView;
    }

    private class ViewHolder{
        public Button btnAddFriend;
        public Button btnPop;
        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public ImageView ivNoteImage;
        public TextView tvNoteContent;
        public TextView tvTime;
        public Button btnForward;
        public Button btnComment;
        public Button btnAgree;
    }
}
