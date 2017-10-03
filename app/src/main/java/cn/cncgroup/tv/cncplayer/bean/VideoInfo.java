package cn.cncgroup.tv.cncplayer.bean;

import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.player.data.VideoPlayInfo;

/**
 * Created by Administrator on 2015/7/21.
 */
public class VideoInfo{
    public VideoPlayInfo playInfo;
    public String id;       //视频ID
    public String name;     //影片名
    public String url;      //影片播放URL
    public String focuse;   //描述
    public String definition;//清晰度
    public VideoSet videoSet;
    public int videoIndex;
    public VideoInfo() {
    }

    public VideoInfo(String name, String id, String focuse, String url, String definition) {
        this.name = name;
        this.focuse = focuse;
        this.url = url;
        this.definition = definition;
        this.id = id;
    }

    @Override
    public String toString() {
        return "[ name=" + name + ",url=" + url + ",definition:" + definition + ",id:"+id+"]";
    }
}
