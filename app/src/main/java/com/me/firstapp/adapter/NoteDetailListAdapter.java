package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.Comment;
import com.me.firstapp.entity.Support;
import com.me.firstapp.utils.ImageUtils;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.view.CircleImageView;

import org.xutils.image.ImageOptions;
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
public class NoteDetailListAdapter extends BaseAdapter {
    private Activity mActivity;
    private Context context;
    private List<Comment> comments;

    public NoteDetailListAdapter(Context context, List<Comment> comments) {
        this.context = context;
        mActivity = (Activity) context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {

        return comments.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addMoreCom(ArrayList<Comment> comments) {
        this.comments.addAll(comments);
        doNotify();
    }

    public void addNewCom(Comment comment) {
        this.comments.add(0, comment);
        doNotify();
    }

    public void doNotify() {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentViewHolder commentViewHolder;
        if (convertView == null) {
            commentViewHolder = new CommentViewHolder();
            convertView = View.inflate(context, R.layout.note_detail_comment_pager_item, null);
            commentViewHolder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.note_detail_comment_pager_item_avatar);
            commentViewHolder.tvUserName = (TextView) convertView.findViewById(R.id.note_detail_comment_pager_item_user_name);
            commentViewHolder.tvDate = (TextView) convertView.findViewById(R.id.note_detail_comment_pager_item_date);
            commentViewHolder.tvComment = (TextView) convertView.findViewById(R.id.note_detail_comment_pager_item_comment);
            commentViewHolder.btnPop = (ImageView) convertView.findViewById(R.id.note_detail_comment_pager_item_pop);
            convertView.setTag(commentViewHolder);
        } else {
            commentViewHolder = (CommentViewHolder) convertView.getTag();
        }

        Comment comment = comments.get(position);
        ImageUtils.bindImageWithOptions(commentViewHolder.ivAvatar, comment.user_avatar,
                R.drawable.person_avatar_default_round, R.drawable.person_avatar_default_round);
        commentViewHolder.tvUserName.setText(comment.user_name);
        commentViewHolder.tvDate.setText(comment.time_stamp);

        if ("Y".equals(comment.reply_yn)) {
            String source = "<font color='#918b8a'>回复 " + comment.reply_to_user_name + ":</font>" + comment.comment_content;
            commentViewHolder.tvComment.setText(Html.fromHtml(source));
        } else if ("N".equals(comment.reply_yn)) {
            commentViewHolder.tvComment.setText(comment.comment_content);
        } else {
            commentViewHolder.tvComment.setText(comment.comment_content);
        }
        commentViewHolder.btnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("btnPopClick", "Click");
            }
        });


        return convertView;
    }

    private class CommentViewHolder {
        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public TextView tvDate;
        public TextView tvComment;
        public ImageView btnPop;
    }
}
