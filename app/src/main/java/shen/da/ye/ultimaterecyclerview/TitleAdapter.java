package shen.da.ye.ultimaterecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author ChenYe
 *         created by on 2017/12/4 0004. 08:59
 **/

public class TitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Sectionizer mSectionizer = null;
    private RecyclerView.Adapter mInnerAdapter = null;
    private boolean mValid = true;
    private static final int TITLE_TYPE = 0;
    private SparseArray<Title> mTitles = new SparseArray<>();

    public TitleAdapter(final RecyclerView.Adapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;

        mInnerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mValid = mInnerAdapter.getItemCount() > 0;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mValid = mInnerAdapter.getItemCount() > 0;
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mValid = mInnerAdapter.getItemCount() > 0;
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                mValid = mInnerAdapter.getItemCount() > 0;
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TITLE_TYPE) {
            Log.e("TitleAdapter", "onCreateViewHolder(true)");
            View view = View.inflate(parent.getContext(), R.layout.item_fen_lan_title, null);
            return new TitleViewHolder(view);
        } else {
            Log.e("TitleAdapter", "onCreateViewHolder(false)");
            return mInnerAdapter.onCreateViewHolder(parent, viewType - 1);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isTitlePosition(position)) {
            Log.e("TitleAdapter", "onBindViewHolder(true)");
            ((TitleViewHolder) holder).mTitleTv.setText(mTitles.get(position).title);
        } else {
            Log.e("TitleAdapter", "onBindViewHolder(false)");
            mInnerAdapter.onBindViewHolder(holder, convertPosition(position));
        }
    }


    @Override
    public int getItemCount() {
        return mValid ? mInnerAdapter.getItemCount() + mTitles.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        Log.e("TitleAdapter", "getItemId()");
        return isTitlePosition(position) ? Integer.MAX_VALUE - mTitles.indexOfKey(position) : mInnerAdapter.getItemId(convertPosition(position));
    }


    @Override
    public int getItemViewType(int position) {
        return isTitlePosition(position) ? TITLE_TYPE : mInnerAdapter.getItemViewType(convertPosition(position)) + 1;
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitleTv;

        public TitleViewHolder(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.item_title_tv);
        }
    }

    public interface Sectionizer<T> {

        /**
         * 返回分栏的item的文本信息
         *
         * @param object
         * @return
         */
        String getSectionTitle(T object);
    }

    public void setSectionizer(Sectionizer sectionizer) {
        this.mSectionizer = sectionizer;
    }

    public static class Title {
        int firstPosition;
        int titlePosition;
        CharSequence title;

        public Title(int firstPosition, CharSequence title) {
            this.firstPosition = firstPosition;
            this.title = title;
        }

        public int getTitlePosition() {
            return titlePosition;
        }

        @Override
        public String toString() {
            return "Title{" +
                    "firstPosition=" + firstPosition +
                    ", titlePosition=" + titlePosition +
                    ", title=" + title +
                    '}';
        }
    }

    private boolean isTitlePosition(int position) {
        boolean b = mTitles.get(position) != null;
        return b;
    }

    private int convertPosition(int position) {
        if (isTitlePosition(position)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mTitles.size(); i++) {
            if (mTitles.valueAt(i).titlePosition > position) {
                break;
            }
            --offset;
        }
        return position + offset;
    }

    public void setTitles(List<? extends Object> values) {
        if (mSectionizer == null) {
            throw new NullPointerException("请在设置调用setTitles()之前调用setSectionizer()方法!");
        }

        if (values == null || values.size() == 0) {
            mTitles.clear();
            notifyDataSetChanged();
            return;
        }

        List<Title> titles = convertToTitleList(values);
        Title[] titleArray = new Title[titles.size()];
        Title[] newTitles = titles.toArray(titleArray);

        mTitles.clear();

        Arrays.sort(newTitles, new Comparator<Title>() {
            @Override
            public int compare(Title o1, Title o2) {
                return (o1.firstPosition == o2.firstPosition ? 0 : (o1.firstPosition < o2.firstPosition ? -1 : 1));
            }
        });

        int offset = 0;
        for (Title title : newTitles) {
            title.titlePosition = title.firstPosition + offset;
            mTitles.append(title.titlePosition, title);
            ++offset;
            Log.e("TitleAdapter", title.toString()+"\n");
        }

        notifyDataSetChanged();
    }

    private List<Title> convertToTitleList(List<? extends Object> values) {
        List<Title> titles = new ArrayList<>();
        int n = values.size();
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();

        for (int i = 0; i < n; i++) {
            String sectionTitle = mSectionizer.getSectionTitle(values.get(i));
            if (!linkedHashMap.containsKey(sectionTitle)) {
                linkedHashMap.put(sectionTitle, i);
                titles.add(new Title(i, sectionTitle));
            }
        }
        Log.e("TitleAdapter", titles.toString());
//        {猫科动物=4, 鸟类=6, 人类=8, 鱼类=12, 蛇类=16}
        return titles;
    }
}
