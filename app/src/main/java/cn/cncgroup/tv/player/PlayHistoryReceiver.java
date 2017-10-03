package cn.cncgroup.tv.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.cncgroup.tv.db.DbUtil;
import cn.cncgroup.tv.db.VideoSetDao;

/**
 * Created by JIF on 2015/6/26.
 */
public class PlayHistoryReceiver extends BroadcastReceiver {
	private static final String TAG = "PlayHistoryReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
//		Log.i(TAG, "onReceive action = " + intent.getAction());
		try {
			VideoSetDao video = new VideoSetDao();
			video.videoSetId = intent.getStringExtra("videoId");
			video.name = intent.getStringExtra("videoName");
			video.image = intent.getStringExtra("videoImgUrl");
//			video.collect = 0;
			video.footmark = 1;
			video.episodeId = intent.getStringExtra("episodeId");
			video.channelId = intent.getStringExtra("chnId");
			video.playOrder = intent.getIntExtra("playorder", 0);
			video.startTime = intent.getIntExtra("currentPosition", 0);

//			Log.e(TAG, "已经存储的数据的个数：：：PlayHistoryReceiver：：：：" + new Select().from(VideoSetDao.class).execute().size());
//			Log.i(TAG, "videoid=" + video.episodeId + ";videoname=" + video.name + ";imgurl=" + video.image + ";startTime = " + video.startTime + ";videosetId=" + video.videoSetId + ";channelId=" + video.channelId);
//			Log.i(TAG, "playorder = " + video.playOrder);
			DbUtil.addByObject(video,true);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
