package cn.cncgroup.tv.conf.model.voole.request;

import cn.cncgroup.tv.conf.model.voole.modle.VooleRecommendVideoSetListData;
import cn.cncgroup.tv.modle.RecommendVideoSetListData;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhang on 2015/10/13.
 */
public class VooleRecommendVideoSetListTransRequest extends
        BaseTransRequest<VooleRecommendVideoSetListData, RecommendVideoSetListData>
{
	public VooleRecommendVideoSetListTransRequest(String url, Object tag,
	        Listener listener, boolean isCacheResult,
	        Class<VooleRecommendVideoSetListData> from,
	        Class<RecommendVideoSetListData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}
}