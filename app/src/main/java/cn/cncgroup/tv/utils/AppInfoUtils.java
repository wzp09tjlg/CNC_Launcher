package cn.cncgroup.tv.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.cncgroup.tv.bean.AppInfo;

/**
 * Created by Wu on 2015/5/19.
 */
public class AppInfoUtils
{
	private static final String TAG = "AppInfoUtils";
	private static ArrayList<AppInfo> sAppList;

	public static ArrayList<AppInfo> getAppsData(Context context)
	{
		PackageManager pManager = context.getPackageManager();
		// 应用的信息
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		// 获取安卓包信息
		List<PackageInfo> pakList = pManager.getInstalledPackages(0);
		int index = 0;
		for (int i = 0; i < pakList.size(); i++)
		{
			PackageInfo pak = pakList.get(i);
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)
			{
				AppInfo appItem = new AppInfo();
				appItem.setAppName(pManager.getApplicationLabel(
				        pak.applicationInfo).toString());// 应用的名字
				appItem.setPakageName(pak.packageName); // 应用的包名
				if (context.getPackageName().equals(pak.packageName))
				{
					continue;
				}
				String versionName = "";
				try
				{
					if (!TextUtils.isEmpty(pak.versionName))
					{
						versionName = pak.versionName;
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				appItem.setVersionName(versionName); // 应用的版本名字
				appItem.setVersionCode("" + pak.versionCode); // 应用的版本的Code
				appItem.setIcon(pManager
				        .getApplicationIcon(pak.applicationInfo));
				index = index + 1;
				appItem.setTag(index); // 显示的数顺序，第一显示的顺序就是1
				appList.add(appItem);
			}
		}
		sAppList = appList;
		return appList;
	}

	public static int getAppCount()
	{
		return sAppList == null ? 0 : sAppList.size();
	}

	public static AppInfo getAppInfo(String strPackageName)
	{
		if (strPackageName.equals(""))
			return null;
        if (getAppCount() == 0) return null;
        for (AppInfo app : sAppList)
		{
			if (app.getPackageName().equals(strPackageName))
			{
				AppInfo appInfo = new AppInfo(app);
				return appInfo;
			}
		}
		return null;
	}
}
