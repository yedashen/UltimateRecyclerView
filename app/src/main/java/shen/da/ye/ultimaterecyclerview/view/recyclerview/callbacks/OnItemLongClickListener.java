package shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks;

import android.view.View;

/**
 * @author ChenYe
 *         created by on 2017/11/20 0020. 15:56
 **/

public interface OnItemLongClickListener {

    /**
     * @param view     就是条目
     * @param position 是数据的position，这个position是减去头部、刷新头部、尾部、和加载更多尾部之后的
     */
    void onItemLongClick(View view, int position);
}
