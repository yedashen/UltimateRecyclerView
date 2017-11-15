package shen.da.ye.ultimaterecyclerview;

import android.app.Application;
import android.content.Context;

/**
 * @author ChenYe
 *         created by on 2017/11/15 0015. 16:43
 **/

public class MyApplication extends Application {

    public static Context mShareInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mShareInstance = getApplicationContext();
    }
}
