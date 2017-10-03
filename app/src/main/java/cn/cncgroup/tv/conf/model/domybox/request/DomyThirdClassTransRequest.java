package cn.cncgroup.tv.conf.model.domybox.request;

import cn.cncgroup.tv.modle.FilterListData;
import cn.cncgroup.tv.conf.model.domybox.modle.DomyThirdClassListContent;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhangbo on 15-6-18.
 */
public class DomyThirdClassTransRequest extends
        BaseTransRequest<DomyThirdClassListContent, FilterListData>
{
	public DomyThirdClassTransRequest(String url, Object tag,
	        Listener listener, boolean isCacheResult,
	        Class<DomyThirdClassListContent> from, Class<FilterListData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}

	@Override
	protected Object getTransStruct(DomyThirdClassListContent contnet)
	{
		return contnet.data;
	}
}
