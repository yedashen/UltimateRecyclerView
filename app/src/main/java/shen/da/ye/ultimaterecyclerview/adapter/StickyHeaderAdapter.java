package shen.da.ye.ultimaterecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import shen.da.ye.ultimaterecyclerview.MyApplication;
import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.StickAdapterListener;

/**
 * @author ChenYe
 *         created by on 2017/11/29 0029. 15:33
 **/

public class StickyHeaderAdapter extends RecyclerView.Adapter<StickyHeaderAdapter.ViewHolder>
        implements StickAdapterListener<StickyHeaderAdapter.HeaderViewHolder> {


    private final LayoutInflater mLayoutInflater;

    public StickyHeaderAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_sticky_header_normal, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText("这是普通数据" + position);
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    @Override
    public long getHeaderId(int position) {
        return (long) position / 7;
    }

    /**
     * 下面这个onCreateHeaderViewHolder()的填充布局只能用LayoutInflater，因为用View.inflate得到的view的
     * view.getLayoutParams()是null，导致那个headerDecoration里面报空指针
     * View view = View.inflate(parent.getContext(), R.layout.item_sticky_header, null);
     *
     * @param parent the header's view parent
     * @return
     */
    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_sticky_header_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder headerViewHolder, int position) {
        headerViewHolder.mHeaderTv.setText("Header" + getHeaderId(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_item);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView mHeaderTv;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mHeaderTv = (TextView) itemView.findViewById(R.id.text_item);
        }
    }
}
