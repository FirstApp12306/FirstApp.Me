package com.me.firstapp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:软键盘工具
 */
public class SoftInputUtils {

    //隐藏软键盘
    public static void hideSoftInputWindow(Activity activity){
        if(activity.getCurrentFocus()!=null){
            ((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //显示软键盘
    public static void showSoftInputWindow(Activity activity){
        ((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
