package com.me.firstapp.utils;

import java.util.Comparator;

import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class SortConvList implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        Conversation conv1 = (Conversation) lhs;
        Conversation conv2 = (Conversation) rhs;
        //返回-1为升序，即将最近收到消息的会话放在第一位
        int flag;
        Message msg1 = conv1.getLatestMessage();
        Message msg2 = conv2.getLatestMessage();
        long compareTime1;
        long compareTime2;
        if (msg1 != null && msg2 != null){
            compareTime1 = msg1.getCreateTime();
            compareTime2 = msg2.getCreateTime();
        }else if (msg1 == null && msg2 != null){
            compareTime1 = conv1.getLastMsgDate();
            compareTime2 = msg2.getCreateTime();
        }else if (msg1 != null && msg2 == null){
            compareTime1 = msg1.getCreateTime();
            compareTime2 = conv2.getLastMsgDate();
        }else {
            compareTime1 = conv1.getLastMsgDate();
            compareTime2 = conv2.getLastMsgDate();
        }
        if(compareTime1 > compareTime2)
            flag = -1;
        else if(compareTime1 < compareTime2)
            flag = 1;
        else flag = 0;
        return flag;
    }
}
