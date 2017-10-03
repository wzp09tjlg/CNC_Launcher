package cn.cncgroup.tv.view.homesecond;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.network.ConnectionChangeListener;
import cn.cncgroup.tv.network.ConnectionChangeReceiver;
import cn.cncgroup.tv.network.utils.NetUtil;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.OnChildViewHolderSelectedListener;
import cn.cncgroup.tv.utils.AppInfoUtils;
import cn.cncgroup.tv.utils.DateUtil;
import cn.cncgroup.tv.utils.FocusZoomUtil;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.utils.UIUtils;
import cn.cncgroup.tv.view.homesecond.adapter.HomePagerAdapter;
import cn.cncgroup.tv.view.homesecond.adapter.HomeTabAdapter;
import cn.cncgroup.tv.view.homesecond.utils.HomeTab;
import cn.cncgroup.tv.view.homesecond.utils.HomeTabManager;
import cn.cncgroup.tv.view.homesecond.widget.FixedSpeedScroller;
import cn.cncgroup.tv.view.homesecond.widget.HomeTabContainer;
import cn.cncgroup.tv.view.setting.AutoShutdownService;
import cn.cncgroup.tv.view.weather.Bean.WeatherBean;
import cn.cncgroup.tv.view.weather.ShowApiRequest;
import cn.cncgroup.tv.view.weather.WeatherActivity;

public class HomeActivity extends BaseActivity implements
        View.OnFocusChangeListener, ConnectionChangeListener,
        OnItemClickListener<HomeTab>, OnItemFocusListener<HomeTab> {
    private static final String TAG = "HomeActivity";
    private static final int TIMETHREE = 3000;
    private static final int NOTNETWORK = -1;
    private static final int WIFI_STATE_ZERO = 0;
    private static final int WIFI_STATE_ONE = 1;
    private static final int WIFI_STATE_TWO = 2;
    private static final int WIFI_STATE_THREE = 3;
    private static final int WIRED = 4;
    private static final int NOT_NETWORK = 5;
    private static final int CHANGE_TO_WEATHER = 6;
    private static final int CHANGE_TO_NETWORK = 7;
    public ArrayList<HomeTab> mList = new ArrayList<HomeTab>();
    private RelativeLayout mLayoutHislect;
    private ImageView mImgHislect;
    private TextView mTextHislect;
    private RelativeLayout mLayoutSearch;
    private ImageView mImageViewWifiState; // wifi状态图标
    private TextView mTextViewTime; // 首页显示的时间
    private TextView mTextViewDate; // 首页显示的日期
    private SimpleDraweeView mImageViewWeatherState; // wifi状态图标
    private TextView mWeatherTime; // 首页显示的时间
    private TextView mWeatherDate; // 首页显示的日期
    private RelativeLayout rl_weather_part;
    private RelativeLayout rl_netstate_part;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WIFI_STATE_ONE:
                    DateUtil.setDete(mTextViewDate, mTextViewTime,
                            mWeatherDate, mWeatherTime);
                    mHandler.sendEmptyMessageDelayed(WIFI_STATE_ONE, 60 * 1000);
                    break;
                case CHANGE_TO_WEATHER:
                    topViewFilp(HomeActivity.this, rl_netstate_part,
                            rl_weather_part);
                    mHandler.sendEmptyMessageDelayed(CHANGE_TO_NETWORK,
                            15 * 1000);
                    break;
                case CHANGE_TO_NETWORK:
                    topViewFilp(HomeActivity.this, rl_weather_part,
                            rl_netstate_part);
                    mHandler.sendEmptyMessageDelayed(CHANGE_TO_WEATHER,
                            15 * 1000);
                    break;
            }
        }
    };
    private FocusZoomUtil mFocusZoomUtil;
    private HomeTabContainer mTitle;
    private HomeTabAdapter mTitleAdapter;
    private ViewPager mViewPager;
    private OnChildViewHolderSelectedListener mOnSelectedListener = new OnChildViewHolderSelectedListener() {
        @Override
        public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder holder, int position, int subposition) {
            mViewPager.setCurrentItem(position);
            if (mTitleAdapter.getLastViewHolder() != null) {
                mFocusZoomUtil.onItemFocused(mTitleAdapter.getLastViewHolder().itemView, false, 1.33f);
            }
            mFocusZoomUtil.onItemFocused(holder.itemView, true, 1.33f);
            mTitleAdapter.setLastViewHolder(holder);
        }
    };
    private HomePagerAdapter mPagerAdapter;
    private OnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mTitle.setSelectedPosition(position);
        }
    };
    private GlobalToast mExitToast;
    private int mBackPressCount = 0;
    private boolean isToastShowing = false;

    private ShadowView mShadowCover;
    private ShadowView mShadowView;
    private View mFocusedView;
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mFocusedView != null) {
                mShadowView.moveTo(mFocusedView);
            }
        }
    };
    private Runnable mHideRunable = new Runnable() {
        @Override
        public void run() {
            mShadowView.setVisibility(View.INVISIBLE);
        }
    };

    private Runnable mHideCoverRunable = new Runnable() {
        @Override
        public void run() {
            mShadowCover.setVisibility(View.INVISIBLE);
        }
    };

    private HomeTabManager mTabManager;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void findView() {
        Log.i(TAG, "baseApplication:" + this.getBaseContext());
        mTitle = (HomeTabContainer) findViewById(R.id.title);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mLayoutHislect = (RelativeLayout) findViewById(R.id.home_view_hisllect);
        mImgHislect = (ImageView) findViewById(R.id.home_img_hisllect);
        mTextHislect = (TextView) findViewById(R.id.home_text_hisllect);
        mLayoutSearch = (RelativeLayout) findViewById(R.id.home_view_search);
        // 网络view
        mImageViewWifiState = (ImageView) findViewById(R.id.main_wifiState);
        mTextViewTime = (TextView) findViewById(R.id.main_time);
        mTextViewDate = (TextView) findViewById(R.id.main_date);
        // 天气view
        mImageViewWeatherState = (SimpleDraweeView) findViewById(R.id.main_weather);
        mWeatherTime = (TextView) findViewById(R.id.main_weather_time);
        mWeatherDate = (TextView) findViewById(R.id.main_weather_date);
        rl_weather_part = (RelativeLayout) findViewById(R.id.rl_weather_part);
        rl_netstate_part = (RelativeLayout) findViewById(R.id.rl_netstate_part);
        mShadowView = (ShadowView) findViewById(R.id.shadow);
        mShadowCover = (ShadowView) findViewById(R.id.cover);
    }

    @Override
    public void onBackPressed() {
        if(Project.get().getConfig().isLauncher()) {
            return;
        }
        if (mExitToast == null) {
            mExitToast = GlobalToast.makeText(this, getResources().getString(R.string.exit_tip), TIMETHREE);
        }
        if (!isToastShowing) {
            mExitToast.show();
            isToastShowing = true;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hiddenToast();
                }
            }, 2000);
        }
        mBackPressCount = mBackPressCount + 1;
        Log.i(TAG, "onBackPressed mBackPressCount=" + mBackPressCount + "--mExitToast.isShowing()=" + isToastShowing);
        if (isToastShowing && mBackPressCount == 2) {
            mExitToast.cancel();
            super.onBackPressed();
        }
    }

    private void hiddenToast() {
        mBackPressCount = 0;
        mExitToast.cancel();
        isToastShowing = false;
    }

    /**
     * 初始化首页tab
     */
    private void initHomeTab() {
        mTabManager = HomeTabManager.getInstance();
        mTitleAdapter = new HomeTabAdapter(this, this, mTabManager);
        mTitle.setAdapter(mTitleAdapter);
        mPagerAdapter = new HomePagerAdapter(this, mTabManager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mTabManager.getDefaultIndex());
        mTabManager.bindHomeActivity(this);
        mTitle.requestFocus();
    }

    /**
     *定时关机服务
     */
    private void initAutoShutdown(boolean flag) {
        Preferences shutdownPref = Preferences.build(this);
        boolean isAutoSutdown = shutdownPref.getBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, false);
        if (isAutoSutdown) {
            Intent intentShutdown = new Intent(HomeActivity.this, AutoShutdownService.class);
            if (flag)
                startService(intentShutdown);
            else
                stopService(intentShutdown);
        }
    }

    /**
     * 处理刷新后焦点乱动的问题
     */
    public void resetAfterNotity() {
        mViewPager.setCurrentItem(mPagerAdapter.getCount() - 1, false);
        mTitle.setSelectedPosition(mTitleAdapter.getItemCount() - 2);
        mTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTitle.setSelectedPosition(mTitleAdapter.getItemCount() - 2);
            }
        }, 100);
    }

    @Override
    protected void initView() {
        mFocusZoomUtil = new FocusZoomUtil(FocusZoomUtil.ZOOM_FACTOR_MEDIUM);
        mLayoutHislect.setOnFocusChangeListener(this);
        mLayoutSearch.setOnFocusChangeListener(this);
        mTitle.setOnChildViewHolderSelectedListener(mOnSelectedListener);
        mViewPager.setOffscreenPageLimit(100);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        DateUtil.setDete(mTextViewDate, mTextViewTime, mWeatherDate, mWeatherTime);
        mHandler.sendEmptyMessage(WIFI_STATE_ONE);
        String weatherCity = Preferences.build(this).getString(GlobalConstant.WEATHERCITY, "");
        String weatherIP = Preferences.build(this).getString(GlobalConstant.WEBNETIP, "");
        final AsyncHttpResponseHandler resHandler = new AsyncHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                // 做一些异常处理
                e.printStackTrace();
            }

            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
//					 在这里得到了json数据以后要进行解析，然后将结果显示在UI的相应控件上
                    CApplication.weatherBean = JSON.parseObject(new String(responseBody,
                            "utf-8"), WeatherBean.class);
                    if (CApplication.weatherBean != null && CApplication.weatherBean.getShowapi_res_code() == 0) {
                        Uri[] requestUriArray = {
                                Uri.parse(CApplication.weatherBean.getShowapi_res_body()
                                        .getF1().getDay_weather_pic())};
                        UIUtils.setFrescoImageRequests(mImageViewWeatherState,
                                requestUriArray);
                    }
                    // 在此对返回内容做处理
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };

        if (TextUtils.isEmpty(weatherCity) && (!TextUtils.isEmpty(weatherIP))) {
            new ShowApiRequest(GlobalConstant.WEATHERURLIP, GlobalConstant.APPKEY,
                    GlobalConstant.APPSECRIT).setResponseHandler(resHandler)
                    .addTextPara("ip", weatherIP)
                    .addTextPara("needMoreDay", "1")
                    .addTextPara("needIndex", "0")
                    .addTextPara("needHourData", "0").post();
        } else {
            new ShowApiRequest(GlobalConstant.WEATHERURL, GlobalConstant.APPKEY,
                    GlobalConstant.APPSECRIT).setResponseHandler(resHandler)
                    .addTextPara("areaid", Preferences.build(this).getString(GlobalConstant.WEATHERCITYCODE, "101010100"))
                    .addTextPara("area", Preferences.build(this).getString(GlobalConstant.WEATHERCITY, "北京"))
                            // 参数包括0和1两个，0之请求三天，1就请求七天
                    .addTextPara("needMoreDay", "1")
                            // 是否需要返回指数数据，比如穿衣指数、紫外线指数等。1为返回，0 为不返回。
                    .addTextPara("needIndex", "0").post();
        }
        // 天气和网络动画循环
        mHandler.sendEmptyMessageDelayed(CHANGE_TO_WEATHER, 5 * 1000);
        mHandler.sendEmptyMessageDelayed(CHANGE_TO_WEATHER, 5 * 1000);
        //配置首页内容
        initHomeTab();
        //定时关机服务
        initAutoShutdown(true);
        mShadowView.init(25);
        mShadowCover.init(20);
        mTitle.addOnScrollListener(mScrollListener);
        //设置viewpager切换时间
        setScrollerTime(500);
        AppInfoUtils.getAppsData(this);
    }

    private void topViewFilp(Context context, RelativeLayout nowView, RelativeLayout newView) {
        Animation animOut = AnimationUtils.loadAnimation(context, R.anim.flip_horizontal_out);
        nowView.setAnimation(animOut);
        newView.setVisibility(View.VISIBLE);
        Animation animIn = AnimationUtils.loadAnimation(context, R.anim.flip_horizontal_in);
        newView.setAnimation(animIn);
        nowView.setVisibility(View.GONE);
    }

    @Override
    public void onFocusChange(final View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.home_view_hisllect:
                if (hasFocus) {
                    ViewGroup.LayoutParams paramView = view.getLayoutParams();
                    paramView.width = getResources().getDimensionPixelOffset(R.dimen.dimen_300dp);
                    view.setLayoutParams(paramView);
                    view.postInvalidate();
                    mImgHislect.setImageLevel(1);
                    mTextHislect.setVisibility(View.VISIBLE);
                    mHandler.removeCallbacks(mHideCoverRunable);
                    if (mFocusedView == mLayoutSearch) {
                        mShadowCover.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mShadowCover.moveTo(view);
                            }
                        }, 1);
                    } else {
                        mShadowCover.moveTo(view);
                    }
                } else {
                    ViewGroup.LayoutParams param = view.getLayoutParams();
                    param.width = getResources().getDimensionPixelOffset(R.dimen.dimen_100dp);
                    view.setLayoutParams(param);
                    view.postInvalidate();
                    mImgHislect.setImageLevel(0);
                    mTextHislect.setVisibility(View.GONE);
                    mShadowCover.post(new Runnable() {
                        @Override
                        public void run() {
                            mShadowCover.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                break;
            case R.id.home_view_search:
                if (hasFocus) {
                    mFocusedView = view;
                    mHandler.removeCallbacks(mHideCoverRunable);
                    mShadowCover.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mShadowCover.moveTo(view);
                        }
                    }, 1);
                } else {
                    mHandler.postDelayed(mHideCoverRunable, 1);
                }
                break;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_view_hisllect:
                Project.get().getConfig().openHistory(this);
                StatService.onEvent(getActivity(), "History_Collection_page", getActivity().getString(R.string.collection_page), 1);
                break;
            case R.id.home_view_search:
                Project.get().getConfig().openSearch(this);
                StatService.onEvent(getActivity(), "Search_launcher", getActivity().getString(R.string.search_launcher), 1);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mViewPager.getCurrentItem() != mTabManager.getDefaultIndex()) {
                mTitle.requestFocus();
                mTitle.setSelectedPosition(mTabManager.getDefaultIndex());
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (getCurrentFocus().getParent().equals(mTitle))
                mLayoutHislect.requestFocus();
            else if (getCurrentFocus().getId() == R.id.home_view_hisllect || getCurrentFocus().getId() == R.id.home_view_search) {
                final boolean uniqueDown = event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN;
                if (!uniqueDown)
                    return false;
                Intent intent = new Intent(this, WeatherActivity.class);
                intent.putExtra(GlobalConstant.WEATHERBEAN, CApplication.weatherBean);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.hold);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConnectionChanged(int type) {
        Log.i("TAG", "onConnectionChanged:" + type);
        if (type == NOTNETWORK) {
            mImageViewWifiState.setImageLevel(NOT_NETWORK);
        } else {
            if (type == ConnectivityManager.TYPE_WIFI) { // Wifi连接
                switch (NetUtil.getWifiLevel(getApplicationContext())) {
                    case WIFI_STATE_ZERO: // 强度一格
                        mImageViewWifiState.setImageLevel(WIFI_STATE_ZERO);
                        break;
                    case WIFI_STATE_ONE: // 强度二格
                        mImageViewWifiState.setImageLevel(WIFI_STATE_ONE);
                        break;
                    case WIFI_STATE_TWO: // 强度三格
                        mImageViewWifiState.setImageLevel(WIFI_STATE_TWO);
                        break;
                    case WIFI_STATE_THREE: // 强度四格
                        mImageViewWifiState.setImageLevel(WIFI_STATE_THREE);
                        break;
                    default:
                        mImageViewWifiState.setImageLevel(WIFI_STATE_THREE);
                }
            }
            if (type == ConnectivityManager.TYPE_ETHERNET) {
                mImageViewWifiState.setImageLevel(WIRED);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectionChangeReceiver.unregisterConnectionListenr(this);
        mHandler.removeCallbacksAndMessages(null);
        initAutoShutdown(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionChangeReceiver.registerConnectionListener(this, this);
    }

    @Override
    public void onItemClickLister(View view, int position, HomeTab tab) {
        if (tab.getCategoryByName() != null) {
            Project.get().getConfig().openCategory(getActivity(), tab.getCategoryByName());
        } else if (tab.name.equals("专题")) {
            Project.get().getConfig().openSubject(getActivity());
        } else if (tab.name.equals("+自定义")) {
            new CustomTabDialog().show(getSupportFragmentManager(), "CustomFragment");
        }
    }

    @Override
    public void onItemFocusLister(View view, int position, HomeTab tab, boolean hasFocus) {
        if (hasFocus) {
            mHandler.removeCallbacks(mHideRunable);
            mFocusedView = view;
            mShadowView.moveTo(mFocusedView);
        } else {
            mHandler.postDelayed(mHideRunable, 16);
        }
        if (tab == null) {
            return;
        }
        if (tab.name.equals("频道")) {
            RecyclerView.ViewHolder holder = mTitle.findViewHolderForAdapterPosition(mTitleAdapter.getItemCount() - 1);
            if (holder != null) {
                if (hasFocus) {
                    holder.itemView.setAlpha(1);
                } else {
                    holder.itemView.setAlpha(0);
                }
            }
        }
        if (tab.name.equals("+自定义")) {
            view.setAlpha(1);
        }
    }

    public void arrowScroll(int direction, boolean isTop) {
        int current = mViewPager.getCurrentItem();
        if (direction == View.FOCUS_RIGHT) {
            if (current + 1 < mPagerAdapter.getCount()) {
                mViewPager.setCurrentItem(current + 1);
                mTabManager.getHomeTabByIndex(current + 1).holder.arrowScroll(true, isTop);
            }
        } else if (direction == View.FOCUS_LEFT) {
            if (current - 1 >= 0) {
                mViewPager.setCurrentItem(current - 1);
                mTabManager.getHomeTabByIndex(current - 1).holder.arrowScroll(false, isTop);
            }
        }
    }

    /**
     * 设置viewpager切屏时间
     *
     * @param scrollerTime
     */
    public void setScrollerTime(int scrollerTime) {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), new DecelerateInterpolator());
            scroller.setTime(scrollerTime);
            mScroller.set(mViewPager, scroller);
        } catch (Exception e) {
        }
    }
}
