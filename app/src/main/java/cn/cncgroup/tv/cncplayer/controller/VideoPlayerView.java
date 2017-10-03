package cn.cncgroup.tv.cncplayer.controller;

/**
 * Created by zhangguang on 2015/8/27.
 */

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;

import cn.cncgroup.tv.cncplayer.PlayerConstants;
import cn.cncgroup.tv.cncplayer.bean.VideoInfo;
import cn.cncgroup.tv.cncplayer.bean.VideoSize;
import cn.cncgroup.tv.cncplayer.callback.NetSpeedListener;
import cn.cncgroup.tv.cncplayer.eventbus.PlayOverEvent;
import cn.cncgroup.tv.cncplayer.utils.ScaleUtil;
import cn.cncgroup.tv.cncplayer.widget.selectview.Constant;
import de.greenrobot.event.EventBus;

/**
 * 播放View
 */
public class VideoPlayerView extends SurfaceView implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener,
        MediaController.MediaPlayerControl, MediaPlayer.OnSeekCompleteListener {
    private static final String TAG = "VideoPlayerView";
    private VideoPlayerView.OnVideoSizeChangedListener mVideoSizeChangeListener;
    /**
     * 视频调起加载回调
     */
    private MediaPlayer.OnVideoSizeChangedListener mVideoSizeListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            Log.i(TAG, "onVideoSizeChanged=" + PlayerConstants.SCREEN_TYPE);
            if (mVideoSizeChangeListener != null)
                mVideoSizeChangeListener.onVideoSizeChanged(mp, width, height);
        }
    };
    // widget
    private MediaController mMediaController;
    private MediaPlayer mediaPlayer;
    private NetSpeedListener mNetSpeedListener;
    // data
    private Context mContext;
    private SufaceHolderCallback mHolderCallback;
    private MediaCompleteListener mMediaCompleteListener;
    private SurfaceHolder mSurfaceHolder;

    private int mPercent;
    private int mContinueTime;
    private int mSetBackgroundCount = 0;
    private int videoWidth, videoHeight;          //当前视频宽高
    private MediaPlayer.OnInfoListener mInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.i(TAG, "what:" + what + "--extra:" + extra);
            switch (what) {
                case MediaPlayer.MEDIA_INFO_UNKNOWN:
                    Log.i(TAG, "MEDIA_INFO_UNKNOWN");
                    break;
                case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    Log.i(TAG, "MEDIA_INFO_NOT_SEEKABLE");
                    break;
                case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    Log.i(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING");
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:// 缓存不够，暂停播放
                    Log.i(TAG, " 缓存不够，暂停播放");
                    showNetSpeed(true);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:// 缓存足够播放
                    Log.i(TAG, " 缓存足够播放");
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                    if (mSetBackgroundCount == 0) {
                        mSetBackgroundCount++;
                    }
                    mMediaController.updateUIState(true);
                    showNetSpeed(false);
                    break;
            }
            return false;
        }
    };
    private String mCurrentUrl;
    private String mTimedTextMimetype;
    private Uri mTimedTextUri;

    public VideoPlayerView(Context context) {
        super(context);
        this.mContext = context;
        setContentView();
    }

    public VideoPlayerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        setContentView();
    }

    public VideoPlayerView(Context context, AttributeSet attributeSet,
                           int defStyle) {
        super(context, attributeSet, defStyle);
        setContentView();
    }

    private void setContentView() {
        initView();
    }

    private void initView() {
        mSurfaceHolder = getHolder();
        mediaPlayer = new MediaPlayer();
        mMediaController = new MediaController(mContext);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(mInfoListener);
        mediaPlayer.setOnVideoSizeChangedListener(mVideoSizeListener);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setScreenOnWhilePlaying(true);
        mHolderCallback = new SufaceHolderCallback(mediaPlayer, mSurfaceHolder);
        mSurfaceHolder.addCallback(mHolderCallback);
    }

    private void setTimedText() throws IOException {
        if (mTimedTextUri == null || mTimedTextMimetype == null)
            return;
        try {
            mediaPlayer.addTimedTextSource(mContext, mTimedTextUri, MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
            MediaPlayer.TrackInfo[] trackInfos = mediaPlayer.getTrackInfo();
            if (trackInfos != null && trackInfos.length > 0) {
                for (int i = 0; i < trackInfos.length; i++) {
                    final MediaPlayer.TrackInfo info = trackInfos[i];
                    Log.w(TAG, "TrackInfo: " + info.getTrackType() + " "
                            + info.getLanguage());
                    if (info.getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
                        mediaPlayer.selectTrack(i);
                    } else if (info.getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT) {
                        mediaPlayer.selectTrack(i);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "timedtext--mTimedTextUri:" + mTimedTextUri);
    }

    public void setOnMediaCompleteListener(MediaCompleteListener listener) {
        mMediaCompleteListener = listener;
    }

    public void setOnNetSpeedListener(NetSpeedListener listener) {
        mNetSpeedListener = listener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i(TAG, "dispatchKeyEvent:" + event);
        return super.dispatchKeyEvent(event);
    }

    /**
     * 开始播放调用
     */
    public void startVideo(String url) {
        continuePlay(url, -1);
    }

    //Media server dead, release the
    // MediaPlayer object and instantiate a new one.
    private void resetPlay(MediaPlayer mp) {
        mp.release();
        initView();
        continuePlay(mCurrentUrl, mContinueTime);
    }

    /**
     * 继续历史播放
     */
    public void continuePlay(String url, int time) {
        mCurrentUrl = url;
        mContinueTime = time;
        if (mediaPlayer == null) return;
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(mContext, Uri.parse(url));
        } catch (Exception e) {
            Log.e(TAG, "startVideo setDataSource:" + e.toString());
        }
        mediaPlayer.prepareAsync();
    }

    public void hiddenControllerView() {
        mMediaController.hide();
    }

    /**
     * 结束播放调用（在Activity中Destroy调用）
     */
    public void destroyVideo() {
        mSurfaceHolder.removeCallback(mHolderCallback);
        mMediaController = null;
        mediaPlayer.release();
        mediaPlayer = null;
    }

    /**
     * 视频准备播放后回调
     */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.i(TAG, "onPrepared");
        VideoPlayerView.this.setBackgroundColor(Color.TRANSPARENT);
        mMediaController.setMediaPlayer(this);
        mMediaController.setAnchorView(this);
        if (PlayerConstants.SCREEN_TYPE == PlayerConstants.ACTIVITY_FULL) {
            videoWidth = mediaPlayer.getVideoWidth();
            videoHeight = mediaPlayer.getVideoHeight();
            VideoSize videoSize = ScaleUtil.getMatchScreenSize(mContext, videoWidth, videoHeight);
            Log.i(TAG, "VideoHeight:" + videoHeight + "--VideoWidth:" + videoWidth);
//            DisplayMetrics dm = getResources().getDisplayMetrics();
//            int w_screen = dm.widthPixels;
//            int h_screen = dm.heightPixels;
//            if (videoWidth != w_screen && videoHeight != h_screen) {
//                VideoSize size = ScaleUtil.getMatchScreenSize(mContext, mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
//                videoWidth = size.width;
//                videoHeight = size.height;
//            }
//            Log.i(TAG, "videoWidth=" + videoWidth + "---videoHeight=" + videoHeight);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(videoSize.width, videoSize.height);
            params.gravity = Gravity.CENTER;
            setLayoutParams(params);
        }
        mediaPlayer.start();
        if (mContinueTime > 0) {
            mediaPlayer.seekTo(mContinueTime);
            mMediaController.firstShow();
        } else {
            mMediaController.show();
        }
        try {
            setTimedText();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTimedText(Uri uri) {
        mTimedTextUri = uri;
        mTimedTextMimetype = "application/x-subrip";
        Log.i(TAG, "setTimedText--uri:" + uri);
    }

    /**
     * 单个视频播放完成后回调
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mMediaCompleteListener != null)
            mMediaCompleteListener.onCompletion();
        Log.i(TAG, "onCompletion  单个视频播放完");
        EventBus.getDefault().post(new PlayOverEvent(Constant.CURRENT_INDEX));
    }

    /**
     * 视频缓冲回调
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
        mPercent = percent;
        if (mMediaController.isShowing()) {
            mMediaController.updateBufferPercent(percent);
        }
    }

    /**
     * 视频异常回调
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "MediaPlayer onError:" + mp + "---what:" + what + "---extra:" + extra);
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.e(TAG, "Media server died");
                resetPlay(mp);
                break;
        }
        return true;
    }

    private void showNetSpeed(boolean isShow) {
        if (mNetSpeedListener != null) {
            mNetSpeedListener.showOrHidenSpeed(isShow);
        }
    }

    public boolean isShowing() {
        if (mMediaController == null) {
            return false;
        } else {
            return mMediaController.isShowing();
        }

    }

    /**************
     * 播控的内容
     *****************/

    /*
     * 播放
     */
    @Override
    public void start() {
        Log.i(TAG, " mediaPlayer.start()");
        mediaPlayer.start();
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    /**
     * 重头开始播放
     */
    @Override
    public void rePlay() {
        Log.i(TAG, "rePlay");
        if (mediaPlayer.isPlaying()) {
            mContinueTime = 0;
            mediaPlayer.seekTo(0);
            mMediaController.rePlay();
        }
    }

    /**
     * 视频总长
     */
    @Override
    public int getDuration() {
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getDuration();
    }

    /**
     * 当前播放位置
     */
    @Override
    public int getCurrentPosition() {
        if (mediaPlayer == null) {
            return 0;
        }
        if (mediaPlayer.getDuration() <= mediaPlayer.getCurrentPosition()) {
            return mediaPlayer.getDuration();
        } else {
            return mediaPlayer.getCurrentPosition();
        }
    }

    /**
     * 设置播放位置
     */
    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    /**
     * 是否正在播放
     */
    @Override
    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.isPlaying();
    }

    /**
     * 获取缓冲百分比
     */
    @Override
    public int getBufferPercentage() {
        return mPercent;
    }

    public boolean onKeyEvent(KeyEvent event) {
        Log.i(TAG, "onKeyEvent:" + event);
        return mMediaController.dispatchKeyEvent(event);
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        Log.d(TAG, "videoInfo:" + videoInfo);
        if (mMediaController != null)
            mMediaController.setVideoInfo(videoInfo);
    }

    public void setCurrentDefinition(final String definition) {




        if(mMediaController==null) return;
        mMediaController.post(new Runnable() {
            @Override
            public void run() {
                if (mMediaController != null)
                    mMediaController.setCurrentDefinition(definition);
            }
        });

    }

    public void setOnMenuClickListener(MediaController.OnMenuClickListener listener) {
        mMediaController.setOnMenuClickListener(listener);
    }


    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.i(TAG, "onSeekComplete");
    }

    public void setVideoOption(int scalType) {
        VideoSize size = null;
        if (mediaPlayer == null)
            return;
        Log.i(TAG, "setVideoOption 原始大小：w,h:" + mediaPlayer.getVideoWidth() + "," + mediaPlayer.getVideoHeight());
        switch (scalType) {
            case PlayerConstants.SCAL_TYPE_W4H3:
                size = ScaleUtil.getVideoSizeW4H3(mContext, mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
                break;
            case PlayerConstants.SCAL_TYPE_W16H9:
                size = ScaleUtil.getVideoSizeW16H9(mContext, mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
                break;
            case PlayerConstants.SCAL_FULL_SCREEN:
                size = ScaleUtil.getVideoSizeFullScreen(mContext);
                break;
            case PlayerConstants.SCAL_NO_CHANGE:
                size = ScaleUtil.getMatchScreenSize(mContext, mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
                break;
        }
        if (size != null) {
            Log.i(TAG, "size.width, size.height:" + size.width + "," + size.height + "----");
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size.width, size.height);
            params.gravity = Gravity.CENTER;
            setLayoutParams(params);
        }
    }

    //视频大小改变
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        mVideoSizeChangeListener = listener;
    }

    public void enableEvent(boolean isEnable) {
        Log.i(TAG, "enableEvent isEnable=" + isEnable);
        setFocusable(isEnable);
        setClickable(isEnable);
    }

    interface MediaCompleteListener {
        void onCompletion();
    }

    class SufaceHolderCallback implements SurfaceHolder.Callback {
        //        private static final String TAG = "SufaceHolderCallback";
        private MediaPlayer mPlayer = null;
        private SurfaceHolder mHolder = null;

        public SufaceHolderCallback(MediaPlayer player, SurfaceHolder holder) {
            this.mPlayer = player;
            this.mHolder = holder;
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.i(TAG, "surfaceCreated");
            mPlayer.setDisplay(mHolder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            Log.i(TAG, "surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.i(TAG, "surfaceDestroyed");
        }
    }

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(MediaPlayer mp, int width, int height);
    }
}
