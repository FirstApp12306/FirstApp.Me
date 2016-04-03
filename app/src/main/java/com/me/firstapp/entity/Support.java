package com.me.firstapp.entity;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class Support {
    public String support_key;
    public String user_avatar;
    public String user_city;
    public String user_id;
    public String user_name;
    public String user_signature;
    public String user_phone;
    public String user_level;
    public String fans_flag;

    @Override
    public String toString() {
        return "support_key:"+support_key;
    }
}
