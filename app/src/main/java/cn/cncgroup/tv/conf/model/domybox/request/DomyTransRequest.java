package cn.cncgroup.tv.conf.model.domybox.request;

import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhangbo on 15-6-19.
 */
public class DomyTransRequest<F extends Object, T> extends BaseTransRequest
{
	public DomyTransRequest(String url, Object tag, Listener listener,
	        boolean isCacheResult, Class from, Class to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}

	@Override
	protected Object getTransStruct(Object o) throws CloneNotSupportedException
	{
		return super.getTransStruct(o);
	}
}
