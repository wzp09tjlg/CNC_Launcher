package cn.cncgroup.tv.network.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.concurrent.TimeUnit;

import cn.cncgroup.tv.network.DiskCache;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.network.request.PingRequest;
import cn.cncgroup.tv.utils.LogUtil;

/**
 * Created by zhang on 2015/4/21.
 */
public class HttpUtil
{

	private static final OkHttpClient sClient = new OkHttpClient();

	static
	{
		sClient.setConnectTimeout(8, TimeUnit.SECONDS);
	}

	public static void get(BaseRequest<?> callback)
	{
		LogUtil.i(callback.url);
		if (NetworkManager.CONNECTION_TYPE == -1)
		{
			LogUtil.i("未连接网络");
			callback.onFailure(null, new BaseRequest.NoActiveNetworkException(
			        "没有网络连接"));
			return;
		}
		sClient.newCall(
		        new Request.Builder().url(callback.url).tag(callback.tag)
		                .build()).enqueue(callback);
	}

	public static void post(BaseRequest<?> callback, RequestBody requestBody)
	{
		LogUtil.i(callback.url);
		if (NetworkManager.CONNECTION_TYPE == -1)
		{
			LogUtil.i("未连接网络");
			callback.onFailure(null, new BaseRequest.NoActiveNetworkException(
			        "没有网络连接"));
			return;
		}
		sClient.newCall(
		        new Request.Builder().url(callback.url).tag(callback.tag)
		                .post(requestBody).build()).enqueue(callback);
	}

	/**
	 * 用head方式测试外网是否联通
	 */
	public static void head(PingRequest callback)
	{
		if (NetworkManager.CONNECTION_TYPE == -1)
		{
			LogUtil.i("未连接网络");
			callback.onFailure(null, new BaseRequest.NoActiveNetworkException(
			        "没有网络连接"));
			return;
		}
		sClient.newCall(
		        new Request.Builder().url(PingRequest.URL).tag(PingRequest.TAG)
		                .head().build()).enqueue(callback);
	}

	public static void getFirstByCache(BaseRequest<?> callback)
	{
		DiskCache.Entry entry = NetworkManager.getInstance().diskCache
		        .get(callback.url);
		if (entry != null)
		{
			LogUtil.i("cache hit");
			callback.onCacheResponse(entry.data);
		}
		get(callback);
	}

	public static void getOnlyByCache(BaseRequest<?> callback)
	{
		DiskCache.Entry entry = NetworkManager.getInstance().diskCache
		        .get(callback.url);
		if (entry != null)
		{
			LogUtil.i("cache hit");
			callback.onCacheResponse(entry.data);
		}
		else
		{
			callback.onCacheFailure();
		}
	}

	/**
	 * 取消相应tag的访问
	 */
	public static void cancelRequest(Object tag)
	{
		sClient.cancel(tag);
	}

}
