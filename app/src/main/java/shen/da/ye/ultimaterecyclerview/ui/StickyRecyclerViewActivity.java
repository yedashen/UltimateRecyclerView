package shen.da.ye.ultimaterecyclerview.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import shen.da.ye.ultimaterecyclerview.MyApplication;
import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.entity.StickyHeadEntity;
import shen.da.ye.ultimaterecyclerview.entity.StockEntity;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.StickyAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.StickyBaseAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.OnItemClickListener;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.devider.StickyItemDecoration;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.stick.StickyHeadContainer;

/**
 * @author ChenYe
 *         created by on 2017/12/5 0005. 14:08
 *         粘性RecyclerView
 **/

public class StickyRecyclerViewActivity extends Activity {

    private StickyAdapter mStickyAdapter;
    private int mStickyPosition;
    private TextView mStickyHeaderTv;
    private CheckBox mStickyHeaderCheckbox;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
        initView();
        parseAndSetData(getStrFromAssets(StickyRecyclerViewActivity.this, "rasking.json"));
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.sticky_rcv);
        StickyHeadContainer mStickyHeader = (StickyHeadContainer) findViewById(R.id.sticky_shc);
        mStickyHeaderTv = (TextView) findViewById(R.id.sticky_header_tv);
        ImageView mHeaderMoreIv = (ImageView) findViewById(R.id.sticky_header_more_iv);
        mStickyHeaderCheckbox = (CheckBox) findViewById(R.id.sticky_header_checkbox);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new StickyItemDecoration(mStickyHeader, StickyBaseAdapter.TYPE_STICKY_HEAD));
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        mStickyHeader.setDataCallback(mDataBack);

        mStickyAdapter = new StickyAdapter();
        mRecyclerView.setAdapter(mStickyAdapter);
        mStickyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StockEntity.StockInfo itemData = mStickyAdapter.getItemData(position).getData();
                if (itemData.getItemType() == StickyBaseAdapter.TYPE_STICKY_HEAD) {
                    Toast.makeText(MyApplication.mShareInstance, "点击了粘性头部", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyApplication.mShareInstance, "点击了普通数据", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mStickyAdapter.setOnItemClickListener2(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MyApplication.mShareInstance, "点击了粘性头部的更多", Toast.LENGTH_SHORT).show();
            }
        });

        mStickyHeaderCheckbox.setOnCheckedChangeListener(mCheckboxChangedListener);
        mStickyHeader.setOnClickListener(mHeaderOnClickListener);
        mHeaderMoreIv.setOnClickListener(mMoreIvClickListener);
    }

    /**
     * @return Json数据（String）
     * @description 通过assets文件获取json数据，这里写的十分简单，没做循环判断。
     */
    public static String getStrFromAssets(Context context, String name) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    private void parseAndSetData(String result) {
        Gson gson = new Gson();

        final StockEntity stockEntity = gson.fromJson(result, StockEntity.class);

        List<StockEntity.StockInfo> data = new ArrayList<>();

        data.add(new StockEntity.StockInfo(StickyBaseAdapter.TYPE_STICKY_HEAD, "涨幅榜"));
        for (StockEntity.StockInfo info : stockEntity.increase_list) {
            info.setItemType(StickyBaseAdapter.TYPE_DATA);
            data.add(info);
        }

        data.add(new StockEntity.StockInfo(StickyBaseAdapter.TYPE_STICKY_HEAD, "跌幅榜"));
        for (StockEntity.StockInfo info : stockEntity.down_list) {
            info.setItemType(StickyBaseAdapter.TYPE_DATA);
            data.add(info);
        }

        data.add(new StockEntity.StockInfo(StickyBaseAdapter.TYPE_STICKY_HEAD, "换手率"));
        for (StockEntity.StockInfo info : stockEntity.change_list) {
            info.setItemType(StickyBaseAdapter.TYPE_DATA);
            data.add(info);
        }

        data.add(new StockEntity.StockInfo(StickyBaseAdapter.TYPE_STICKY_HEAD, "振幅榜"));
        for (StockEntity.StockInfo info : stockEntity.amplitude_list) {
            info.setItemType(StickyBaseAdapter.TYPE_DATA);
            data.add(info);
        }

        List<StickyHeadEntity<StockEntity.StockInfo>> list = new ArrayList<>(data.size());
        list.add(new StickyHeadEntity<StockEntity.StockInfo>(null, StickyAdapter.TYPE_HEAD, null));
        for (StockEntity.StockInfo info : data) {
            list.add(new StickyHeadEntity<>(info, info.getItemType(), info.stickyHeadName));
        }

        mStickyAdapter.setDataList(list);
        mRecyclerView.setAdapter(mStickyAdapter);

    }


    private CheckBox.OnCheckedChangeListener mCheckboxChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mStickyAdapter.getItemData(mStickyPosition).getData().check = isChecked;
            mStickyAdapter.notifyItemChanged(mStickyPosition);
        }
    };

    private StickyHeadContainer.DataCallback mDataBack = new StickyHeadContainer.DataCallback() {
        @Override
        public void onDataChange(int pos) {
            mStickyPosition = pos;
            StockEntity.StockInfo item = mStickyAdapter.getItemData(pos).getData();
            mStickyHeaderTv.setText(item.stickyHeadName);
            mStickyHeaderCheckbox.setChecked(item.check);
        }
    };

    private View.OnClickListener mHeaderOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MyApplication.mShareInstance, "点击了粘性头部", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener mMoreIvClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e("Sticky", "点击了粘性头部的更多");
            Toast.makeText(MyApplication.mShareInstance, "点击了粘性头部的更多", Toast.LENGTH_SHORT).show();
        }
    };

}
