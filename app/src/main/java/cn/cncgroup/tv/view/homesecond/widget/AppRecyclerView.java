package cn.cncgroup.tv.view.homesecond.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import cn.cncgroup.tv.view.homesecond.HomeActivity;

/**
 * Created by zhang on 2015/11/4.
 */
public class AppRecyclerView extends RecyclerView {
    private static final String TAG = "AppRecyclerView";
    private FocuseDownListener mFocuseDownListener;
    private GridLayoutManager mLayoutManager;

    public AppRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mLayoutManager = new GridLayoutManager(context, 5, GridLayoutManager.VERTICAL, false);
        setLayoutManager(mLayoutManager);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        if (direction == RecyclerView.FOCUS_DOWN && mFocuseDownListener != null && getChildLayoutPosition(focused) > 4) {
            return mFocuseDownListener.nextFocuseDown(focused, getChildLayoutPosition(focused));
        }
        return super.focusSearch(focused, direction);
    }

    public void setFocuseDownListener(FocuseDownListener listener) {
        mFocuseDownListener = listener;
    }

    public interface FocuseDownListener {
        View nextFocuseDown(View focused, int postion);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (getFocusedChild() != null) {
                int keyCode = event.getKeyCode();
                int focusIndex = ((RecyclerView.LayoutParams) getFocusedChild().getLayoutParams()).getViewAdapterPosition();
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (focusIndex == 4) {
                        ((HomeActivity) getContext()).arrowScroll(View.FOCUS_RIGHT, true);
                        return true;
                    } else if (focusIndex == 9) {
                        ((HomeActivity) getContext()).arrowScroll(View.FOCUS_RIGHT, false);
                        return true;
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
