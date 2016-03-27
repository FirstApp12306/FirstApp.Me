package com.me.firstapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("qqq","接收到消息");
        Bundle bundle = intent.getExtras();
        String action = intent.getAction();
        LogUtils.d("action", action);


        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了自定义消息。消息标题是：" + bundle.getString(JPushInterface.EXTRA_TITLE));
            System.out.println("消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            System.out.println("附件字段是：" + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //评论消息
            if ("comment".equals(bundle.getString(JPushInterface.EXTRA_TITLE))){
                String newCommentNum = PrefUtils.getString(context, "new_comment_num", "0");
                newCommentNum = (Long.parseLong(newCommentNum)+1)+"";
                LogUtils.d("newCommentNum", newCommentNum);
                PrefUtils.setString(context, "new_comment_num", newCommentNum);
                EventBus.getDefault().post(new Event.NewCommentEvent(bundle.getString(JPushInterface.EXTRA_MESSAGE), bundle.getString(JPushInterface.EXTRA_EXTRA)));
            }
            //点赞消息
            if ("support".equals(bundle.getString(JPushInterface.EXTRA_TITLE))){
                String newSupportNum = PrefUtils.getString(context, "new_support_num", "0");
                newSupportNum = (Long.parseLong(newSupportNum)+1)+"";
                LogUtils.d("newSupportNum", newSupportNum);
                PrefUtils.setString(context, "new_support_num", newSupportNum);
                EventBus.getDefault().post(new Event.NewSupportEvent(bundle.getString(JPushInterface.EXTRA_MESSAGE), bundle.getString(JPushInterface.EXTRA_EXTRA)));
            }
            //加粉丝消息
            if ("fans".equals(bundle.getString(JPushInterface.EXTRA_TITLE))){
                String newFansNum = PrefUtils.getString(context, "new_fans_num", "0");
                newFansNum = (Long.parseLong(newFansNum)+1)+"";
                LogUtils.d("newFansNum", newFansNum);
                PrefUtils.setString(context, "new_support_num", newFansNum);
                EventBus.getDefault().post(new Event.NewFansEvent(bundle.getString(JPushInterface.EXTRA_MESSAGE), bundle.getString(JPushInterface.EXTRA_EXTRA)));
            }

            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
//            Intent i = new Intent(context, TestActivity.class);  //自定义打开的界面
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
        } else {

        }
    }
}
