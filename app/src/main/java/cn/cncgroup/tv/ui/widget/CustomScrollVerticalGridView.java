package cn.cncgroup.tv.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * Created by zhangbo on 15-9-16.
 */
public class CustomScrollVerticalGridView extends VerticalGridView {
    private boolean isFling = false;
    private int mLastAction;
    private long mLastTime;

    public CustomScrollVerticalGridView(Context context) {
        super(context);
    }

    public CustomScrollVerticalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_SETTLING)
            isFling = true;
        else
            isFling = false;
    }

    @Override
    public View focusSearch(View focused, int direction) {
        if (isFling)
            return focused;
        return super.focusSearch(focused, direction);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            if (mLastAction == KeyEvent.ACTION_DOWN) {
                if (System.currentTimeMillis() - mLastTime < 300) {
                    return true;
                }
            }
        }
        mLastTime = System.currentTimeMillis();
        mLastAction = action;
        return super.dispatchKeyEvent(event);
    }
}
