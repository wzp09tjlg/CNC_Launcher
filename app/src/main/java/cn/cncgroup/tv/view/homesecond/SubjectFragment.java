package cn.cncgroup.tv.view.homesecond;

import android.view.View;

import com.squareup.okhttp.Request;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.conf.model.voole.utils.VooleURLUtil;
import cn.cncgroup.tv.modle.SubjectSet;
import cn.cncgroup.tv.modle.SubjectSetData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.view.homesecond.adapter.GallerySubjectAdapter;
import cn.cncgroup.tv.view.homesecond.adapter.GridSubjectAdapter;
import cn.cncgroup.tv.view.homesecond.widget.CoverFlowHorizontal;
import cn.cncgroup.tv.view.homesecond.widget.VarietyRecyclerView;

/**
 * Created by zhang on 2015/11/12.
 */
public class SubjectFragment extends BaseHomeFragment implements OnItemClickListener<SubjectSet>,
        OnItemFocusListener<SubjectSet>, BaseRequest.Listener<SubjectSetData> {
    public static final String TAG = "SubjectFragment";
    private CoverFlowHorizontal mGallery;
    private VarietyRecyclerView mGrid;
    private GallerySubjectAdapter mGalleryAdapter;
    private GridSubjectAdapter mGridAdapter;
    private ShadowView mShadowView;
    private Runnable mHideRunable = new Runnable() {
        @Override
        public void run() {
            mShadowView.setVisibility(View.INVISIBLE);
        }
    };
    private View mFocuseView;
    private OnItemFocusListener<SubjectSet> mFocuseListener = new OnItemFocusListener<SubjectSet>() {
        @Override
        public void onItemFocusLister(View view, int position, SubjectSet set, boolean hasFocus) {
            if (hasFocus) {
                mHandler.removeCallbacks(mHideRunable);
                mShadowView.moveTo(mFocuseView);
            } else {
                mHandler.postDelayed(mHideRunable, 16);
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.fragment_variety;
    }

    @Override
    protected void findView(View view) {
        mGallery = (CoverFlowHorizontal) view.findViewById(R.id.gallery);
        mGrid = (VarietyRecyclerView) view.findViewById(R.id.grid);
        mShadowView = (ShadowView) view.findViewById(R.id.shadow);
        mFocuseView = view.findViewById(R.id.coverflow_focuseview);
    }

    @Override
    protected void initView() {
        mGalleryAdapter = new GallerySubjectAdapter(this, mFocuseListener, isDefaultPage());
        mGallery.setAdapter(mGalleryAdapter);
        mGridAdapter = new GridSubjectAdapter(this, this, isDefaultPage());
        mGrid.setAdapter(mGridAdapter);
        mShadowView.init(25);
        resetLayout();
    }

    @Override
    protected void getData() {
        NetworkManager.getInstance().getSubjectSetList(VooleURLUtil.getSubjectSetListUrl(14, 1), this, TAG);
    }

    @Override
    public void onItemClickLister(View view, int position, SubjectSet subject) {
        if (subject == null && position == 0) {
            Project.get().getConfig().openSubject(getActivity());
        } else {
            Project.get().getConfig().openSubjectDetail(getActivity(), subject);
        }
    }

    @Override
    void arrowScroll(boolean isLeft, boolean isTop) {
        if (isTop) {
            mGallery.requestFocus();
        } else {
            if (isLeft) {
                mGrid.findViewHolderForAdapterPosition(0).itemView.requestFocus();
            } else {
                mGrid.findViewHolderForAdapterPosition(7).itemView.requestFocus();
            }
        }
    }

    @Override
    protected void resetLayout() {
        if (mGallery != null) {
            mGallery.setSelectedPosition(2);
        }
    }

    @Override
    protected void onVisible() {
        if (mGalleryAdapter != null && mGridAdapter != null) {
            mGalleryAdapter.restoreImage();
            mGridAdapter.restoreImage();
        }
    }

    @Override
    protected void onInVisible() {
        if (mGalleryAdapter != null && mGridAdapter != null) {
            mGalleryAdapter.clearImage();
            mGridAdapter.clearImage();
        }
    }

    @Override
    public void onItemFocusLister(View view, int position, SubjectSet set, boolean hasFocus) {
        if (hasFocus) {
            mHandler.removeCallbacks(mHideRunable);
            mShadowView.moveTo(view);
        } else {
            mHandler.postDelayed(mHideRunable, 16);
        }
    }

    @Override
    public void onResponse(SubjectSetData result, boolean isFromCache) {
        mGridAdapter.bindData(result.getResult().subList(0, 8));
        mGalleryAdapter.bindData(result.getResult().subList(8, 14));
    }

    @Override
    public void onFailure(int errorCode, Request response) {

    }
}
