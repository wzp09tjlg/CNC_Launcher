package cn.cncgroup.tv.cncplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.SeekBar;

/**
 * Created by Administrator on 2015/11/24.
 */
public class MySeekBar extends SeekBar {

    private int mIncreament = 10;
    private static final String TAG = "MySeekBar";
    private SeekListener mSeekListener;

    public MySeekBar(Context context) {
        this(context, null);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int pos ;

    @Override
    public void setVisibility(int v) {
        super.setVisibility(v);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        pos = getProgress();
        int keyCode = event.getKeyCode();
        boolean actionDown = event.getAction() == KeyEvent.ACTION_DOWN;
        Log.i(TAG, "event=" + event);
        if (event.getRepeatCount() == 3) {
            mIncreament = mIncreament * 2;
        } else if (event.getRepeatCount() == 10) {
            mIncreament = mIncreament * 5;
        }
        if (actionDown && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            pos = pos - mIncreament;
            setProgress(pos);
            mSeekListener.beforeSeekStart(pos);
            return true;
        }
        if (actionDown && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            pos = pos + mIncreament;
            setProgress(pos);
            mSeekListener.beforeSeekStart(pos);
            return true;
        }
        if (event.getAction() == KeyEvent.ACTION_UP) {
            mSeekListener.beforeSeekEnd(pos);
            mIncreament = 10;
            setKeyProgressIncrement(mIncreament);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setSeekListener(SeekListener seekListener) {
        mSeekListener = seekListener;
    }


    public interface SeekListener {
        void beforeSeekStart(int pos);
        void beforeSeekEnd(int pos);
    }
}
