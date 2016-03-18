package com.me.firstapp.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.Comment;
import com.me.firstapp.entity.Support;
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
    private Context context;
    private ArrayList<Support> supports;
    private List<Comment> comments;
    private boolean typeFlag;//判断出入的是support数据还是comment数据

    public NoteDetailListAdapter(Context context, ArrayList<Support> supports) {
        this.context = context;
        this.supports = supports;
        typeFlag = true;
    }

    public NoteDetailListAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
        typeFlag = false;
    }

    @Override
    public int getCount() {
        if (typeFlag == true){
            return supports.size();
        }else{
            return  comments.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (typeFlag == true){
            return supports.get(position);
        }else{
            return  comments.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SupportViewHolder supportViewHolder = null;
        CommentViewHolder commentViewHolder = null;
        if (typeFlag == true){
            if (convertView == null){
                supportViewHolder = new SupportViewHolder();
                convertView = View.inflate(context, R.layout.note_detail_agree_pager_item, null);
                supportViewHolder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.note_detail_agree_pager_item_avatar);
                supportViewHolder.tvUserName = (TextView) convertView.findViewById(R.id.note_detail_agree_pager_item_user_name);
                supportViewHolder.tvUserID = (TextView) convertView.findViewById(R.id.note_detail_agree_pager_item_user_id);
                supportViewHolder.tvCity = (TextView) convertView.findViewById(R.id.note_detail_agree_pager_item_city);
                convertView.setTag(supportViewHolder);
            }else {
                supportViewHolder = (SupportViewHolder) convertView.getTag();
            }
        }else{
            if (convertView == null){
                commentViewHolder = new CommentViewHolder();
                convertView = View.inflate(context, R.layout.note_detail_comment_pager_item, null);
                commentViewHolder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.note_detail_comment_pager_item_avatar);
                commentViewHolder.tvUserName = (TextView) convertView.findViewById(R.id.note_detail_comment_pager_item_user_name);
                commentViewHolder.tvDate = (TextView) convertView.findViewById(R.id.note_detail_comment_pager_item_date);
                commentViewHolder.tvComment = (TextView) convertView.findViewById(R.id.note_detail_comment_pager_item_comment);
                commentViewHolder.btnPop = (ImageView) convertView.findViewById(R.id.note_detail_comment_pager_item_pop);
                convertView.setTag(commentViewHolder);
            }else{
                commentViewHolder = (CommentViewHolder) convertView.getTag();
            }
        }

        if (typeFlag == true){
            Support support = supports.get(position);
            ImageOptions imageOptions = new ImageOptions.Builder()
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    // 默认自动适应大小
                    // .setSize(...)
                    .setIgnoreGif(true)
                            // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                            //.setUseMemCache(false)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
            x.image().bind(supportViewHolder.ivAvatar, support.user_avatar, imageOptions);
            supportViewHolder.tvUserName.setText(support.user_name);
            supportViewHolder.tvUserID.setText("ID:"+support.user_id);
            supportViewHolder.tvCity.setText(support.user_city);
        }

        if (typeFlag == false){
            Comment comment = comments.get(position);
            ImageOptions imageOptions = new ImageOptions.Builder()
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    // 默认自动适应大小
                    // .setSize(...)
                    .setIgnoreGif(true)
                            // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                            //.setUseMemCache(false)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
            x.image().bind(commentViewHolder.ivAvatar, comment.user_avatar, imageOptions);
            commentViewHolder.tvUserName.setText(comment.user_name);
            commentViewHolder.tvDate.setText(comment.time_stamp);

            if ("Y".equals(comment.reply_yn)){
                String source = "<font color='#918b8a'>回复 "+comment.reply_to_user_name+":</font>"+comment.comment_content;
                commentViewHolder.tvComment.setText(Html.fromHtml(source));
            }else if ("N".equals(comment.reply_yn)){
                commentViewHolder.tvComment.setText(comment.comment_content);
            }else{
                commentViewHolder.tvComment.setText(comment.comment_content);
            }
            commentViewHolder.btnPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.d("btnPopClick", "Click");
                }
            });

        }
        return convertView;
    }

    private class SupportViewHolder{
        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public TextView tvUserID;
        public TextView tvCity;
    }

    private class CommentViewHolder{
        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public TextView tvDate;
        public TextView tvComment;
        public ImageView btnPop;
    }
}
