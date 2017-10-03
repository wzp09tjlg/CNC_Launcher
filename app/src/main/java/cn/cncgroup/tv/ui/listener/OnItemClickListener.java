package cn.cncgroup.tv.ui.listener;

import android.view.View;

/**
 * Created by zhang on 2015/5/5.
 */
public interface OnItemClickListener<T> {
    void onItemClickLister(View view, int position, T t);
}
