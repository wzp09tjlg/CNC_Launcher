package cn.cncgroup.tv.view.homesecond;

import android.util.Log;
import android.view.View;

import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.Iterator;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.db.DbUtil;
import cn.cncgroup.tv.db.VideoSetDao;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.utils.LogUtil;
import cn.cncgroup.tv.view.homesecond.adapter.GalleryHorizontalAdapter;
import cn.cncgroup.tv.view.homesecond.adapter.GridHorizontalAdapter;
import cn.cncgroup.tv.view.homesecond.widget.CoverFlowHorizontal;
import cn.cncgroup.tv.view.homesecond.widget.VarietyRecyclerView;

/**
 * Created by futuo on 2015/9/14.
 */
public class VarietyFragment extends BaseHomeFragment implements OnItemClickListener<VideoSet>, OnItemFocusListener<VideoSet> {
    public static final String TAG = "VarietyFragment";
    private CoverFlowHorizontal mGallery;
    private VarietyRecyclerView mGrid;
    private GalleryHorizontalAdapter mGalleryAdapter;
    private GridHorizontalAdapter mGridAdapter;
    private VideoSetListData mHotResult;
    private VideoSetListData mNewResult;
    private ShadowView mShadowView;
    private boolean firstItemisHistory;
    private Runnable mHideRunable = new Runnable() {
        @Override
        public void run() {
            mShadowView.setVisibility(View.INVISIBLE);
        }
    };
    private View mFocuseView;
    private OnItemFocusListener<VideoSet> mFocuseListener = new OnItemFocusListener<VideoSet>() {
        @Override
        public void onItemFocusLister(View view, int position, VideoSet adapter, boolean hasFocus) {
            if (hasFocus) {
                mHandler.removeCallbacks(mHideRunable);
                mShadowView.moveTo(mFocuseView);
            } else {
                mHandler.postDelayed(mHideRunable, 16);
            }
        }
    };
    private BaseRequest.Listener<VideoSetListData> mHotListener = new BaseRequest.Listener<VideoSetListData>() {
        @Override
        public void onResponse(VideoSetListData result, boolean isFromCache) {
            if (result != null && result.getResult() != null && result.getResult().getContent() != null) {
                Log.i(TAG," VarietyFragment  mHotListener");
                mHotResult = result;
                checkResult();
            }
        }

        @Override
        public void onFailure(int errorCode, Request response) {
        }
    };
    private BaseRequest.Listener<VideoSetListData> mNewListener = new BaseRequest.Listener<VideoSetListData>() {
        @Override
        public void onResponse(VideoSetListData result, boolean isFromCache) {
            if (result != null && result.getResult() != null && result.getResult().getContent() != null) {
                mNewResult = result;
                checkResult();
            }
        }

        @Override
        public void onFailure(int errorCode, Request response) {
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
        mGalleryAdapter = new GalleryHorizontalAdapter(this, mFocuseListener, isDefaultPage());
        mGallery.setAdapter(mGalleryAdapter);
        mGridAdapter = new GridHorizontalAdapter(this, this, isDefaultPage());
        mGrid.setAdapter(mGridAdapter);
        mShadowView.init(25);
        resetLayout();
    }

    @Override
    protected void getData() {
        String type = getCategory().getId();
        NetworkManager.getInstance().getHotVideoSetList(type, 14, 1, mHotListener, TAG);
        NetworkManager.getInstance().getNewVideoSetList(type, 14, 1, mNewListener, TAG);
    }

    private void checkResult() {
        if (mNewResult != null && mHotResult != null) {
            VideoSet history = getHistoryVideoSet();
            int count = 0;
            ArrayList<VideoSet> content = new ArrayList<VideoSet>();
            for (VideoSet set : mNewResult.getResult().getContent()) {
                if (history == null || !set.getName().equals(history.getName())) {
                    content.add(set);
                    if (++count >= 8) {
                        break;
                    }
                }
            }
            Iterator iter = mHotResult.getResult().getContent().iterator();
            while (iter.hasNext()) {
                VideoSet videoSet = (VideoSet) iter.next();
                if (history != null && videoSet.getName().equals(history.getName())) {
                    iter.remove();
                }
                for (VideoSet set : content) {
                    if (videoSet.getName().equals(set.getName())) {
                        iter.remove();
                    }
                }
            }
            if (history != null) {
                mNewResult.getResult().getContent().add(0, history);
            }
            mGridAdapter.bindData(mNewResult.getResult().getContent());
            Log.i(TAG,"List:"+mHotResult.getResult().getContent());
            mGalleryAdapter.bindData(mHotResult.getResult().getContent());
            mHotResult = null;
            mNewResult = null;
        }
    }

    private VideoSet getHistoryVideoSet() {
        VideoSetDao dao = DbUtil.queryByChannelId(getCategory().getId());
        VideoSet set = null;
        if (dao != null) {
            set = new VideoSet();
            set.setName(dao.name);
            set.setId(dao.videoSetId);
            set.setImage(dao.image);
            LogUtil.i(getCategory().getName() + "找到历史记录:" + dao.name);
        } else {
            LogUtil.i(getCategory().getName() + "没有找到历史记录");
        }
        return set;
    }

    @Override
    public void onItemClickLister(View view, int position, VideoSet videoSet) {
        if (videoSet == null && position == 0) {
            Project.get().getConfig().openCategory(getActivity(), getCategory());
        } else {
            Project.get().getConfig().openVideoDetail(getActivity(), videoSet);
            Log.i(TAG, "" + videoSet.getName() + videoSet.getId());
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
    public void onItemFocusLister(View view, int position, VideoSet set, boolean hasFocus) {
        if (hasFocus) {
            mHandler.removeCallbacks(mHideRunable);
            mShadowView.moveTo(view);
        } else {
            mHandler.postDelayed(mHideRunable, 16);
        }
    }
}
