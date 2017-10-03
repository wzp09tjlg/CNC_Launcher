package cn.cncgroup.tv.player.controller;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.qiyi.tvapi.vrs.IVrsCallback;
import com.qiyi.tvapi.vrs.VrsHelper;
import com.qiyi.tvapi.vrs.model.M3u8Info;
import com.qiyi.tvapi.vrs.model.Vid;
import com.qiyi.tvapi.vrs.result.ApiResultM3u8;
import com.qiyi.video.api.ApiException;

import cn.cncgroup.tv.player.data.VideoPlayInfo;
import cn.cncgroup.tv.utils.ListUtils;


/**
 * Created by JIF on 2015/6/30.
 */
public class VideoPlayInfoController {
	private static VideoPlayInfoController instance = null;
	private static final String UUID = "20131028184210947WwplwcvS10135";
	private static final String TAG = "VideoPlayInfoController";
	private VideoPlayInfoController() {}


	/**
	 * 获取视频播放相关信息，包括播放地址、片头时间、片尾时间
	 * @param callback 回调
	 * @param videoId 视频id对应domy接口的videoId
	 * @param videoVid 视频不同清晰度对应的vid，对应domy接口的epvid
	 */

	public static void getVideoPlayInfo(final IVideoPlayInfoCallback callback, String videoId, String videoVid) {
		VrsHelper.m3u8FromTvidVid.call(new IVrsCallback<ApiResultM3u8>() {
			@Override
			public void onSuccess(ApiResultM3u8 result) {
				if (result != null && result.data != null) {
					M3u8Info auth = result.data;
                    String url = (TextUtils.isEmpty(auth.m3utx) ? auth.m3u : auth.m3utx);
					Log.d(TAG, "onSuccess: m3u8=" + auth.m3u
							+ ", m3u8tx=" + auth.m3utx
							+ ", head time=" + auth.head
							+ ", tail time=" + auth.tail
							+ ", url=" + url);
					int head = TextUtils.isEmpty(auth.head) ? 0 : Integer.parseInt(auth.head);
					int tail = TextUtils.isEmpty(auth.tail) ? 0 : Integer.parseInt(auth.tail);
					VideoPlayInfo info = new VideoPlayInfo(url, head, tail);
					callback.onSuccess(info);
				}
			}

			@Override
			public void onException(ApiException e) {
				Log.e(TAG, "onException", e);
				callback.onFailed(e);
			}
		}, videoId, videoVid, UUID, null);
	}

	/**
	 * 获取清晰度信息
	 * @param callback 回调
	 * @param videoId 视频id对应domy接口的videoId
	 * @param videoVid 视频不同清晰度对应的vid，对应domy接口的epvid
	 */
	public static void getVideoDefinition(final IVideoDefinitonCallback callback,String videoId, String videoVid) {
		VrsHelper.m3u8FromTvidVid.call(new IVrsCallback<ApiResultM3u8>() {
			@Override
			public void onSuccess(ApiResultM3u8 result) {
				if (result != null && result.data != null) {
					M3u8Info auth = result.data;
					if (!ListUtils.isEmpty(result.data.vidl)) {
						SparseArray<String> vrsVidList = new SparseArray<String>(auth.vidl.size());
						for (Vid vid : auth.vidl) {
							vrsVidList.put(vid.vd, vid.vid);
							Log.d(TAG, vid.vd + "--" + vid.vid);
						}
						callback.onSuccess(vrsVidList);
					}
				}
			}
			@Override
			public void onException(ApiException e) {
				Log.e(TAG, "onException", e);
				callback.onFailed(e);
			}
		}, videoId, videoVid, UUID, null);
	}
}
