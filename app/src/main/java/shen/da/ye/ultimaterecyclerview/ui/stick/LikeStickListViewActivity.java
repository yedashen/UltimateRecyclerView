package shen.da.ye.ultimaterecyclerview.ui.stick;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import shen.da.ye.ultimaterecyclerview.R;

/**
 * @author ChenYe
 *         created by on 2017/11/29 0029. 10:42
 *         有三个fragment：
 **/

public class LikeStickListViewActivity extends AppCompatActivity {

    private RadioGroup mRadioGroup;
    private List<Fragment> mFragments = null;
    private List<Fragment> mVisibleFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_stick_lv);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg);
        initFragmentList();
        mRadioGroup.setOnCheckedChangeListener(mOnCheckChangedListener);
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
    }

    private void initFragmentList() {
        mFragments = new ArrayList<>();
        mFragments.add(new StickyHeaderFragment());
        mFragments.add(new InLineStickyHeaderFragment());
        mFragments.add(new DoubleStickyHeaderFragment());
    }

    private RadioGroup.OnCheckedChangeListener mOnCheckChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            changeView(radioGroup.indexOfChild(radioGroup.findViewById(i)));
        }
    };

    private void changeView(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = mFragments.get(position);
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.sticky_container, fragment);
            mVisibleFragments.add(fragment);
        }
        for (Fragment fragment1 : mVisibleFragments) {
            if (fragment1 != fragment) {
                if (fragment1.isVisible()) {
                    transaction.hide(fragment1);
                    transaction.addToBackStack(null);
                }
            }
        }
        transaction.commit();
    }

}
