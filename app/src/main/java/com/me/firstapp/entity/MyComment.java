package com.me.firstapp.entity;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class MyComment {
    public String comment_key;
    public String comment_content;
    public String comment_user_avatar;
    public String comment_user_key;
    public String comment_user_name;
    public String key;
    public String note_content;
    public String note_image;
    public String note_key;
    public String note_user_avatar;
    public String note_user_key;
    public String note_user_name;
    public String topic_describe;
    public String topic_key;
    public String topic_title;
    public long myrownum;
    public long note_agree_counts;
    public long note_comment_counts;
    public String time_stamp;
    public long create_date;
    public long create_time;

    @Override
    public String toString() {
        return "comment_key:"+comment_key+" comment_content:"+comment_content;
    }
}
