package shen.da.ye.ultimaterecyclerview.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import shen.da.ye.ultimaterecyclerview.MyApplication;
import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.adapter.TitleAdapter;
import shen.da.ye.ultimaterecyclerview.adapter.ColumnsAdapter;
import shen.da.ye.ultimaterecyclerview.entity.AnimalObject;

/**
 * @author ChenYe
 *         created by on 2017/11/30 0030. 15:25
 *         分栏
 **/

public class ColumnsActivity extends AppCompatActivity {

    private RecyclerView mRcv;
    private List<AnimalObject> mFlData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenlan);
        mRcv = (RecyclerView) findViewById(R.id.fen_lan_rcv);
        initData();
        mRcv.setLayoutManager(new LinearLayoutManager(this));
        ColumnsAdapter innerAdapter = new ColumnsAdapter(mFlData);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRcv.addItemDecoration(decoration);

        innerAdapter.setOnItemClickListener(new ColumnsAdapter.NormalItemClickListener() {
            @Override
            public void normalItemClick(String name) {
                Toast.makeText(MyApplication.mShareInstance, name, Toast.LENGTH_SHORT).show();
            }
        });

        TitleAdapter titleAdapter = new TitleAdapter(innerAdapter);
        titleAdapter.setSectionizer(new TitleAdapter.Sectionizer() {
            @Override
            public String getSectionTitle(Object object) {
                return ((AnimalObject) object).type;
            }
        });

        titleAdapter.setTitles(mFlData);
        mRcv.setAdapter(titleAdapter);
    }

    private void initData() {
        mFlData = new ArrayList<>();
        mFlData.add(new AnimalObject("猫科动物", "猫"));
        mFlData.add(new AnimalObject("猫科动物", "狮子"));
        mFlData.add(new AnimalObject("猫科动物", "狗"));
        mFlData.add(new AnimalObject("猫科动物", "猴子"));
        mFlData.add(new AnimalObject("猫科动物", "豹子"));

        mFlData.add(new AnimalObject("鸟类", "鸽子"));
        mFlData.add(new AnimalObject("鸟类", "燕子"));

        mFlData.add(new AnimalObject("人类", "男人"));
        mFlData.add(new AnimalObject("人类", "女人"));

        mFlData.add(new AnimalObject("鱼类", "鲫鱼"));
        mFlData.add(new AnimalObject("鱼类", "黑鱼"));
        mFlData.add(new AnimalObject("鱼类", "草鱼"));
        mFlData.add(new AnimalObject("鱼类", "鲈鱼"));


        mFlData.add(new AnimalObject("蛇类", "眼镜蛇"));
        mFlData.add(new AnimalObject("蛇类", "竹叶青蛇"));
        mFlData.add(new AnimalObject("蛇类", "蟒蛇"));
        mFlData.add(new AnimalObject("蛇类", "七步蛇"));
    }
}
