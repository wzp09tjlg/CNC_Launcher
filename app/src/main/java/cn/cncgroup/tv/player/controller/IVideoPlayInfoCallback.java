package cn.cncgroup.tv.player.controller;

import cn.cncgroup.tv.player.data.VideoPlayInfo;

/**
 * Created by JIF on 2015/6/30.
 */
public interface IVideoPlayInfoCallback {
	void onSuccess(VideoPlayInfo info);
	void onFailed(Exception e);
}
