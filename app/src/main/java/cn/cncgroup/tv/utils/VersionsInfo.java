package cn.cncgroup.tv.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.widget.CustomDialog;


/**
 * Created by zhangguang on 2015/5/19.
 * 获得版本信息和应用是否安装
 */
public class VersionsInfo {


    private static final String TAG = "VersionsInfo";
    /**
     * mac地址
     */
    public static String mac;
	private static Preferences sp;

    /**
     * 型号
     *
     * @return
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }


    /**
     * 安卓版本
     *
     * @return
     */
    public static String getAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }


    public static String getLocalIpAddress() {
        try
        {
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();en.hasMoreElements();){
                NetworkInterface intf = en.nextElement();
                for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)){
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }
        catch(SocketException s)
        {
            Log.i("getLocalIPAddress", "getLocalIPAddress");
        }

        return "null";
    }

    /**
     * 获取本机Ip地址
     *
     * @return
     */
    public static String getIPadress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //检查Wifi状态
        if (!wm.isWifiEnabled())
            wm.setWifiEnabled(true);
        WifiInfo wi = wm.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd = wi.getIpAddress();
        //把整型地址转换成“*.*.*.*”地址
        String ip = intToIp(ipAdd);
        return ip;
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 获得WifiMAC
     *
     * @param context
     * @return
     */
    public static String getWifiMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 软件versionName
     *
     * @param context
     * @return
     */
    public static String getAppVsersionName(Context context, String packageName) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            versionName = pi.versionName;
            if (versionName.equals("") || versionName == null) {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return versionName;
    }

    /**
     * 软件versionCode
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context, String packageName){
         int versionCode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            versionCode = pi.versionCode;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return versionCode;
    }

    /**
     * 软件versionCode
     * @param context
     * @return
     */
    public static String getAppVersion(Context context, String packageName){
        String version = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            version = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return version;
    }
    /**
     * 去掉mac地址中的符号“：” 或“-”
     *
     * @return
     */
    public static String getFomatMacAddr() {
        String formatMac = getMacAddr();
        formatMac = formatMac.replaceAll("-", "").replaceAll(":", "");
        return formatMac;
    }


    /**
     * 获取mac地址
     *
     * @return
     */
    public static String getMacAddr() {
        if (!isEmpty(mac)) return mac;

        if (!TextUtils.isEmpty(""))
            return "";
		String macAddress;
        macAddress = getEthMAC();
        return macAddress;
    }


    /**
     * 获取无线网卡mac地址
     *
     */
    public static String getWifiMAC(Context ctx) {
        try{
            String macAddress = null;
            WifiManager wifiMgr = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info)
                macAddress = info.getMacAddress();
            return macAddress;
        }catch(Exception e){
            return "";
        }
    }

        /**
         * 判断字符串是否为空
         * @param str
         * @return true - 全为空， false - 有一个不为空
         */
        public static boolean isEmpty(String ...str){
            if (str == null) return true;
            for (int i=0;i<str.length;i++)
                if (str[i]!=null && !"".equals(str[i]))return false;

            return true;
        }

    /**
     * liuzm
     *
     * 获取文件MAC地址 /sys/class/net/eth0/address
     *
     */
    private static String getEthMAC() {
        Reader reader = null;
        StringBuffer str = new StringBuffer();
        try {
            char[] tempchars = new char[20];
			int charread;
            reader = new InputStreamReader(new FileInputStream(
                    "/sys/class/net/eth0/address"));
            while ((charread = reader.read(tempchars)) != -1) {
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            str.append(tempchars[i]);
                        }
                    }
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
        }

        return str.toString().trim();
    }


    public static String getlocalip(Context ctx){
        WifiManager wifiManager = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        if(ipAddress==0)return null;
        return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."
                +(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
    }


    /**
     * 首页检查应用更新
     * @param versionCode      服务器返回的版本号
     * @param apkUrl           服务器最新版本apk下载地址
     */
//    public static DownloadUtil updateCheck(final Context mContext, final int versionCode, final String apkUrl){
//        final CustomDialog updateDialog = new CustomDialog(mContext);
//        final DownloadUtil dialogUtil = new DownloadUtil();
//        sp = Preferences.build(mContext);
////        if(!checkUpdate(versionCode)){          //不为忽略的版本号
//            if (versionCode > VersionsInfo.getAppVersionCode(mContext, mContext.getPackageName())) {           //有更新
//
//                updateDialog.setMessage(mContext.getResources().getString(R.string.update_dialog_msg));                     //立即下载
//                updateDialog.setPositiveButton(mContext.getResources().getString(R.string.update), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialogUtil.checkUpdata(mContext, apkUrl, "cnc");
//                    }
//                });
//                updateDialog.setNegativeButton(mContext.getResources().getString(R.string.noUpdate), new View.OnClickListener() { //取消
//                    @Override
//                    public void onClick(View view) {            //就不更新
////                        sp.putInt("ignore_version",versionCode);
//                        updateDialog.dismiss();
//                    }
//                });
//                updateDialog.show();
////            }
//        }
//        return dialogUtil;
//    }

    /**
     * 忽略的版本号
     * @param versionCode
     * @return
     */
    private static boolean checkUpdate(int versionCode){
        int ignoreCode = sp.getInt("ignore_version",0);
        if(ignoreCode > 0){
            return(versionCode == ignoreCode);
        }
        return false;
    }
}
