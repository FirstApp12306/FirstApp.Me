package com.me.firstapp.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.utils.TimeFormat;
import com.me.firstapp.view.CircleImageView;

import org.xutils.x;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class ConversationListAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<Conversation> convDatas;

    public ConversationListAdapter(Activity mActivity,List<Conversation> convDatas) {
        this.mActivity = mActivity;
        this.convDatas = convDatas;
    }


    @Override
    public int getCount() {
        return convDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return convDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 收到消息后将会话置顶
     */
    public void setToTop(String targetID) {
        Conversation conv = JMessageClient.getSingleConversation(targetID);
        for (Conversation conversation : convDatas) {
            if (conv.getId().equals(conversation.getId())) {
                convDatas.remove(conversation);
                convDatas.add(0, conv);
                doNotify();
                return;
            }
        }
        // 如果是新的会话
        addNewConversation(conv, targetID);
    }

    public void doNotify(){
        mActivity.runOnUiThread(new Runnable() {//因为消息的接收事件是在子线程中的

            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    //新增新的会话
    public void addNewConversation(final Conversation conv,final String targetID) {
        convDatas.add(0, conv);
        doNotify();
    }

    //重置未读消息数量
    public void setUnReadCount(Conversation conversation){
        for(Conversation conv : convDatas ){
            if (conv.getId().equals(conversation.getId())){
                conv.resetUnreadCount();
                break;
            }
        }
        doNotify();
    }

    //刷新会话列表
    public void refreshConv(List<Conversation> convDatas){
        this.convDatas = convDatas;
        doNotify();
    }

    /**
     * 加载头像并刷新
     *
     * @param targetID
     *            用户名
     * @param path
     *            头像路径
     */
    public void loadAvatarAndRefresh(String targetID, String path) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mActivity, R.layout.view_pager_message_conv_listview_item, null);
            viewHolder.userAvatar = (CircleImageView) convertView.findViewById(R.id.view_pager_message_conv_listview_item_avatar);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.view_pager_message_conv_listview_item_user_name);
            viewHolder.msgContent = (TextView) convertView.findViewById(R.id.view_pager_message_conv_listview_item_msg);
            viewHolder.datetime = (TextView) convertView.findViewById(R.id.view_pager_message_conv_listview_item_time);
            viewHolder.newMsgNumber = (TextView) convertView.findViewById(R.id.view_pager_message_conv_listview_item_msg_num);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Conversation convItem = convDatas.get(position);
        Message lastMsg = convItem.getLatestMessage();

        //设置日期和消息内容
        if (lastMsg != null) {
            TimeFormat timeFormat = new TimeFormat(mActivity,lastMsg.getCreateTime());
            viewHolder.datetime.setText(timeFormat.getTime());
            viewHolder.msgContent.setText(((TextContent) lastMsg.getContent()).getText());
        } else {
            TimeFormat timeFormat = new TimeFormat(mActivity,convItem.getLastMsgDate());
            viewHolder.datetime.setText(timeFormat.getTime());
            viewHolder.msgContent.setText("");
        }

        //设置用户名
        viewHolder.userName.setText(convItem.getTitle());

        //设置头像
        String avatar = ((UserInfo) convItem.getTargetInfo()).getAvatar();
        if (TextUtils.isEmpty(avatar)){
            viewHolder.userAvatar.setImageResource(R.drawable.person_avatar_default_round);
        }else{
            x.image().bind(viewHolder.userAvatar, ((UserInfo) convItem.getTargetInfo()).getAvatar());
        }

        // TODO 更新Message的数量,
        if (convItem.getUnReadMsgCnt() > 0) {
            viewHolder.newMsgNumber.setVisibility(View.VISIBLE);
            if (convItem.getUnReadMsgCnt() < 100) {
                viewHolder.newMsgNumber.setText(String.valueOf(convItem.getUnReadMsgCnt()));
            } else {
                viewHolder.newMsgNumber.setText("99+");
            }
        } else {
            viewHolder.newMsgNumber.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder {
        CircleImageView userAvatar;
        TextView userName;
        TextView msgContent;
        TextView datetime;
        TextView newMsgNumber;
    }
}
