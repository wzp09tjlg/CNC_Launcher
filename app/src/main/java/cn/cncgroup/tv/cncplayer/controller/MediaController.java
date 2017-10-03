package cn.cncgroup.tv.cncplayer.controller;

import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.Formatter;
import java.util.Locale;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.PlayerConstants;
import cn.cncgroup.tv.cncplayer.bean.VideoInfo;
import cn.cncgroup.tv.cncplayer.widget.MySeekBar;
import cn.cncgroup.tv.utils.GlobalConstant;

public class MediaController extends FrameLayout {
    private static final String TAG = "MediaController";
    private static final int PLAYER_STOP = 0;
    private static final int PLAYER_START = 1;
    private static final int HIDDEN_VIEW_SHORT = 500;
    private static final int MSG_HIDDEN_VIEW_SHORT = 3;
    private static final int INCREAMENT = 10;
    private MediaPlayerControl mPlayer;

    private static int SEEKBAR_LENGTH = 1000;
    private View mAnchor;
    private View mRoot;
    private WindowManager mWindowManager;
    private Window mWindow;
    private View mDecor;
    private WindowManager.LayoutParams mDecorLayoutParams;
    private MySeekBar mProgress;
    private TextView mEndTime, mCurrentTime, mSystemCurrentTime;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 3000;          //设置MediaController显示时间
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private AudioManager mAudioManager;
    private Context mContext;
    private ImageView mPlayerState;        //播放、暂停
    private TextView mVideoName;
    private TextView mVideoDesc;
    private TextView mVideoDpi;
    private VideoInfo mVideoInfo;
    //    private NetSpeedWidget mNetSpeed;
    private String mBaseDpi;
    private OnMenuClickListener mKeyEventListener;
    //    private TextView mContinueTips;
    private OnBackClickListener mBackListener;
    private String mCurrentDpi;
    private View mVideoTips;
    private volatile boolean isSeekOver = true;
    private boolean fromUser = false;

    public MediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = this;
        mContext = context;
        mAudioManager = (AudioManager) context
                .getSystemService(Service.AUDIO_SERVICE);
    }

    public MediaController(Context context, boolean useFastForward) {
        super(context);
        mContext = context;
        mAudioManager = (AudioManager) context
                .getSystemService(Service.AUDIO_SERVICE);
        initFloatingWindowLayout();
        initFloatingWindow();
    }

    public MediaController(Context context) {
        this(context, true);
    }

    @Override
    public void onFinishInflate() {
        if (mRoot != null)
            initControllerView(mRoot);
        super.onFinishInflate();
    }

    private void initFloatingWindow() {
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager=" + mWindowManager);
        try {
            Class clazz = Class
                    .forName("com.android.internal.policy.PolicyManager");
            Method method = clazz.getDeclaredMethod("makeNewWindow",
                    android.content.Context.class);
            mWindow = (Window) method.invoke(null, mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mWindow.setWindowManager(mWindowManager, null, null);
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mDecor = mWindow.getDecorView();
        mDecor.setOnTouchListener(mTouchListener);
        mWindow.setContentView(this);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        // While the media controller is up, the volume control keys should
        // affect the media stream type
        mWindow.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }

    /**
     * 设置播放信息
     */
    public void setVideoInfo(VideoInfo videoInfo) {
        this.mVideoInfo = videoInfo;
        updateView();
    }

    // Allocate and initialize the static parts of mDecorLayoutParams. Must
    // also call updateFloatingWindowLayout() to fill in the dynamic parts
    // (y and width) before mDecorLayoutParams can be used.
    private void initFloatingWindowLayout() {
        mDecorLayoutParams = new WindowManager.LayoutParams();
        WindowManager.LayoutParams p = mDecorLayoutParams;
        p.gravity = Gravity.TOP | Gravity.START;
        p.height = LayoutParams.MATCH_PARENT;
        p.width = LayoutParams.MATCH_PARENT;
        p.x = 0;
        p.format = PixelFormat.TRANSLUCENT;
        p.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        p.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
        p.token = null;
        p.windowAnimations = 0; // android.R.style.DropDownAnimationDown;
    }

    // Update the dynamic parts of mDecorLayoutParams
    // Must be called with mAnchor != NULL.
    private void updateFloatingWindowLayout() {
        int[] anchorPos = new int[2];
        mAnchor.getLocationOnScreen(anchorPos);

        // set the size of the controller
        mDecor.measure(MeasureSpec.makeMeasureSpec(mAnchor.getWidth(),
                MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(
                mAnchor.getHeight(), MeasureSpec.AT_MOST));
        DisplayMetrics dm = getResources().getDisplayMetrics();
        WindowManager.LayoutParams p = mDecorLayoutParams;
        p.width = dm.widthPixels;
        p.x = anchorPos[0] + (mAnchor.getWidth() - p.width) / 2;
        p.y = anchorPos[1] + mAnchor.getHeight() - mDecor.getMeasuredHeight();
    }

    // This is called whenever mAnchor's layout bound changes
    private OnLayoutChangeListener mLayoutChangeListener = new OnLayoutChangeListener() {
        public void onLayoutChange(View v, int left, int top, int right,
                                   int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            updateFloatingWindowLayout();
            if (mShowing) {
                mWindowManager.updateViewLayout(mDecor, mDecorLayoutParams);
            }
        }
    };

    private OnTouchListener mTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                {
                    hide();
                }
            }
            return false;
        }
    };

    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
    }

    /**
     * Set the view that acts as the anchor for the control view. This can for
     * example be a VideoView, or your Activity's main view. When VideoView
     * calls this method, it will use the VideoView's parent as the anchor.
     *
     * @param view The view to which to anchor the controller when it is visible.
     */
    public void setAnchorView(View view) {
        if (mAnchor != null) {
            mAnchor.removeOnLayoutChangeListener(mLayoutChangeListener);
        }
        mAnchor = view;
        if (mAnchor != null) {
            mAnchor.addOnLayoutChangeListener(mLayoutChangeListener);
        }

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    /**
     * Create the view that holds the widgets that control playback. Derived
     * classes can override this to create their own.
     *
     * @return The controller view.
     */
    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.media_controller, null);
        initControllerView(mRoot);
        return mRoot;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown:" + event);
        return super.onKeyDown(keyCode, event);
    }

    private void initControllerView(View v) {
        Log.i(TAG, "initControllerView");
        mProgress = (MySeekBar) v.findViewById(R.id.mp_progress);
        mProgress.setSeekListener(new MySeekBar.SeekListener() {
            @Override
            public void beforeSeekStart(int pos) {
                isSeekOver = false;
                fromUser = false;
            }

            @Override
            public void beforeSeekEnd(int pos) {
                fromUser = false;
                long duration = mPlayer.getDuration();
                long newposition = (duration * pos) / (long) SEEKBAR_LENGTH;
                mPlayer.seekTo((int) newposition);
                isSeekOver = true;
            }
        });
        if (mProgress != null) {
            mProgress.post(new Runnable() {
                @Override
                public void run() {
                    SEEKBAR_LENGTH = mPlayer.getDuration() / 1000;
                    mProgress.setOnSeekBarChangeListener(mSeekListener);
                    mProgress.setMax(SEEKBAR_LENGTH);
                    mProgress.setKeyProgressIncrement(INCREAMENT);
                }
            });

        }
        mPlayerState = (ImageView) v.findViewById(R.id.player_state);
        mPlayerState.setImageLevel(PLAYER_START);
        mEndTime = (TextView) v.findViewById(R.id.time);
        mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        mSystemCurrentTime = (TextView) v.findViewById(R.id.controller_currentTime);
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), GlobalConstant.DS_DIGI);
        mSystemCurrentTime.setTypeface(typeface);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mVideoName = (TextView) v.findViewById(R.id.video_name);
        mVideoTips = v.findViewById(R.id.video_tips);
        mVideoDesc = (TextView) v.findViewById(R.id.video_info);
        mVideoDpi = (TextView) v.findViewById(R.id.video_tips_before);
        if (TextUtils.isEmpty(mBaseDpi)) {
            mBaseDpi = getResources().getString(R.string.video_tips_before);
        }
        if (TextUtils.isEmpty(mCurrentDpi)) {
            mVideoTips.setVisibility(GONE);
        } else {
            mVideoTips.setVisibility(VISIBLE);
        }
        mVideoDpi.setText(mCurrentDpi);
        installPrevNextListeners();
        setSystemCurrentTime();
    }

    /**
     * Show the controller on screen. It will go away automatically after 3
     * seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
        Log.i(TAG, "Show the controller on screen:");
        updateView();
    }

    public void firstShow() {
        show(5000);
        updateView();
    }

    /**
     * Show the controller on screen. It will go away automatically after
     * 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show the controller
     *                until hide() is called.
     */
    public void show(int timeout) {
        if (PlayerConstants.SCREEN_TYPE == PlayerConstants.FRAGMENT_SMALL) return;
        if (!mShowing && mAnchor != null) {
//            setProgress();
            updateFloatingWindowLayout();
            mWindowManager.addView(mDecor, mDecorLayoutParams);
            mShowing = true;
        }
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (mAnchor == null)
            return;
        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                mWindowManager.removeView(mDecor);
            } catch (IllegalArgumentException ex) {
                Log.w("MediaController", "already removed");
            }
            mShowing = false;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing && mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
                case MSG_HIDDEN_VIEW_SHORT:
                    hide();
                    break;
            }
        }
    };

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return mFormatter.format("%02d:%02d:%02d", 0, minutes, seconds).toString();
        }
    }


    private int setProgress() {
        if (mPlayer == null||!isSeekOver) {
            return 0;
        }
        int position;
        position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (!mPlayer.isPlaying()) {
            duration = 0;
        }
        if (mProgress != null) {
            Log.i(TAG, "setProgress position="+position);
            if (duration > 0) {
                long pos = (long) SEEKBAR_LENGTH * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mEndTime != null)
            mEndTime.setText(stringForTime(mPlayer.getDuration()));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

    private void setSystemCurrentTime() {
        CharSequence charSequence = DateFormat.format("HH:mm", System.currentTimeMillis()).toString();
        if (mSystemCurrentTime != null)
            mSystemCurrentTime.setText(charSequence);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        setSystemCurrentTime();
        Log.i(TAG, "KeyEvent:" + event);
        if (PlayerConstants.SCREEN_TYPE == PlayerConstants.FRAGMENT_SMALL) {
            return super.dispatchKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (!(KeyEvent.KEYCODE_DPAD_DOWN == keyCode) && !(KeyEvent.KEYCODE_DPAD_UP == keyCode) && event.getAction() == KeyEvent.ACTION_DOWN)
            show();
        if (isShowing() && uniqueDown && keyCode == KeyEvent.KEYCODE_MENU) {
            hide();
            if (mKeyEventListener != null) {
                mKeyEventListener.onKeyClick(event);
            }
            return true;
        }
        if (uniqueDown && mKeyEventListener != null) {
            mKeyEventListener.onKeyClick(event);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && uniqueDown) {
            Log.i(TAG, "keyCode == KeyEvent.KEYCODE_VOLUME_UP");
            hide();
            if (mBackListener != null)
                mBackListener.onBackClick();
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (mProgress != null)
                mProgress.requestFocus();
        } else if ((KeyEvent.KEYCODE_DPAD_DOWN == keyCode || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) && uniqueDown) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND
                            | AudioManager.FLAG_SHOW_UI);
        } else if ((KeyEvent.KEYCODE_DPAD_UP == keyCode || keyCode == KeyEvent.KEYCODE_VOLUME_UP) && uniqueDown) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND
                            | AudioManager.FLAG_SHOW_UI);
        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) && uniqueDown) {
            doPauseResume();
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 暂停和开始播放
     */
    public void doPauseResume() {
        Log.i(TAG, "doPauseResume");
        if (mPlayer.isPlaying()) {
            mPlayerState.setImageLevel(PLAYER_STOP);
            mPlayer.pause();
            Log.i(TAG, "mPlayer.isPlaying():" + mPlayer.isPlaying());
            show();
        } else {
            mPlayerState.setImageLevel(PLAYER_START);
            mPlayer.start();
            mHandler.removeMessages(MSG_HIDDEN_VIEW_SHORT);
            Message msg = Message.obtain();
            msg.what = MSG_HIDDEN_VIEW_SHORT;
            mHandler.sendMessageDelayed(msg, HIDDEN_VIEW_SHORT);
            Log.i(TAG, "mPlayer.isPlaying():" + mPlayer.isPlaying());
        }
    }

    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {//触摸时调用
//            show(3600000);
//            mDragging = true;
//            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress,
                                      boolean fromuser) {
            Log.i(TAG, "progress=" + progress + "--fromuser=" + fromuser);
            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / (long) SEEKBAR_LENGTH;
            if (mCurrentTime != null)
                mCurrentTime.setText(stringForTime((int) newposition));
        }

        public void onStopTrackingTouch(SeekBar bar) {//触摸时调用
//            Log.i(TAG, "onStopTrackingTouch()");
//            mDragging = false;
//            setProgress();
//            show(sDefaultTimeout);
//            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }

    /**
     * 数据发生改变时更新UI
     */
    private void updateView() {
        if (mVideoInfo == null)
            return;
        if (mVideoName != null)
            mVideoName.setText(mVideoInfo.name);
        if (mVideoDesc != null)
            mVideoDesc.setText(mVideoInfo.focuse);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(MediaController.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(MediaController.class.getName());
    }

    private void installPrevNextListeners() {
    }

    public void updateBufferPercent(int percent) {
        mProgress.setSecondaryProgress(percent * 10);
    }

    public void setCurrentDefinition(final String definition) {
        if (TextUtils.isEmpty(mBaseDpi))
            mBaseDpi = getResources().getString(R.string.video_tips_before);
        mCurrentDpi = mBaseDpi.replace("{1}", definition);
    }

    public void updateUIState(boolean isPlaying) {
        if (isPlaying) {
            mPlayerState.setImageLevel(PLAYER_START);
        }
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        mKeyEventListener = listener;
    }

    public void setOnBackClickListener(OnBackClickListener listener) {
        mBackListener = listener;
    }

    //重放时更改UI
    public void rePlay() {
        if (mProgress != null)
            mProgress.setProgress(0);
    }

    public interface MediaPlayerControl {
        void start();

        void pause();

        void rePlay();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int pos);

        boolean isPlaying();

        int getBufferPercentage();
    }

    public interface OnMenuClickListener {
        void onKeyClick(KeyEvent event);
    }

    public interface OnBackClickListener {
        void onBackClick();
    }

    public interface OnValumeChangeListener {
        int RAISE = 1;
        int LOWER = -1;
        int SAME = 2;

        void onValumeChange(int direction, float streamVolume);
    }
}
