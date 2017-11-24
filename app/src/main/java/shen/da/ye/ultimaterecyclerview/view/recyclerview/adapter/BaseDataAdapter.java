package shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenYe
 *         created by on 2017/11/17 0017. 09:52
 *         <p>
 *         这个是单列的数据baseAdapter:如果你用这个自定义的recyclerView的话，你的使用场景是跟listview
 *         差不多的效果，就是单列的话，就可以写一个adapter来继承这个BaseAdapter,在你写的adapter只
 *         需要对控件设置数据就可以了。这个BaseAdapter的使用范例的adapter是DataAdapter。
 **/

public abstract class BaseDataAdapter<T> extends RecyclerView.Adapter<SuperViewHolder> {

    protected List<T> mDataList = new ArrayList<>();

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (setLayout() instanceof Integer) {
            View view = View.inflate(parent.getContext(), ((Integer) setLayout()), null);
            return new SuperViewHolder(view);
        } else if (setLayout() instanceof View) {
            return new SuperViewHolder(((View) setLayout()));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        onBindItemHolder(holder, position);
    }

    //这个是用来解决局部刷新数据的
    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            //主要是这里
            onBindItemHolder(holder, position, payloads);
        }
    }

    /**
     * 这个是继承这个BaseAdapter需要传进来的item布局，可以传资源id，可以传view
     *
     * @return
     */
    protected abstract Object setLayout();

    /**
     * 这个是让继承这个BaseAdapter去实现的，然后在这个方法里面去把数据设置到item的
     * 控件上去的
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindItemHolder(SuperViewHolder holder, int position);

    public void onBindItemHolder(SuperViewHolder holder, int position, List<Object> payloads) {

    }


    /**
     * 向外部提供的传递数据到adapter里面刷新数据用的
     *
     * @param data 传一个集合进来，那么代表是想刷新整个adapter的数据
     */
    public void setDataList(ArrayList<T> data) {
        mDataList.clear();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 向外部提供插入一个集合数据到adapter使用的
     *
     * @param data
     */
    public void addDataList(List<T> data) {
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * @param data
     */
    public void setData(T data) {

    }

    /**
     * 添加一条数据
     *
     * @param data
     */
    public void addOne(T data) {
        mDataList.add(data);
        notifyDataSetChanged();
    }

    /**
     * 返回点击的实体的数据，这个数据是从adapter里面的集合拿到这个数据，postion也是adapter里面的，
     * 这样拿出来的数据才是最安全的。
     *
     * @param position
     * @return
     */
    public Object getItemData(int position) {
        if (position < mDataList.size()) {
            return mDataList.get(position);
        }
        return null;
    }
}
