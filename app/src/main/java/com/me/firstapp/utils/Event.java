package com.me.firstapp.utils;

import com.me.firstapp.entity.Note;
import com.me.firstapp.entity.User;

import cn.jpush.im.android.api.model.Conversation;

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

    /**
     * 完成头像图片选择的事件
     */
    public static class CompleteAvatarSelectEvent{

    }

    /**
     * 完成用户名修改事件
     */
    public static class CompleteAlterNameEvent{

        private String name;

        public CompleteAlterNameEvent(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 完成用户城市修改事件
     */
    public static class CompleteAlterCityEvent{
        private String city;

        public CompleteAlterCityEvent(String city) {
            this.city = city;
        }

        public String getCity() {
            return city;
        }
    }

    /**
     * 完成用户签名修改事件
     */
    public static class CompleteAlterSignatureEvent{
        private String signature;

        public CompleteAlterSignatureEvent(String signature) {
            this.signature = signature;
        }

        public String getSignature() {
            return signature;
        }
    }

    /**
     * 完成用户性别修改事件
     */
    public static class CompleteAlterSexEvent{
        private String sex;

        public CompleteAlterSexEvent(String sex) {
            this.sex = sex;
        }

        public String getSex() {
            return sex;
        }
    }

    /**
     * 新评论事件
     */
    public static class NewCommentEvent{
        private String extraMsg;
        private String extraExtra;

        public NewCommentEvent(String extraMsg, String extraExtra) {
            this.extraMsg = extraMsg;
            this.extraExtra = extraExtra;
        }

        public String getExtraMsg() {
            return extraMsg;
        }

        public String getExtraExtra() {
            return extraExtra;
        }
    }

    /**
     * 新点赞事件
     */
    public static class NewSupportEvent{
        private String extraMsg;
        private String extraExtra;

        public NewSupportEvent(String extraMsg, String extraExtra) {
            this.extraMsg = extraMsg;
            this.extraExtra = extraExtra;
        }

        public String getExtraMsg() {
            return extraMsg;
        }

        public String getExtraExtra() {
            return extraExtra;
        }
    }

    /**
     * 新粉丝事件
     */
    public static class NewFansEvent{
        private String extraMsg;
        private String extraExtra;

        public NewFansEvent(String extraMsg, String extraExtra) {
            this.extraMsg = extraMsg;
            this.extraExtra = extraExtra;
        }

        public String getExtraMsg() {
            return extraMsg;
        }

        public String getExtraExtra() {
            return extraExtra;
        }
    }

    /**
     * 更新会话列表事件
     */
    public static class UpdateConvEvent{

    }

    /**
     * 重置新消息数量事件
     */
    public static class ResetNewMsgNumEvent{
        private Conversation conv;

        public ResetNewMsgNumEvent(Conversation conv) {
            this.conv = conv;
        }

        public Conversation getConv() {
            return conv;
        }
    }

    /**
     * 刷新话题事件
     */
    public static class RefreshTopicsEvent{

    }

    /**
     * 刷新帖子事件
     */
    public static class RefreshNotesEvent{
        private Note note;
        private User user;

        public RefreshNotesEvent(Note note, User user) {
            this.note = note;
            this.user = user;
        }

        public Note getNote() {
            return note;
        }

        public User getUser() {
            return user;
        }
    }
}
