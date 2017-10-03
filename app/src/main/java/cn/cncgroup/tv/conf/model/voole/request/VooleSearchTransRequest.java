package cn.cncgroup.tv.conf.model.voole.request;

import cn.cncgroup.tv.conf.model.voole.modle.VooleSearchData;
import cn.cncgroup.tv.conf.model.voole.modle.VooleSearchResult;
import cn.cncgroup.tv.modle.SearchData;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhang on 2015/10/14.
 */
public class VooleSearchTransRequest
        extends BaseTransRequest<VooleSearchResult, SearchData>
{

	public VooleSearchTransRequest(String url, Object tag, Listener listener,
	        boolean isCacheResult, Class<VooleSearchResult> from,
	        Class<SearchData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}

	@Override
	protected Object getTransStruct(VooleSearchResult result)
	        throws CloneNotSupportedException
	{
		VooleSearchData data = new VooleSearchData();
		data.result = result;
		return data;
	}
}
