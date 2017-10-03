package cn.cncgroup.tv.cncplayer.widget.selectview;

import android.view.View;

/**
 * 第一行获得焦点事件
 */
public interface ItemFocusListener {
    void onItemFocusListener(View view, int postition, boolean hasFocus);
}
