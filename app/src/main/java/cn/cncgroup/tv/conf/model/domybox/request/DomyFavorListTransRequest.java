package cn.cncgroup.tv.conf.model.domybox.request;

import cn.cncgroup.tv.modle.RecommendVideoSetListData;
import cn.cncgroup.tv.conf.model.domybox.modle.DomyFavorListContent;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhangbo on 15-6-18.
 */
public class DomyFavorListTransRequest extends
        BaseTransRequest<DomyFavorListContent, RecommendVideoSetListData>
{
	public DomyFavorListTransRequest(String url, Object tag, Listener listener,
	        boolean isCacheResult, Class<DomyFavorListContent> from,
	        Class<RecommendVideoSetListData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}

	@Override
	protected Object getTransStruct(DomyFavorListContent content)
	{
		return content.data;
	}
}
