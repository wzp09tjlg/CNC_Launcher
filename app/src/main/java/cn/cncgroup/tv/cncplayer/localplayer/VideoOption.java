package cn.cncgroup.tv.cncplayer.localplayer;

import cn.cncgroup.tv.cncplayer.bean.VideoSize;

/**
 * Created by Administrator on 2015/10/13.
 * yqh
 */
public class VideoOption {
    private static VideoOption mOption;
    private VideoSize mVideSize;
    private VideoOption(){}
    public static VideoOption getInstance(){
        if(mOption==null){
            mOption = new VideoOption();
        }
        return  mOption;
    }

    public void setVideoSizeOption(VideoSize size){
        mVideSize = size;
    }

    public VideoSize getVideoSize(){
        return mVideSize;
    }

}
