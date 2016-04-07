package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.Blog;
import com.me.firstapp.utils.ImageUtils;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class BlogPagerListAdapter extends BaseAdapter {
    private Context context;
    private Activity mActivity;
    private ArrayList<Blog> blogs;

    public BlogPagerListAdapter(Context context, ArrayList<Blog> blogs) {
        this.context = context;
        this.mActivity = (Activity) context;
        this.blogs = blogs;
    }

    @Override
    public int getCount() {
        return blogs.size();
    }

    @Override
    public Object getItem(int position) {
        return blogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.pager_blog_list_item, null);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.pager_blog_list_item_blog_title);
            holder.tvTime = (TextView) convertView.findViewById(R.id.pager_blog_list_item_blog_time);
            holder.tvPubDate = (TextView) convertView.findViewById(R.id.pager_blog_list_item_blog_date);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.pager_blog_list_item_blog_image);
            holder.tvSummary = (TextView) convertView.findViewById(R.id.pager_blog_list_item_blog_summary);
            holder.tvMore = (TextView) convertView.findViewById(R.id.pager_blog_list_item_blog_more);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Blog blog = blogs.get(position);
        holder.tvTitle.setText(blog.blog_title);
        holder.tvTime.setText(blog.time_stamp);
        holder.tvPubDate.setText(blog.pub_date+"");
        holder.tvSummary.setText(blog.blog_summary);
        ImageUtils.bindImage(holder.ivImage, blog.blog_image);
        return convertView;
    }

    private class ViewHolder{
        TextView tvTitle;
        TextView tvTime;
        TextView tvPubDate;
        ImageView ivImage;
        TextView tvSummary;
        TextView tvMore;
    }
}
