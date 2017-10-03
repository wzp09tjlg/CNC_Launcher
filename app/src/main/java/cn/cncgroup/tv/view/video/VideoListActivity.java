package cn.cncgroup.tv.view.video;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.mobstat.StatService;
import com.squareup.okhttp.Request;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.FilmMenuAdapter;
import cn.cncgroup.tv.adapter.VideoListMenuAdapter;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.modle.Category;
import cn.cncgroup.tv.modle.FilterList;
import cn.cncgroup.tv.modle.FilterListData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.ui.listener.OnMenuSearchOrSelectListener;
import cn.cncgroup.tv.ui.widget.slidingmenu.SlidingMenu;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.view.video.VideoListSelectFragment.ConfirmSelectListener;

/**
 * Created by Wu on 2015/9/9.
 */
public class VideoListActivity extends BaseActivity implements
        OnMenuSearchOrSelectListener, ConfirmSelectListener {
    private static final String TAG = "VideoListActivity";
    private static int mSelectCount; // 记录筛选的数字,当在同一个播单选项中，记录上一次选择的筛选结果。
    private VideoListMenu_voole mVideoListMenu;
    private VideoListMenuAdapter mVideoListMenuAdapter;
    private SlidingMenu mSlidingMenu;
//    private Handler mHandler = new Handler();
    private View loadingView;
    private ArrayList<FilmMenuAdapter.FilmMenu> mMenuList = new ArrayList<FilmMenuAdapter.FilmMenu>(); // 显示在menu的数据
    private ArrayList<FilterList> mListSelect; // 显示在筛选中的数据
    // (3竖图)
    // interface
    private BaseRequest.Listener<FilterListData> mFilterListDataSelect = new BaseRequest.Listener<FilterListData>() {// 筛选接口的网络访问
        @Override
        public void onResponse(FilterListData result, boolean isFromCache) {
            showContent();
            hideLodingView();
            hideError();
            mListSelect = result.getResult();
        }

        @Override
        public void onFailure(int errorCode, Request request) {
            showError();
            hideLodingView();
            hideContent();
        }
    };
    private Category mCategory;
    private String sTypeName;
    private String mChannelId; // 频道
    private int mTypeStyle; // 1竖图 2横图
    private int mShowType; // 显示一句话简介(1竖图 -1横图) 显示名称(2竖图(主演) -2横图(电影)) 显示剧集期数
    private int mLastPosition = -1; // 最近的position
    private VideoListMenuAdapter.Listener<FilmMenuAdapter.FilmMenu> mMenuListener = new VideoListMenuAdapter.Listener<FilmMenuAdapter.FilmMenu>() { // 侧滑gridview的点击和光标移动回调
        @Override
        public void onMenuItemClickListener(View view, int position,
                                            FilmMenuAdapter.FilmMenu result) {
        }

        @Override
        public void onMenuItemFocusListener(View view, final int position,
                                            FilmMenuAdapter.FilmMenu s, boolean hasFocus) {
            if (hasFocus) {
                mSelectCount = mSelectCount + 100;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (position != mLastPosition) {
                            Fragment mCurrentFragment = Fragment.instantiate(VideoListActivity.this,
                                    mVideoListMenuAdapter.getItem(position).fragment.getName(),
                                    mVideoListMenuAdapter.getItem(position).bundle);
                            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                                    .beginTransaction();
                            transaction.replace(R.id.video_list_contain, mCurrentFragment);
                            transaction.commit();
                        }
                        mLastPosition = position;
                    }
                }, 400);
            }
        }
    };

    @Override
    public void confirmSelectListener(ArrayList<FilterList> list) {
        String url = Project.get().getConfig().getFilterResultUrl(mChannelId, list);
        Category category = new Category();
        category.setContentUrl(url);
        category.setName(sTypeName);
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.VIDEO_TYPENAME, sTypeName);
        bundle.putInt(GlobalConstant.VIDEO_SHOWTYPE, mShowType);
        bundle.putSerializable(GlobalConstant.KEY_CATEGORY, category);
        Fragment baseFragment;
        if (mTypeStyle == 1) {
            baseFragment = VideoListVFragment.getInstance(bundle);
        } else {
            baseFragment = VideoListHFragment.getInstance(bundle);
        }
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.video_list_contain, baseFragment);
        transaction.commit();
    }

    @Override
    public void onVedioSelectOrSearchClickListener(int id, Bundle bundle) {
        mVideoListMenuAdapter.clearSelect();
        if (id == OnMenuSearchOrSelectListener.SEARCH) {
            Project.get().getConfig().openSearch(this);
        } else if (id == OnMenuSearchOrSelectListener.SELECT) {
            VideoListSelectFragment.getInstance(mListSelect, this, mSelectCount)
                    .show(getSupportFragmentManager(), "");
            mLastPosition = -1; // 当焦点是有全部移动到筛选,然后再由筛选移到下来.由于播单上两次位置是相同的，并不加载数据。导致数据为空
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mSlidingMenu.isMenuShowing()) {
                mSlidingMenu.toggle(true);
            }
            VideoListSelectFragment.getInstance(mListSelect, this, mSelectCount)
                    .show(getSupportFragmentManager(), "");
            //按菜单键 弹出筛选 添加统计
            StatService.onEvent(getActivity(), "Filter_menu", getActivity().getString(R.string.filter_menu), 1);
            mLastPosition = -1;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isSlidingMenuShowing()) {
            mSlidingMenu.toggle(true);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_videolist_voole);
    }

    @Override
    protected void findView() {
        mVideoListMenu = new VideoListMenu_voole(this, true);
    }

    @Override
    protected void initView() {
        initValue();
        initNetWork();
        initSlidingMenu();
        initVideoMenu();
        for (int i = 0; i < mCategory.getData().size(); i++) {
            Category category = mCategory.getData().get(i);
            FilmMenuAdapter.FilmMenu filmMenu;
            Bundle bundle = new Bundle();
            bundle.putInt(GlobalConstant.VIDEO_SHOWTYPE, mShowType);
            bundle.putString(GlobalConstant.VIDEO_TYPENAME, sTypeName);
            category.setContentUrl(category.getContentUrl());
            bundle.putSerializable(GlobalConstant.KEY_CATEGORY, category);
            if (i == 0) {
                bundle.putString(GlobalConstant.VDEIO_CODE, "");
            } else {
                bundle.putString(GlobalConstant.VDEIO_CODE, "" + category.getId());
            }
            if (mTypeStyle == 1) { // 影视分类1
                filmMenu = new FilmMenuAdapter.FilmMenu(category.getName(),
                        bundle, VideoListVFragment.class);
            } else { // 影视分类2
                filmMenu = new FilmMenuAdapter.FilmMenu(category.getName(),
                        bundle, VideoListHFragment.class);
            }
            mMenuList.add(filmMenu);
        }
        mVideoListMenuAdapter = new VideoListMenuAdapter(mMenuList, mMenuListener);
        mVideoListMenu.setMenuAdapter(mVideoListMenuAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoListSelectFragment.clearSeletPosition();
    }

    private void initValue() {
        mCategory = (Category) getIntent().getExtras().getSerializable(
                GlobalConstant.KEY_CATEGORY);
        mChannelId = mCategory.getId();
        sTypeName = mCategory.getName();
        if (GlobalConstant.VIDEOLIST_TYPEFRAGMENT_MAP.containsKey(sTypeName)) {
            mTypeStyle = GlobalConstant.VIDEOLIST_TYPEFRAGMENT_MAP
                    .get(sTypeName) > 0 ? 1 : 0; // sTypeName对于的值 大于 0是竖图，小于0
            // 是横图
        }
        if (GlobalConstant.VIDEOLIST_TYPEFRAGMENT_MAP.containsKey(sTypeName)) {
            mShowType = GlobalConstant.VIDEOLIST_TYPEFRAGMENT_MAP
                    .get(sTypeName);
        }
    }

    private void initNetWork() {
        NetworkManager.getInstance().getTagFilter(mChannelId, mFilterListDataSelect, TAG); // 筛选的网络访问
    }

    private void initSlidingMenu() {
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        mSlidingMenu.setBehindOffsetRes(R.dimen.dimen_1620dp); // 侧滑Menu的宽度
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mSlidingMenu.setBehindScrollScale(1); // 设置侧滑与contain同进同出
        mSlidingMenu.setMenu(mVideoListMenu);
        mSlidingMenu.setOnOpenListener(mVideoListMenu);

        int dimen = (int) this.getResources().getDimension(R.dimen.dimen_120dp);
        loadingView = getLoadingView();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                dimen, dimen);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        loadingView.setLayoutParams(layoutParams);
        mSlidingMenu.addView(loadingView);
    }

    private void initVideoMenu() {
        mVideoListMenu.setSearchOrSelectListener(this);
    }

    public void doHideSlidingMenu() {
        if (mSlidingMenu.isMenuShowing())
            mSlidingMenu.toggle(true);
    }

    public boolean isSlidingMenuShowing() {
        if (mSlidingMenu.isMenuShowing())
            return true;
        else
            return false;
    }

    public void showLoadingView() {
        loadingView.setVisibility(View.VISIBLE);

    }

    private View getLoadingView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading, null);
    }

    public boolean isLoadingIsShowing() {
        if (loadingView.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }

    }

    public void hideLodingView() {
        if (loadingView != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingView.setVisibility(View.INVISIBLE);
                }
            }, 500);
        }
    }
}
