package cn.cncgroup.tv.conf.model.voole.request;

import cn.cncgroup.tv.conf.model.voole.modle.VooleDetailData;
import cn.cncgroup.tv.modle.VideoSetDetailData;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhang on 2015/10/12.
 */
public class VooleDetailVideoSetTransRequest
        extends BaseTransRequest<VooleDetailData, VideoSetDetailData>
{

	public VooleDetailVideoSetTransRequest(String url, Object tag,
	        BaseRequest.Listener listener, boolean isCacheResult,
	        Class<VooleDetailData> from, Class<VideoSetDetailData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}

	@Override
	protected Object getTransStruct(VooleDetailData vooleDetailData)
	        throws CloneNotSupportedException
	{
		vooleDetailData.film.videoListUrl = vooleDetailData.contentSet.sourceUrl;
		vooleDetailData.film.contentCount = vooleDetailData.contentSet.contentCount;
		vooleDetailData.film.contentTrueCount = vooleDetailData.contentSet.contentTrueCount;
		if (vooleDetailData.film.contentCount > 1)
		{
			vooleDetailData.film.seriesType = 1;
		}
		return vooleDetailData;
	}
}