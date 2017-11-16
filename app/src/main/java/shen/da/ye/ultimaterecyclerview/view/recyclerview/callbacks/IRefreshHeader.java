package shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks;

import android.view.View;

/**
 * @author ChenYe
 *         created by on 2017/11/15 0015. 16:53
 **/

public interface IRefreshHeader {

    /**
     * 定义头部四种状态的常量
     */
    //正常
    int STATE_NORMAL = 0;

    //松开就会刷新
    int STATE_RELEASE_TO_REFRESH = 1;

    //正在刷新中
    int STATE_REFRESHING = 2;

    //
    int STATE_DONE = 3;

    /**
     * 下拉距离已经超过了指定大小，松手就会刷新
     */
    void onPrepare();

    /**
     * 已经正在刷新中了
     */
    void onRefreshing();

    /**
     * 头部正在移动中
     *
     * @param offset    刚刚移动了多少距离
     * @param sumOffset 总共移动了多少距离
     */
    void onMove(int offset, int sumOffset);

    /**
     * 下拉松开了
     *
     * @return
     */
    boolean release();

    /**
     * 刚刚刷新完毕了
     */
    void refreshComplete();

    /**
     * 返回头部view
     *
     * @return
     */
    View getHeaderView();

    /**
     * 返回当前头部漏出了多少高度
     *
     * @return
     */
    int getVisibleHeight();

    /**
     * 重置头部
     */
    void onReset();
}
