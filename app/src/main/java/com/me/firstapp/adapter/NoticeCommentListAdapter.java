package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.MyComment;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.view.CircleImageView;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class NoticeCommentListAdapter extends BaseAdapter {
    private Activity mActivity;
    private Context context;
    private ArrayList<MyComment> myComments;

    public NoticeCommentListAdapter(Context context, ArrayList<MyComment> myComments) {
        this.context = context;
        mActivity = (Activity) context;
        this.myComments = myComments;
    }

    @Override
    public int getCount() {
        return myComments.size();
    }

    @Override
    public Object getItem(int position) {
        return myComments.get(position);
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

    public void addMore(ArrayList<MyComment> moreComments) {
        this.myComments.addAll(moreComments);
        doNotify();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.notice_comment_list_item, null);
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.notice_comment_list_item_avatar);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.notice_comment_list_item_user_name);
            holder.tvContent = (TextView) convertView.findViewById(R.id.notice_comment_list_item_comment);
            holder.tvTime = (TextView) convertView.findViewById(R.id.notice_comment_list_item_time);
            holder.ivNoteImage = (ImageView) convertView.findViewById(R.id.notice_comment_list_item_note_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyComment myComment = myComments.get(position);

        ImageUtils.bindImageWithOptions(holder.ivAvatar, myComment.comment_user_avatar,
                R.drawable.person_avatar_default_round,
                R.drawable.person_avatar_default_round);
        holder.tvUserName.setText(myComment.comment_user_name);
        holder.tvContent.setText(myComment.comment_content);
        holder.tvTime.setText(myComment.time_stamp);
        if ("#".equals(myComment.note_image) || TextUtils.isEmpty(myComment.note_image)) {
            holder.ivNoteImage.setVisibility(View.GONE);
        } else {
            ImageUtils.bindImage(holder.ivNoteImage, myComment.note_image);
        }
        return convertView;
    }

    private class ViewHolder {
        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public TextView tvContent;
        public TextView tvTime;
        public ImageView ivNoteImage;
    }
}
