package cn.cncgroup.tv.network.request;

import com.alibaba.fastjson.JSON;

/**
 * Created by zhang on 2015/4/21. 自动解析json为指定类型的Request
 */
public class JsonRequest<T> extends BaseRequest
{
	private Class<T> mCls;

	public JsonRequest(String url, Object tag, Listener<T> listener,
	        boolean isCacheResult, Class<T> cls)
	{
		super(url, tag, listener, isCacheResult);
		mCls = cls;
	}

	/**
	 * 重写解析方法，转换为指定类型
	 */
	@Override
	protected T parseNetworkResponse(byte[] data)
	{
		try
		{
			return JSON.parseObject(new String(data), mCls);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
