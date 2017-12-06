package shen.da.ye.ultimaterecyclerview.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.BaseDataAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.SuperViewHolder;

/**
 * @author ChenYe
 *         created by on 2017/12/6 0006. 14:52
 **/

public class SwipeRecyclerViewAdapter extends BaseDataAdapter<String> {

    private SwipeFunctionClickListener mListener;

    @Override
    protected Object setLayout() {
        return R.layout.item_swipe_rcv;
    }

    @Override
    protected void onBindItemHolder(SuperViewHolder holder, final int position) {
        ((TextView) holder.getView(R.id.item_name_tv)).setText(mDataList.get(position));
        ImageView itemIv = (ImageView) holder.getView(R.id.item_iv);
        Button deleteBt = (Button) holder.getView(R.id.item_delete_bt);
        Button testBt = (Button) holder.getView(R.id.item_bt);
        LinearLayout itemContent = (LinearLayout) holder.getView(R.id.item_content);

        if (mListener != null) {
            itemIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickIv(position);
                }
            });

            deleteBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickDelete(position);
                }
            });

            testBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickTest(position);
                }
            });

            itemContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });
        }
    }

    public interface SwipeFunctionClickListener {

        void onClickDelete(int position);

        void onClickTest(int position);

        void onClickIv(int position);

        void onItemClick(int position);
    }

    public void setSwipeListener(SwipeFunctionClickListener listener) {
        mListener = listener;
    }
}
