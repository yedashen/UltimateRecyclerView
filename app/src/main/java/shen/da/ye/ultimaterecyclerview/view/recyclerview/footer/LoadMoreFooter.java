package shen.da.ye.ultimaterecyclerview.view.recyclerview.footer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.view.animation.RecyclerViewLoadingView;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.callbacks.ILoadMoreFooter;

/**
 * @author ChenYe
 *         created by on 2017/11/16 0016. 11:08
 **/

public class LoadMoreFooter extends RelativeLayout implements ILoadMoreFooter {

    private static final String TAG = LoadMoreFooter.class.getSimpleName();
    private FooterState mState = null;
    private View mLoadingView = null;
    private View mLoadedAllView = null;
    private View mNetErrorView = null;
    private View mNormalView = null;
    private RecyclerViewLoadingView mAnimationView;


    public LoadMoreFooter(Context context) {
        this(context, null);
    }

    public LoadMoreFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.footer_layout, this);
        setOnClickListener(null);
        setFooterState(FooterState.NORMAL, true);
    }

    public void setFooterState(FooterState state, boolean isShow) {
        if (state == mState) {
            return;
        }

        mState = state;

        switch (state) {
            case NORMAL:
                setOnClickListener(null);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }

                if (mLoadedAllView != null) {
                    mLoadedAllView.setVisibility(GONE);
                }

                if (mNetErrorView != null) {
                    mNetErrorView.setVisibility(GONE);
                }

                if (mNormalView == null) {
                    mNormalView = ((ViewStub) findViewById(R.id.normal_view_stub)).inflate();
                }

                mNormalView.setVisibility(isShow ? VISIBLE : GONE);
                break;
            case LOADING:
                setOnClickListener(null);
                if (mLoadedAllView != null) {
                    mLoadedAllView.setVisibility(GONE);
                }

                if (mNetErrorView != null) {
                    mNetErrorView.setVisibility(GONE);
                }

                if (mNormalView != null) {
                    mNormalView.setVisibility(GONE);
                }

                if (mLoadingView == null) {
                    mLoadingView = ((ViewStub) findViewById(R.id.loading_view_stub)).inflate();
                    mAnimationView = mLoadingView.findViewById(R.id.loading_view);
                }
                mLoadingView.setVisibility(isShow ? VISIBLE : GONE);
                mAnimationView.setVisibility(VISIBLE);
                break;
            case NO_MORE:
                setOnClickListener(null);

                if (mNetErrorView != null) {
                    mNetErrorView.setVisibility(GONE);
                }

                if (mLoadedAllView == null) {
                    mLoadedAllView = ((ViewStub) findViewById(R.id.loaded_all_view_stub)).inflate();
                }

                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }

                if (mNormalView != null) {
                    mNormalView.setVisibility(GONE);
                }

                mLoadedAllView.setVisibility(isShow ? VISIBLE : GONE);
                break;
            case NET_WORK_ERROR:
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }

                if (mLoadedAllView != null) {
                    mLoadedAllView.setVisibility(GONE);
                }

                if (mNormalView != null) {
                    mNormalView.setVisibility(GONE);
                }

                if (mNetErrorView == null) {
                    mNetErrorView = ((ViewStub) findViewById(R.id.net_error_view_stub)).inflate();
                }


                mNetErrorView.setVisibility(isShow ? VISIBLE : GONE);
                break;
            default:
                break;
        }
    }


    @Override
    public void onLoading() {
        setFooterState(FooterState.LOADING, true);
    }

    @Override
    public void onLoadComplete() {
        setFooterState(FooterState.NORMAL, true);
    }

    @Override
    public void onNoMore() {
        setFooterState(FooterState.NO_MORE, true);
    }

    @Override
    public void onReset() {
        onLoadComplete();
    }

    @Override
    public View getLoadMoreFooter() {
        return this;
    }
}
