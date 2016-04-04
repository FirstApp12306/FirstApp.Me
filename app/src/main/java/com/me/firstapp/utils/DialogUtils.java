package com.me.firstapp.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.me.firstapp.R;
import com.me.firstapp.view.CircleImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class DialogUtils {

    /**
     * 加载对话框
     * @param context
     * @param msg
     * @return
     */
    public static Dialog creatLoadingDialog(Context context, String msg){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        return dialog;
    }

    /**
     * 普通对话框
     * @param context
     * @param listener
     * @return
     */
    public static Dialog createCommonDialog(Context context, View.OnClickListener listener, String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_base_with_button, null);
        builder.setView(view);
        Button cancelBtn = (Button) view.findViewById(R.id.dialog_base_with_button_cancel_btn);
        Button resendBtn = (Button) view.findViewById(R.id.dialog_base_with_button_commit_btn);
        Dialog dialog = builder.create();
        TextView title = (TextView) view.findViewById(R.id.dialog_base_with_button_title);
        title.setText(text);
        cancelBtn.setText("否");
        resendBtn.setText("是");
        cancelBtn.setOnClickListener(listener);
        resendBtn.setOnClickListener(listener);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    /**
     * 创建联系人列表中点击的dialog
     * @param context
     * @param listener
     * @return
     */
    public static Map<String , Object> createContactsItemDialog(Context context, View.OnClickListener listener, String user_avatar, String user_name, String user_id, String friend_flag){
        Map<String , Object> map = new HashMap<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.contacts_list_item_click_dialog, null);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.contacts_list_item_click_dialog_btn_close);
        CircleImageView ivAvatar = (CircleImageView) view.findViewById(R.id.contacts_list_item_click_dialog_iv_avatar);
        TextView tvUserName = (TextView) view.findViewById(R.id.contacts_list_item_click_dialog_user_name);
        TextView tvUserID = (TextView) view.findViewById(R.id.contacts_list_item_click_dialog_user_id);
        ImageButton btnFollow = (ImageButton) view.findViewById(R.id.contacts_list_item_click_dialog_btn_follow);
        Button btnChat = (Button) view.findViewById(R.id.contacts_list_item_click_dialog_btn_chat);
        Button btnHome = (Button) view.findViewById(R.id.contacts_list_item_click_dialog_btn_home);
        btnClose.setOnClickListener(listener);
        btnFollow.setOnClickListener(listener);
        btnChat.setOnClickListener(listener);
        btnHome.setOnClickListener(listener);
        if ("true".equals(friend_flag)){
            btnFollow.setImageResource(R.drawable.person_follow_eachother);
        }else{
            btnFollow.setImageResource(R.drawable.person_follow_cancel);
        }
        ImageUtils.bindImageWithOptions(ivAvatar, user_avatar, R.drawable.person_avatar_default_round, R.drawable.person_avatar_default_round);
        tvUserName.setText(user_name);
        tvUserID.setText(user_id);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        map.put("dialog", dialog);
        map.put("btnFollow", btnFollow);
        return map;
    }
}
