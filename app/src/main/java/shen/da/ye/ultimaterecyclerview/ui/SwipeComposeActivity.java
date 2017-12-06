package shen.da.ye.ultimaterecyclerview.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.view.swipe.SwipeItem;

/**
 * @author ChenYe
 *         created by on 2017/12/5 0005. 17:12
 *         这个是侧滑item的自定义控件activity,目前我还是不懂qq阻塞式交互是什么意思。
 **/

public class SwipeComposeActivity extends Activity {

    private SwipeItem mSwipeItem1;
    private SwipeItem mSwipeItem2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        mSwipeItem1 = (SwipeItem) findViewById(R.id.swipe_item1);
        mSwipeItem1.setSwipeLeftOrRight(true);

        mSwipeItem2 = (SwipeItem) findViewById(R.id.swipe_item2);
        mSwipeItem2.setSwipeLeftOrRight(false);
    }

    public void close(View view) {
        mSwipeItem1.closeItem();
        mSwipeItem2.closeItem();
    }
}
