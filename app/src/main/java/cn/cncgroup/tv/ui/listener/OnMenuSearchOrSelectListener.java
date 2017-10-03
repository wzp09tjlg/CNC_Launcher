package cn.cncgroup.tv.ui.listener;

import android.os.Bundle;

/**
 * Created by Wu on 2015/8/25.
 */
public interface OnMenuSearchOrSelectListener {
    int SEARCH = 1;
    int SELECT = 2;

    void onVedioSelectOrSearchClickListener(int id, Bundle bundle);
    // ID为1
    // 表示搜索
    // 2表示筛选
}
