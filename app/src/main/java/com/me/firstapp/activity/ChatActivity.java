package com.me.firstapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.me.firstapp.R;
import com.me.firstapp.adapter.ChatListAdapter;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.view.DropDownListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.ref.WeakReference;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.activity_chat_btn_return)
    private ImageButton btnReturn;
    @ViewInject(R.id.activity_chat_title)
    private TextView tvTitle;
    @ViewInject(R.id.chat_activity_chat_list)
    private DropDownListView mListView;
    @ViewInject(R.id.chat_activity_chat_input)
    private EditText mEditText;
    @ViewInject(R.id.chat_activity_send_msg)
    private Button btnSend;

    private String targetID;
    private String userName;
    private String userAvatar;
    private Conversation conv;
    private ChatListAdapter mChatAdapter;
    private static final int REFRESH_LAST_PAGE = 1023;
    private static final int UPDATE_CHAT_LISTVIEW = 1026;
    private final MyHandler myHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
        btnSend.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        targetID = getIntent().getStringExtra("targetID");
        userName = getIntent().getStringExtra("user_name");
        conv = JMessageClient.getSingleConversation(targetID); // 用targetID得到会话
        if(conv != null){
            UserInfo userInfo = (UserInfo) conv.getTargetInfo();
            if (TextUtils.isEmpty(userName)) {//无用户名则显示账号
                tvTitle.setText(userInfo.getUserName());
            }else{
                tvTitle.setText(userName);
            }
        }
        if (conv == null) {
            conv = Conversation.createSingleConversation(targetID);
            UserInfo userInfo = (UserInfo) conv.getTargetInfo();
            if (TextUtils.isEmpty(userInfo.getNickname())) {//无用户名则显示账号
                tvTitle.setText(userInfo.getUserName());
            }else{
                tvTitle.setText(userInfo.getNickname());
            }
        }
        if(conv != null){
            mChatAdapter = new ChatListAdapter(this, targetID);
            mListView.setAdapter(mChatAdapter);
            setListviewToBottom();
            mListView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {

                @Override
                public void onDropDown() {
                    System.out.println("执行刷新！");
                }
            });
        }
        EventBus.getDefault().post(new Event.ResetNewMsgNumEvent(conv));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    /**
     * 将listview定位到底部
     */
    private void setListviewToBottom() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(mListView.getBottom());//将listview定位到底部
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_chat_btn_return:
                finish();
                break;
            case R.id.chat_activity_send_msg:
                setListviewToBottom();
                if (TextUtils.isEmpty(mEditText.getText().toString())){
                    Toast.makeText(this, "消息不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                TextContent content = new TextContent(mEditText.getText().toString());
                Message msg = conv.createSendMessage(content);
                msg.setOnSendCompleteCallback(new BasicCallback() {

                    @Override
                    public void gotResult(int status, String desc) {
                        if (status == 803008) {//发送消息失败，发送者已被接收者拉入黑名单，仅限单聊
                            CustomContent customContent = new CustomContent();
                            customContent.setBooleanValue("blackList", true);
                            Message customMsg = conv.createSendMessage(customContent);
                            mChatAdapter.addMsgToList(customMsg);
                        } else if (status != 0) {
                            // HandleResponseCode.onHandle(ChatActivity.this, status);
                        }
                        // 发送成功或失败都要刷新一次
                        myHandler.sendEmptyMessage(UPDATE_CHAT_LISTVIEW);
                    }
                });
                mChatAdapter.addMsgToList(msg);
                JMessageClient.sendMessage(msg);
                mEditText.setText("");
                // 暂时使用EventBus更新会话列表，以后sdk会同步更新Conversation
                EventBus.getDefault().post(new Event.UpdateConvEvent());
                break;

            default:
                break;
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<ChatActivity> mChatActivity;
        public MyHandler(ChatActivity chatActivity) {
            mChatActivity = new WeakReference(chatActivity);
        }
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            ChatActivity chatActivity = mChatActivity.get();
            switch (msg.what) {
                case REFRESH_LAST_PAGE:
                    chatActivity.mChatAdapter.dropDownToRefresh();
                    chatActivity.mListView.onDropDownComplete();
                    if(chatActivity.mChatAdapter.isHasLastPage()){
                        chatActivity.mListView.setSelection(chatActivity.mChatAdapter.getOffset());
                        chatActivity.mChatAdapter.refreshStartPosition();
                    } else {
                        chatActivity.mListView.setSelection(0);
                    }
                    break;
                case UPDATE_CHAT_LISTVIEW:
                    chatActivity.mChatAdapter.doNotify();
                    break;
            }
        }
    }

    /**
     * 接收消息类事件
     *
     * @param event 消息事件
     */
    public void onEvent(MessageEvent event) {
        LogUtils.d("MessageEvent", "MessageEvent");
        EventBus.getDefault().post(new Event.ResetNewMsgNumEvent(conv));
        final Message msg = event.getMessage();
        //刷新消息
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String targetID = ((UserInfo) msg.getTargetInfo()).getUserName();
                //判断消息是否在当前会话中
                if (targetID.equals(mChatAdapter.getTargetID())) {
                    mChatAdapter.addMsgToList(msg);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        JMessageClient.exitConversaion();
    }


}
