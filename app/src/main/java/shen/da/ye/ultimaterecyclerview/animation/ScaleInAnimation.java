package shen.da.ye.ultimaterecyclerview.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author ChenYe
 *         created by on 2017/12/7 0007. 10:39
 **/

public class ScaleInAnimation implements BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", .5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", .5f, 1f);
        return new ObjectAnimator[]{scaleX, scaleY};
    }
}
