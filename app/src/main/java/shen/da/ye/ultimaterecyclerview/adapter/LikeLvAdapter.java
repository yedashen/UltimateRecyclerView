package shen.da.ye.ultimaterecyclerview.adapter;

import android.util.Log;
import android.widget.TextView;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.BaseDataAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.SuperViewHolder;

/**
 * @author ChenYe
 *         created by on 2017/11/23 0023. 16:51
 *         这是LikeListViewActivity界面的adapter
 **/

public class LikeLvAdapter extends BaseDataAdapter<String> {

    @Override
    protected Object setLayout() {
        return R.layout.item_like_lv;
    }

    @Override
    protected void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView textView = holder.getView(R.id.item_like_lv_tv);
        textView.setText(mDataList.get(position));
    }
}
