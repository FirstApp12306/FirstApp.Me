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
public class NoteDetailListView extends ListView implements AbsListView.OnScrollListener {
    private Context context;
    private int startY = -1;// 滑动起点的y坐标
    private boolean isBottom;
    private View mFooterView;
    private int mFooterViewHeight;
//    private boolean isLoadingMore;
    OnRefreshListener mListener;

    public NoteDetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initFooterView();
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.listview_refresh_footer, null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏

        this.setOnScrollListener(this);
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
                if (startY == -1) {// 确保startY有效
                    startY = (int) ev.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {// 确保startY有效
                    startY = (int) ev.getRawY();
                }
                int endY = (int) ev.getRawY();
                int dy = endY - startY;// 移动便宜量
//                LogUtils.d("dy", dy+" getFirstVisiblePosition:"+getFirstVisiblePosition());
                if (dy > 0 && getFirstVisiblePosition() == 0){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE
                || scrollState == SCROLL_STATE_FLING) {

            if (getLastVisiblePosition() == getCount() - 1) {// 滑动到最后
                System.out.println("到底了.....");
                mFooterView.setPadding(0, 0, 0, 0);// 显示
                setSelection(getCount() - 1);// 改变listview显示位置
                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        }
    }

    /**
     * 收起下拉刷新的控件
     * @param success
     */
    public void onRefreshComplete(boolean success) {
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏脚布局
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public interface OnRefreshListener {
        void onLoadMore();// 加载下一页数据
    }
}
