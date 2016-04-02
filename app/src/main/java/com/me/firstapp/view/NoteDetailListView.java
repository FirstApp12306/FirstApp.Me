package com.me.firstapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;

import com.me.firstapp.R;
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

    public NoteDetailListView(Context context) {
        super(context);
    }

    public NoteDetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteDetailListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN :
                break;
            case MotionEvent.ACTION_SCROLL :
                return false;

        }
        return super.onTouchEvent(ev);
    }
}
