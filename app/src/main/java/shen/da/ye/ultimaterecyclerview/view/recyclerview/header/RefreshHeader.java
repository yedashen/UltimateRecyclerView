package shen.da.ye.ultimaterecyclerview.view.recyclerview.header;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.IRefreshHeader;

/**
 * @author ChenYe
 *         created by on 2017/11/15 0015. 17:10
 **/

public class RefreshHeader extends LinearLayout implements IRefreshHeader {

    private static final String TAG = RefreshHeader.class.getSimpleName();

    public RefreshHeader(Context context) {
        this(context, null);
    }

    public RefreshHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultLayoutParams();
        addViewAndFindId();
    }

    /**
     * 添加布局和findId
     */
    private void addViewAndFindId() {

    }

    /**
     * 初始化layoutParams
     */
    private void initDefaultLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        this.setLayoutParams(layoutParams);
        this.setPadding(0, 0, 0, 0);
    }


    @Override
    public void onPrepare() {

    }

    @Override
    public void onRefreshing() {

    }

    @Override
    public void onMove(int offset, int sumOffset) {

    }

    @Override
    public boolean release() {
        return false;
    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public View getHeaderView() {
        return null;
    }

    @Override
    public int getVisibleHeight() {
        return 0;
    }

    @Override
    public void onReset() {

    }
}
