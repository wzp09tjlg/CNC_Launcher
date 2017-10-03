package cn.cncgroup.tv.conf.model.domybox.request;

import cn.cncgroup.tv.conf.model.domybox.modle.DomySearchContent;
import cn.cncgroup.tv.modle.SearchData;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhangbo on 15-6-19.
 */
public class DomySearchTransRequest
        extends BaseTransRequest<DomySearchContent, SearchData>
{

	public DomySearchTransRequest(String url, Object tag, Listener listener,
	        boolean isCacheResult, Class<DomySearchContent> from,
	        Class<SearchData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}

	@Override
	protected Object getTransStruct(DomySearchContent content)
	{
		return content.data;
	}
}
