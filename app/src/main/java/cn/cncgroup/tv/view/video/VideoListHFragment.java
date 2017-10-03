package cn.cncgroup.tv.view.video;

import android.os.Bundle;
import android.widget.RelativeLayout;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.view.video.adapter.VideoListHAdapter;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * Created by Wu on 2015/9/10.
 */
public class VideoListHFragment extends BaseListFragment<VideoListHAdapter> {
    private VideoListHAdapter mAdapter;

    public static VideoListHFragment getInstance(Bundle bundle) {
        VideoListHFragment fragment = new VideoListHFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResponse(VideoSetListData result, boolean isFromCache) {
        super.onResponse(result, isFromCache);
        if (getActivity() == null) {
            return;
        }
        if (result.getResult() == null || result.getResult().getContent() == null || result.getResult().getContent().size() == 0) {
            mActivity.hideLodingView();
            showError();
            return;
        }
        mAdapter.addData(result);
    }

    @Override
    void onVisible() {
        mAdapter.restoreImage();
    }

    @Override
    void onInVisible() {
        mAdapter.clearImage();
    }

    @Override
    VideoListHAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new VideoListHAdapter(this, this, getShowType());
        }
        return mAdapter;
    }

    @Override
    void initGridView(VerticalGridView gridView) {
        gridView.setNumColumns(3);
        gridView.setHorizontalMargin(getResources().getDimensionPixelSize(R.dimen.dimen_50dp));
        gridView.setVerticalMargin(getResources().getDimensionPixelSize(R.dimen.dimen_80dp));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) gridView.getLayoutParams();
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.dimen_80dp);
        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dimen_30dp);
        gridView.setLayoutParams(params);
    }

    @Override
    public void loadPage(int nextPage) {
        int nextPageStart = (nextPage - 1) * PAGE_SIZE;
        if (mAdapter.getItem(nextPageStart) == null) {
            getData();
        }
    }
}
