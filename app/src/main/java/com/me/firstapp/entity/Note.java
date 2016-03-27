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
    public String approve_sts;
    public String approve_yn;
    public long create_date;
    public long create_time;
    public String time_stamp;
    public String display_yn;
    public int myrownum;
    public long note_agree_counts;
    public long note_comment_counts;
    public String note_content;
    public String report_sts;
    public String topic_key;
    public String image_key;

    @Override
    public String toString() {
        return "note_key:"+note_key+"note_content:"+note_content;
    }
}
