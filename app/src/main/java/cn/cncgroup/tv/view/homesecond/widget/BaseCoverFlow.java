package cn.cncgroup.tv.view.homesecond.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import cn.cncgroup.tv.ui.widget.gridview.HorizontalGridView;
import cn.cncgroup.tv.view.homesecond.HomeActivity;

/**
 * Created by Administrator on 2015/11/4.
 */
public class BaseCoverFlow extends HorizontalGridView {
    private static final String TAG = "BaseCoverFlow";

    public BaseCoverFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        setStaticTransformationsEnabled(true);
        setWindowAlignment(WINDOW_ALIGN_NO_EDGE);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keyCode = event.getKeyCode();
            int focusIndex = getSelectedPosition();
            int itemCount = getAdapter().getItemCount();
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (focusIndex + 1 <= itemCount - 1) {
                    setSelectedPositionSmooth(focusIndex + 1);
                } else {
                    ((HomeActivity) getContext()).arrowScroll(View.FOCUS_RIGHT, true);
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (focusIndex - 1 >= 0) {
                    setSelectedPositionSmooth(focusIndex - 1);
                } else {
                    ((HomeActivity) getContext()).arrowScroll(View.FOCUS_LEFT, true);
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
