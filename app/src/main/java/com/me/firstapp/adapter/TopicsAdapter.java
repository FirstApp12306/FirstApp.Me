package com.me.firstapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.Topic;

import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class TopicsAdapter extends BaseAdapter {
    private Activity mActivity;
    private Context context;
    private ArrayList<Topic> topicList;

    public TopicsAdapter(Context context,ArrayList<Topic> topicList){
        this.context = context;
        this.mActivity = (Activity)context;
        this.topicList = topicList;
    }
    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public Topic getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addMore(ArrayList<Topic> moreTopicsList){
        topicList.addAll(moreTopicsList);
        doNotify();
    }

    public void clearList(){
        this.topicList.clear();
        doNotify();
    }

    public void doNotify(){
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.pager_topic_listview_item, null);
            holder = new ViewHolder();
            holder.tvTopicTitle = (TextView) convertView.findViewById(R.id.pager_topic_listview_item_topic_title);
            holder.tvCounts = (TextView) convertView.findViewById(R.id.pager_topic_listview_item_browse_counts);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Topic topic = getItem(position);
        holder.tvTopicTitle.setText(topic.topic_title);
        holder.tvCounts.setText(topic.browse_counts+" 次浏览");

        return convertView;
    }

    private class ViewHolder {
        public TextView tvTopicTitle;
        public TextView tvCounts;
    }
}
