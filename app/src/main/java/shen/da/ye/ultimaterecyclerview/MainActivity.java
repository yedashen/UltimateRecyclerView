package shen.da.ye.ultimaterecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import shen.da.ye.ultimaterecyclerview.view.animation.RecyclerViewLoadingView;

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

}
