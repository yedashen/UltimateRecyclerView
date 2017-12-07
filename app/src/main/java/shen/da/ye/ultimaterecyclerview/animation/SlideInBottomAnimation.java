package shen.da.ye.ultimaterecyclerview.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author ChenYe
 *         created by on 2017/12/7 0007. 10:40
 **/

public class SlideInBottomAnimation implements BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationY",
                        view.getMeasuredHeight(), 0)
        };
    }
}
