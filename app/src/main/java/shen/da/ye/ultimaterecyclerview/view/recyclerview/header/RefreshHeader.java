package shen.da.ye.ultimaterecyclerview.view.recyclerview.header;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.view.animation.RecyclerViewLoadingView;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.IRefreshHeader;

/**
 * @author ChenYe
 *         created by on 2017/11/15 0015. 17:10
 **/

public class RefreshHeader extends LinearLayout implements IRefreshHeader {

    private static final String TAG = RefreshHeader.class.getSimpleName();
    private View mHeaderLayout;
    private ImageView mArrowIv;
    private TextView mHeaderStateTv;
    private RecyclerViewLoadingView mLoadingView;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private static final int ROTATE_ANIM_DURATION = 180;
    private int mMeasuredHeight;
    private int mState = STATE_NORMAL;

    public RefreshHeader(Context context) {
        this(context, null);
    }

    public RefreshHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultLayoutParams();
        addViewAndFindId();
        setupArrowAnimation();
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }

    /**
     * 设置箭头动画
     */
    private void setupArrowAnimation() {
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    /**
     * 添加布局和findId
     */
    private void addViewAndFindId() {
        mHeaderLayout = LayoutInflater.from(getContext()).inflate(R.layout.header_layout, null);
        mArrowIv = mHeaderLayout.findViewById(R.id.refresh_arrow);
        mHeaderStateTv = mHeaderLayout.findViewById(R.id.header_state_tv);
        mLoadingView = mHeaderLayout.findViewById(R.id.loading_view);
    }

    /**
     * 初始化layoutParams
     */
    private void initDefaultLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        this.setLayoutParams(layoutParams);
        this.setPadding(0, 0, 0, 0);
    }


    public int getHeaderState() {
        return mState;
    }

    public void setHeaderState(int state) {
        if (state == mState) {
            return;
        }

        //下面这个三重判断是为了重置头部
        if (state == STATE_REFRESHING) {
            // 正在刷新
            mArrowIv.clearAnimation();
            mArrowIv.setVisibility(View.INVISIBLE);
            mLoadingView.setVisibility(View.VISIBLE);
            smoothScrollTo(mMeasuredHeight);
        } else if (state == STATE_DONE) {
            //刷新完毕
            mArrowIv.setVisibility(View.INVISIBLE);
            mLoadingView.setVisibility(View.INVISIBLE);
        } else {
            // 显示箭头图片
            mArrowIv.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.INVISIBLE);
        }

        //下面的switch是为了设置头部
        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_RELEASE_TO_REFRESH) {
                    mArrowIv.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    mArrowIv.clearAnimation();
                }
                mHeaderStateTv.setText("下拉刷新");
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    mArrowIv.clearAnimation();
                    mArrowIv.startAnimation(mRotateUpAnim);
                    mHeaderStateTv.setText("释放立即刷新");
                }
                break;
            case STATE_REFRESHING:
                mHeaderStateTv.setText("正在刷新");
                break;
            case STATE_DONE:
                mHeaderStateTv.setText("刷新完成");
                break;
            default:
                break;
        }
        mState = state;
    }

    public void reset() {
        smoothScrollTo(0);
        //TODO 使用的时候改成rxJava写法
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setHeaderState(STATE_NORMAL);
            }
        }, 500);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LayoutParams lp = (LayoutParams) mHeaderLayout.getLayoutParams();
        lp.height = height;
        mHeaderLayout.setLayoutParams(lp);
    }


    @Override
    public void onPrepare() {
        setHeaderState(STATE_RELEASE_TO_REFRESH);
    }

    @Override
    public void onRefreshing() {
        setHeaderState(STATE_REFRESHING);
    }

    @Override
    public void onMove(int offset, int sumOffset) {
        //第二个参数只是记录行为(记录一共滑动了多少距离，其实可以直接获取当前头部露出的距离更有实际用途)，实际没有用到
        if (getVisibleHeight() > 0 || offset > 0) {
            setVisibleHeight(offset + getVisibleHeight());
            // 未处于刷新状态，更新箭头
            if (mState <= STATE_RELEASE_TO_REFRESH) {
                if (getVisibleHeight() > mMeasuredHeight) {
                    onPrepare();
                } else {
                    onReset();
                }
            }
        }
    }

    @Override
    public boolean release() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        //下面这句代码可以屏蔽
        if (height == 0) {
            isOnRefresh = false;
        }

        //头部可见高度大于正常高度并且不是处于刷新以及刷新完毕状态
        if (height > mMeasuredHeight && mState < STATE_REFRESHING) {
            setHeaderState(STATE_REFRESHING);
            isOnRefresh = true;
        }

        // 在手指松开的时候，其实已经是正在刷新了（也就是在按下之前，是正在刷新的），所以松开时候回到正在刷新的位置，然后返回false
        if (mState == STATE_REFRESHING && height > mMeasuredHeight) {
            smoothScrollTo(mMeasuredHeight);
            //这里不能自己加那句话isOnRefresh = true;因为如果加了，那么松手的时候，就返回true了。
            // 其实在按下手指之前就onRelease()返回了true，然后执行正在刷新的回调了，如果这里在加上isRefresh = true,
            // 就会再次返回true,那么就松手的手又会再次刷新了，要是不懂就点击onRelease()看回调就懂了
        }

        //如果松手的时候不是正在刷新，那么就滑动到0的位置
        if (mState != STATE_REFRESHING) {
            smoothScrollTo(0);
        }

        //这句代码很重要，当你第二个手指触摸到屏幕的时候会复原到正在刷新的位置
        if (mState == STATE_REFRESHING) {
            int destHeight = mMeasuredHeight;
            smoothScrollTo(destHeight);
            //这里不能自己加那句话isOnRefresh = true;理由同上
        }

        return isOnRefresh;
    }

    @Override
    public void refreshComplete() {
        setHeaderState(STATE_DONE);
        //TODO 自己用的时候改成rxJava写法
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
            }
        }, 200);
    }

    @Override
    public View getHeaderView() {
        return this;
    }

    @Override
    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mHeaderLayout.getLayoutParams();
        return lp.height;
    }

    @Override
    public void onReset() {
        setHeaderState(STATE_NORMAL);
    }
}
