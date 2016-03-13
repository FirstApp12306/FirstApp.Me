package com.me.firstapp.entity;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class Topic{
    public String topic_key;
    public String topic_title;
    public String topic_detail;
    public String top_topic_yn;
    public String image_url;
    public String user_id;
    public String pub_date;
    public String pub_time;
    public long browse_counts;

    @Override
    public String toString() {
        return "topic_key:"+topic_key+" topic_title:"+topic_title;
    }


}
