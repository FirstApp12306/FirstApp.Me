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

    public static void d(Class<?> clazz,String msg){
        if(isDebug){
            Log.d(clazz.getSimpleName(), msg + "");
        }
    }

    public static void i(Class<?> clazz,String msg){
        if(isDebug){
            Log.i(clazz.getSimpleName(), msg + "");
        }
    }

    public static void w(Class<?> clazz,String msg){
        if(isDebug){
            Log.w(clazz.getSimpleName(), msg + "");
        }
    }

    public static void e(Class<?> clazz,String msg){
        if(isDebug){
            Log.e(clazz.getSimpleName(), msg + "");
        }
    }
}
