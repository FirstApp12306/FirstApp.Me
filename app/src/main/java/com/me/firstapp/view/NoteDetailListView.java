package com.me.firstapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class NoteDetailListView extends ListView {
    private Context context;
    private int startY = -1;// 滑动起点的y坐标
    private boolean isBottom;
    public NoteDetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
//                isBottom = PrefUtils.getBoolean(context, "isBottom", false);
//                LogUtils.d("PrefUtilsisBottom", isBottom+"");
//                if (!isBottom){
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
                break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {// 确保startY有效
                    startY = (int) ev.getRawY();
                }
                int endY = (int) ev.getRawY();
                int dy = endY - startY;// 移动便宜量
                LogUtils.d("dy", dy+" getFirstVisiblePosition:"+getFirstVisiblePosition());
                if (dy > 0 && getFirstVisiblePosition() == 0){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:
        }
        return super.onTouchEvent(ev);
    }
}
