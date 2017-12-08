package shen.da.ye.ultimaterecyclerview.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.adapter.BaseMultiItemQuickAdapter;
import shen.da.ye.ultimaterecyclerview.entity.MultiItemEntity;
import shen.da.ye.ultimaterecyclerview.util.CreateDataUtil;

/**
 * @author ChenYe
 *         created by on 2017/12/7 0007. 15:41
 *         扩展关系是：学校》学院》》专业》》班级》》学生
 *         <p>
 *         这个是可以支持N及扩展的，只要你看懂了我这个代码就可以实现的，尤其注意数据是怎么设置的
 **/

public class ExpandRcvActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable);
        ArrayList<MultiItemEntity> dataList = CreateDataUtil.generateExpandData();
        RecyclerView mRcv = (RecyclerView) findViewById(R.id.expandable_rcv);
        BaseMultiItemQuickAdapter adapter = new BaseMultiItemQuickAdapter();
        adapter.setDataList(dataList);
        mRcv.setAdapter(adapter);
        mRcv.setLayoutManager(new LinearLayoutManager(this));
        mRcv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
