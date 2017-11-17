package shen.da.ye.ultimaterecyclerview.view.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import shen.da.ye.ultimaterecyclerview.MyApplication;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.appbar.AppBarStateChangeListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.ILoadMoreFooter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.IRefreshHeader;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnRefreshListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.footer.LoadMoreFooter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.header.RefreshHeader;

/**
 * @author ChenYe
 *         created by on 2017/11/15 0015. 16:39
 **/

public class UltimateRecyclerView extends RecyclerView {

    private IRefreshHeader mIRefreshHeader = null;
    private ILoadMoreFooter mILoadMoreFooter = null;
    private View mLoadMoreFooterView = null;
    private float mStartX;
    private float mStartY;
    private boolean mIsVpDrager = false;
    private int mTouchSlop;
    private float mLastY = -1;
    private float mSumOffset;
    private static final float DRAG_RATE = 2.0f;
    private OnRefreshListener mOnRefreshListener = null;
    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;
    /**
     * 是否启用下拉刷新功能，默认是启用的
     */
    private boolean mPullRefreshEnabled = true;
    /**
     * 是否正在下拉刷新
     */
    private boolean mRefreshing = false;
    /**
     * 是否正在加载数据
     */
    private boolean mLoadingData = false;

    public UltimateRecyclerView(Context context) {
        this(context, null);
    }

    public UltimateRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UltimateRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //获取这个手机手指点击最小滑动距离（超过这个距离就算滑动不算点击）
        mTouchSlop = ViewConfiguration.get(MyApplication.mShareInstance).getScaledTouchSlop();
        initRefreshHeader();
        initLoadMoreFooter();
    }

    private void initLoadMoreFooter() {
        mILoadMoreFooter = new LoadMoreFooter(MyApplication.mShareInstance);
        mLoadMoreFooterView = mILoadMoreFooter.getLoadMoreFooter();
        mLoadMoreFooterView.setVisibility(GONE);
        ViewGroup.LayoutParams layoutParams = mLoadMoreFooterView.getLayoutParams();
        if (layoutParams == null) {
            mLoadMoreFooterView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }

    }

    private void initRefreshHeader() {
        mIRefreshHeader = new RefreshHeader(MyApplication.mShareInstance);
    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * 解决嵌套RecyclerView滑动冲突问题,返回false代表不进行拦截,
     * 如果recyclerView不是在viewpager里面，可以屏蔽下面代码
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                mStartY = ev.getY();
                mStartX = ev.getX();
                // 初始化标记
                mIsVpDrager = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if (mIsVpDrager) {
                    return false;
                }

                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - mStartX);
                float distanceY = Math.abs(endY - mStartY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsVpDrager = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsVpDrager = false;
                break;
            default:
                break;
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev);
    }


    /**
     * 在这里是处理下拉刷新的
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                mSumOffset = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = (ev.getRawY() - mLastY) / DRAG_RATE;
                mLastY = ev.getRawY();
                mSumOffset += deltaY;
                if (isOnTop() && !mRefreshing
                        && (appbarState == AppBarStateChangeListener.State.EXPANDED)) {
                    mIRefreshHeader.onMove(deltaY, mSumOffset);
                    if (mIRefreshHeader.getVisibleHeight() > 0) {
                        //TODO onTouchEvent的返回值含义
                        return false;
                    }
                }
                break;
            default:
                // reset
                mLastY = -1;
                if (isOnTop() && !mRefreshing) {
                    //TODO 好好注意下这个onRelease()方法里面是怎么走的，很值得学习
                    if (mIRefreshHeader.onRelease()) {
                        if (mOnRefreshListener != null) {
                            mRefreshing = true;
                            mLoadMoreFooterView.setVisibility(GONE);
                            mOnRefreshListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isOnTop() {
        //完全可见的时候getParent不为空，不完整可见的时候getParent为null,我猜测这个应该是涉及到一个知识点:
        // 通过View.getParent是否为null来判断这个View是否完整可见,为null代表不是完整可见
        return mPullRefreshEnabled && (mIRefreshHeader.getHeaderView().getParent() != null);
    }

}
