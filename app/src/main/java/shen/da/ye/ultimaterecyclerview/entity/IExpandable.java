package shen.da.ye.ultimaterecyclerview.entity;

import java.util.List;

/**
 * @author ChenYe
 *         created by on 2017/12/8 0008. 10:15
 **/

public interface IExpandable<T> {
    /**
     * @return 返回当前item是否是展开的
     */
    boolean isExpanded();

    /**
     * 设置 item 展开/收缩 状态
     *
     * @param expanded
     */
    void setExpanded(boolean expanded);

    /**
     * 返回当前item的下一级所有数据
     *
     * @return
     */
    List<T> getSubItems();

    /**
     * @return 返回当前item的级别
     */
    int getLevel();

}
