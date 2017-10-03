package cn.cncgroup.tv.view.video;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Request;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.modle.Category;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.ui.listener.IPageLoader;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.PageLoadUtil;

/**
 * Created by zhang on 2015/11/13.
 */
public abstract class BaseListFragment<T extends RecyclerView.Adapter> extends BaseFragment
        implements OnItemFocusListener<VideoSet>, OnItemClickListener<VideoSet>,
        BaseRequest.Listener<VideoSetListData>, IPageLoader {
    protected static final String TAG = "BaseListFragment";
    protected static final int PAGE_SIZE = 24;
    protected VideoListActivity mActivity;
    protected VideoSetListData mLastData;
    private int mShowType;
    private String mShowName;
    private Category mCategory;
    private T mAdapter;
    private TextView mTextType;
    private VerticalGridView mGrid;
    private View mArrow;
    private ShadowView mShadowView;
    private View mFocusedView;

    @Override
    public void onResponse(VideoSetListData result, boolean isFromCache) {
        if (mActivity.isLoadingIsShowing()) {
            mActivity.hideLodingView();
        }
        mLastData = result;
    }

    @Override
    public void onFailure(int errorCode, Request request) {
        mActivity.hideLodingView();
        showError();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (VideoListActivity) activity;
        Bundle bundle = getArguments();
        mCategory = (Category) bundle.getSerializable(GlobalConstant.KEY_CATEGORY);
        mShowType = bundle.getInt(GlobalConstant.VIDEO_SHOWTYPE, 1);
        mShowName = bundle.getString(GlobalConstant.VIDEO_TYPENAME, getResources().getString(R.string.film));
    }

    @Override
    public void onStop() {
        super.onStop();
        onInVisible();
        NetworkManager.cancelRequest(TAG);
    }

    @Override
    public void onStart() {
        super.onStart();
        onVisible();
    }

    abstract void onVisible();

    abstract void onInVisible();

    abstract T getAdapter();

    abstract void initGridView(VerticalGridView gridView);

    protected Category getCategory() {
        return mCategory;
    }

    protected String getShowName() {
        return mShowName;
    }

    protected int getShowType() {
        return mShowType;
    }

    @Override
    public void onItemClickLister(View view, int position, VideoSet videoSet) {
        if (mCategory.getName().equals("综艺")) {
            if (videoSet.total == 1) {        //等于1单剧集，直接播放
                Project.get().getConfig().playVideo(getActivity(), videoSet.id, videoSet.id, "" + 1, 0, videoSet.getName(), videoSet.getImage(), videoSet.getChannelId());
            } else {
                Project.get().getConfig().openVideoDetail(getActivity(), videoSet);
            }
        } else {
            Project.get().getConfig().openVideoDetail(getActivity(), videoSet);
        }
    }

    protected void getData() {
        String url = mLastData == null ? getCategory().getContentUrl() : mLastData.getNextPageUrl();
        NetworkManager.getInstance().getVideoSetList(url, this, TAG, PAGE_SIZE);
    }


    @Override
    public void onItemFocusLister(View view, int position, VideoSet videoSet, boolean hasFocus) {
        if (hasFocus) {
            mFocusedView = view;
            mShadowView.moveTo(view.findViewById(R.id.image));
            PageLoadUtil.loadPage(position, PAGE_SIZE, mAdapter.getItemCount(), this);
            if (position % 6 == 0 || position % 6 == 1) {
                mArrow.setVisibility(View.VISIBLE);
            }
            mActivity.doHideSlidingMenu();
        } else {
            mShadowView.setVisibility(View.GONE);
            if (position % 6 == 0 || position % 6 == 1)
                mArrow.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_video_list;
    }

    @Override
    protected void findView(View view) {
        mTextType = (TextView) view.findViewById(R.id.type);
        mGrid = (VerticalGridView) view.findViewById(R.id.grid);
        mShadowView = (ShadowView) view.findViewById(R.id.shadow);
        mArrow = view.findViewById(R.id.arrow);
    }

    @Override
    protected void initView() {
        mActivity.showLoadingView();
        mTextType.setText(getShowName());
        mGrid.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_PAGE);
        initGridView(mGrid);
        mAdapter = getAdapter();
        mGrid.setAdapter(mAdapter);
        if (!mActivity.isSlidingMenuShowing()) {
            mGrid.requestFocus();
        }
        mShadowView.init(25);
        mGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mFocusedView != null && mGrid.getFocusedChild() != null) {
                    mShadowView.moveTo(mFocusedView.findViewById(R.id.image));
                }
            }
        });
        getData();
    }
}
