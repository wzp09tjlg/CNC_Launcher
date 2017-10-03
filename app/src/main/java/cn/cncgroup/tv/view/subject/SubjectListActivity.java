package cn.cncgroup.tv.view.subject;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;

import com.squareup.okhttp.Request;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.conf.model.voole.utils.VooleURLUtil;
import cn.cncgroup.tv.modle.SubjectSet;
import cn.cncgroup.tv.modle.SubjectSetData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.ui.listener.IPageLoader;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.PageLoadUtil;

/**
 * Created by zhang on 2015/10/21.
 */
public class SubjectListActivity extends BaseActivity
        implements BaseRequest.Listener<SubjectSetData>, OnItemClickListener<SubjectSet>,
        OnItemFocusListener<SubjectSet>, IPageLoader {
    public static final String TAG = "SubjectListActivity";
    public static final int PAGE_SIZE = 24;
    private VerticalGridView mGridView;
    private SubjectListAdapter mAdapter;

    private SubjectSetData mLastData;

    private ShadowView mShadowView;
    private View mFocusedView;
    private OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mFocusedView != null) {
                mShadowView.moveTo(mFocusedView.findViewById(R.id.image));
            }
        }
    };

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_subject_list);
    }

    @Override
    protected void findView() {
        mShadowView = (ShadowView) findViewById(R.id.shadow);
        mGridView = (VerticalGridView) findViewById(R.id.gridview);
    }

    @Override
    protected void initView() {
        mGridView.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_PAGE);
        mAdapter = new SubjectListAdapter(this, this);
        mGridView.setAdapter(mAdapter);
        mShadowView.init(25);
        mGridView.addOnScrollListener(mScrollListener);
        showLoading();
        NetworkManager.getInstance().getSubjectSetList(VooleURLUtil.getSubjectSetListUrl(PAGE_SIZE, 1), this, TAG);
    }

    @Override
    public void onResponse(SubjectSetData result, boolean isFromCache) {
        if (getActivity() == null) {
            return;
        }
        if (result.getPageNo() == 1) {
            hideLoading();
        }
        mLastData = result;
        mAdapter.bindData(result);
    }

    @Override
    public void onFailure(int errorCode, Request request) {
        if (mLastData == null) {
            hideLoading();
            hideContent();
            showError();
        }
    }

    @Override
    public void onItemClickLister(View v, int p, SubjectSet set) {
        Project.get().getConfig().openSubjectDetail(this, set);
    }

    @Override
    public void onItemFocusLister(View v, int p, SubjectSet set, boolean hasFocus) {
        if (hasFocus) {
            mFocusedView = v;
            mShadowView.moveTo(mFocusedView.findViewById(R.id.image));
            PageLoadUtil.loadPage(p, PAGE_SIZE, mAdapter.getItemCount(), this);
        }
    }

    @Override
    public void loadPage(int nextPage) {
        int nextPageStart = (nextPage - 1) * PAGE_SIZE;
        if (mAdapter.getItem(nextPageStart) == null) {
            NetworkManager.getInstance().getSubjectSetList(VooleURLUtil.getSubjectSetListUrl(PAGE_SIZE, nextPage), this, TAG);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.clearImage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.restoreImage();
    }
}
