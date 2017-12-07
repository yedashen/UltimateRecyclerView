package shen.da.ye.ultimaterecyclerview.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author ChenYe
 *         created by on 2017/12/7 0007. 10:41
 **/

public class SlideInLeftAnimation implements BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationX",
                        -view.getRootView().getWidth(), 0)
        };
    }
}
