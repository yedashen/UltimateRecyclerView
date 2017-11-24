package shen.da.ye.ultimaterecyclerview.view.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import shen.da.ye.ultimaterecyclerview.MyApplication;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.UltimateRecyclerViewAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.appbar.AppBarStateChangeListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.ILoadMoreFooter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.IRefreshHeader;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnLoadMoreListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnNetErrorListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnRefreshListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.UltimateScrollListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.footer.FooterState;
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
    private boolean mIsVpDrag = false;
    private int mTouchSlop;
    private float mLastY = -1;
    private float mSumOffset;
    private static final float DRAG_RATE = 2.0f;
    private AppBarStateChangeListener.State mAppbarState = AppBarStateChangeListener.State.EXPANDED;
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

    /**
     * 当recyclerView没有数据的时候展示的
     */
    private View mEmptyView = null;

    /**
     * 协调头部、尾部、中间数据部分的adapter
     */
    private UltimateRecyclerViewAdapter mUltimateAdapter = null;

    /**
     * 数据分页、默认是10
     */
    private int mPageSize = 10;

    /**
     * 是否数据记载完毕了，还需不需要加载更多的尾部（功能）出现，默认是不需要(false)
     */
    private boolean mIsNoMore = false;

    /**
     * 是否启用了数据观察者,默认是没有
     */
    private boolean mIsRegisterDataObserver;

    /**
     * 数据观察者，这里是实现局部刷新的第一步。
     */
    private final DataObserver DATA_OBSERVER = new DataObserver();

    /**
     * 是否启用加载更多功能,默认是启用的
     */
    private boolean mLoadMoreEnable = true;

    /**
     * 是否启用下拉刷新功能，默认是启用的
     */
    private boolean mRefreshEnable = true;

    /**
     * 刷新接口
     */
    private OnRefreshListener mOnRefreshListener = null;

    /**
     * 加载更多接口
     */
    private OnLoadMoreListener mOnLoadMoreListener = null;

    /**
     * 滑动接口回调
     */
    private UltimateScrollListener mUltimateScrollLister = null;

    /**
     * 布局管理器的类型，抽取出来进行赋值是为了复用
     */
    private LayoutManagerType mLayoutManagerType = null;

    /**
     * 最后一个可见的item的position
     */
    private int mLastVisibleItemPosition = 0;

    /**
     * 当时瀑布流布局的时候，不能直接获取最后一个可见item的position，所以先用一个数组
     * 来装一些潜在数据，然后排除这些数据最后找到对得“最后一个可见的item的position”
     */
    private int[] mLastVisibleItemPositions = null;

    /**
     * 当前滑动了多少距离
     */
    private int mDistance = 0;

    /**
     * 是否是手指从上往下滑动，默认是
     */
    private boolean mIsUpToDown = true;

    /**
     * 触发在上下滑动监听器的容差距离,意思就是超过这个距离计算滑动了
     */
    private static final int HIDE_THRESHOLD = 20;

    /**
     * Y轴移动的实际距离（最顶部为0）
     */
    private int mScrolledYDistance = 0;

    /**
     * X轴移动的实际距离（最左侧为0）
     */
    private int mScrolledXDistance = 0;

    /**
     * 当前的滑动状态
     */
    private int mCurrentScrollState = 0;


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

    /**
     * 初始化尾部,这里可以设置默认尾部是否可见，目前我的尾部是默认可见的，
     * 设置 mLoadMoreFooterView.setVisibility(GONE);之后就默认不可见了
     */
    private void initLoadMoreFooter() {
        mILoadMoreFooter = new LoadMoreFooter(MyApplication.mShareInstance);
        mLoadMoreFooterView = mILoadMoreFooter.getLoadMoreFooter();
        ViewGroup.LayoutParams layoutParams = mLoadMoreFooterView.getLayoutParams();
        if (layoutParams == null) {
            mLoadMoreFooterView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }

    }

    private void initRefreshHeader() {
        if (mIsRegisterDataObserver) {
            throw new RuntimeException("必须在设置adapter之前设置刷新头部");
        }
        mIRefreshHeader = new RefreshHeader(MyApplication.mShareInstance);
    }


    @Override
    public void setAdapter(Adapter adapter) {
        //如果之前设置过了数据观察者，那么取消掉
        if (mUltimateAdapter != null && mIsRegisterDataObserver) {
            mUltimateAdapter.getInnerAdapter().unregisterAdapterDataObserver(DATA_OBSERVER);
        }
        mUltimateAdapter = ((UltimateRecyclerViewAdapter) adapter);
        super.setAdapter(mUltimateAdapter);
        mUltimateAdapter.getInnerAdapter().registerAdapterDataObserver(DATA_OBSERVER);
        DATA_OBSERVER.onChanged();
        mIsRegisterDataObserver = true;
        mUltimateAdapter.setIRfreshHeader(mIRefreshHeader);
        if (mLoadMoreEnable && mUltimateAdapter.getFooterViewCount() == 0) {
            mUltimateAdapter.addFooterView(mLoadMoreFooterView);
        }

    }

    /**
     * 解决嵌套RecyclerView与viewPager滑动冲突问题,返回false代表不进行拦截,
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
                mIsVpDrag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if (mIsVpDrag) {
                    return false;
                }

                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - mStartX);
                float distanceY = Math.abs(endY - mStartY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsVpDrag = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsVpDrag = false;
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
                if (isOnTop() && !mRefreshing && mRefreshEnable
                        && (mAppbarState == AppBarStateChangeListener.State.EXPANDED)) {
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


    public class DataObserver extends AdapterDataObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            Adapter<?> adapter = getAdapter();
            if (adapter != null && adapter instanceof UltimateRecyclerViewAdapter) {
                UltimateRecyclerViewAdapter ultimateAdapter = (UltimateRecyclerViewAdapter) adapter;
                Adapter innerAdapter = ultimateAdapter.getInnerAdapter();
                if (innerAdapter != null && mEmptyView != null) {
                    if (innerAdapter.getItemCount() == 0) {
                        mEmptyView.setVisibility(VISIBLE);
                        // TODO: 2017/11/23 0023  整个recyclerView隐藏起来就会导致刷新也不可以用了，
                        // TODO 还是说这个隐藏不会影响到刷新头部和加载更多的尾部
                        UltimateRecyclerView.this.setVisibility(GONE);
                    } else {
                        mEmptyView.setVisibility(GONE);
                        UltimateRecyclerView.this.setVisibility(VISIBLE);
                    }
                }
            } else if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(VISIBLE);
                    UltimateRecyclerView.this.setVisibility(GONE);
                } else {
                    mEmptyView.setVisibility(GONE);
                    UltimateRecyclerView.this.setVisibility(VISIBLE);
                }
            }

            if (mUltimateAdapter != null) {
                mUltimateAdapter.notifyDataSetChanged();
                //这里就已经可以把尾部进行隐藏了
                if (mUltimateAdapter.getInnerAdapter().getItemCount() < mPageSize) {
                    mLoadMoreFooterView.setVisibility(GONE);
                }
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            mUltimateAdapter.notifyItemRangeChanged(positionStart +
                    mUltimateAdapter.getHeaderViewCount() + 1, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            mUltimateAdapter.notifyItemRangeInserted(positionStart +
                    mUltimateAdapter.getHeaderViewCount() + 1, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            mUltimateAdapter.notifyItemMoved(positionStart + mUltimateAdapter.getHeaderViewCount()
                    + 1, itemCount);

            //TODO 我觉得下面这句话可以不要，因为在onChanged里面已经做了下面这个事了
            if (mUltimateAdapter.getInnerAdapter().getItemCount() < mPageSize) {
                mLoadMoreFooterView.setVisibility(GONE);
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            int headerViewCount = mUltimateAdapter.getHeaderViewCount();
            mUltimateAdapter.notifyItemRangeChanged(fromPosition + headerViewCount + 1,
                    toPosition + headerViewCount + 1 + itemCount);
        }
    }

    public void setEmptyView(View view) {
        this.mEmptyView = view;
    }

    /**
     * 调用刷新接口成功并且快完毕的时候调用下面这个方法
     * 注意：下面有一个mIsNoMore赋值，目前我这里定义的是，当刷新之后就可以继续调用加载更多功能了，
     * 即使我之前已经全部加载完了，然后再次刷新之后就可以继续加载更多了。我是为了写demo才这样的，
     * 如果你的需求跟我的不一样，你可以把下面的那个“ mIsNoMore = false;”注释掉
     */
    public void refreshCompleted(int pageSize) {
        this.mPageSize = pageSize;
        boolean flag = mUltimateAdapter.getInnerAdapter().getItemCount() < pageSize;
        if (mRefreshing) {
            mRefreshing = false;
            //TODO 注意这个赋值，看我上面的解释
            mIsNoMore = false;
            mIRefreshHeader.refreshComplete();

            if (flag) {
                mLoadMoreFooterView.setVisibility(GONE);
            }

        }
    }

    /**
     * 调用记载更多接口完毕之后调用下面这个方法
     *
     * @param flag flag是代表还有没有更多的数据
     */
    public void loadMoreComplete(boolean flag) {
        if (mLoadingData) {
            mLoadingData = false;
            setNoMore(flag);
        }
    }

    /**
     * 刷新判断和刷新操作
     */
    public void refresh() {

        if (mRefreshing) {
            Toast.makeText(MyApplication.mShareInstance, "已经在刷新了，请勿重复刷新...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mIRefreshHeader.getVisibleHeight() > 0) {
            //当正在刷新是不会重复刷新的；当刷新头部被下拉了，说明用户可能正在下拉，这个时候也不刷新
            return;
        }
        if (mRefreshEnable && mOnRefreshListener != null) {
            mRefreshing = true;
            //1、修改头部的状态到正在刷新中，以此来显示刷新动画
            mIRefreshHeader.onRefreshing();
            //2、正在刷新的时候把加载更多的尾部设置消失
            mLoadMoreFooterView.setVisibility(GONE);
            //3、调用刷新接口
            mOnRefreshListener.onRefresh();
        }
    }

    /**
     * 强制刷新,下面这个判断看需求要不要
     */
    public void forceToRefresh() {
        if (mLoadingData) {
            Toast.makeText(MyApplication.mShareInstance, "请等待加载更多完毕再刷新...", Toast.LENGTH_SHORT).show();
            return;
        }
        refresh();
    }

    /**
     * 设置是否已加载了全部，在调用加载更多的接口回调成功并快完毕的的时候调用下面这个方法。
     * 当没有更多的时候传值为true
     *
     * @param flag true代表没有更多了,false代表还有更多的
     */
    private void setNoMore(boolean flag) {
        mIsNoMore = flag;
        if (mIsNoMore) {
            mILoadMoreFooter.onNoMore();
        } else {
            mILoadMoreFooter.onLoadComplete();
        }
    }

    /**
     * 这里是外面控制是否启用下拉刷新的方法，其实在一开始走着UltimateRecyclerView的构造方法的时候是默认启用了
     * 下拉刷新的方法的，但是如果你不需要这个功能，可以在调用这个方法来再禁止启用下拉刷新的方法，因为这个
     * mRefreshEnable可以在onTouchEvent哪里
     *
     * @param flag
     */
    public void setRefreshEnable(boolean flag) {
        mRefreshEnable = flag;
    }

    /**
     * 加载更多是否可用
     *
     * @param flag
     */
    public void setLoadMoreEnable(boolean flag) {
        if (mUltimateAdapter == null) {
            throw new RuntimeException("在设置加载更多是否可用之前，必须先给recyclerViw设置adapter");
        }

        mLoadMoreEnable = flag;
        if (!mLoadMoreEnable) {
            mUltimateAdapter.removeFirstFooterView();
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.mOnLoadMoreListener = loadMoreListener;
    }

    /**
     * 网络出错，点击“重新加载”等提示来重新加载更多的接口
     *
     * @param netErrorListener
     */
    public void setNetErrorListener(final OnNetErrorListener netErrorListener) {
        final LoadMoreFooter loadMoreFooter = (LoadMoreFooter) this.mLoadMoreFooterView;
        loadMoreFooter.setFooterState(FooterState.NET_WORK_ERROR, true);
        loadMoreFooter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                netErrorListener.reload();
            }
        });
    }

    public void setScrollListener(UltimateScrollListener listener) {
        this.mUltimateScrollLister = listener;
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        int firstVisibleItemPosition = 0;
        LayoutManager layoutManager = getLayoutManager();
        if (mLayoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                mLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                mLayoutManagerType = LayoutManagerType.STAGGERED_GRID_LAYOUT;
            } else if (layoutManager instanceof GridLayoutManager) {
                mLayoutManagerType = LayoutManagerType.GRID_LAYOUT;
            } else {
                throw new RuntimeException("老铁，你没设置布局管理器，还是设置了什么奇怪的类型");
            }
        }

        switch (mLayoutManagerType) {
            case LINEAR_LAYOUT:
                firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                break;
            case STAGGERED_GRID_LAYOUT:
                firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                mLastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GRID_LAYOUT:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (mLastVisibleItemPositions == null) {
                    mLastVisibleItemPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(mLastVisibleItemPositions);
                mLastVisibleItemPosition = findMax(mLastVisibleItemPositions);
                staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(mLastVisibleItemPositions);
                firstVisibleItemPosition = findMax(mLastVisibleItemPositions);
                break;
            default:
                break;
        }

        calculateScrollUpOrDown(firstVisibleItemPosition, dy);
        mScrolledXDistance += dx;
        mScrolledYDistance += dy;
        mScrolledXDistance = (mScrolledXDistance < 0) ? 0 : mScrolledXDistance;
        mScrolledYDistance = (mScrolledYDistance < 0) ? 0 : mScrolledYDistance;
        if (mIsUpToDown && (dy == 0)) {
            mScrolledYDistance = 0;
        }

        if (mUltimateScrollLister != null) {
            mUltimateScrollLister.onScrolled(mScrolledXDistance, mScrolledYDistance);
        }
    }


    /**
     * 滑动状态改变，当滑动到底部的时候就自动去加载更多
     * SCROLL_STATE_TOUCH_SCROLL 正在滚动
     * SCROLL_STATE_FLING 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
     * SCROLL_STATE_IDLE 停止滚动
     *
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        mCurrentScrollState = state;

        if (mUltimateScrollLister != null) {
            mUltimateScrollLister.onScrollStateChanged(state);
        }

        if (mOnLoadMoreListener != null && mLoadMoreEnable) {
            if (mCurrentScrollState == RecyclerView.SCROLL_STATE_IDLE) {
                LayoutManager layoutManager = getLayoutManager();
                int currVisibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                boolean canLoadMore = (currVisibleItemCount > 0)
                        && (mLastVisibleItemPosition >= totalItemCount - 1)
                        && (totalItemCount > currVisibleItemCount)
                        && !mIsNoMore
                        && !mRefreshing;
                if (canLoadMore) {
                    mLoadMoreFooterView.setVisibility(VISIBLE);
                    if (mLoadingData) {
                        Log.e("UltimateRecyclerView", "已经在加载更多了");
                        return;
                    } else {
                        mLoadingData = true;
                        mILoadMoreFooter.onLoading();
                        mOnLoadMoreListener.onLoadMore();
                    }
                }
            }
        }
    }

    public enum LayoutManagerType {
        /**
         * LinearLayoutManager
         */
        LINEAR_LAYOUT,
        /**
         * StaggeredGridLayoutManager
         */
        STAGGERED_GRID_LAYOUT,
        /**
         * GridLayoutManager
         */
        GRID_LAYOUT
    }

    /**
     * 找到数组中的最大值并返回
     *
     * @param lastPositions
     * @return
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 通过当前第一个可见的item的position和竖向平移的距离来判断是“从下往上滑”还是“从上往下滑”
     *
     * @param firstVisibleItemPosition
     * @param dy
     */
    private void calculateScrollUpOrDown(int firstVisibleItemPosition, int dy) {
        if (mUltimateScrollLister != null) {
            if (firstVisibleItemPosition == 0) {
                if (!mIsUpToDown) {
                    mIsUpToDown = true;
                    mUltimateScrollLister.onScrollUpToDown();
                    Log.e("UltimateRecyclerView", "firstVisibleItemPosition = 0,手指从上往下滑");
                }
            } else {
                if (mDistance > HIDE_THRESHOLD && mIsUpToDown) {
                    mIsUpToDown = false;
                    mUltimateScrollLister.onScrollDownToUp();
                    mDistance = 0;
                    Log.e("UltimateRecyclerView", "手指从下往上滑");
                } else if (mDistance < -HIDE_THRESHOLD && !mIsUpToDown) {
                    mIsUpToDown = true;
                    mUltimateScrollLister.onScrollUpToDown();
                    mDistance = 0;
                    Log.e("UltimateRecyclerView", "手指从上往下滑");
                }
            }
        } else {
            Log.e("UltimateRecyclerView", "没有设置滚动监听，所以没有捕捉当前滑动的状态");
        }

        boolean b = (mIsUpToDown && dy > 0) || (!mIsUpToDown && dy < 0);
        if (b) {
            mDistance += dy;
        }
    }

    /**
     * 解决LRecyclerView与CollapsingToolbarLayout滑动冲突的问题
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        mAppbarState = state;
                    }
                });
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mUltimateAdapter != null && DATA_OBSERVER != null && mIsRegisterDataObserver) {
            mUltimateAdapter.getInnerAdapter().unregisterAdapterDataObserver(DATA_OBSERVER);
            mIsRegisterDataObserver = false;
        }
    }
}
