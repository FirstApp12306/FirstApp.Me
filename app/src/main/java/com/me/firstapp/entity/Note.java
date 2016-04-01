package com.me.firstapp.entity;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class Note {
    public String note_key;
    public long create_date;
    public long create_time;
    public String time_stamp;
    public long note_agree_counts;
    public long note_comment_counts;
    public String note_content;
    public String topic_key;
    public String image_key;
    public String support_flag;

    @Override
    public String toString() {
        return "note_key:"+note_key+"note_content:"+note_content;
    }
}
