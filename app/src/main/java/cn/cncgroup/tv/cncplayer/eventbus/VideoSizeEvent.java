package cn.cncgroup.tv.cncplayer.eventbus;

/**
 * Created by Administrator on 2015/11/17.
 */
public class VideoSizeEvent {
    public static final int FULL_SCREEN = 1;
    public static final int SMALL_SCREEN = 2;
    private int mScreentSize;

    private VideoSizeEvent() {
    }

    public VideoSizeEvent(int screenSize) {
        mScreentSize = screenSize;
    }

    public int getScreenSize() {
        return mScreentSize;
    }

    public boolean isFullScreen() {
        return mScreentSize == FULL_SCREEN;
    }

    @Override
    public String toString() {
        return "VideoSizeEvent{" +
                "mScreentSize=" + mScreentSize + ",isFullScreen=" + (mScreentSize == FULL_SCREEN) +
                '}';
    }
}
