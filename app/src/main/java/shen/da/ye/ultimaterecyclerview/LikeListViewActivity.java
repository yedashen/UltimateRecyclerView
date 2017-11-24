package shen.da.ye.ultimaterecyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import shen.da.ye.ultimaterecyclerview.adapter.LikeLvAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.UltimateRecyclerView;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.UltimateRecyclerViewAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnItemClickListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnLoadMoreListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnNetErrorListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnRefreshListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.UltimateScrollListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.devider.DividerDecoration;

/**
 * @author ChenYe
 *         created by on 2017/11/23 0023. 16:22
 *         这是个单列的像listView一样功能的，刷新和加载更多
 *
 *         问题：
 *         目前推出activity走了onDestry方法之后，在进入界面走了onCreate()方法，但是这个时候上拉加载更多是
 *         有问题的。。。。?
 **/

public class LikeListViewActivity extends Activity {

    private UltimateRecyclerView mUltimateRecyclerView;
    private ArrayList<String> names = null;
    private static final int DATA_SIZE = 30;
    private UltimateRecyclerViewAdapter mUltimateAdapter;
    private Handler mHandler = null;
    private LikeLvAdapter mInnerAdapter;
    /**
     * 这是估计统计加载更多的次数，我这里固定当加载到了3次的时候，让他出现
     * 网络错误
     */
    private int mRequestCount = 0;
    private static final int REQUEST_ERROR_COUNT = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_lv);
        Log.e("Like","onCreate()");
        mUltimateRecyclerView = findViewById(R.id.like_lv_rcv);
        mHandler = new Handler();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        DividerDecoration decoration = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.divide_height)
                .setPadding(R.dimen.divide_padding)
                .setColor(getResources().getColor(R.color.colorAccent))
                .build();
        mUltimateRecyclerView.addItemDecoration(decoration);

        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mInnerAdapter = new LikeLvAdapter();
        mInnerAdapter.setDataList(names);
        mUltimateAdapter = new UltimateRecyclerViewAdapter(mInnerAdapter);
        mUltimateRecyclerView.setAdapter(mUltimateAdapter);

        setupListener();
    }

    private void setupListener() {
        mUltimateAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        mUltimateRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mUltimateRecyclerView.refreshCompleted(10);
                    }
                }, 2000L);
            }
        });


        mUltimateRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRequestCount++;
                        if (mRequestCount == REQUEST_ERROR_COUNT) {
                            mUltimateRecyclerView.setNetErrorListener(mOnNetErrorListener);
                        } else {
                            mInnerAdapter.addOne("新增的数据");
                            mUltimateRecyclerView.loadMoreComplete(false);
                        }
                    }
                }, 2000L);
            }
        });


        mUltimateRecyclerView.setScrollListener(new UltimateScrollListener() {
            @Override
            public void onScrollDownToUp() {

            }

            @Override
            public void onScrollUpToDown() {

            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }

            @Override
            public void onScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        names = new ArrayList<>();
        for (int i = 0; i < DATA_SIZE; i++) {
            names.add("Test_" + i);
        }
    }

    public void forceRefresh(View view) {
        mUltimateRecyclerView.forceToRefresh();
    }

    private OnNetErrorListener mOnNetErrorListener = new OnNetErrorListener() {
        @Override
        public void reload() {
            mInnerAdapter.addOne("新增的数据");
            mUltimateRecyclerView.loadMoreComplete(false);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Like", "onDestroy()");
    }
}
