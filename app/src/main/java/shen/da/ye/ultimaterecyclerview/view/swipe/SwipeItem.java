package shen.da.ye.ultimaterecyclerview.view.swipe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import shen.da.ye.ultimaterecyclerview.MyApplication;
import shen.da.ye.ultimaterecyclerview.R;

/**
 * @author ChenYe
 *         created by on 2017/12/6 0006. 09:07
 **/

public class SwipeItem extends ViewGroup {

    private int mTouchSlop;
    private int mMaxVelocity;
    /**
     * 是否支持右滑，默认是true支持的
     */
    private boolean mSwipeDeleteEnable = true;

    /**
     * 设置左右滑动，默认是true，代表手指从右往左拖出“侧滑部分”
     * false代表手指从左往右拖出“侧滑部分”
     */
    private boolean mIsLeftSwipe = true;

    /**
     * IOS、QQ式交互，默认是打开的
     */
    private boolean mIsIos = true;

    /**
     * IOS类型下，是否拦截事件的flag,默认不拦截
     */
    private boolean mIosInterceptFlag = false;

    /**
     * 存储当前正在打开的SwipeItem
     */
    private static SwipeItem mCacheView;

    /**
     * 右侧菜单宽度总和(最大滑动距离)
     */
    private int mRightMenuWidths = 0;

    /**
     * 自己的高度
     */
    private int mHeight = 0;

    /**
     * 存储SwipeItem里面的第一个子View(也就是认为是左边的那个View)
     */
    private View mContentView;

    /**
     * 滑动判定临界值（右侧菜单宽度的40%） 手指抬起时，超过了展开，没超过收起menu
     */
    private int mLimit;

    /**
     * 是否是用户滑动。
     */
    private boolean mIsUserSwiped;

    /**
     * 滑动速度变量
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 在Intercept函数的up时，判断这个变量，如果仍为true 说明是点击事件，则关闭菜单。
     */
    private boolean mIsUnMoved = true;

    /**
     * 防止多只手指一起滑我的flag 在每次down里判断， touch事件结束清空
     */
    private static boolean mIsTouching;

    /**
     * 上一次的xy
     */
    private PointF mLastP = new PointF();

    /**
     * up-down的坐标，判断是否是滑动，如果是，则屏蔽一切点击事件
     */
    private PointF mFirstP = new PointF();

    /**
     * 平滑展开、平滑关闭动画
     */
    private ValueAnimator mExpandAnim, mCloseAnim;

    /**
     * 是否是展开的
     */
    private boolean mIsExpand = false;
    /**
     * 多点触摸只算第一根手指的速度
     */
    private int mPointerId;

    public SwipeItem(Context context) {
        this(context, null);
    }

    public SwipeItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //在我们认为用户正在滚动之前，触摸可以移动的像素距离
        mTouchSlop = ViewConfiguration.get(MyApplication.mShareInstance).getScaledTouchSlop();
        //以像素/秒为单位测量的最大速度来启动一个投掷。
        mMaxVelocity = ViewConfiguration.get(MyApplication.mShareInstance).getScaledMaximumFlingVelocity();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeItemView);
        mIsLeftSwipe = typedArray.getBoolean(R.styleable.SwipeItemView_leftSwipeEnable, true);
        mSwipeDeleteEnable = typedArray.getBoolean(R.styleable.SwipeItemView_rightSwipeEnable, true);
        mIsIos = typedArray.getBoolean(R.styleable.SwipeItemView_ios, false);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //令自己可点击，从而获取触摸事件
        setClickable(true);
        //由于ViewHolder的复用机制，每次这里要手动恢复初始值
        mRightMenuWidths = 0;
        mHeight = 0;
        //适配GridLayoutManager，将以第一个子Item(即ContentItem)的宽度为控件宽度
        int contentWidth = 0;
        int childCount = getChildCount();
        //为了子View的高，可以matchParent(参考的FrameLayout 和LinearLayout的Horizontal)
        final boolean measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        boolean isNeedMeasureChildHeight = false;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //令每一个子View可点击，从而获取触摸事件
            childView.setClickable(true);
            if (childView.getVisibility() != GONE) {
                //后续计划加入上滑、下滑，则将不再支持Item的margin
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                //measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                mHeight = Math.max(mHeight, childView.getMeasuredHeight());
                if (measureMatchParentChildren && lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildHeight = true;
                }
                if (i > 0) {
                    //第一个布局是Left item，从第二个开始才是RightMenu
                    mRightMenuWidths += childView.getMeasuredWidth();
                } else {
                    mContentView = childView;
                    contentWidth = childView.getMeasuredWidth();
                }
            }
        }

        //宽度取第一个Item(Content)的宽度
        setMeasuredDimension(getPaddingLeft() + getPaddingRight() + contentWidth,
                mHeight + getPaddingTop() + getPaddingBottom());
        //滑动判断的临界值
        mLimit = mRightMenuWidths * 4 / 10;
        //如果子View的height有MatchParent属性的，设置子View高度
        if (isNeedMeasureChildHeight) {
            forceUniformHeight(childCount, widthMeasureSpec);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = 0 + getPaddingLeft();
        int right = 0 + getPaddingLeft();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                if (i == 0) {
                    //第一个子View是内容 宽度设置为全屏
                    childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                    left = left + childView.getMeasuredWidth();
                } else {
                    if (mIsLeftSwipe) {
                        childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                        left = left + childView.getMeasuredWidth();
                    } else {
                        childView.layout(right - childView.getMeasuredWidth(), getPaddingTop(), right, getPaddingTop() + childView.getMeasuredHeight());
                        right = right - childView.getMeasuredWidth();
                    }
                }
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mSwipeDeleteEnable) {
            acquireVelocityTracker(ev);
            final VelocityTracker verTracker = mVelocityTracker;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    mIsUserSwiped = false;
                    //仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                    mIsUnMoved = true;
                    //每次DOWN时，默认是不拦截的
                    mIosInterceptFlag = false;
                    if (mIsTouching) {
                        //如果有别的指头摸过了，那么就return false。这样后续的move..等事件也不会再来找这个View了。
                        return false;
                    } else {
                        //第一个摸的指头，赶紧改变标志，宣誓主权。
                        mIsTouching = true;
                    }
                    mLastP.set(ev.getRawX(), ev.getRawY());
                    //判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    mFirstP.set(ev.getRawX(), ev.getRawY());
                    //如果down，view和cacheView不一样，则立马让它还原。且把它置为null
                    if (mCacheView != null) {
                        if (mCacheView != this) {
                            mCacheView.smoothClose();
                            //IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。
                            mIosInterceptFlag = mIsIos;
                        }
                        //只要有一个侧滑菜单处于打开状态， 就不给外层布局上下滑动了
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    //求第一个触点的id， 此时可能有多个触点，但至少一个，计算滑动速率用
                    mPointerId = ev.getPointerId(0);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (mIosInterceptFlag) {
                        break;
                    }
                    float gap = mLastP.x - ev.getRawX();
                    //为了在水平滑动中禁止父类ListView等再竖直滑动
                    if (Math.abs(gap) > 10 || Math.abs(getScrollX()) > 10) {
                        //修改此处，使屏蔽父布局滑动更加灵敏，
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    //仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。begin
                    if (Math.abs(gap) > mTouchSlop) {
                        mIsUnMoved = false;
                    }
                    //滑动使用scrollBy
                    scrollBy((int) (gap), 0);
                    //越界修正
                    if (mIsLeftSwipe) {
                        //左滑
                        if (getScrollX() < 0) {
                            scrollTo(0, 0);
                        }
                        if (getScrollX() > mRightMenuWidths) {
                            scrollTo(mRightMenuWidths, 0);
                        }
                    } else {//右滑
                        if (getScrollX() < -mRightMenuWidths) {
                            scrollTo(-mRightMenuWidths, 0);
                        }
                        if (getScrollX() > 0) {
                            scrollTo(0, 0);
                        }
                    }

                    mLastP.set(ev.getRawX(), ev.getRawY());
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    //判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    if (Math.abs(ev.getRawX() - mFirstP.x) > mTouchSlop) {
                        mIsUserSwiped = true;
                    }

                    //IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (!mIosInterceptFlag) {
                        //且滑动了 才判断是否要收起、展开menu
                        //求伪瞬时速度
                        verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                        final float velocityX = verTracker.getXVelocity(mPointerId);
                        if (Math.abs(velocityX) > 1000) {
                            //滑动速度超过阈值
                            if (velocityX < -1000) {
                                if (mIsLeftSwipe) {
                                    //左滑
                                    //平滑展开Menu
                                    smoothExpand();

                                } else {
                                    //平滑关闭Menu
                                    smoothClose();
                                }
                            } else {
                                if (mIsLeftSwipe) {
                                    //左滑
                                    // 平滑关闭Menu
                                    smoothClose();
                                } else {
                                    //平滑展开Menu
                                    smoothExpand();

                                }
                            }
                        } else {
                            if (Math.abs(getScrollX()) > mLimit) {
                                //否则就判断滑动距离
                                //平滑展开Menu
                                smoothExpand();
                            } else {
                                // 平滑关闭Menu
                                smoothClose();
                            }
                        }
                    }
                    //释放
                    releaseVelocityTracker();
                    //没有手指在摸我了
                    mIsTouching = false;
                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //禁止侧滑时，点击事件不受干扰。
        if (mSwipeDeleteEnable) {
            switch (ev.getAction()) {
                // fix 长按事件和侧滑的冲突。
                case MotionEvent.ACTION_MOVE:
                    //屏蔽滑动时的事件
                    if (Math.abs(ev.getRawX() - mFirstP.x) > mTouchSlop) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //为了在侧滑时，屏蔽子View的点击事件
                    if (mIsLeftSwipe) {
                        if (getScrollX() > mTouchSlop) {
                            //解决一个智障问题~ 居然不给点击侧滑菜单 我跪着谢罪
                            //这里判断落点在内容区域屏蔽点击，内容区域外，允许传递事件继续向下的的。。。
                            if (ev.getX() < getWidth() - getScrollX()) {
                                //仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                                if (mIsUnMoved) {
                                    smoothClose();
                                }
                                //true表示拦截
                                return true;
                            }
                        }
                    } else {
                        if (-getScrollX() > mTouchSlop) {
                            if (ev.getX() > -getScrollX()) {
                                //点击范围在菜单外 屏蔽
                                //仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                                if (mIsUnMoved) {
                                    smoothClose();
                                }
                                return true;
                            }
                        }
                    }
                    // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    if (mIsUserSwiped) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
            //模仿IOS 点击其他区域关闭：
            if (mIosInterceptFlag) {
                //IOS模式开启，且当前有菜单的View，且不是自己的 拦截点击事件给子View
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 给MatchParent的子View设置高度
     *
     * @param count
     * @param widthMeasureSpec
     * @see android.widget.LinearLayout# 同名方法
     */
    private void forceUniformHeight(int count, int widthMeasureSpec) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent
        //以父布局高度构建一个Exactly的测量参数
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
                MeasureSpec.EXACTLY);
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured width
                    // FIXME: this may not be right for something like wrapping text?
                    //measureChildWithMargins 这个函数会用到宽，所以要保存一下
                    int oldWidth = lp.width;
                    lp.width = child.getMeasuredWidth();
                    // Remeasure with new dimensions
                    measureChildWithMargins(child, widthMeasureSpec, 0,
                            uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see VelocityTracker#obtain()
     * @see VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }


    /**
     * * 释放VelocityTracker
     *
     * @see VelocityTracker#clear()
     * @see VelocityTracker#recycle()
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    /**
     * 平滑关闭
     */
    public void smoothClose() {
        mCacheView = null;

        //侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView.setLongClickable(true);
        }

        cancelAnim();
        mCloseAnim = ValueAnimator.ofInt(getScrollX(), 0);
        mCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mCloseAnim.setInterpolator(new AccelerateInterpolator());
        mCloseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIsExpand = false;

            }
        });
        mCloseAnim.setDuration(300).start();
    }

    /**
     * 平滑关\展开
     */
    public void smoothExpand() {
        //展开就加入ViewCache：
        mCacheView = SwipeItem.this;

        //侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView.setLongClickable(false);
        }

        cancelAnim();
        mExpandAnim = ValueAnimator.ofInt(getScrollX(), mIsLeftSwipe ?
                mRightMenuWidths : -mRightMenuWidths);
        mExpandAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mExpandAnim.setInterpolator(new OvershootInterpolator());
        mExpandAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIsExpand = true;
            }
        });
        mExpandAnim.setDuration(300).start();
    }

    /**
     * 每次执行动画之前都应该先取消之前的动画
     */
    private void cancelAnim() {
        if (mCloseAnim != null && mCloseAnim.isRunning()) {
            mCloseAnim.cancel();
        }
        if (mExpandAnim != null && mExpandAnim.isRunning()) {
            mExpandAnim.cancel();
        }
    }


    public boolean getReightSwipeEnable() {
        return mSwipeDeleteEnable;
    }

    public boolean getLeftSwipeEnable() {
        return mIsLeftSwipe;
    }

    public boolean getIsIos() {
        return mIsIos;
    }

    public SwipeItem setSwipeDeleteEnable(boolean flag) {
        mSwipeDeleteEnable = flag;
        return this;
    }

    /**
     * 设置滑动的方向
     *
     * @param flag
     * @return
     */
    public SwipeItem setSwipeLeftOrRight(boolean flag) {
        mIsLeftSwipe = flag;
        return this;
    }

    public SwipeItem setIos(boolean flag) {
        mIsIos = false;
        return this;
    }

    public static SwipeItem getCacheView() {
        return mCacheView;
    }

    //每次ViewDetach的时候，判断一下 ViewCache是不是自己，如果是自己，关闭侧滑菜单，且ViewCache设置为null，
    // 理由：1 防止内存泄漏(ViewCache是一个静态变量)
    // 2 侧滑删除后自己后，这个View被Recycler回收，复用，下一个进入屏幕的View的状态应该是普通状态，而不是展开状态。
    @Override
    protected void onDetachedFromWindow() {
        if (this == mCacheView) {
            mCacheView.smoothClose();
            mCacheView = null;
        }
        super.onDetachedFromWindow();
    }

    //展开时，禁止长按
    @Override
    public boolean performLongClick() {
        if (Math.abs(getScrollX()) > mTouchSlop) {
            return false;
        }
        return super.performLongClick();
    }

    /**
     * 快速关闭。
     * 用于 点击侧滑菜单上的选项,同时想让它快速关闭(删除 置顶)。
     * 这个方法在ListView里是必须调用的，
     * 在RecyclerView里，视情况而定，如果是mAdapter.notifyItemRemoved(pos)方法不用调用。
     */
    public void quickClose() {
        if (this == mCacheView) {
            //先取消展开动画
            cancelAnim();
            //关闭
            mCacheView.scrollTo(0, 0);
            mCacheView = null;
        }
    }

    /**
     * 这个是点击SwipeItem以外的地方就关闭掉当前正在开着的那个swipeItem，可以根据实际情况来选择是否调用
     * 下面这个方法
     */
    public void closeItem() {
        if (mCacheView != null) {
            mCacheView.smoothClose();
            mCacheView = null;
        }
    }

}
