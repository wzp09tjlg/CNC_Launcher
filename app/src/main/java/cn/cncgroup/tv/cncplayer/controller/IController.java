package cn.cncgroup.tv.cncplayer.controller;

import java.util.ArrayList;

import cn.cncgroup.tv.cncplayer.bean.VideoInfo;

/**
 * Created by Administrator on 2015/11/16.
 */
public interface IController {
    void setUIState(boolean isOpen);

    void setContentView();

    void startPlay(VideoInfo info);

    void destroyPlay();

    void setBottomPannelViewDefitionResult(ArrayList<String> defitionList);

    void setCurrentDefinition(String definition);

    /*
    * 更新历史播放记录时间
    */
    void updateHistory();

    void setVedioCount(int size);

    void showDefitionTip();

//    int getTime();
}
