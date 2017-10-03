package cn.cncgroup.tv.cncplayer.eventbus;

import android.view.View;

/**
 * Created by zhangguang on 2015/8/28.
 */
public class FocuseEvent {
    public int num;
    public View view;
    public boolean isShow;

    public FocuseEvent(int pos,View view, boolean isShow){
        this.view = view;
        this.isShow = isShow;
        this.num = pos;
    }
}
