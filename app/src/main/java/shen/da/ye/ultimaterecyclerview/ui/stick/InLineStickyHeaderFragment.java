package shen.da.ye.ultimaterecyclerview.ui.stick;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shen.da.ye.ultimaterecyclerview.R;

/**
 * @author ChenYe
 *         created by on 2017/11/29 0029. 14:30
 **/

public class InLineStickyHeaderFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sticky_in_line, null);
        return view;
    }
}
