package cn.cncgroup.tv.conf.model.domybox.request;

import cn.cncgroup.tv.modle.CategoryData;
import cn.cncgroup.tv.conf.model.domybox.modle.DomyFirstClassListContent;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhangbo on 15-6-19.
 */
public class DomyFirstClassTransRequest extends
        BaseTransRequest<DomyFirstClassListContent, CategoryData>
{
	public DomyFirstClassTransRequest(String url, Object tag,
	        Listener listener, boolean isCacheResult,
	        Class<DomyFirstClassListContent> from, Class<CategoryData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}

	@Override
	protected Object getTransStruct(DomyFirstClassListContent content)
	{
		return content.data;
	}
}
