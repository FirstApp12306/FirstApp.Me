package com.me.firstapp.entity;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class User {
    public String id;
    public String name;
    public String phone;
    public String avatar;
    public String password;
    public String signature;
    public String sex;
    public String city;
    public String level;
    public String points;
    public String sts;

    @Override
    public String toString() {
        return "ID："+id+" 用户名："+name;
    }
}
