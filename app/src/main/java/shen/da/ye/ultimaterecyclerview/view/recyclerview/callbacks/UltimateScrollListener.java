package shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks;

/**
 * @author ChenYe
 *         created by on 2017/11/23 0023. 14:29
 **/

public interface UltimateScrollListener {

    /**
     * 从下往上滑
     */
    void onScrollDownToUp();

    /**
     * 从上往下滑
     */
    void onScrollUpToDown();

    /**
     * 正在滑动中，可以获取到总共滑动了多少的x和y的距离
     *
     * @param distanceX
     * @param distanceY
     */
    void onScrolled(int distanceX, int distanceY);

    /**
     * 滑动状态改变了
     *
     * @param state
     */
    void onScrollStateChanged(int state);
}
