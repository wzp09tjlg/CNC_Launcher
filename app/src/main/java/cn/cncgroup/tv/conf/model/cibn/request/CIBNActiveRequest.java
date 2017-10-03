package cn.cncgroup.tv.conf.model.cibn.request;

import org.simpleframework.xml.core.Persister;

import cn.cncgroup.tv.conf.model.cibn.modle.CIBNXMLActiveData;
import cn.cncgroup.tv.network.request.BaseRequest;

/**
 * Created by zhang on 2015/6/2.
 */
public class CIBNActiveRequest extends BaseRequest<CIBNXMLActiveData>
{
	public CIBNActiveRequest(String url, Object tag, Listener listener,
	        boolean isCacheResult)
	{
		super(url, tag, listener, isCacheResult);
	}

	@Override
	protected CIBNXMLActiveData parseNetworkResponse(byte[] data)
	{
		// LogUtil.i(new String(data));
		try
		{
			return new Persister().read(CIBNXMLActiveData.class, new String(
			        data));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
