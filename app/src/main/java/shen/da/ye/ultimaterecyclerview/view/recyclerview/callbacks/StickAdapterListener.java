package shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @author ChenYe
 *         created by on 2017/11/29 0029. 15:37
 **/

public interface StickAdapterListener<T extends RecyclerView.ViewHolder> {


    /**
     * Returns the header id for the item at the given position.
     *
     * @param position the item position
     * @return the header id
     */
    long getHeaderId(int position);

    /**
     * Creates a new header ViewHolder.
     *
     * @param parent the header's view parent
     * @return a view holder for the created view
     */
    T onCreateHeaderViewHolder(ViewGroup parent);

    /**
     * Updates the header view to reflect the header data for the given position
     *
     * @param viewholder the header view holder
     * @param position   the header's item position
     */
    void onBindHeaderViewHolder(T viewholder, int position);

}
