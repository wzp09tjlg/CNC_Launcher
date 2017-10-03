package cn.cncgroup.tv.conf.model.voole.request;

import cn.cncgroup.tv.conf.model.voole.modle.VooleVideoListData;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhang on 2015/10/12.
 */
public class VooleDetailVideoListTransRequest
        extends BaseTransRequest<VooleVideoListData, VideoListData>
{
	public VooleDetailVideoListTransRequest(String url, Object tag,
	        Listener listener, boolean isCacheResult,
	        Class<VooleVideoListData> from, Class<VideoListData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}
}