package shen.da.ye.ultimaterecyclerview.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.adapter.LikeLvAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.UltimateRecyclerView;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.UltimateRecyclerViewAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.devider.DividerDecoration;

/**
 * @author ChenYe
 *         created by on 2017/12/6 0006. 15:44
 **/

public class PartialRefreshActivity extends Activity {

    private LikeLvAdapter mInnerAdapter;
    private UltimateRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partial);
        UltimateRecyclerView mUltimateRecyclerView = (UltimateRecyclerView) findViewById(R.id.partial_rcv);
        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mUltimateRecyclerView.addItemDecoration(decoration);
        ArrayList names = initData();
        mInnerAdapter = new LikeLvAdapter();
        mInnerAdapter.setDataList(names);

        mAdapter = new UltimateRecyclerViewAdapter(mInnerAdapter);
        mUltimateRecyclerView.setAdapter(mAdapter);
        mUltimateRecyclerView.setRefreshEnable(false);
        mUltimateRecyclerView.setLoadMoreEnable(false);
    }

    private ArrayList initData() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            names.add("CY_" + i);
        }
        return names;
    }

    public void partialRefresh(View view) {
        //我指定刷新低5条数据(position = 4)
        mInnerAdapter.getDataList().set(4, "这是局部刷新之后的数据");
        mAdapter.notifyItemChanged(mAdapter.getAdapterPosition(false, 4), "Test");
    }
}
