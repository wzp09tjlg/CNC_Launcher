package cn.cncgroup.tv.network;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.cncgroup.tv.network.utils.NetUtil;

/**
 * Created by zhang on 2015/4/29.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver
{
	private static ArrayList<ConnectionChangeListener> mList = new ArrayList<ConnectionChangeListener>();

	/**
	 * 一般在activity，onstart方法调用注册
	 */
	public static void registerConnectionListener(
	        Context context,ConnectionChangeListener listener)
	{
		mList.add(listener);
		// 每次注册，先返回一下
		NetworkManager.CONNECTION_TYPE = NetUtil.getActiveNetwork(context);
		listener.onConnectionChanged(NetworkManager.CONNECTION_TYPE);
	}

	/**
	 * 一般在activity，onstop方法解除注册
	 */
	public static void unregisterConnectionListenr(
	        ConnectionChangeListener listener)
	{
		mList.remove(listener);
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		NetworkManager.CONNECTION_TYPE = NetUtil.getActiveNetwork(context);
		for (ConnectionChangeListener listener : mList)
		{
			listener.onConnectionChanged(NetworkManager.CONNECTION_TYPE);
		}
	}

}
