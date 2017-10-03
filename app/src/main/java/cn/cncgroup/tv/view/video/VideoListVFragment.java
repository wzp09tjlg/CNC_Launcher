package cn.cncgroup.tv.view.video;

import android.os.Bundle;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.view.video.adapter.VideoListVAdapter;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * Created by Wu on 2015/9/9.
 */
public class VideoListVFragment extends BaseListFragment<VideoListVAdapter> {
    private VideoListVAdapter mAdapter;

    public static VideoListVFragment getInstance(Bundle bundle) {
        VideoListVFragment fragment = new VideoListVFragment();
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
    VideoListVAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new VideoListVAdapter(this, this, getShowType());
        }
        return mAdapter;
    }

    @Override
    void initGridView(VerticalGridView gridView) {
        gridView.setNumColumns(6);
        gridView.setHorizontalMargin(getResources().getDimensionPixelSize(R.dimen.dimen_56dp));
        gridView.setVerticalMargin(getResources().getDimensionPixelSize(R.dimen.dimen_56dp));
    }

    @Override
    public void loadPage(int nextPage) {
        int nextPageStart = (nextPage - 1) * PAGE_SIZE;
        if (mAdapter.getItem(nextPageStart) == null) {
            getData();
        }
    }
}
