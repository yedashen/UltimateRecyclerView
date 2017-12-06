package shen.da.ye.ultimaterecyclerview.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import shen.da.ye.ultimaterecyclerview.MyApplication;
import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.adapter.SwipeRecyclerViewAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.UltimateRecyclerView;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.UltimateRecyclerViewAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnItemClickListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.devider.DividerDecoration;

/**
 * @author ChenYe
 *         created by on 2017/12/5 0005. 17:12
 *         这个是侧滑item的自定义控件activity
 **/

public class SwipeRecyclerViewActivity extends Activity {

    private UltimateRecyclerView mUltimateRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_rcv);
        mUltimateRecyclerView = (UltimateRecyclerView) findViewById(R.id.swipe_rcv);
        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration decoration = new DividerDecoration.Builder(this)
                .build();
        mUltimateRecyclerView.addItemDecoration(decoration);
        ArrayList<String> names = initData();
        final SwipeRecyclerViewAdapter innerAdapter = new SwipeRecyclerViewAdapter();
        innerAdapter.setDataList(names);
        innerAdapter.setSwipeListener(new SwipeRecyclerViewAdapter.SwipeFunctionClickListener() {
            @Override
            public void onClickDelete(int position) {
                innerAdapter.remove(position);
            }

            @Override
            public void onClickTest(int position) {
                Toast.makeText(MyApplication.mShareInstance, "点击了" + innerAdapter.getItemData(position) + "的备用按钮", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickIv(int position) {
                Toast.makeText(MyApplication.mShareInstance, "点击了" + innerAdapter.getItemData(position) + "的图片", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(int position) {
                Toast.makeText(MyApplication.mShareInstance, "点击了" + innerAdapter.getItemData(position) + "条目", Toast.LENGTH_SHORT).show();
            }
        });

        UltimateRecyclerViewAdapter adapter = new UltimateRecyclerViewAdapter(innerAdapter);
        mUltimateRecyclerView.setAdapter(adapter);
        mUltimateRecyclerView.setRefreshEnable(false);
        mUltimateRecyclerView.setLoadMoreEnable(false);
    }

    private ArrayList<String> initData() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < 29; i++) {
            names.add("Cy_" + i);
        }
        return names;
    }
}
