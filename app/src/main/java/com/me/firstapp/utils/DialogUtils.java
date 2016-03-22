package com.me.firstapp.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.me.firstapp.R;

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
     * 重新发送消息的对话框
     * @param context
     * @param listener
     * @return
     */
    public static Dialog createResendDialog(Context context, View.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_base_with_button, null);
        builder.setView(view);
        Button cancelBtn = (Button) view.findViewById(R.id.dialog_base_with_button_cancel_btn);
        Button resendBtn = (Button) view.findViewById(R.id.dialog_base_with_button_commit_btn);
        Dialog dialog = builder.create();
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("是否重新发送？");
        cancelBtn.setText("否");
        resendBtn.setText("是");
        cancelBtn.setOnClickListener(listener);
        resendBtn.setOnClickListener(listener);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}
