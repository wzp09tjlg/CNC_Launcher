package cn.cncgroup.tv.player;

import android.content.Context;
import android.util.Log;

import com.qiyi.tv.client.ConnectionListener;
import com.qiyi.tv.client.QiyiClient;
import com.qiyi.tv.client.data.Video;

/**
 * Created by JIF on 2015/6/17.
 */
public class QiyiPlayer {
	private QiyiClient mQiyiClient = null;
	private static final String TAG = "QiyiPlayer";
	private static QiyiPlayer instance = null;
	private static final String SIGNATURE = "5td7zn014thfv63bwdxuv&yjky3etji16uj1il#d8s9i8vrv";

	private QiyiPlayer() {
	}

	public static synchronized QiyiPlayer getInstance() {
		if(instance == null) {
			instance = new QiyiPlayer();
		}
		return  instance;
	}

	public void init(Context context) {
		Log.i(TAG, "init");
		mQiyiClient = QiyiClient.instance();
		mQiyiClient.initialize(context.getApplicationContext(), SIGNATURE); // 第二个参数signature为测试数据
		mQiyiClient.setListener(mConnectionListener);
		mQiyiClient.connect();
	}

	public void release() {
		if (mQiyiClient!=null && mQiyiClient.isConnected()) {
			mQiyiClient.disconnect();
			mQiyiClient.release();
		}
	}

	private ConnectionListener mConnectionListener = new ConnectionListener() {
		@Override
		public void onAuthSuccess() {
			Log.d(TAG, "onAuthSuccess() threadid = " + Thread.currentThread().getId());
		}
		@Override
		public void onError(int code) {
			Log.d(TAG, "onError()" + ", code = " + code);
		}
		@Override
		public void onDisconnected() {
			Log.d(TAG, "onDisconnected()");
		}
		@Override
		public void onConnected() {
			Log.d(TAG, "onConnected()");
		}
	};

	public void playVideo(String videoSetId, String videoId, int startTime) {
		Log.i(TAG, "playVideo videoSetId = " + videoSetId + "; videoId = " + videoId + "; startTime = " + startTime);
		//videoSetId <-> qpId; videoId <-> tvQid
		Video video = new Video();
		video.setAlbumId(videoSetId);
		video.setId(videoId);
		video.setStartTime(startTime);
		int code = mQiyiClient.playMedia(video);
		Log.i(TAG,"playVideo ret = " + code);
	}
}
