package shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.entity.StickyHeadEntity;
import shen.da.ye.ultimaterecyclerview.entity.StockEntity;
import shen.da.ye.ultimaterecyclerview.util.FullSpanUtil;

/**
 * @author ChenYe
 *         created by on 2017/12/5 0005. 15:57
 **/

public class StickyAdapter extends StickyBaseAdapter<StockEntity.StockInfo, StickyHeadEntity<StockEntity.StockInfo>> implements CompoundButton.OnCheckedChangeListener {

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, TYPE_STICKY_HEAD);
    }

    @Override
    public void onViewAttachedToWindow(SuperViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        FullSpanUtil.onViewAttachedToWindow(holder, this, TYPE_STICKY_HEAD);
    }


    @Override
    protected Object setLayout(int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                return R.layout.item_stock_head;
            case TYPE_STICKY_HEAD:
                return R.layout.item_stock_sticky_head;
            case TYPE_DATA:
                return R.layout.item_stock_data;
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onBindItemHolder(SuperViewHolder holder, int position, StockEntity.StockInfo item) {
        int itemViewType = holder.getItemViewType();
        switch (itemViewType) {
            case TYPE_STICKY_HEAD:
                CheckBox checkBox = holder.getView(R.id.checkbox);
                checkBox.setTag(position);
                checkBox.setOnCheckedChangeListener(this);
                checkBox.setChecked(item.check);
                ((TextView) holder.getView(R.id.tv_stock_name)).setText(item.stickyHeadName);
                break;
            case TYPE_DATA:
                setData(holder, item);
                break;
            default:
                break;
        }
    }

    private void setData(SuperViewHolder holder, StockEntity.StockInfo item) {
        final String stockNameAndCode = item.stock_name + "\n" + item.stock_code;
        SpannableStringBuilder ssb = new SpannableStringBuilder(stockNameAndCode);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#a4a4a7")), item.stock_name.length(), stockNameAndCode.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new AbsoluteSizeSpan(dip2px(holder.itemView.getContext(), 13)), item.stock_name.length(), stockNameAndCode.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ((TextView) holder.getView(R.id.tv_stock_name_code)).setText(ssb);
        ((TextView) holder.getView(R.id.tv_current_price)).setText(item.current_price);
        ((TextView) holder.getView(R.id.tv_rate)).setText(item.rate < 0 ? String.format("%.2f", item.rate) : ("+" + String.format("%.2f", item.rate)) + "%");
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos = (Integer) buttonView.getTag();
        mDataList.get(pos).getData().check = isChecked;
    }
}
