package cn.cncgroup.tv.cncplayer.localplayer;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.PlayerConstants;
import cn.cncgroup.tv.cncplayer.bean.VideoInfo;
import cn.cncgroup.tv.cncplayer.controller.VideoPlayerView;
import cn.cncgroup.tv.cncplayer.eventbus.PlayOverEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/9.
 * 播放本地视频
 */
public class LocalPlayerActivity extends FragmentActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = "LocalPlayerActivity";
    private static final int NO_TIMED = 0;
    private static final int TIMED_NOT_OPEN = 1;
    private static final int TIMED_OPENED = 2;
    private static final long DELAY_TIME_HIDDEN = 5000;
    private static final int WHAT_HIDDEN_MENU = 3;
    private VideoPlayerView mPlayerView;
    private String mVideoName;
    private int mHistory;
    private String mUri;
    private int mCurrentScal = 0;//当前缩放位置
    private int mCurrentTimed = NO_TIMED;//当前字幕
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_HIDDEN_MENU:
                    showOrHiddenMenu();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private View mMenuView;
    private LinearLayout mScalmenu;
    private LinearLayout mTimedmenu;
    private TextView mTimedDesc;
    private TextView mClosetimed;
    private Uri mTimedUri;
    private View mScalContener;
    private View mTimedContener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localplayer);
        EventBus.getDefault().register(this);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
        findView();
        initView();
    }

    private void findView() {
        mMenuView = findViewById(R.id.menu);
        mScalmenu = (LinearLayout) findViewById(R.id.ll_scalmenu);
        mTimedmenu = (LinearLayout) findViewById(R.id.menu_timedtext);
        mTimedDesc = (TextView) findViewById(R.id.timedtext_desc);
        mClosetimed = (TextView) findViewById(R.id.close_timed);
        mScalContener = findViewById(R.id.scale);
        mTimedContener = findViewById(R.id.timed);
    }

    private void initView() {
        int num = mScalmenu.getChildCount();
        for (int i = 0; i < num; i++) {
            View child = mScalmenu.getChildAt(i);
            child.setOnFocusChangeListener(this);
            child.setOnClickListener(this);
        }
        num = mTimedmenu.getChildCount();
        for (int i = 0; i < num; i++) {
            View child = mTimedmenu.getChildAt(i);
            child.setOnFocusChangeListener(this);
            child.setOnClickListener(this);
        }
    }

    @Override
    protected void onPause() {
        LocalPlayerHelper.getInstance(this).putHistory(mUri, mPlayerView.getCurrentPosition());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mPlayerView != null) {
            mPlayerView.destroyVideo();
        }
        super.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mMenuView.getVisibility() == View.VISIBLE) {//移除，并重新计时隐藏menu
            mHandler.removeMessages(WHAT_HIDDEN_MENU);
            mHandler.sendEmptyMessageDelayed(WHAT_HIDDEN_MENU,DELAY_TIME_HIDDEN);
        }
        final boolean actionUp = event.getAction() == KeyEvent.ACTION_UP;
        int keyCode = event.getKeyCode();
        Log.i(TAG, "mTimedmenu.getVisibility() == View.VISIBLE:" + (mTimedmenu.getVisibility() == View.VISIBLE));
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mMenuView.getVisibility() == View.VISIBLE && actionUp) {
                    showOrHiddenMenu();
                } else if (actionUp) {
                    finish();
                }
                return true;
            case KeyEvent.KEYCODE_MENU:
                if (actionUp) {
                    showOrHiddenMenu();
                }
                return true;

            default:
        }
        Log.w(TAG, "dispatchKeyEvent:" + event);
        if (mPlayerView != null && mMenuView.getVisibility() != View.VISIBLE) {
            return mPlayerView.onKeyEvent(event);
        }
        if (mMenuView.getVisibility() == View.VISIBLE) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP && actionUp) {
                showScalMenu();
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && actionUp) {
                showTimedMenu();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    //显示或隐藏全部菜单
    private void showOrHiddenMenu() {
        Log.i(TAG, "showOrHiddenMenu");
        boolean isVisibility = mMenuView.getVisibility() == View.VISIBLE;
        mMenuView.setVisibility(isVisibility ? View.GONE : View.VISIBLE);
        if (mMenuView.getVisibility() == View.VISIBLE) {
            mHandler.removeMessages(WHAT_HIDDEN_MENU);
            mHandler.sendEmptyMessageDelayed(WHAT_HIDDEN_MENU, DELAY_TIME_HIDDEN);
        }
        showScalMenu();
    }

    //显示缩放菜单
    private void showScalMenu() {
        Log.i(TAG, "showScalMenu");
        TextView tv = (TextView) mScalmenu.getChildAt(mCurrentScal);
        tv.setTextColor(getResources().getColor(R.color.localmenu_yellow));
        if (mScalmenu.getVisibility() == View.VISIBLE) {
            tv.requestFocus();
            return;
        }
        mTimedmenu.setVisibility(View.GONE);
        mTimedContener.setBackgroundColor(Color.TRANSPARENT);
        mScalmenu.setVisibility(View.VISIBLE);
        mScalContener.setBackground(getResources().getDrawable(R.drawable.localmenu_bg));
        tv.requestFocus();
    }

    //显示字幕菜单
    private void showTimedMenu() {
        Log.i(TAG, "showTimedMenu mCurrentTimed:" + mCurrentTimed);
        if (mCurrentTimed == NO_TIMED) {
            mClosetimed.setVisibility(View.GONE);
            mTimedDesc.requestFocus();
        } else if (mCurrentTimed == TIMED_NOT_OPEN) {
            mClosetimed.setVisibility(View.VISIBLE);
            mTimedDesc.requestFocus();
        } else if (mCurrentTimed == TIMED_OPENED) {
            mTimedDesc.setTextColor(getResources().getColor(R.color.localmenu_yellow));
            mClosetimed.setVisibility(View.VISIBLE);
            mClosetimed.requestFocus();
        }
        if (mTimedmenu.getVisibility() == View.VISIBLE)
            return;
        mScalmenu.setVisibility(View.GONE);
        mScalContener.setBackgroundColor(Color.TRANSPARENT);
        mTimedmenu.setVisibility(View.VISIBLE);
        mTimedContener.setBackground(getResources().getDrawable(R.drawable.localmenu_bg));
    }

    //播放视频的处理
    private void initData() {
        Intent intent = getIntent();
        mUri = intent.getDataString();
        mHistory = LocalPlayerHelper.getInstance(this).getHistory(mUri);
        mVideoName = getVideoName(mUri);
        Log.i(TAG, "uri=" + mUri + "--history time:" + mHistory);
        final VideoInfo info = new VideoInfo();
        info.focuse = mVideoName;
        info.name = "";
        info.url = mUri;
        mPlayerView = (VideoPlayerView) findViewById(R.id.base_connectroll_VideoPlayerView);
        mPlayerView.continuePlay(mUri, mHistory);
        mPlayerView.post(new Runnable() {
            @Override
            public void run() {
                mPlayerView.setVideoInfo(info);
            }
        });
        String timedPath = mUri.substring(0, mUri.lastIndexOf(".")) + ".srt";
        mTimedUri = Uri.parse(timedPath);
        File file = new File(mTimedUri.getPath());
        if (file.exists()) {
            mCurrentTimed = TIMED_OPENED;
            mPlayerView.setTimedText(mTimedUri);
            mTimedDesc.setText(R.string.timed_text);
        } else {
            mCurrentTimed = NO_TIMED;
            mTimedDesc.setText(R.string.no_timed);
            mClosetimed.setVisibility(View.GONE);
        }
    }

    private String getVideoName(String uri) {
        String result = uri;
        try {
            result = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result.substring(result.lastIndexOf("/") + 1);
    }

    public void onEventMainThread(PlayOverEvent event) {
        finish();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick:" + v);
        if (v.getId() != R.id.close_timed && v.getId() != R.id.timedtext_desc) {
            TextView tv = (TextView) mScalmenu.getChildAt(mCurrentScal);
            tv.setTextColor(getResources().getColor(R.color.localmenu_whrit));
            ((TextView) v).setTextColor(getResources().getColor(R.color.localmenu_yellow));
        }
        switch (v.getId()) {
            case R.id.close_timed: //关闭字幕
                mPlayerView.setTimedText(null);
                mCurrentTimed = TIMED_NOT_OPEN;
                mTimedDesc.setTextColor(getResources().getColor(R.color.localmenu_whrit));
                break;
            case R.id.timedtext_desc: //打开字幕
                if (mCurrentTimed == TIMED_OPENED || mCurrentTimed == NO_TIMED) return;
                mTimedDesc.setTextColor(getResources().getColor(R.color.localmenu_yellow));
                mPlayerView.setTimedText(mTimedUri);
                break;
            case R.id.no_change:
                if (mCurrentScal == 0) return;
                mPlayerView.setVideoOption(PlayerConstants.SCAL_NO_CHANGE);
                mCurrentScal = 0;
                break;
            case R.id.full_screen:
                if (mCurrentScal == 1) return;
                mPlayerView.setVideoOption(PlayerConstants.SCAL_FULL_SCREEN);
                mCurrentScal = 1;
                break;
            case R.id.w16h9:
                if (mCurrentScal == 2) return;
                mPlayerView.setVideoOption(PlayerConstants.SCAL_TYPE_W16H9);
                mCurrentScal = 2;
                break;
            case R.id.w4h3:
                if (mCurrentScal == 3) return;
                mPlayerView.setVideoOption(PlayerConstants.SCAL_TYPE_W4H3);
                mCurrentScal = 3;
                break;
            default:
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.i(TAG, "onFocusChange:" + v);
    }
}
