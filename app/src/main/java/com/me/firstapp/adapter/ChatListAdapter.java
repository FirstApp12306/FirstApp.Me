package com.me.firstapp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.entity.User;
import com.me.firstapp.utils.DatabaseUtils;
import com.me.firstapp.utils.DialogUtils;
import com.me.firstapp.utils.PrefUtils;
import com.me.firstapp.utils.TimeFormat;
import com.me.firstapp.view.CircleImageView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class ChatListAdapter extends BaseAdapter {

    private Context context;
    private String mTargetID;
    private Conversation mConv;
    private List<Message> mMsgList = new ArrayList<Message>();// 所有消息列表
    private final int TYPE_RECEIVE_TXT = 0;
    private final int TYPE_SEND_TXT = 1;
    private int mStart;// 当前第0项消息的位置
    private int mOffset = 18;// 上一页的消息数
    private Dialog mDialog;
    private Activity mActivity;
    private boolean mHasLastPage = false;

    public ChatListAdapter(Context context, String targetID) {
        this.context = context;
        this.mActivity = (Activity) context;
        this.mTargetID = targetID;
        this.mConv = JMessageClient.getSingleConversation(mTargetID);
        this.mMsgList = mConv.getMessagesFromNewest(0, mOffset);

        reverse(mMsgList);
        mStart = mOffset;
        List<String> userIDList = new ArrayList<>();
        userIDList.add(targetID);
        userIDList.add(JMessageClient.getMyInfo().getUserName());

    }

    /**
     *倒序排列
     * @param list
     */
    private void reverse(List<Message> list) {
        Collections.reverse(list);
    }

    /**
     * 是否有最后一页
     * @return
     */
    public boolean isHasLastPage() {
        return mHasLastPage;
    }

    public int getOffset() {
        return mOffset;
    }

    public void refreshStartPosition() {
        mStart += mOffset;
    }

    public void doNotify(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public String getTargetID() {
        return mTargetID;
    }

    //当有新消息加到MsgList，自增mStart
    private void incrementStartPosition() {
        ++mStart;
    }

    public void addMsgToList(Message msg) {
        mMsgList.add(msg);
        incrementStartPosition();
        doNotify();
    }

    public void dropDownToRefresh() {
        if (mConv != null) {
            List<Message> msgList = mConv.getMessagesFromNewest(mStart, 18);
            if (msgList != null) {
                for (Message msg : msgList) {
                    mMsgList.add(0, msg);
                }
                if (msgList.size() > 0) {
                    mOffset = msgList.size();
                    mHasLastPage = true;
                } else{
                    mHasLastPage = false;
                }
                doNotify();
            }
        }
    }

    private View createViewByType(Message msg, int position) {
        return getItemViewType(position) == TYPE_SEND_TXT ? View.inflate(context, R.layout.chat_item_send_text, null) : View.inflate(context,R.layout.chat_item_receive_text, null);
    }

    @Override
    public int getCount() {
        return mMsgList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMsgList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = mMsgList.get(position);
        return msg.getDirect().equals(MessageDirect.send) ? TYPE_SEND_TXT: TYPE_RECEIVE_TXT;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message msg = mMsgList.get(position);
        ViewHolder holder = new ViewHolder();
        convertView = createViewByType(msg, position);
        if(msg.getDirect().equals(MessageDirect.send)){
            holder.sendTime = (TextView) convertView.findViewById(R.id.send_time_txt);
            holder.sendAvatar = (CircleImageView) convertView.findViewById(R.id.send_avatar_iv);
            holder.sendFail = (ImageButton) convertView.findViewById(R.id.send_fail_resend_ib);
            holder.sendMsg = (TextView) convertView.findViewById(R.id.send_msg_content);
            holder.sendSending = (ImageView) convertView.findViewById(R.id.send_sending_iv);
        }else if(msg.getDirect().equals(MessageDirect.receive)){
            holder.receiveTime = (TextView) convertView.findViewById(R.id.receive_time_txt);
            holder.receiveAvatar = (CircleImageView) convertView.findViewById(R.id.receive_avatar_iv);
            holder.receiveMsg = (TextView) convertView.findViewById(R.id.receive_msg_content);
        }

        if(msg.getDirect().equals(MessageDirect.receive)){
            showTime(holder.receiveTime, msg, position);
            showAvatar(holder.receiveAvatar, msg);
            String content = ((TextContent) msg.getContent()).getText();
            holder.receiveMsg.setText(content);
        }

        if(msg.getDirect().equals(MessageDirect.send)){
            showTime(holder.sendTime, msg, position);
            showAvatar(holder.sendAvatar, msg);
            String content = ((TextContent) msg.getContent()).getText();
            holder.sendMsg.setText(content);
            handleTextMsg(msg, holder);
        }

        return convertView;
    }

    private void handleTextMsg(final Message msg, final ViewHolder holder) {
        // 检查发送状态，发送方有重发机制
        final Animation sendingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        sendingAnim.setInterpolator(lin);
        switch (msg.getStatus()) {
            case send_success:
                if (sendingAnim != null) {
                    holder.sendSending.clearAnimation();
                    holder.sendSending.setVisibility(View.GONE);
                }
                holder.sendFail.setVisibility(View.GONE);
                break;
            case send_fail:
                if (sendingAnim != null) {
                    holder.sendSending.clearAnimation();
                    holder.sendSending.setVisibility(View.GONE);
                }
                holder.sendFail.setVisibility(View.VISIBLE);
                break;
            case send_going:
                sendingText(holder, sendingAnim, msg);
                break;
            default:
                break;
        }
        // 点击重发按钮，重发消息
        holder.sendFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("重发按钮被点击！");
                showResendDialog(holder, sendingAnim, msg);
            }
        });
    }

    //正在发送文字
    private void sendingText(ViewHolder holder, Animation sendingAnim, Message msg) {
        holder.sendSending.setVisibility(View.VISIBLE);
        holder.sendSending.startAnimation(sendingAnim);
        holder.sendFail.setVisibility(View.GONE);
        //消息正在发送，重新注册一个监听消息发送完成的Callback
        if (!msg.isSendCompleteCallbackExists()) {
            msg.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(final int status, final String desc) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status != 0)
                               // HandleResponseCode.onHandle(context, status);
                            notifyDataSetChanged();
                        }
                    });
                }
            });
        }
    }

    //重发对话框
    private void showResendDialog(final ViewHolder holder, final Animation sendingAnim, final Message msg) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.dialog_base_with_button_cancel_btn:
                        mDialog.dismiss();
                        break;
                    case R.id.dialog_base_with_button_commit_btn:
                        mDialog.dismiss();
                        resendText(holder, sendingAnim, msg);
                        break;
                }
            }
        };
        mDialog = DialogUtils.createResendDialog(context, listener);
        mDialog.show();
    }

    private void resendText(final ViewHolder holder, Animation sendingAnim, Message msg) {
        holder.sendFail.setVisibility(View.GONE);
        holder.sendSending.setVisibility(View.VISIBLE);
        holder.sendSending.startAnimation(sendingAnim);

        if (!msg.isSendCompleteCallbackExists()) {
            msg.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(final int status, String desc) {
                    if (status != 0) {
                        //HandleResponseCode.onHandle(context, status);
                        holder.sendSending.clearAnimation();
                        holder.sendSending.setVisibility(View.GONE);
                        holder.sendFail.setVisibility(View.VISIBLE);
                    }
                    notifyDataSetChanged();
                }
            });
        }

        JMessageClient.sendMessage(msg);
    }

    /**
     * 显示时间
     */
    private void showTime(TextView msgTime, Message msg, int position) {
        long nowDate = msg.getCreateTime();
        if (mOffset == 18) {
            if (position == 0 || position % 18 == 0) {
                TimeFormat timeFormat = new TimeFormat(context, nowDate);
                msgTime.setText(timeFormat.getDetailTime());
                msgTime.setVisibility(View.VISIBLE);
            }else{
                long lastDate = mMsgList.get(position - 1).getCreateTime();
                // 如果两条消息之间的间隔超过十分钟则显示时间
                if (nowDate - lastDate > 600000) {
                    TimeFormat timeFormat = new TimeFormat(context, nowDate);
                    msgTime.setText(timeFormat.getDetailTime());
                    msgTime.setVisibility(View.VISIBLE);
                } else {
                    msgTime.setVisibility(View.GONE);
                }
            }

        }else{
            if (position == 0 || position == mOffset || (position - mOffset) % 18 == 0) {
                TimeFormat timeFormat = new TimeFormat(context, nowDate);
                msgTime.setText(timeFormat.getDetailTime());
                msgTime.setVisibility(View.VISIBLE);
            } else {
                long lastDate = mMsgList.get(position - 1).getCreateTime();
                // 如果两条消息之间的间隔超过十分钟则显示时间
                if (nowDate - lastDate > 600000) {
                    TimeFormat timeFormat = new TimeFormat(context, nowDate);
                    msgTime.setText(timeFormat.getDetailTime());
                    msgTime.setVisibility(View.VISIBLE);
                } else {
                    msgTime.setVisibility(View.GONE);
                }
            }
        }
    }

    private void showAvatar(ImageView avatarView,Message msg){
        String avatar = msg.getFromUser().getAvatar();
        if (TextUtils.isEmpty(avatar)){
            avatarView.setImageResource(R.drawable.person_avatar_default_round);
        }else{
            x.image().bind(avatarView, avatar);
        }
    }

    private class ViewHolder {
        TextView receiveTime;
        CircleImageView receiveAvatar;
        TextView receiveMsg;
        TextView sendTime;
        ImageButton sendFail;
        ImageView sendSending;
        TextView sendMsg;
        CircleImageView sendAvatar;
    }
}
