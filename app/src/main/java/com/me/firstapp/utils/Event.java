package com.me.firstapp.utils;

import com.me.firstapp.entity.User;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class Event {

    /**
     * 用户注册事件
     */
    public static class SignUpEvent{
        private User user;
        public SignUpEvent (User user){
            this.user = user;
        }
        public User getUser() {
            return user;
        }
    }

    /**
     * 完成帖子新增图片的事件
     */
    public static class CompleteNoteAddimageEvent{

    }
}
