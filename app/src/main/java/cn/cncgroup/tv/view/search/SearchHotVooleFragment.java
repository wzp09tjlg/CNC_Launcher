package cn.cncgroup.tv.view.search;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Request;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.modle.Video;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * Created by zhang on 2015/10/27.
 */
public class SearchHotVooleFragment extends BaseFragment implements BaseRequest.Listener<VideoListData>,OnItemClickListener<Video>,OnItemFocusListener<Video> {
    private static final String TAG = "SearchHotVooleFragment";
    private TextView mTitle;
    private VerticalGridView mGridView;
    private VooleRecommendAdapter mAdapter;
    private int mTotal=0;
    private ShadowView mShadowView;
    private View mFocusedView;

    @Override
    protected int getContentView() {
        return R.layout.fragment_recommend_voole;
    }

    @Override
    protected void findView(View view) {
        mTitle = (TextView) view.findViewById(R.id.title);
        mTitle.setText(getText(R.string.hot_search));
        mGridView = (VerticalGridView) view.findViewById(R.id.gridview);
        mGridView.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_PAGE);
        mShadowView = (ShadowView) view.findViewById(R.id.shadow);
    }

    @Override
    protected void initView() {
        mAdapter = new VooleRecommendAdapter(this, this);
        mGridView.setAdapter(mAdapter);
        mGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if ( mFocusedView != null) {
                    mShadowView.moveTo(mFocusedView.findViewById(R.id.image));
                }
            }
        });
        mShadowView.init(25);
        NetworkManager.getInstance().getVooleRecommendVideoList(1, this, TAG);
        initLoadingState();
    }

    @Override
    public void onResponse(VideoListData result,boolean isFromCache) {
        Log.i(TAG,"onResponse result="+result);
        if (getActivity() == null)
        {
            return;
        }
        mTotal = result.result.size();
        if (mTotal == 0)
        {
            hideLoading();
            hideContent();
            showError();
            return;
        }
        if (result.pageNo == 1)
        {
            hideLoading();
            hideError();
            showContent();
        }
        mAdapter.addData(result);
    }

    @Override
    public void onStop() {
        NetworkManager.cancelRequest(TAG);
        super.onStop();
    }

    @Override
    public void onFailure(int errorCode,Request request) {
        if (getActivity() == null)
        {
            return;
        }
        hideLoading();
        hideContent();
        showError();
    }

    @Override
    public void onItemClickLister(View view, int position, Video search) {
        Log.i("search","search="+search);
        VideoSet set = new VideoSet();
        set.setId(search.getId());
        Project.get().getConfig().openVideoDetail(getActivity(), set);
    }

    @Override
    public void onItemFocusLister(View view, int position, Video search, boolean hasFocus) {
        if (hasFocus) {
            mFocusedView = view;
            mShadowView.moveTo(view.findViewById(R.id.image));
        } else {
            mShadowView.setVisibility(View.GONE);
        }
    }
}
