package cn.cncgroup.tv.conf.model.cibn.request;

import org.simpleframework.xml.core.Persister;

import cn.cncgroup.tv.conf.model.cibn.modle.CIBNXMLLoginData;
import cn.cncgroup.tv.network.request.BaseRequest;

/**
 * Created by zhang on 2015/6/2.
 */
public class CIBNLoginRequest extends BaseRequest<CIBNXMLLoginData>
{
	public CIBNLoginRequest(String url, Object tag, Listener listener,
	        boolean isCacheResult)
	{
		super(url, tag, listener, isCacheResult);
	}

	@Override
	protected CIBNXMLLoginData parseNetworkResponse(byte[] data)
	{
		// LogUtil.i(new String(data));
		try
		{
			return new Persister().read(CIBNXMLLoginData.class,
			        new String(data));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
