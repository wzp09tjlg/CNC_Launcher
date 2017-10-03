package cn.cncgroup.tv.cncplayer.widget;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.PlayerConstants;

/**
 * Created by Administrator on 2015/8/25.
 */
public class NetSpeedWidget extends RelativeLayout {
    private static final String TAG = "NetSpeedWidget";
    private static final long SLEEP_TIME = 1000;
    private static final int VIEW_GONE = -2;
    private static final int VIEW_SUGGEST_SHOW = 3;
    private Typeface typeFace;       //提示框设置字体
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == VIEW_SUGGEST_SHOW) {
                mSuggest.setVisibility(VISIBLE);
            } else {
                mSpeed.setText("" + msg.obj + "/s");
            }
        }
    };
    // data
    private static boolean sFlag = true;
    private final View mView;
    // widget
    private ProgressBar mProcess;
    private TextView mSpeed;
    private TextView mSuggest;
    private Context mContext;
    private Thread mThread;
    private int UID;
    private long mLastData;

    public NetSpeedWidget(Context context) {
        this(context,null);
    }

    public NetSpeedWidget(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NetSpeedWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context.getApplicationContext();
        mView = LayoutInflater.from(context).inflate(R.layout.layout_netspeed, this);
        startView();
    }

    private void startView() {
        findView();
        initView();
    }

    private void findView() {
        mSpeed = (TextView) mView.findViewById(R.id.tv_netspeed);
        mProcess = (ProgressBar) mView.findViewById(R.id.net_process);
        mSuggest = (TextView) mView.findViewById(R.id.tv_suggest);
    }

    private void initView() {
        typeFace = Typeface.createFromAsset(mContext.getAssets(), "fzjlFont.fon");
        mSuggest.setTypeface(typeFace);
        UID = getUid();
        startSpeedThread();
    }

    private int getUid() {
        try {
            PackageManager pm = mContext.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            return ai.uid;
        } catch (Exception e) {
            Log.e("speed", e.getMessage());
        }
        return -1;
    }

    private void startSpeedThread() {
        mThread = new Thread() {
            @Override
            public void run() {
                sFlag = true;
                mLastData = TrafficStats.getUidRxBytes(UID);
                while (sFlag) {
                    Message msg = Message.obtain();
                    msg.obj = getNetSpeed();
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        Log.e("speed", "InterruptedException:" + e.getMessage());
                    }
                }
            }
        };
        mThread.start();
    }

    private String getNetSpeed() {
        long traffic_data = TrafficStats.getTotalRxBytes() - mLastData;
        mLastData = TrafficStats.getTotalRxBytes();
        return Formatter.formatFileSize(mContext, traffic_data / SLEEP_TIME * 1000);
    }

    @Override
    public void setVisibility(int visibility) {
        if (PlayerConstants.SCREEN_TYPE == PlayerConstants.FRAGMENT_SMALL) {
            super.setVisibility(GONE);
            return;
        }
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            Message msg = Message.obtain();
            msg.what = VIEW_SUGGEST_SHOW;
            mHandler.sendMessageDelayed(msg, 6 * 1000);
        } else if (visibility == View.GONE) {
            mSuggest.setVisibility(GONE);
            mHandler.removeMessages(VIEW_SUGGEST_SHOW);
        }
    }

    public void release() {
        sFlag = false;
    }

}
