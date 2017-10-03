package cn.cncgroup.tv.conf.model.voole.request;

import cn.cncgroup.tv.conf.model.voole.modle.VooleCategoryData;
import cn.cncgroup.tv.modle.CategoryData;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhang on 2015/9/29.
 */
public class VooleCategoryTransRequest
        extends BaseTransRequest<VooleCategoryData, CategoryData>
{
	public VooleCategoryTransRequest(String url, Object tag, Listener listener,
	        boolean isCacheResult, Class<VooleCategoryData> from,
	        Class<CategoryData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}
}
