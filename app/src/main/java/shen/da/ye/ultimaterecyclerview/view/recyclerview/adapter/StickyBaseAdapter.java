package shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import shen.da.ye.ultimaterecyclerview.entity.StickyHeadEntity;
import shen.da.ye.ultimaterecyclerview.util.FullSpanUtil;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnItemClickListener;

/**
 * @author ChenYe
 *         created by on 2017/12/5 0005. 15:40
 *         这个是专门给粘性RecyclerView的BaseAdapter的，跟BaseDataAdapter的却别是重写了
 *         onAttachedToRecyclerView 和 onViewAttachedToWindow,所以就不复用了。
 **/

public abstract class StickyBaseAdapter<T, V extends StickyHeadEntity<T>> extends RecyclerView.Adapter<SuperViewHolder> {

    /**
     * 普通数据类型
     */
    public final static int TYPE_DATA = 1;
    /**
     * header
     */
    public final static int TYPE_STICKY_HEAD = 2;

    /**
     * 普通的头部
     */
    public final static int TYPE_HEAD = 3;

    protected List<V> mDataList = new ArrayList<>();

    protected OnItemClickListener mOnItemClickListener = null;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, TYPE_STICKY_HEAD);
    }

    @Override
    public void onViewAttachedToWindow(SuperViewHolder holder) {
        FullSpanUtil.onViewAttachedToWindow(holder, this, TYPE_STICKY_HEAD);
    }


    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (setLayout(viewType) instanceof Integer) {
            View view = View.inflate(parent.getContext(), ((Integer) setLayout(viewType)), null);
            return new SuperViewHolder(view);
        } else if (setLayout(viewType) instanceof View) {
            return new SuperViewHolder(((View) setLayout(viewType)));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }
        onBindItemHolder(holder, position, mDataList.get(position).getData());
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getItemType();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 这个是继承这个BaseAdapter需要传进来的item布局，可以传资源id，可以传view
     *
     * @param viewType
     * @return
     */
    protected abstract Object setLayout(int viewType);

    /**
     * 这个是让继承这个BaseAdapter去实现的，然后在这个方法里面去把数据设置到item的
     * 控件上去的
     *
     * @param holder
     * @param position
     * @param item
     */
    protected abstract void onBindItemHolder(SuperViewHolder holder, int position, T item);


    /**
     * 向外部提供的传递数据到adapter里面刷新数据用的
     *
     * @param data 传一个集合进来，那么代表是想刷新整个adapter的数据
     */
    public void setDataList(List<V> data) {
        mDataList.clear();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 向外部提供插入一个集合数据到adapter使用的
     *
     * @param data
     */
    public void addDataList(List<V> data) {
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * @param data
     */
    public void setData(V data) {

    }

    /**
     * 添加一条数据
     *
     * @param data
     */
    public void addOne(V data) {
        mDataList.add(data);
        notifyDataSetChanged();
    }

    /**
     * 返回点击的实体的数据，这个数据是从adapter里面的集合拿到这个数据，position也是adapter里面的，
     * 这样拿出来的数据才是最安全的。
     *
     * @param position
     * @return
     */
    public V getItemData(int position) {
        if (position < mDataList.size()) {
            return mDataList.get(position);
        }
        return null;
    }

    /**
     * 删除指定的那一条数据
     *
     * @param position
     */
    public void remove(int position) {
        this.mDataList.remove(position);
        notifyItemRemoved(position);
        if (position != getDataList().size()) {
            notifyItemRangeChanged(position, this.mDataList.size() - position);
        }
    }

    /**
     *
     */
    public List<V> getDataList() {
        return mDataList;
    }
}
