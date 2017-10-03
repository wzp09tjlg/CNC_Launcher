package cn.cncgroup.tv.view.footmark;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.FilmMenuAdapter;
import cn.cncgroup.tv.adapter.VideoListMenuAdapter;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.db.DbUtil;
import cn.cncgroup.tv.ui.widget.FootmarkDialog;
import cn.cncgroup.tv.ui.widget.slidingmenu.SlidingMenu;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.view.video.VideoListMenu_voole;

/**
 * Created by jinwenchao on 2015/11/16
 */
public class FootmarkActivity extends BaseActivity {

    private VideoListMenu_voole footmarkMenu;
    private SlidingMenu mSlidingMenu;
    private ArrayList<FilmMenuAdapter.FilmMenu> mMenuList = new ArrayList<FilmMenuAdapter.FilmMenu>(); // 显示在menu的数据
    private VideoListMenuAdapter menuAdapter;
    private int mLastPosition = -1;


    private VideoListMenuAdapter.Listener<FilmMenuAdapter.FilmMenu> mMenuListener = new VideoListMenuAdapter.Listener<FilmMenuAdapter.FilmMenu>() { // 侧滑gridview的点击和光标移动回调
        @Override
        public void onMenuItemClickListener(View view, int position,
                                            FilmMenuAdapter.FilmMenu result) {
        }

        @Override
        public void onMenuItemFocusListener(View view, final int position,
                                            FilmMenuAdapter.FilmMenu s, boolean hasFocus) {
            if (hasFocus) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (position != mLastPosition) {
                            Fragment mCurrentFragment = Fragment.instantiate(FootmarkActivity.this,
                                    menuAdapter.getItem(position).fragment.getName(),
                                    menuAdapter.getItem(position).bundle);
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
    protected void setContentView() {
        setContentView(R.layout.activity_videolist_voole);

    }

    @Override
    protected void findView() {
        footmarkMenu = new VideoListMenu_voole(this, false);

    }

    @Override
    protected void initView() {
        initSlidingMenu();
        initMenuDate();
    }

    private void initMenuDate() {
        FilmMenuAdapter.FilmMenu menuCollect, menuHistory;
        Bundle collectBundle = new Bundle();
        collectBundle.putString(GlobalConstant.FOOTMARKSTRING, getResources().getString(R.string.activity_collect_title));
        menuCollect = new FilmMenuAdapter.FilmMenu(getResources().getString(R.string.activity_collect_title),
                collectBundle, CollectFragment.class);

        Bundle historyBundle = new Bundle();
        historyBundle.putString(GlobalConstant.FOOTMARKSTRING, getResources().getString(R.string.activity_history_title));
        menuHistory = new FilmMenuAdapter.FilmMenu(getResources().getString(R.string.activity_history_title),
                historyBundle, CollectFragment.class);

        mMenuList.add(menuHistory);
        mMenuList.add(menuCollect);

        menuAdapter = new VideoListMenuAdapter(mMenuList, mMenuListener);
        footmarkMenu.setMenuAdapter(menuAdapter);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mSlidingMenu.isMenuShowing()) {
                mSlidingMenu.toggle(true);
            }
            CollectFragment feetMarkFragment = (CollectFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.video_list_contain);
            feetMarkFragment.footmarkMenuClick(keyCode, event);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CollectFragment feetMarkFragment = (CollectFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.video_list_contain);
            if (isSlidingMenuShowing()) {
                mSlidingMenu.toggle(true);
                if (getResources().getString(R.string.history).equals(feetMarkFragment.titleType)) {
                    feetMarkFragment.history_main_gridview.requestFocus();
                } else {
                    feetMarkFragment.vgridview_collect_content.requestFocus();
                }
                return true;
            } else {
                if (getResources().getString(R.string.history).equals(feetMarkFragment.titleType)) {
                    if (!feetMarkFragment.isOpenOrDelete){
                        feetMarkFragment.footmarkMenuClick(keyCode, event);
                        return true;
                    }
                } else {
                    feetMarkFragment.vgridview_collect_content.requestFocus();
                    if (feetMarkFragment.isDelete){
                        feetMarkFragment.footmarkMenuClick(keyCode, event);
                        return true;
                    }
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initSlidingMenu() {
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        mSlidingMenu.setBehindOffsetRes(R.dimen.dimen_1620dp); // 侧滑Menu的宽度
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mSlidingMenu.setBehindScrollScale(1); // 设置侧滑与contain同进同出
        mSlidingMenu.setMenu(footmarkMenu);
        mSlidingMenu.setOnOpenListener(footmarkMenu);
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

    private boolean firstin = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (firstin){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSlidingMenu.showMenu(true);

                }
            }, 400);
            firstin = false;
        }else{

        }

    }

}
