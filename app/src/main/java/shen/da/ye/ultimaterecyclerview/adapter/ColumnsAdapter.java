package shen.da.ye.ultimaterecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.entity.AnimalObject;

/**
 * @author ChenYe
 *         created by on 2017/11/30 0030. 16:33
 *         基本显示以及做完了，然后应该是写分栏的adapter.
 **/

public class ColumnsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AnimalObject> mAnimals;
    private NormalItemClickListener mListener = null;


    public ColumnsAdapter(List<AnimalObject> mAnimals) {
        this.mAnimals = mAnimals;
    }

    @Override
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fenlan_normal, parent, false);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final String name = mAnimals.get(position).name;
        ((NormalViewHolder) holder).mTv.setText(name);
        if (mListener != null) {
            ((NormalViewHolder) holder).mTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.normalItemClick(name);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mAnimals.size();
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {

        private TextView mTv;

        public NormalViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.item_fen_lan_normal_tv);
        }
    }

    public interface NormalItemClickListener {
        void normalItemClick(String name);
    }

    public void setOnItemClickListener(NormalItemClickListener listener) {
        mListener = listener;
    }

}

