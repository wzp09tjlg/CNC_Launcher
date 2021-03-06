package cn.cncgroup.tv.view.homesecond.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import cn.cncgroup.tv.view.homesecond.HomeActivity;

/**
 * Created by zhang on 2015/11/4.
 */
public class VarietyRecyclerView extends RecyclerView {

    public VarietyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        GridLayoutManager manager = new GridLayoutManager(context, 2, LinearLayoutManager.HORIZONTAL, false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        setLayoutManager(manager);
        setItemAnimator(null);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keyCode = event.getKeyCode();
            int focusIndex = ((RecyclerView.LayoutParams) getFocusedChild().getLayoutParams()).getViewAdapterPosition();
            int itemCount = getAdapter().getItemCount();
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (focusIndex == 0) {
                    findViewHolderForAdapterPosition(1).itemView.requestFocus();
                } else {
                    if (focusIndex + 2 <= itemCount - 1) {
                        findViewHolderForAdapterPosition(focusIndex + 2).itemView.requestFocus();
                    } else {
                        ((HomeActivity) getContext()).arrowScroll(View.FOCUS_RIGHT, false);
                    }
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (focusIndex == 1) {
                    findViewHolderForAdapterPosition(0).itemView.requestFocus();
                } else {
                    if (focusIndex - 2 >= 0) {
                        findViewHolderForAdapterPosition(focusIndex - 2).itemView.requestFocus();
                    } else {
                        ((HomeActivity) getContext()).arrowScroll(View.FOCUS_LEFT, false);
                    }
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
