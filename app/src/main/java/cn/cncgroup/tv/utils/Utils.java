package cn.cncgroup.tv.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.R;
import cn.cncgroup.tv.bean.AppInfo;
import cn.cncgroup.tv.ui.widget.GlobalToast;

/**
 * Created by JIF on 2015/5/27.
 */
public class Utils {
    public static void startAppByPackageName(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                    .setPackage(pi.packageName);
            List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
            ResolveInfo info = apps.iterator().next();
            if (info != null) {
                ActivityInfo acInfo = info.activityInfo;
//				 Drawable pkgIcon = context.getResources().getDrawable(acInfo.icon);
                String appName = acInfo.name;

                ComponentName comp =
                        new ComponentName(acInfo.packageName, appName);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(comp);
                addOpenHistory(pm, pi, packageName);
                context.startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, R.string.app_invoke_error,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private static void addOpenHistory(PackageManager pm, PackageInfo pi, String packageName) {
        AppInfo appInfo = new AppInfo();
        appInfo.setIcon(pm.getApplicationIcon(pi.applicationInfo));
        appInfo.setAppName(pm.getApplicationLabel(pi.applicationInfo).toString());
        appInfo.setPackageName(packageName);
        CApplication.addAppOpenLists(appInfo);
    }

    public static void installApkFromAsset(Context ctx, String apkname) {
        if (copyApkFromAssets(ctx, apkname, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + apkname)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + apkname),
                    "application/vnd.android.package-archive");
            ctx.startActivity(intent);
        }
    }

    private static boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    public static void startActivityUseActivityName(Context context, String packageName, String activityName) {
        Intent netIntent = new Intent();
        try {
            ComponentName netName = new ComponentName(
                    packageName,
                    activityName);
            netIntent.setComponent(netName);
            netIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(netIntent);
        } catch (Exception e) {
            e.printStackTrace();
            GlobalToast.makeText(context, "未找到该应用", GlobalConstant.ToastShowTime).show();
        }
    }

    public static void startActivity(Context context,
                                     Class<? extends Activity> cls, Bundle bundle) {
        try {
            Intent intent = new Intent(context, cls);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
    public static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException s) {
            Log.i("getLocalIPAddress", "getLocalIPAddress");
        }
        return "null";
    }

    public static String GetNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        try {
            //http://iframe.ip138.com/ic.asp
            infoUrl = new URL("http://city.ip138.com/ip2city.asp");
//            infoUrl = new URL("http://iframe.ip138.com/ic.asp");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();
                //从反馈的结果中提取出IP地址
                int start = strber.indexOf("[");
                int end = strber.indexOf("]", start + 1);
                line = strber.substring(start + 1, end);
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static String getDay(Long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        return formatter.format(date);
    }

    public static String milltimeToHour(int millTime) {
        String str = new String();
        if (millTime == 60 * 60 * 1000) {
            return "1:00:00";
        }
        int hour = millTime / (60 * 60 * 1000);
        if (hour >= 1) {
            str = formatNum(hour) + ":";
        } else {
            str = "00:";
        }
        String str2 = "";
        if (hour >= 1) {
            millTime = millTime - (hour * 60 * 60 * 1000);
        }
        millTime = millTime / 1000;
        int minu = millTime / 60;
        int mill = millTime % 60;
        str2 = formatNum(minu) + ":" + formatNum(mill);
        return str + str2;
    }

    public static String formatNum(int singleNum) {
        String str = "";
        if (singleNum >= 0 && singleNum <= 9) {
            str = "0" + singleNum;
        } else {
            str = "" + singleNum;
        }
        return str;
    }
}
