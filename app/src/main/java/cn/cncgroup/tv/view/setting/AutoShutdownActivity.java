package cn.cncgroup.tv.view.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.view.setting.adapter.AutoShutdownAdapter;
import cn.cncgroup.tv.view.setting.bean.SettingBean;

/**
 * Created by Wu on 2015/11/18.
 */
public class AutoShutdownActivity extends BaseActivity implements
        OnItemClickListener<SettingBean>,
        OnItemFocusListener<SettingBean> {
    private static final String TAG = "AutoShutdownActivity";
    private static final String TYPE = "TYPE";
    private static final String TIP_MSG = "当前默认关机时间为23:00";
    //widget
    private VerticalGridView mGrid;
    private FrameLayout mFrameLayout;
    private ShadowView mShadowCover;
    //data
    private AutoShutdownAdapter mAutoShutdownAdapter;
    private Fragment mCurrentFragment;
    private Fragment mLastFragment;
    private Preferences shutdownPref;
    private boolean isAutoShutdown;
    private String sHour;
    private String sMinute;
    private ArrayList<SettingBean> mList;
    private Runnable mHideCoverRunable = new Runnable() {
        @Override
        public void run() {
            mShadowCover.setVisibility(View.INVISIBLE);
        }
    };

    //interface
    @Override
    public void onItemClickLister(View view, int position, SettingBean bean) {
        Bundle bundle = new Bundle();
        if (!bean.isState()) {
            bundle.putBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, true);
            switchAutoShutdown(true);
            bean.setState(true);
            operateShutdownService(true);
            GlobalToast.makeText(getActivity(), TIP_MSG, 1).show();
        } else {
            bundle.putBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, false);
            bean.setState(false);
            operateShutdownService(false);
            switchAutoShutdown(false);
        }
        mCurrentFragment = Fragment.instantiate(AutoShutdownActivity.this,
                AutoShutdownFragment.class.getName(),
                bundle);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.remove(mLastFragment);
        transaction.replace(R.id.shutdown_contain, mCurrentFragment);
        mLastFragment = mCurrentFragment;
        transaction.commit();
    }

    @Override
    public void onItemFocusLister(View view, int position, SettingBean bean, boolean hasFocus) {
        if (hasFocus) {
            mHandler.removeCallbacks(mHideCoverRunable);
            mShadowCover.moveTo(view);
        } else {
            mHandler.postDelayed(mHideCoverRunable, 16);
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_auto_shutdown);
    }

    @Override
    protected void findView() {
        mGrid = (VerticalGridView) this.findViewById(R.id.shutdown_grid_menu);
        mFrameLayout = (FrameLayout) this.findViewById(R.id.shutdown_contain);
        mShadowCover = (ShadowView) this.findViewById(R.id.shutdown_cover);
        mShadowCover.init(25);
    }

    @Override
    protected void initView() {
        initPreference();
        getData();
        mAutoShutdownAdapter = new AutoShutdownAdapter(mList, this, this);
        mGrid.setAdapter(mAutoShutdownAdapter);
        mGrid.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, isAutoShutdown);
        bundle.putString(GlobalConstant.AUTO_SHUTDOWN_HOUR, sHour);
        bundle.putString(GlobalConstant.AUTO_SHUTDOWN_MINUTE, sMinute);
        mCurrentFragment = Fragment.instantiate(AutoShutdownActivity.this,
                AutoShutdownFragment.class.getName(),
                bundle);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.shutdown_contain, mCurrentFragment);
        mLastFragment = mCurrentFragment;
        transaction.commit();
    }

    private void getData() {
        mList = new ArrayList<SettingBean>();
        SettingBean bean = new SettingBean();
        bean.setName(getResources().getString(R.string.autoShutdown));
        bean.setState(isAutoShutdown);
        mList.add(bean);
    }

    private void initPreference() {
        shutdownPref = Preferences.build(this);
        isAutoShutdown = shutdownPref.getBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, false);
        sHour = shutdownPref.getString(GlobalConstant.AUTO_SHUTDOWN_HOUR, "23");
        sMinute = shutdownPref.getString(GlobalConstant.AUTO_SHUTDOWN_MINUTE, "00");
    }

    private void switchAutoShutdown(boolean flag) {
        shutdownPref = Preferences.build(this);
        shutdownPref.putBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, flag);
        shutdownPref.putString(GlobalConstant.AUTO_SHUTDOWN_HOUR, "23");
        shutdownPref.putString(GlobalConstant.AUTO_SHUTDOWN_MINUTE, "00");
    }

    private void operateShutdownService(boolean flag) {
        Intent intentShutdownService = new Intent(AutoShutdownActivity.this, AutoShutdownService.class);
        if (flag)
            startService(intentShutdownService);
        else
            stopService(intentShutdownService);
    }
}
