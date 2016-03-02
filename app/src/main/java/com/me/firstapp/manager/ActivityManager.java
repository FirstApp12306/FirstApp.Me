package com.me.firstapp.manager;

import java.util.Stack;

import android.app.Activity;
import android.content.Intent;

/**
 * 管理所有Activity 当启动一个Activity时，就将其保存到Stack中， 退出时，从Stack中删除 
 * @author FirstApp.Me
 *
 */
public class ActivityManager {
    /** 
     * 保存所有Activity 
     */  
    private volatile Stack<Activity> activityStack = new Stack<Activity>();
    private static volatile ActivityManager instance;  
    
    private ActivityManager(){
    	
    }  
      
    /** 
     * 创建单例类，提供静态方法调用 
     *  
     * @return ActivityManager 
     */  
    public static ActivityManager getInstance(){  
        if (instance == null){  
            instance = new ActivityManager();  
        }  
        return instance;  
    }
    
    /** 
     * 退出Activity 
     *  
     * @param activity Activity 
     */  
    public void popActivity(Activity activity) {  
        if (activity != null) {  
            activityStack.remove(activity);  
        }  
    }
    
    /** 
     * 获得当前栈顶的Activity 
     *  
     * @return Activity Activity 
     */  
    public Activity currentActivity(){  
        Activity activity = null;  
        if (!activityStack.empty()){  
            activity = activityStack.lastElement();  
        }  
        return activity;  
    }
    
    /** 
     * 将当前Activity推入栈中 
     *  
     * @param activity Activity 
     */  
    public void pushActivity(Activity activity){  
        activityStack.add(activity);
    }
    
    /** 
     * 退出栈中其他所有Activity 
     *  
     * @param cls Class 类名 
     */  
    @SuppressWarnings("rawtypes")  
    public void popOtherActivity(Class cls) {  
        if(null == cls){  
            return;  
        }
        
        for(Activity activity : activityStack){  
            if (null == activity || activity.getClass().equals(cls)){  
                continue;  
            }  
            activity.finish();  
        }  
    }
    
    /** 
     * 退出栈中所有Activity 
     *  
     */  
    public void popAllActivity(){  
        while (true){  
            Activity activity = currentActivity();  
            if (activity == null){  
                break;  
            }  
            activity.finish();  
            popActivity(activity); 
        }  
    }
    
    /**
     * 启动当前栈顶的Activity 
     * @param activity
     */
    public void startCurActivity(Class<?> activity){  
        Activity curActivity = currentActivity();  
        Intent intent = new Intent(curActivity, activity);  
        curActivity.startActivity(intent);
    }
    
    /**
     * 结束当前的Activity
     * @param curActivity
     */
    public void finishCurActivity(Activity curActivity){
    	curActivity.finish();  
        popActivity(curActivity); 
    }
}
