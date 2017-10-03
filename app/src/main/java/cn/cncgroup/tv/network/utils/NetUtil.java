package cn.cncgroup.tv.network.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by zhang on 2015/4/30.
 */
public class NetUtil
{
	/**
	 * 返回网络类型
	 */
	public static int getActiveNetwork(Context context)
	{
		ConnectivityManager manager = (ConnectivityManager) context
		        .getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null)
		{
			return info.getType();
		}
		return -1;
	}

	/**
	 * 如果网络类型是WIFI，返回WIFI信号强度
	 */
	public static int getWifiLevel(Context context)
	{
		return WifiManager.calculateSignalLevel(((WifiManager) context
		        .getSystemService(Context.WIFI_SERVICE)).getConnectionInfo()
		        .getRssi(), 4);
	}
}
