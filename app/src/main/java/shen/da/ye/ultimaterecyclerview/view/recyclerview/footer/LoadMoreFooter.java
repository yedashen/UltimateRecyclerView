package shen.da.ye.ultimaterecyclerview.view.recyclerview.footer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.ILoadMoreFooter;

/**
 * @author ChenYe
 *         created by on 2017/11/16 0016. 11:08
 **/

public class LoadMoreFooter extends RelativeLayout implements ILoadMoreFooter {

    public LoadMoreFooter(Context context) {
        this(context, null);
    }

    public LoadMoreFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoadComplete() {

    }

    @Override
    public void onNoMore() {

    }

    @Override
    public void onReset() {

    }

    @Override
    public View getLoadMoreFooter() {
        return null;
    }
}
