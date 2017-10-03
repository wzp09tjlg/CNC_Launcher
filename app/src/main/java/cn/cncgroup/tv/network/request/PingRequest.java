package cn.cncgroup.tv.network.request;

import java.io.IOException;

import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.utils.HttpUtil;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by zhang on 2015/4/30. 测试网络联通状况的请求
 */
public class PingRequest implements Callback
{
	public static final String TAG = "Ping";
	public static final String URL = "http://www.baidu.com";
	private PingListener mListener;

	public PingRequest(PingListener listener)
	{
		this.mListener = listener;
	}

	/**
	 * 执行ping操作
	 */
	public static void performPing(PingListener listener)
	{
		HttpUtil.head(new PingRequest(listener));
	}

	public static void cancelPing()
	{
		HttpUtil.cancelRequest(TAG);
	}

	@Override
	public void onFailure(Request request, IOException e)
	{
		NetworkManager.getInstance().UIHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mListener.onPingFinished(false);
			}
		});
	}

	@Override
	public void onResponse(Response response) throws IOException
	{
		NetworkManager.getInstance().UIHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mListener.onPingFinished(true);
			}
		});
	}

	public interface PingListener
	{
		void onPingFinished(boolean isOK);
	}
}
