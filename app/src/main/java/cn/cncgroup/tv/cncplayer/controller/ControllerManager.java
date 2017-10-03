package cn.cncgroup.tv.cncplayer.controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.PlayerConstants;
import cn.cncgroup.tv.cncplayer.bean.VideoInfo;
import cn.cncgroup.tv.cncplayer.callback.NetSpeedListener;
import cn.cncgroup.tv.cncplayer.eventbus.VideoSizeEvent;
import cn.cncgroup.tv.cncplayer.widget.NetSpeedWidget;
import cn.cncgroup.tv.cncplayer.widget.QuickAccessPannel;
import cn.cncgroup.tv.cncplayer.widget.VideoSelecterPannel;
import cn.cncgroup.tv.cncplayer.widget.selectview.Constant;
import cn.cncgroup.tv.db.DbUtil;
import cn.cncgroup.tv.db.HistoryDbHelper;
import cn.cncgroup.tv.db.VideoSetDao;
import de.greenrobot.event.EventBus;

/**
 * Created by yqh on 2015/8/27.
 * .
 */
public class ControllerManager extends FrameLayout implements
        MediaController.OnMenuClickListener,
        VideoPlayerView.MediaCompleteListener, VideoPlayerView.OnVideoSizeChangedListener, IController {
    private static final String TAG = "ControllerManager";
    private static final int TIPS_GONE = -2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIPS_GONE:
                    mContinueTips.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private Context mContext;
    private View mView;
    private VideoPlayerView mVideoPlayerView;                   //播放View
    private VideoSelecterPannel mVideoSelecterPannel;   //按菜单键View
    private VideoInfo mVideoInfo;
    private NetSpeedWidget mNetSpeed;
    private TextView mContinueTips;
    private QuickAccessPannel mQuickAccess;
    private TextView mDefitionTip;

    public ControllerManager(Context context) {
        this(context,null);
    }

    public ControllerManager(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public ControllerManager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        setContentView();
    }

    @Override
    public void setUIState(boolean isOpen) {
    }

    @Override
    public void setContentView() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.videoplayer_base_controller, null);
        addView(mView);
        findView();
        init();
        enableEvent(PlayerConstants.SCREEN_TYPE != PlayerConstants.FRAGMENT_SMALL);
    }

    public void enableEvent(boolean isEnable) {
        setFocusable(isEnable);
        setClickable(isEnable);
        if (isEnable)
            requestFocus();
        mView.setFocusable(isEnable);
        mView.setClickable(isEnable);
        mVideoPlayerView.enableEvent(isEnable);
        if (!isEnable) {
            hiddenAllView();
        }
    }

    private void init() {
        mVideoPlayerView.setOnMenuClickListener(this);
        mVideoPlayerView.setOnMediaCompleteListener(this);
        mVideoPlayerView.setOnNetSpeedListener(new NetSpeedListener() {
            @Override
            public void showOrHidenSpeed(boolean isShow) {
                doShowOrHidenSpeed(isShow);
            }
        });
        mVideoPlayerView.setOnVideoSizeChangedListener(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i(TAG, "PlayerConstants.SCREEN_TYPE=" + PlayerConstants.SCREEN_TYPE + "---event:" + event);
        if (PlayerConstants.SCREEN_TYPE == PlayerConstants.FRAGMENT_SMALL) {
            if (event.getAction() == KeyEvent.ACTION_UP && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                EventBus.getDefault().post(new VideoSizeEvent(VideoSizeEvent.FULL_SCREEN));
            }
            return super.dispatchKeyEvent(event);
        }
        if (PlayerConstants.SCREEN_TYPE == PlayerConstants.FRAGMENT_FULL
                && (event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
            if (event.getAction() == KeyEvent.ACTION_UP)
                EventBus.getDefault().post(new VideoSizeEvent(VideoSizeEvent.SMALL_SCREEN));
            return true;
        }
        int keyCode = event.getKeyCode();
        boolean uniqueUp = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_UP;
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER://continue
            case KeyEvent.KEYCODE_DPAD_CENTER://
                if (mContinueTips.getVisibility() == View.VISIBLE && uniqueUp) {
                    mVideoPlayerView.rePlay();
                    mContinueTips.setVisibility(View.GONE);
                }
                break;//不return，
            case KeyEvent.KEYCODE_MENU:
                if (uniqueUp) {
                    if (mVideoSelecterPannel.getVisibility() != VISIBLE) {
                        mQuickAccess.setVisibility(GONE);
                        mVideoPlayerView.hiddenControllerView();
                    }
                    mVideoSelecterPannel.doShowOrHiden();
                }
                Log.d(TAG, "KeyEvent.KEYCODE_MENU");
                return true;
            case KeyEvent.KEYCODE_BACK:
                if (mQuickAccess.getVisibility() == VISIBLE)//退出应用
                    return super.dispatchKeyEvent(event);
                if (uniqueUp) {
                    if ((mVideoSelecterPannel.isShowing())) {
                        hiddenAllView();
                    } else {
                        hiddenAllView();
                        mQuickAccess.setVisibility(VISIBLE);
                    }
                }
                return true;
            default:
        }
        //分发事件
        if (mQuickAccess.getVisibility() == VISIBLE) {
            return mQuickAccess.dispatchKeyEvent(event);
        } else if (mVideoSelecterPannel.isShowing()) {
            return mVideoSelecterPannel.dispatchKeyEvent(event);
        }
        return mVideoPlayerView.onKeyEvent(event);
    }

    /**
     * MediaController获取焦点时点击按键的回调--单次点击UP
     */
    @Override
    public void onKeyClick(KeyEvent event) {
        Log.i(TAG, "onKeyClick from MediaController:" + event);
        int keyCode = event.getKeyCode();
        if (mContinueTips.getVisibility() == View.VISIBLE) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                mVideoPlayerView.rePlay();
            }
            mContinueTips.setVisibility(View.GONE);
        }
        if (PlayerConstants.SCREEN_TYPE == PlayerConstants.ACTIVITY_FULL) {
            if (mQuickAccess.getVisibility() != View.VISIBLE && keyCode == KeyEvent.KEYCODE_BACK) {
                mQuickAccess.setVisibility(View.VISIBLE);
            }
        }
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP)
            mVideoSelecterPannel.doShowOrHiden();
    }


    private void findView() {
        mVideoPlayerView = (VideoPlayerView) mView.findViewById(R.id.base_connectroll_VideoPlayerView);
        mVideoSelecterPannel = (VideoSelecterPannel) mView.findViewById(R.id.base_connectroll_BottomPannelView);
        mNetSpeed = (NetSpeedWidget) mView.findViewById(R.id.netspeed);
        mContinueTips = (TextView) mView.findViewById(R.id.continue_tips);
        mQuickAccess = (QuickAccessPannel) mView.findViewById(R.id.quick_access_pannel);
        mDefitionTip = (TextView) mView.findViewById(R.id.defition_tips);
    }

    /**
     * 显示或隐藏网速控件
     */
    private void doShowOrHidenSpeed(boolean isShow) {
        if (PlayerConstants.SCREEN_TYPE == PlayerConstants.FRAGMENT_SMALL) return;
        if (mNetSpeed == null)
            mNetSpeed = (NetSpeedWidget) mView.findViewById(R.id.netspeed);
        if (isShow) {
            mDefitionTip.setVisibility(GONE);
            mNetSpeed.setVisibility(View.VISIBLE);
        } else {
            mNetSpeed.setVisibility(View.GONE);
        }
    }

    @Override
    public void startPlay(VideoInfo info) {
        if (info == null) {
            return;
        }
        hiddenAllView();
        Log.i(TAG, "startPlay info:" + info);
        Log.i(TAG, "mVideoInfo:" + mVideoInfo);
        if (mVideoInfo != null && mVideoInfo.id.equals(info.id)) {
            mVideoInfo = info;
            int pos = mVideoPlayerView.getCurrentPosition();
            mVideoPlayerView.continuePlay(info.url, pos);
            return;
        }
        mVideoInfo = info;
        int time = getHistoryTime(info.videoSet.id);
        if (time != -1) {
            mNetSpeed.setVisibility(View.VISIBLE);
            mVideoPlayerView.continuePlay(info.url, time);
        } else {
            //重头播放
            mVideoPlayerView.startVideo(info.url);
        }
        mVideoPlayerView.setVideoInfo(info);
    }

    /**
     * 停止播放调用
     */
    @Override
    public void destroyPlay() {
        mVideoPlayerView.destroyVideo();
        mNetSpeed.release();
    }

    /**
     * 菜单键View设置分辨率数据
     */
    @Override
    public void setBottomPannelViewDefitionResult(ArrayList<String> defitionList) {
        for (String bean : defitionList) {
            Log.i(TAG, "DefitionBean:" + bean);
        }
        mVideoSelecterPannel.setDefitionViewResult(defitionList);
    }

    private void hiddenAllView() {
        Log.i(TAG, "hiddenAllView VideoSelecterPannel.isShowing():" + mVideoSelecterPannel.isShowing());
        if (mVideoSelecterPannel.isShowing()) {
            mVideoSelecterPannel.doShowOrHiden();
        }
        if (mQuickAccess.getVisibility() == VISIBLE) {
            mQuickAccess.setVisibility(GONE);
        }
        if (mVideoPlayerView.isShowing()) {
            mVideoPlayerView.hiddenControllerView();
        }
    }

    private void startTimer() {
        mHandler.removeMessages(TIPS_GONE);
        mHandler.sendEmptyMessageDelayed(TIPS_GONE, 3000);
    }

    /**
     * 获取历史播放时间
     */
    private int getHistoryTime(String id) {
        int time = -1;
        VideoSetDao bean = DbUtil.queryById(id);
        if (bean != null) {
            time = bean.startTime;
        }
        Log.i(TAG, "getHistoryTime VideoSetDao:" + bean);
        Log.i(TAG, "getHistoryTime time:" + time);
        return time;
    }

    /**
     * 设置当前视频分辨率
     */
    @Override
    public void setCurrentDefinition(final String definition) {
        Log.i(TAG, "setCurrentDefinition:" + definition);
        mVideoPlayerView.post(new Runnable() {
            @Override
            public void run() {
                mVideoPlayerView.setCurrentDefinition(definition);
            }
        });

    }

    /*
     * 更新历史播放记录时间
     */
    @Override
    public void updateHistory() {
        Log.i(TAG, "updateHistory");
        int time = -1;
        if (mVideoPlayerView != null)
            time = mVideoPlayerView.getCurrentPosition();
        if (mVideoInfo != null && time > 0) {
            VideoSetDao video = getVideoSetDao();
            video.startTime = time;
            HistoryDbHelper.middlePlay(video);
        }
    }

    /*
     * 视频播放完的回调
     */
    @Override
    public void onCompletion() {
        VideoSetDao video = getVideoSetDao();
        video.startTime = mVideoPlayerView.getCurrentPosition();
        HistoryDbHelper.endPlay(video);
    }

    /*
     * 根据VideoInfo获取VideoSetDao
     */
    private VideoSetDao getVideoSetDao() {
        VideoSetDao videosetDao = new VideoSetDao();
        videosetDao.videoSetId = mVideoInfo.id;
        if (mVideoInfo.videoSet.seriesType == 0) {
            videosetDao.name = mVideoInfo.videoSet.name;
        } else {
            videosetDao.name = mVideoInfo.videoSet.name + "第" + (Constant.CURRENT_INDEX + 1) + "集";
        }
        Log.i(TAG, "mVideoInfo.name=" + mVideoInfo.videoSet.name);
        videosetDao.footmark = 1;
        if (mVideoInfo.videoSet != null) {
            videosetDao.image = mVideoInfo.videoSet.getImage();
            videosetDao.episodeId = mVideoInfo.videoSet.id;
            videosetDao.channelId = mVideoInfo.videoSet.channelId;
            videosetDao.length = mVideoInfo.videoSet.getLength() * 1000;
            videosetDao.videoSetId = mVideoInfo.videoSet.id;
            videosetDao.count = Constant.CURRENT_INDEX + 1;
        }
        return videosetDao;
    }

    @Override
    public void setVedioCount(int size) {
        mVideoSelecterPannel.setVideoCount(size);
    }

    //开始播放才显示续播提示
    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.i(TAG, "onVideoSizeChanged=" + PlayerConstants.SCREEN_TYPE);
        int time = getHistoryTime(mVideoInfo.videoSet.id);
        if (time != -1) {
            Log.i(TAG, "继续历史播放");
            if (mDefitionTip.getVisibility() == VISIBLE)
                mDefitionTip.setVisibility(GONE);
            mContinueTips.setVisibility(View.VISIBLE);
            startTimer();
        }
    }

    @Override
    public void showDefitionTip() {
        mDefitionTip.setVisibility(VISIBLE);
    }
}
