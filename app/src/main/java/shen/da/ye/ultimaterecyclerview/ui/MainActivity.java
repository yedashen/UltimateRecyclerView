package shen.da.ye.ultimaterecyclerview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.ui.stick.LikeStickListViewActivity;

/**
 * @author Chenye
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goLikeListViewActivity(View view) {
        startActivity(new Intent(this, LikeListViewActivity.class));
    }

    public void goLikeStickListViewActivity(View view) {
        startActivity(new Intent(this, LikeStickListViewActivity.class));
    }

    public void goLikeFenLanListViewActivity(View view) {
        startActivity(new Intent(this, ColumnsActivity.class));
    }

    public void goStickyActivity(View view) {
        startActivity(new Intent(this, StickyRecyclerViewActivity.class));
    }

    public void goCustomCompose(View view) {
        startActivity(new Intent(this, SwipeComposeActivity.class));
    }

    public void goSwipeRecyclerViewActivity(View view) {
        startActivity(new Intent(this, SwipeRecyclerViewActivity.class));
    }

    public void goPartialRefresh(View view) {
        startActivity(new Intent(this, PartialRefreshActivity.class));
    }

    public void goAnimationActivity(View view) {
        startActivity(new Intent(this, AnimationRecyclerViewActivity.class));
    }

    public void goMultiTypeRevActivity(View view) {
        startActivity(new Intent(this, MultiTypeActivity.class));
    }

    public void goExpandActivity(View view) {
        startActivity(new Intent(this, ExpandRcvActivity.class));
    }
}
