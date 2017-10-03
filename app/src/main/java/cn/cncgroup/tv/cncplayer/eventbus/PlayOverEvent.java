package cn.cncgroup.tv.cncplayer.eventbus;

/**
 * Created by Administrator on 2015/9/18.
 */
public class PlayOverEvent {
    public int pos = 0;
    private PlayOverEvent() {
    }
    public PlayOverEvent(int type) {
        this.pos = type;
    }
}
