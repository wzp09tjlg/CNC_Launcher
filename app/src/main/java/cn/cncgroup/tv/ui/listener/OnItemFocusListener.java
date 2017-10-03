package cn.cncgroup.tv.ui.listener;

import android.view.View;

/**
 * Created by zhang on 2015/5/5.
 */
public interface OnItemFocusListener<T> {
    void onItemFocusLister(View view, int position, T t, boolean hasFocus);
}


