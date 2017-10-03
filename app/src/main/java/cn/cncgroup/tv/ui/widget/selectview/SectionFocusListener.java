package cn.cncgroup.tv.ui.widget.selectview;

import android.view.View;

/**
 * 第二行获得焦点事件
 */
public interface SectionFocusListener {
    void onItemFocusListener(View view, int section, boolean hasFocus);
}
