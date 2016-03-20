package com.me.firstapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.me.firstapp.R;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener, android.widget.AdapterView.OnItemClickListener {
    private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
    private static final int STATE_REFRESHING = 2;// 正在刷新

    private View mHeaderView;
    private int startY = -1;// 滑动起点的y坐标
    private int mHeaderViewHeight;
    private View mFooterView;
    private int mFooterViewHeight;

    private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态
    private TextView tvTitle;
    private ProgressBar pbProgress;
    private boolean isLoadingMore;
    OnRefreshListener mListener;

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.listview_refresh_header, null);
        this.addHeaderView(mHeaderView);

        tvTitle = (TextView) mHeaderView.findViewById(R.id.listview_refresh_header_tv_title);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.listview_refresh_header_pb_progress);

        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();

        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏头布局
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        mFooterView = View.inflate(getContext(),
                R.layout.listview_refresh_footer, null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏

        this.setOnScrollListener(this);
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

                if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
                    break;
                }

                int endY = (int) ev.getRawY();
                int dy = endY - startY;// 移动便宜量

                if (dy > 0 && getFirstVisiblePosition() == 0) {// 只有下拉并且当前是第一个item,才允许下拉
                    int padding = dy - mHeaderViewHeight;// 计算padding
                    mHeaderView.setPadding(0, padding, 0, 0);// 设置当前padding

                    if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {// 状态改为松开刷新
                        mCurrrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
                        mCurrrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }
                    super.onTouchEvent(ev);//调用父类方法，防止滑动时触发点击事件
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;// 重置

                if (mCurrrentState == STATE_RELEASE_REFRESH) {
                    mCurrrentState = STATE_REFRESHING;// 正在刷新
                    mHeaderView.setPadding(0, 0, 0, 0);// 显示
                    refreshState();
                } else if (mCurrrentState == STATE_PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏
                }

                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉控件的布局
     */
    private void refreshState() {
        switch (mCurrrentState) {
            case STATE_PULL_REFRESH:
                tvTitle.setText("下拉刷新");
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                if (mListener != null) {
                    mListener.onRefresh();
                }
                break;

            default:
                break;
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();// 加载下一页数据
    }

    /**
     * 收起下拉刷新的控件
     * @param success
     */
    public void onRefreshComplete(boolean success) {
        if (isLoadingMore) {// 正在加载更多...
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏脚布局
            isLoadingMore = false;
        } else {
            mCurrrentState = STATE_PULL_REFRESH;
            tvTitle.setText("下拉刷新");

            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏

        }
    }

    OnItemClickListener mItemClickListener;

    @Override
    public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
        mItemClickListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(parent, view, position
                    - getHeaderViewsCount(), id);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE
                || scrollState == SCROLL_STATE_FLING) {

            if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {// 滑动到最后
                System.out.println("到底了.....");
                mFooterView.setPadding(0, 0, 0, 0);// 显示
                setSelection(getCount() - 1);// 改变listview显示位置

                isLoadingMore = true;

                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
