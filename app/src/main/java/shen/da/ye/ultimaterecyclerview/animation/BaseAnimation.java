package shen.da.ye.ultimaterecyclerview.animation;

import android.animation.Animator;
import android.view.View;

/**
 * @author ChenYe
 *         created by on 2017/12/7 0007. 10:35
 **/

public interface BaseAnimation {

    /**
     * @param view 控件
     * @return 返回要对这个控件设置的动画集合
     */
    Animator[] getAnimators(View view);
}
