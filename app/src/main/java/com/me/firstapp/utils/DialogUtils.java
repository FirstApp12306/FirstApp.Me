package com.me.firstapp.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class DialogUtils {
    public static Dialog creatLoadingDialog(Context context, String msg){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        return dialog;
    }
}
