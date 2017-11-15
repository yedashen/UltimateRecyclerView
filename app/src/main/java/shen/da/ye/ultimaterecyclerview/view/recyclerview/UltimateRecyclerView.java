package shen.da.ye.ultimaterecyclerview.view.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

import shen.da.ye.ultimaterecyclerview.MyApplication;

/**
 * @author ChenYe
 *         created by on 2017/11/15 0015. 16:39
 **/

public class UltimateRecyclerView extends RecyclerView {

    public UltimateRecyclerView(Context context) {
        this(context, null);
    }

    public UltimateRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UltimateRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //获取这个手机手指点击最小滑动距离（超过这个距离就算滑动不算点击）
        int scaledTouchSlop = ViewConfiguration.get(MyApplication.mShareInstance).getScaledTouchSlop();

    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }


}
