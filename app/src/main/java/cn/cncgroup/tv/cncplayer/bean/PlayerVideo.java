package cn.cncgroup.tv.cncplayer.bean;

import java.io.Serializable;

import cn.cncgroup.tv.modle.Video;

/*
 * Created by yqh on 2015/9/24.
 */
public class PlayerVideo extends Video implements  Serializable {
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFocus() {
        return focus;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getEpvid() {
        return epvid;
    }
}
