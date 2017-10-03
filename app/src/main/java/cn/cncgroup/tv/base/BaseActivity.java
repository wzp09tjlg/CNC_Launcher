package cn.cncgroup.tv.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.baidu.mobstat.StatService;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.view.setting.SettingBootCustomActivity;

/**
 * Created by zhang on 2015/4/27. Activity基类
 */
public abstract class BaseActivity extends FragmentActivity {
    private static final int DURATION = 150;
    protected Handler mHandler = new Handler();
    private CApplication mCApplication;
    private FrameLayout mContainer;
    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mCApplication = (CApplication) getApplication();
        mCApplication.addActivity(this);
        setContentView();
        addLoadingAndError();
        findView();
        initView();
    }

    @Override
    protected void onResume() {
        initCustomTheme();
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    protected abstract void setContentView();

    protected abstract void findView();

    protected abstract void initView();

    private void addLoadingAndError() {
        mContainer = (FrameLayout) findViewById(android.R.id.content);
        mContentView = mContainer.getChildAt(0);
        mLoadingView = getLoadingView();
        mErrorView = getErrorView();
        mContainer.addView(mLoadingView);
        mContainer.addView(mErrorView);

    }

    public void initLoadingState() {
        mLoadingView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
    }

    private View getLoadingView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading, mContainer, false);
    }

    private View getErrorView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading_error, mContainer, false);
    }

    protected BaseActivity getActivity() {
        return this;
    }

    private void initCustomTheme() {
        setBackgroundResource(R.drawable.recommend_back);
    }

    public void setBackgroundResource(int res) {
        getWindow().getDecorView().setBackgroundResource(res);
    }

    public void gotoActivity(Class<? extends Activity> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void gotoActivity(Class<? extends Activity> cls) {
        gotoActivity(cls, null);
    }

    public void gotoActivityWithFinish(Class<? extends Activity> cls,
                                       Bundle bundle) {
        gotoActivity(cls, bundle);
        finish();
    }

    public void gotoActivityWithFinish(Class<? extends Activity> cls) {
        gotoActivityWithFinish(cls, null);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mCApplication.removeActivity(this);
        mCApplication = null;
        System.gc();
    }

    /**
     * 显示全局的提示框
     *
     * @param msg         提示内容
     * @param displayTime 提示框显示时间(单位秒)
     */
    public void showToast(String msg, int displayTime) {
        GlobalToast.makeText(mContext, msg, displayTime).show();
    }

    public void hindToast() {
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
            view.animate().alpha(0f).setDuration(DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            view.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //RK的系统点击菜单键后好像有一个透明的菜单，然后焦点就不起作用了
        if (keyCode == KeyEvent.KEYCODE_MENU) {
//            gotoActivity(SettingBootCustomActivity.class);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
