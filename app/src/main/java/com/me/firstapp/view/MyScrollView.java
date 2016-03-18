package com.me.firstapp.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.utils.PrefUtils;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class MyScrollView extends ScrollView{

    private GestureDetector mGestureDetector;
    private OnScrollToBottomListener onScrollToBottom;
    private boolean isBottom;
    private Context context;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                //return !isBottom;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //return !isBottom;
                break;
            }
        }

        //return super.onInterceptTouchEvent(ev) & mGestureDetector.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev) & mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            /**
             * 如果我们滚动更接近水平方向,返回false,让子视图来处理它
             */
            return (Math.abs(distanceY) > Math.abs(distanceX));
        }
    }


    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
                                  boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
//        if(scrollY != 0){
////            onScrollToBottom.onScrollBottomListener(clampedY);
//        }
        LogUtils.d("clampedY", clampedY+"");
        isBottom = clampedY;
        PrefUtils.setBoolean(context, "isBottom", isBottom);
    }


    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener){
        onScrollToBottom = listener;
    }

    public interface OnScrollToBottomListener{
        public void onScrollBottomListener(boolean isBottom);
    }

}
