package cn.cncgroup.tv.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.utils.Utils;

/**
 * Created by zhang on 2015/5/29.
 */
public abstract class BaseFragment extends Fragment {
    private static final int DURATION = 150;
    private static final String TAG = "BaseFragment";
    protected FrameLayout mContainer;
    protected View mContentView;
    protected View mLoadingView;
    protected View mErrorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContainer = getContainer();
        mContentView = inflater.inflate(getContentView(), container, false);
        mLoadingView = getLoadingView();
        mErrorView = getErrorView();
        mContainer.addView(mContentView);
        mContainer.addView(mLoadingView);
        mContainer.addView(mErrorView);
        return mContainer;
    }

    private View getLoadingView() {
        return LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.loading, mContainer, false);
    }

    private View getErrorView() {
        return LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.loading_error, mContainer, false);
    }

    private FrameLayout getContainer() {
        return new FrameLayout(getActivity().getApplicationContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        findView(view);
        initView();
    }

    protected abstract int getContentView();

    protected abstract void findView(View view);

    protected abstract void initView();

    public void gotoActivity(Class<? extends Activity> cls, Bundle bundle) {
        Utils.startActivity(getActivity(), cls, bundle);
    }

    public void gotoActivity(Class<? extends Activity> cls) {
        gotoActivity(cls, null);
    }

    public void gotoActivityWithFinish(Class<? extends Activity> cls,
                                       Bundle bundle) {
        gotoActivity(cls, bundle);
        getActivity().finish();
    }

    public void gotoActivityWithFinish(Class<? extends Activity> cls) {
        gotoActivityWithFinish(cls, null);
    }

    protected void initLoadingState() {
        mLoadingView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
    }

    /**
     * 显示全局的提示框
     *
     * @param msg         提示内容
     * @param displayTime 提示框显示时间(单位秒)
     */
    public void showToast(String msg, int displayTime) {
        Log.i(TAG,"getActivity().getActivity().getApplicationContext()()="+getActivity().getApplicationContext());
        GlobalToast.makeText(getActivity(), msg, displayTime).show();
    }

    public void hindToast() {
        Log.i(TAG, "BaseFragment hindToast()");
        GlobalToast.cancel();
    }

    public void showContent() {
        showView(mContentView);
    }

    public void hideContent() {
        hideView(mContentView);
    }

    public void showLoading() {
        showView(mLoadingView);
    }

    public void hideLoading() {
        hideView(mLoadingView);
    }

    public void showError() {
        showView(mErrorView);
    }

    public void hideError() {
        hideView(mErrorView);
    }

    private void showView(View view) {
        if (view != null && view.getVisibility() != View.VISIBLE) {
            view.setAlpha(0f);
            view.setVisibility(View.VISIBLE);
            view.animate().alpha(1f).setDuration(DURATION).setListener(null);
        }
    }

    private void hideView(final View view) {
        if (view != null && view.getVisibility() != View.GONE) {
            view.animate().alpha(0f).setDuration(DURATION).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                }
            });
        }
    }

}
