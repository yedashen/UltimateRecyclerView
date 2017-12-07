package shen.da.ye.ultimaterecyclerview.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.adapter.AnimationAdapter;

/**
 * @author ChenYe
 *         created by on 2017/12/7 0007. 11:03
 **/

public class AnimationRecyclerViewActivity extends Activity {

    private TextView mStateTv;
    private RecyclerView mRcv;
    private AnimationAdapter mAdapter;
    private boolean mFlag = false;
    private TextView mAnimationTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anition_rcv);
        mStateTv = (TextView) findViewById(R.id.state_tv);
        mRcv = (RecyclerView) findViewById(R.id.rcv);
        mAnimationTv = (TextView) findViewById(R.id.animation_tv);
        ArrayList<String> names = initData();
        mAdapter = new AnimationAdapter();
        mAdapter.setDataList(names);
        mRcv.setLayoutManager(new LinearLayoutManager(this));
        mRcv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRcv.setAdapter(mAdapter);
    }

    private ArrayList<String> initData() {
        ArrayList names = new ArrayList();
        for (int i = 0; i < 20; i++) {
            names.add("Cy_" + i);
        }
        return names;
    }

    public void defaultAnimation(View view) {
        mAdapter.setAnimationStyle(AnimationAdapter.DEFAULT_ANIMATION);
        mRcv.setAdapter(mAdapter);
        mAnimationTv.setText("当前动画是: [ 默认动画 ] ");
    }

    public void alphaAnimation(View view) {
        mAdapter.setAnimationStyle(AnimationAdapter.ALPHA_ANIMATION);
        mRcv.setAdapter(mAdapter);
        mAnimationTv.setText("当前动画是: [ 透明动画 ] ");
    }

    public void scaleAnimation(View view) {
        mAdapter.setAnimationStyle(AnimationAdapter.SCALE_ANIMATION);
        mRcv.setAdapter(mAdapter);
        mAnimationTv.setText("当前动画是: [ 缩放动画 ] ");
    }

    public void fromLeftAnimation(View view) {
        mAdapter.setAnimationStyle(AnimationAdapter.SLIDE_FROM_LEFT_ANIMATION);
        mRcv.setAdapter(mAdapter);
        mAnimationTv.setText("当前动画是: [ 从左滑出动画 ] ");
    }

    public void fromRightAnimation(View view) {
        mAdapter.setAnimationStyle(AnimationAdapter.SLIDE_FROM_RIGHT_ANIMATION);
        mRcv.setAdapter(mAdapter);
        mAnimationTv.setText("当前动画是: [ 从右滑出动画 ] ");
    }

    public void controlAnimation(View view) {
        mFlag = !mFlag;
        mAdapter.setFirstOnly(mFlag);
        mAdapter.notifyDataSetChanged();
        mStateTv.setText(mFlag ? "是否会重复: [ 不会 ] " : "是否会重复: [ 会 ] ");
    }

}
