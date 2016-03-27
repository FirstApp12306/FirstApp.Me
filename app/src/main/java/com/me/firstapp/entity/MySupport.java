package com.me.firstapp.entity;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class MySupport {
    public long create_date;
    public long create_time;
    public String key;
    public long note_agree_counts;
    public long note_comment_counts;
    public String note_content;
    public String note_image;
    public String note_key;
    public String note_user_avatar;
    public String note_user_key;
    public String note_user_name;
    public String support_key;
    public String support_user_avatar;
    public String support_user_key;
    public String support_user_name;
    public String time_stamp;
    public String topic_describe;
    public String topic_key;
    public String topic_title;

    @Override
    public String toString() {
        return "note_key: "+note_key+"note_content:"+note_content;
    }
}
