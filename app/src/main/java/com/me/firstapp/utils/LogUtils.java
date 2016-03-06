package com.me.firstapp.utils;

import android.util.Log;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 */
public class LogUtils {
    /**
     * 是否开启debug
     */
    public static boolean isDebug=true;

    public static void d(String tag,String msg){
        if(isDebug){
            Log.d(tag, msg + "");
        }
    }

    public static void i(String tag,String msg){
        if(isDebug){
            Log.i(tag, msg + "");
        }
    }

    public static void w(String tag,String msg){
        if(isDebug){
            Log.w(tag, msg + "");
        }
    }

    public static void e(String tag,String msg){
        if(isDebug){
            Log.e(tag, msg + "");
        }
    }
}
