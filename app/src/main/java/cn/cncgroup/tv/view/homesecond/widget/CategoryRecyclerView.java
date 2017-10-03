package cn.cncgroup.tv.view.homesecond.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import cn.cncgroup.tv.view.homesecond.HomeActivity;

/**
 * Created by zhang on 2015/10/20.
 */
public class CategoryRecyclerView extends RecyclerView {
    private View mFocusedView;

    public CategoryRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void setAdapter(final Adapter adapter) {
        super.setAdapter(adapter);
        ((GridLayoutManager) getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position);
            }
        });
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        mFocusedView = focused;
        super.requestChildFocus(child, focused);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        if (direction == View.FOCUS_RIGHT) {
            return focused;
        }
        return super.focusSearch(focused, direction);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (mFocusedView != null) {
            return mFocusedView.requestFocus(direction, previouslyFocusedRect);
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keyCode = event.getKeyCode();
            int focusIndex = ((RecyclerView.LayoutParams) getFocusedChild().getLayoutParams()).getViewAdapterPosition();
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (focusIndex == 0) {
                    ((HomeActivity) getContext()).arrowScroll(View.FOCUS_LEFT, true);
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
