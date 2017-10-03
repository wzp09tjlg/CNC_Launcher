package cn.cncgroup.tv.view.homesecond;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.modle.Category;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.LogUtil;
import cn.cncgroup.tv.view.homesecond.utils.HomeTabManager;

/**
 * Created by zhang on 2015/10/28.
 */
public abstract class BaseHomeFragment extends BaseFragment {
    protected Handler mHandler = new Handler();
    private int mPageIndex;
    private Category mCategory;
    private Runnable mDataRunable = new Runnable() {
        @Override
        public void run() {
            getData();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle = getArguments();
        mPageIndex = bundle.getInt(GlobalConstant.KEY_INDEX, 0);
        mCategory = (Category) bundle.getSerializable(GlobalConstant.KEY_CATEGORY);
    }

    protected int getPageIndex() {
        return mPageIndex;
    }

    protected boolean isDefaultPage() {
        return getPageIndex() == HomeTabManager.getInstance().getDefaultIndex();
    }

    protected Category getCategory() {
        return mCategory;
    }

    abstract void arrowScroll(boolean isLeft, boolean isTop);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
//            if (mLoadingView != null && mLoadingView.isShown()) {
//                mHandler.postDelayed(mDataRunable, 500);
//            }
        } else {
            onInVisible();
            resetLayout();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (isDefaultPage()) {
        getData();
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getUserVisibleHint()) {
            onInVisible();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getUserVisibleHint()) {
            onVisible();
        }
    }

    protected void getData() {
    }

    protected void onVisible() {

    }

    protected void onInVisible() {

    }

    protected void resetLayout() {
    }
}
