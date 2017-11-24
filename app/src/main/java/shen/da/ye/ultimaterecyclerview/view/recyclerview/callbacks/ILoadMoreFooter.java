package shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks;

import android.view.View;

/**
 * @author ChenYe
 *         created by on 2017/11/16 0016. 11:09
 **/

public interface ILoadMoreFooter {

    /**
     * 正在加载更多种
     */
    void onLoading();

    /**
     * 加载更多完毕
     */
    void onLoadComplete();

    /**
     * 没有更多可以加载了
     */
    void onNoMore();

    /**
     * 重置尾部
     */
    void onReset();

    /**
     * 获取加载更多的view
     *
     * @return
     */
    View getLoadMoreFooter();
}
