package shen.da.ye.ultimaterecyclerview.ui.stick;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import shen.da.ye.ultimaterecyclerview.MyApplication;
import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.adapter.StickyHeaderAdapter;
import shen.da.ye.ultimaterecyclerview.view.decoration.StickyHeaderDecoration;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.UltimateRecyclerView;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.UltimateRecyclerViewAdapter;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.devider.DividerDecoration;

/**
 * @author ChenYe
 *         created by on 2017/11/29 0029. 14:30
 **/

public class StickyHeaderFragment extends Fragment implements RecyclerView.OnItemTouchListener {

    private UltimateRecyclerView mUltimateRecyclerView;
    private StickyHeaderDecoration mDecoration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sticky, null);
        mUltimateRecyclerView = (UltimateRecyclerView) view.findViewById(R.id.sticky_rcv);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DividerDecoration dividerDecoration = new DividerDecoration.Builder(MyApplication.mShareInstance)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setHeight(getResources().getDimension(R.dimen.divide_height))
                .setPadding(getResources().getDimension(R.dimen.divide_padding))
                .build();
        mUltimateRecyclerView.addItemDecoration(dividerDecoration);

        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUltimateRecyclerView.setRefreshEnable(false);

        StickyHeaderAdapter innerAdapter = new StickyHeaderAdapter(getContext());
        mDecoration = new StickyHeaderDecoration(innerAdapter);

        UltimateRecyclerViewAdapter adapter = new UltimateRecyclerViewAdapter(innerAdapter);
        mUltimateRecyclerView.setAdapter(adapter);

        mUltimateRecyclerView.addItemDecoration(mDecoration, 1);

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View v = rv.findChildViewUnder(e.getX(), e.getY());
        return v == null;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            View headerViewUnder = mDecoration.findHeaderViewUnder(e.getX(), e.getY());
            Log.e("StickHeader", "click");
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
