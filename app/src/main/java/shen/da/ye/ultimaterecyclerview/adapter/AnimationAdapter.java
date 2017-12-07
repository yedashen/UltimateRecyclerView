package shen.da.ye.ultimaterecyclerview.adapter;

import android.animation.Animator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.animation.AlphaInAnimation;
import shen.da.ye.ultimaterecyclerview.animation.BaseAnimation;
import shen.da.ye.ultimaterecyclerview.animation.DefaultAnimation;
import shen.da.ye.ultimaterecyclerview.animation.ScaleInAnimation;
import shen.da.ye.ultimaterecyclerview.animation.SlideInLeftAnimation;
import shen.da.ye.ultimaterecyclerview.animation.SlideInRightAnimation;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.BaseDataAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.SuperViewHolder;

/**
 * @author ChenYe
 *         created by on 2017/12/7 0007. 13:43
 **/

public class AnimationAdapter extends BaseDataAdapter<String> {

    /**
     * 默认动画，包含了缩放和透明动画
     */
    public static final int DEFAULT_ANIMATION = 1;

    /**
     * 透名动画
     */
    public static final int ALPHA_ANIMATION = 2;

    /**
     * 缩放动画
     */
    public static final int SCALE_ANIMATION = 3;

    /**
     * item从左边出来的动画
     */
    public static final int SLIDE_FROM_LEFT_ANIMATION = 4;

    /**
     * item从右边出来的动画
     */
    public static final int SLIDE_FROM_RIGHT_ANIMATION = 5;

    private BaseAnimation mSelectAnimation = new DefaultAnimation();

    private int mDuration = 300;

    private Interpolator mInterpolator = new LinearInterpolator();

    /**
     * 是否重复出现动画，意思就是同一个item如果在滑动的过程中他已经执行过一次动画了，不再执行第二次了。默认是
     * 重复执行
     */
    private boolean mFirstOnly = false;

    /**
     * 目前出现过的最大的itemPosition，会在判断是否重复重复出现动画的时候用到
     */
    private int mLastPosition;

    @Override
    public void onViewAttachedToWindow(SuperViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (!mFirstOnly || holder.getLayoutPosition() > mLastPosition) {
            for (Animator anim : mSelectAnimation.getAnimators(holder.itemView)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(mInterpolator);
            }
            mLastPosition = holder.getLayoutPosition();
        }
    }

    @Override
    protected Object setLayout() {
        return R.layout.item_animation;
    }

    @Override
    protected void onBindItemHolder(SuperViewHolder holder, int position) {
        holder.setText(R.id.item_animation_tv, mDataList.get(position));
    }

    public void setAnimationStyle(int style) {
        switch (style) {
            case ALPHA_ANIMATION:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case SCALE_ANIMATION:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case SLIDE_FROM_LEFT_ANIMATION:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDE_FROM_RIGHT_ANIMATION:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            default:
                mSelectAnimation = new DefaultAnimation();
                break;
        }
    }

    public void setFirstOnly(boolean flag) {
        mFirstOnly = flag;
        mLastPosition = 0;
    }
}
