package cn.cncgroup.tv.view.homesecond;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Wu on 2015/10/29.
 */
public class AppUninstallBroadcast extends BroadcastReceiver
{
	private static List<AppUninstallListener> mList = new ArrayList<AppUninstallListener>();
	private final String TAG = "AppUninstallBroadcast";

	public static void addUninstallListener(AppUninstallListener listener)
	{
		mList.add(listener);
	}

	public static void deleteUninstallListener(AppUninstallListener listener)
	{
		mList.remove(listener);
	}

	public void onReceive(Context context, Intent intent)
	{
		String packageName = intent.getData().getSchemeSpecificPart();
		if ((intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)))
		{
			doAppUninstallOperate(packageName);
		}
		else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED))
		{
			doInstallAppOperate(packageName);
		}

	}

	private void doAppUninstallOperate(String packageName)
	{
		for (AppUninstallListener listener : mList)
		{
			listener.doAppUninstallListener(packageName);
		}
	}

	private void doInstallAppOperate(String packageName)
	{
		for (AppUninstallListener listener : mList)
		{
			listener.doAppInstallListener(packageName);
		}
	}

	public interface AppUninstallListener
	{
		void doAppUninstallListener(String packageName);

		void doAppInstallListener(String namePackage);
	}
}
