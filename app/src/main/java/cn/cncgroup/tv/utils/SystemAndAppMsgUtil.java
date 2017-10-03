package cn.cncgroup.tv.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.bean.AppInfo;
import cn.cncgroup.tv.ui.widget.CustomDialog;

/**
 * Created by jinwenchao on 15/5/13.
 * 该工具用来获取一些系统的信息
 * <p/>
 * 所安装的app的一些基本信息
 */
public class SystemAndAppMsgUtil {

//    public static class cacheBean {
//        private static long cachesize; //缓存大小
//        private static long datasize;  //数据大小
//        private static long codesize;  //应用程序大小
//    }

    public static ArrayList<AppInfo> getAppsData(Context context,Handler mHandler) {
        PackageManager pManager = context.getPackageManager();
        // 应用的信息
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        // 获取安卓包信息
        List<PackageInfo> pakList = pManager.getInstalledPackages(0);
        for (int i = 0; i < pakList.size(); i++) {
            PackageInfo pak = pakList.get(i);
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                AppInfo appItem = new AppInfo();
                appItem.setAppName(pManager.getApplicationLabel(
                        pak.applicationInfo).toString());// 应用的名字
                appItem.setPakageName(pak.packageName); // 应用的包名
                if (context.getPackageName().equals(pak.packageName)) {
                    continue;
                }
                String versionName = "";
                try {
                    if (!TextUtils.isEmpty(pak.versionName)) {
                        versionName = pak.versionName;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                appItem.setVersionName(versionName); // 应用的版本名字
                appItem.setVersionCode("" + pak.versionCode); // 应用的版本的Code
                appItem.setIcon(pManager
                        .getApplicationIcon(pak.applicationInfo));
                appItem.setTag(i + 1); // 显示的数顺序，第一显示的顺序就是1
                appItem.setCodesize("");
                appItem.setCachesize("");
//                try {
//                    getpkginfo(pak.packageName, context);
//                    appItem.setCodesize(formatFileSize(cacheBean.codesize));
//                    appItem.setCachesize(formatFileSize(cacheBean.cachesize));
//                } catch (Exception e) {
//
//                }

                appList.add(appItem);
            }
        }
        return appList;
    }

    public static String getSDTotalSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();




        return convertStorage(blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();

        return convertStorage(blockSize * availableBlocks);
    }

    // storage, G M K B
    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f G", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f M" : "%.1f M", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f K" : "%.1f K", f);
        } else
            return String.format("%d B", size);
    }


    public static int getSDcardPregress(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long availableBlocks = stat.getAvailableBlocks();
        long totalBlocks = stat.getBlockCount();
        int pregress = 0;
        if (totalBlocks != 0){
            pregress = (int) (100 * ((float) availableBlocks / (float) totalBlocks));
        }
        return pregress;
    }

    /**
     * 检查某个App是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkAPP(Context context, String packageName) throws Exception{
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            boolean hasInstalled = false;
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> list = pm
                    .getInstalledPackages(PackageManager.PERMISSION_GRANTED);
            for (PackageInfo p : list) {
                if (packageName != null && packageName.equals(p.packageName)) {
                    hasInstalled = true;
                    break;
                }
            }
            return hasInstalled;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 启动应用未安装，提示用户下载
     */
//    public static DownloadUtil dialogShow(final Context ctx, final String apkUrl, final String appName)
//    {
//        CustomDialog customDialog = new CustomDialog(ctx);
//        final DownloadUtil dialogUtil = new DownloadUtil();
//        customDialog.setMessage(ctx.getResources().getString(R.string.message));
//        customDialog.setPositiveButton(ctx.getResources().getString(R.string.confirm),
//                new View.OnClickListener() {
//                    public static final String TAG = "DownloadUtil";
//
//                    @Override
//                    public void onClick(View view) {
////                        dialogUtil = new DownloadUtil();
//                        dialogUtil.checkUpdata(ctx, apkUrl, appName);
//                        Log.i(TAG, "'dialogUtil:" + dialogUtil);
//                    }
//                });
//        customDialog.setNegativeButton(ctx.getResources().getString(R.string.cancel),
//                new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View view)
//                    {
//
//                    }
//                });
//        customDialog.show();
//        return dialogUtil;
//    }

//    public static void getpkginfo(String pkg, Context context) {
//        PackageManager pm = context.getPackageManager();
//        try {
//            Method getPackageSizeInfo = pm.getClass()
//                    .getMethod("getPackageSizeInfo", String.class,
//                            IPackageStatsObserver.class);
//            getPackageSizeInfo.invoke(pm, pkg,
//                    new PkgSizeObserver());
//        } catch (Exception e) {
//            System.out.println("这里是有问题的，有可能拿不到缓存数据");
//        }
//    }

//    public static class PkgSizeObserver extends IPackageStatsObserver.Stub {
//        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
//            cacheBean.cachesize = pStats.cacheSize;
//            cacheBean.codesize = pStats.codeSize;
//            cacheBean.datasize = pStats.dataSize;
//
//        }
//    }

//    public static String formatFileSize(long length) {
//        String result = null;
//        int sub_string = 0;
//        if (length >= 1073741824) {
//            sub_string = String.valueOf((float) length / 1073741824).indexOf(
//                    ".");
//            result = ((float) length / 1073741824 + "000").substring(0,
//                    sub_string + 3)
//                    + "GB";
//        } else if (length >= 1048576) {
//            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
//            result = ((float) length / 1048576 + "000").substring(0,
//                    sub_string + 3)
//                    + "MB";
//        } else if (length >= 1024) {
//            sub_string = String.valueOf((float) length / 1024).indexOf(".");
//            result = ((float) length / 1024 + "000").substring(0,
//                    sub_string + 3)
//                    + "KB";
//        } else if (length < 1024)
//            result = Long.toString(length) + "B";
//        return result;
//    }




}
