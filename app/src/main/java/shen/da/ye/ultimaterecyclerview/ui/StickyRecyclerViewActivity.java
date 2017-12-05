package shen.da.ye.ultimaterecyclerview.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import shen.da.ye.ultimaterecyclerview.MyApplication;
import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.devider.DividerDecoration;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.devider.StickyItemDecoration;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.stick.StickyHeadContainer;

/**
 * @author ChenYe
 *         created by on 2017/12/5 0005. 14:08
 **/

public class StickyRecyclerViewActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.sticky_rcv);
        StickyHeadContainer mStickyHeader = (StickyHeadContainer) findViewById(R.id.sticky_shc);
        TextView mStickyHeaderTv = (TextView) findViewById(R.id.sticky_header_tv);
        ImageView mHeaderMoreIv = (ImageView) findViewById(R.id.sticky_header_more_iv);
        CheckBox mStickyHeaderCheckbox = (CheckBox) findViewById(R.id.sticky_header_checkbox);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new StickyItemDecoration(mStickyHeader, 2));
        DividerDecoration dividerDecoration = new DividerDecoration.Builder(MyApplication.mShareInstance)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setHeight(getResources().getDimension(R.dimen.divide_height))
                .setPadding(getResources().getDimension(R.dimen.divide_padding))
                .build();
        mRecyclerView.addItemDecoration(dividerDecoration);
        mStickyHeader.setDataCallback(mDataBack);
        mStickyHeaderCheckbox.setOnCheckedChangeListener(mCheckboxChangedListener);
        mStickyHeader.setOnClickListener(mHeaderOnClickListener);
        mHeaderMoreIv.setOnClickListener(mMoreIvClickListener);
    }

    private CheckBox.OnCheckedChangeListener mCheckboxChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    };

    private StickyHeadContainer.DataCallback mDataBack = new StickyHeadContainer.DataCallback() {
        @Override
        public void onDataChange(int pos) {

        }
    };

    private View.OnClickListener mHeaderOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener mMoreIvClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
