package com.me.firstapp.entity;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class User {
    public String user_id;
    public String password;
    public String user_avatar;
    public long user_level;
    public String user_city;
    public String user_name;
    public String user_phone;
    public long user_points;
    public String user_sex;
    public String user_signature;
    public String sts;
    public String fans;
    public String follow;
    public String fans_flag;
    public String friend_flag;
    public String sortLetters;//显示数据拼音的首字母


    @Override
    public String toString() {
        return "user_id："+user_id+"  user_name："+user_name;
    }
}
