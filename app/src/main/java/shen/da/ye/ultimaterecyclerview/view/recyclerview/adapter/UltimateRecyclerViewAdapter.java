package shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.IRefreshHeader;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnItemClickListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnItemLongClickListener;

/**
 * @author ChenYe
 *         created by on 2017/11/17 0017. 10:23
 *         这个adapter是自定义的UltimateReyclerView的adapter，做过recyclerview添加头部和尾部的同学就知道
 *         recyclerview的adapter是有两个的，一个是数据的adapter，也就是数据列表（不包含你添加的头部和尾部）
 *         的adapter，一个是协调你的数据adapter和头部、尾部之间关系的adapter，而后者才是直接设置到
 *         recyclerview的adapter，当前类就是后者。
 **/

public class UltimateRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = UltimateRecyclerViewAdapter.class.getSimpleName();
    /**
     * 这个adapter是处理数据的adapter
     */
    private RecyclerView.Adapter mInnerAdapter = null;

    /**
     * 可以理解这个就是刷新头部，通过这个接口可以拿到刷新的头部并且可以对头部进行任何操作
     */
    private IRefreshHeader mIRefreshHeader = null;

    /**
     * 这个集合专门用来装头部类型的，当然不包括刷新的头部
     */
    private final List<Integer> HEADER_TYPES = new ArrayList<>();

    /**
     * 专门来装头部view的集合,当然不包括刷新的头部
     */
    private List<View> HEADER_VIEWS = new ArrayList<>();

    /**
     * 专门装尾部view的集合，不包括加载更多的尾部
     */
    private List<View> FOOTER_VIEWS = new ArrayList<>();

    /**
     * 这个是头部的默认type索引，有头部就对其设置itemType 设置 ++;
     * 然后方便在根据类型来判断item的类型判断的时候做处理的。
     */
    private static final int HEADER_DEFAULT_INDEX = 10002;

    private OnItemClickListener mItemClickListener = null;

    private OnItemLongClickListener mItemLongClickListener = null;

    /**
     * 刷新头部的item的itemType
     */
    private static final int TYPE_REFRESH_HEADER = 10000;

    /**
     * 数据item的itemType
     */
    private static final int TYPE_NORMAL = 0;

    /**
     * 尾部item的itemType
     */
    private static final int TYPE_FOOTER_VIEW = 10001;

    private SpanSizeLookup mSpanSizeLookup = null;

    public UltimateRecyclerViewAdapter(RecyclerView.Adapter innerAdapter) {
        this.mInnerAdapter = innerAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_REFRESH_HEADER) {
            return new ViewHolder(mIRefreshHeader.getHeaderView());
        } else if (isHeaderType(viewType)) {
            return new ViewHolder(getHeaderViewByItemType(viewType));
        } else if (viewType == TYPE_FOOTER_VIEW) {
            return new ViewHolder(getFirstFooterView());
        } else {
            return mInnerAdapter.createViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position == 11) {
            Log.e(TAG, "11");
        }
        if (isHeader(position) || isRefreshHeader(position)) {
            //这是在bindHeader和刷新头部条目
            return;
        }
        final int adjPosition = position - (getHeaderViewCount() + 1);
        if (mInnerAdapter != null) {
            //这是在bind数据条目
            if (adjPosition < mInnerAdapter.getItemCount()) {
                mInnerAdapter.onBindViewHolder(holder, adjPosition);
                //下面的点击事件目前在这里写，但是也可以在mInnerAdapter里面的onBindItemHolder里面写，
                //但是那里面的position是没有处理的，需要自己注意;
                //并且这下面的点击事件用rxBind来防止重复点击
                if (mItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemClickListener.onItemClick(holder.itemView, adjPosition);
                        }
                    });
                }

                if (mItemLongClickListener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            mItemLongClickListener.onItemLongClick(holder.itemView, adjPosition);
                            return true;
                        }
                    });
                }
            } else {
                //这是在bindFooter
                Log.e(TAG, "这是在bindFooter__position:" + position);
            }
        } else {
            //mInnerAdapter是null
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }
            final int adjPosition = position - (getHeaderViewCount() + 1);
            int adapterCount;
            if (mInnerAdapter != null) {
                adapterCount = mInnerAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    mInnerAdapter.onBindViewHolder(holder, adjPosition, payloads);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mInnerAdapter == null) {
            return getHeaderViewCount() + getFooterViewCount() + 1;
        } else {
            return getHeaderViewCount() + mInnerAdapter.getItemCount() + getFooterViewCount() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isRefreshHeader(position)) {
            return TYPE_REFRESH_HEADER;
        } else if (isHeader(position)) {
            return HEADER_TYPES.get(position - 1);
        } else if (isFooter(position)) {
            return TYPE_FOOTER_VIEW;
        }

        if (mInnerAdapter != null) {
            int adjPosition = position - (getHeaderViewCount() + 1);
            if (adjPosition < mInnerAdapter.getItemCount()) {
                return mInnerAdapter.getItemViewType(adjPosition);
            }
        }
        return TYPE_NORMAL;
    }

    @Override
    public long getItemId(int position) {
        if (mInnerAdapter != null && position >= getHeaderViewCount()) {
            int adjPosition = position - getHeaderViewCount();
            if (hasStableIds()) {
                adjPosition--;
            }

            int adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mInnerAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mSpanSizeLookup == null) {
                        return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                                ? gridLayoutManager.getSpanCount() : 1;
                    } else {
                        return (isHeader(position) || isFooter(position) ||
                                isRefreshHeader(position)) ? gridLayoutManager.getSpanCount() :
                                mSpanSizeLookup.getSpanSize(gridLayoutManager,
                                        (position - (getHeaderViewCount() + 1)));
                    }
                }
            });
        }
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            if (isHeader(holder.getLayoutPosition()) || isRefreshHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition())) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        mInnerAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewRecycled(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    public RecyclerView.Adapter getInnerAdapter() {
        return mInnerAdapter;
    }

    public void setIRfreshHeader(IRefreshHeader refreshHeader) {
        mIRefreshHeader = refreshHeader;
    }

    /**
     * 添加头部，这个头部是在刷新头部下面的头部
     */
    public void addHeaderView(View view) {
        if (view == null) {
            throw new RuntimeException("亲，你不要设置一个空对象作为头部啊");
        }
        HEADER_TYPES.add(HEADER_DEFAULT_INDEX + HEADER_VIEWS.size());
        HEADER_VIEWS.add(view);
    }

    /**
     * 添加尾部，这个尾部是在刷新尾部上面的尾部,下面这个目前暂定的需求是，每次添加新的尾部的时候，就把之前的尾部
     * 移除掉，保证只有一个尾部，当然如果你的需求不一样的话，可以自己修改下面的方法
     *
     * @param view
     */
    public void addFooterView(View view) {
        if (view == null) {
            throw new RuntimeException("亲，你不要设置一个空对象作为尾部啊");
        }
        removeFirstFooterView();
        FOOTER_VIEWS.add(view);
    }

    /**
     * 移除第一个footerView
     */
    public void removeFirstFooterView() {
        if (getFooterViewCount() > 0) {
            View footerView = getFirstFooterView();
            FOOTER_VIEWS.remove(footerView);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 移除第一个headerView
     */
    public void removeFirstHeaderView() {
        if (getHeaderViewCount() > 0) {
            View headerView = getFirstHeaderView();
            HEADER_VIEWS.remove(headerView);
        }
    }

    public int getFooterViewCount() {
        return FOOTER_VIEWS.size();
    }

    public int getHeaderViewCount() {
        return HEADER_VIEWS.size();
    }

    /**
     * 返回第一个尾部
     *
     * @return
     */
    public View getFirstFooterView() {
        return getFooterViewCount() > 0 ? FOOTER_VIEWS.get(0) : null;
    }

    /**
     * 返回第一个头部
     *
     * @return
     */
    public View getFirstHeaderView() {
        return getHeaderViewCount() > 0 ? HEADER_VIEWS.get(0) : null;
    }

    /**
     * 根据传进来的条目类型来判断是不是头部条目，如果是就返回头部view，不是就返回null
     *
     * @param itemType
     * @return
     */
    private View getHeaderViewByItemType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return HEADER_VIEWS.get(itemType - HEADER_DEFAULT_INDEX);
    }

    private boolean isHeaderType(int itemType) {
        return HEADER_VIEWS.size() > 0 && HEADER_TYPES.contains(itemType);
    }

    /**
     * 根据条目的position来判断是不是头部，不包括刷新的头部,刷新的头部position =0；
     *
     * @param position
     * @return
     */
    public boolean isHeader(int position) {
        return position > 0 && position < HEADER_VIEWS.size() + 1;
    }

    /**
     * 根据条目的position来判断是不是刷新的头部
     *
     * @param position
     * @return
     */
    public boolean isRefreshHeader(int position) {
        return position == 0;
    }

    /**
     * 根据条目的position来判断是不是尾部，我觉得这个判断包含了加载更多的尾部，如果position是最后一个，那么加载
     * 更多的那个也算footer，
     *
     * @param position
     * @return
     */
    public boolean isFooter(int position) {
        int i = getItemCount() - getFooterViewCount();
        return getItemCount() > 0 && position >= i;
    }

    /**
     * @param isCallback position是否来自回调界面
     * @param position
     * @return
     */
    public int getAdapterPosition(boolean isCallback, int position) {
        if (isCallback) {
            int adjPosition = position - (getHeaderViewCount() + 1);
            int adapterPosition = mInnerAdapter.getItemCount();
            if (adjPosition < adapterPosition) {
                return adjPosition;
            }
        } else {
            return position + getHeaderViewCount() + 1;
        }
        return -1;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener;
    }

    public interface SpanSizeLookup {
        /**
         * 返回列数
         *
         * @param gridLayoutManager
         * @param position
         * @return
         */
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }
}
