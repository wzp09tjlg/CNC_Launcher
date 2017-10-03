package cn.cncgroup.tv.cncplayer.bean;

import android.view.View;

/**
 * Created by Administrator on 2015/8/31.
 */
public class FocuseTipInfo {
    public View view;
    public int pos;
    public boolean isShow;
    public FocuseTipInfo(View view,int pos, boolean isShow){
        this.isShow = isShow;
        this.pos = pos;
        this.view = view;
    }

    @Override
    public String toString() {
        return "[view:+"+view+", pos:"+pos+", isShow:"+isShow+"]";
    }
}
