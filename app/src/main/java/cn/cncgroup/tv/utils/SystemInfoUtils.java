package cn.cncgroup.tv.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author yqh:
 * @version date：2015-7-31 下午3:32:38
 */
public class SystemInfoUtils {
	private static final String TAG = "SystemInfoUtils";

	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Activity.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
		if (runningTaskInfos != null)
			return (runningTaskInfos.get(0).topActivity).toString();
		else
			return null;
	}

	public static List<RunningAppProcessInfo> getAllRunningAppInfo(
			Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
		return list;
	}

	public static List<Drawable> clearProcess(Context context) {
		List<RunningAppProcessInfo> listBefore = getAllRunningAppInfo(context);
		List<String> nameBefore = new ArrayList<String>();
		for (RunningAppProcessInfo info : listBefore) {
			nameBefore.add(info.processName);
		}
		List<Drawable> result = new ArrayList<Drawable>();
		killBackgroundPro(context);
		List<RunningAppProcessInfo> listAfter = getAllRunningAppInfo(context);
		List<String> nameAfter = new ArrayList<String>();
		for (RunningAppProcessInfo info : listAfter) {
			nameAfter.add(info.processName);
		}
		PackageManager pm = context.getPackageManager();
		for (String pkn : nameBefore) {
			if (nameAfter.contains(pkn))
				continue;
			try {
				result.add(pm.getApplicationIcon(pkn));
			} catch (NameNotFoundException e) {
				Log.e(TAG, "NameNotFoundException:" + e.getMessage());
			}
		}
		return result;

	}

	/**
	 * 根据包名停止进程，可以杀死输入法进程
	 * @param packageName
	 * @return
	 */
	public static int killeProcessByPackageName(String packageName) {
		try {
			String cmd = "am force-stop " + packageName + " \n";
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			int exitValue = process.exitValue();
			Log.e("killProcessByPID=", "------" + exitValue);
			if (process.exitValue() == 0)
				return 0;
			return exitValue;
		} catch (Exception e) {
			Log.e(TAG, "killeProcessByPackageName--packageName: "+e.getMessage());
		}
		return -1;
	}

	public static void killBackgroundPro(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcessList = activityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo info : appProcessList) {
			if (TextUtils.equals(getLauncherPackageName(context),
					info.processName))
				continue;
			activityManager.killBackgroundProcesses(info.processName);
		}
	}

	/**
	 * unuse
	 * 
	 * @param context
	 * @param info
	 * @return
	 */
	private static boolean killProcess(Context context,
			RunningAppProcessInfo info) {
		if (info.processName.equals(context.getPackageName())
				|| info.processName.startsWith("com.android.")
				|| info.processName.startsWith("system")
				|| getLauncherPackageName(context).equals(info.processName)) {
			Log.e(TAG, "skip name=" + info.processName + "  persistentId="
					+ info.pid);
			return false;
		}
		return killProcessByPID(info.pid);
	}

	public static List<ApplicationInfo> getRunningProcess(Context context) {
		List<ApplicationInfo> result = new ArrayList<ApplicationInfo>();
		PackageManager pm = context.getPackageManager();
		List<RunningAppProcessInfo> list = getAllRunningAppInfo(context);
		String pkn = null;
		for (RunningAppProcessInfo info : list) {
			pkn = info.processName;
			try {
				ApplicationInfo info2 = pm.getApplicationInfo(pkn,
						PackageManager.GET_UNINSTALLED_PACKAGES);
				if ((info2.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)
					result.add(info2);
			} catch (NameNotFoundException e1) {
				Log.w(TAG, "NameNotFoundException : " + e1.getMessage());
			}
		}

		return result;
	}

	public static String getApplicationName(Context context, String pkn) {
		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo info = pm.getApplicationInfo(pkn,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return pm.getApplicationLabel(info).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getSystemProperties(String name) {
		String result = null;
		try {
			Class<?> clazz = Class.forName("android.os.SystemProperties");
			Object obj = clazz.newInstance();
			Method method = clazz.getMethod("get", String.class);
			result = (String) method.invoke(obj, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void setSystemProperties(String name, String val) {
		try {
			Log.d(TAG, "setSystemProperties--name=" + name + "------val=" + val);
			Class<?> clazz = Class.forName("android.os.SystemProperties");
			Method method = clazz.getMethod("set", String.class, String.class);
			method.invoke(clazz, name, val);
		} catch (Exception e) {
			Log.w(TAG, " Exception  setSystemProperties :" + e.getMessage());
		}
	}

	/**
	 * 获取MAC地址
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getMacAddress(Context mContext) {
		String macStr = "0.0.0.0";
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo.getMacAddress() != null) {
			macStr = wifiInfo.getMacAddress();// MAC地址
		} else {
			macStr = "0.0.0.0";
		}

		return macStr.toUpperCase();
	}

	public static String getEthMAc(Context ctx) {
		String strMacAddr = "0.0.0.0";
		try {
			byte[] b = NetworkInterface.getByName("eth0").getHardwareAddress();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < b.length; i++) {
				if (i != 0) {
					buffer.append(':');
				}
				String str = Integer.toHexString(b[i] & 0xFF);
				buffer.append(str.length() == 1 ? 0 + str : str);
			}
			strMacAddr = buffer.toString().toUpperCase();
		} catch (SocketException e) {
			Log.w(TAG, "getEthMAc---:" + e.getMessage());
		}
		return strMacAddr;
	}

	public static String getMacAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				if (intf.getName().equals("eth0")) {
					StringBuffer sb = new StringBuffer();
					byte[] macBytes = intf.getHardwareAddress();
					for (int i = 0; i < macBytes.length; i++) {
						String sTemp = Integer.toHexString(0xFF & macBytes[i]);
						if (sTemp.length() == 1) {
							sb.append("0");
						}
						sb.append(sTemp);
					}
					return sb.toString();
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isActivityRunning() {

		return false;
	}

	/**
	 * 杀死指定的process
	 * 
	 * @param pid
	 *            进程ID
	 */
	public static boolean killProcessByPID(int pid) {
		try {
			Log.e("killProcessByPID=", "-------pid=" + pid);
			Process exec = Runtime.getRuntime().exec("kill -9 " + pid);
			exec.waitFor();
			if (exec.exitValue() == 0)
				return true;
			Log.e("killProcessByPID=", "------" + exec.exitValue());
		} catch (Exception e) {
			Log.e("killProcessByPID=", "" + e.getMessage());
		}
		return false;
	}

	/**
	 * 获取luncher的包名
	 */
	private static String getLauncherPackageName(Context context) {
		final PackageManager pm = context.getPackageManager();
		ActivityInfo homeInfo = new Intent(Intent.ACTION_MAIN).addCategory(
				Intent.CATEGORY_HOME).resolveActivityInfo(pm, 0);
		return homeInfo.packageName;
	}

	/**
	 * 判断服务是否在运行
	 * 
	 * @param context
	 * @param className
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		Log.i(TAG, "serviceList.size()=" + serviceList.size());
		for (int i = 0; i < serviceList.size(); i++) {
			Log.i(TAG, "isServiceRunning? className:"
					+ serviceList.get(i).service.getClassName());
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		Log.i(TAG, "isServiceRunning? className:" + className + "---isRunning:"
				+ isRunning);
		return isRunning;
	}
}
