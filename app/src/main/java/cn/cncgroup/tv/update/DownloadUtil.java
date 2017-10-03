package cn.cncgroup.tv.update;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.widget.CustomDialog;
import cn.cncgroup.tv.update.dbcontrol.FileHelper;
import cn.cncgroup.tv.update.dbcontrol.bean.SQLDownLoadInfo;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.NetworkUtils;
import cn.cncgroup.tv.utils.SystemInfoUtils;

/**
 * Created by zhangguang on 2015/5/27.
 */
public class DownloadUtil {
    private static final String TAG = "DownloadUtil";
    private TextView mPercent;
    private CustomDialog mProcessDialog;
    private RelativeLayout positive, negative;  //后台下载和取消按钮
    private ProgressBar mProcess;
    private TextView mSpeed;
    private long startTime = 0;
    private long startSize = 0;


    /**
     * 检查更新
     *
     * @param context 上下文
     * @param url     apk Url地址
     * @param AppName 应用名
     */
    public void checkUpdata(final Context context, final String url, String AppName, String version) {
        Log.d(TAG,"SystemInfoUtils.isServiceRunning(context,\"cn.cncgroup.tv.update.DownLoadService\")="+SystemInfoUtils.isServiceRunning(context,"cn.cncgroup.tv.update.DownLoadService"));
//        if(!)
        final DownLoadManager manager = DownLoadService.getDownLoadManager();
        manager.changeUser(GlobalConstant.APPUSER);
        manager.setSupportBreakpoint(GlobalConstant.SupportBreakpoint);
        // 检查网络连接
        if (!NetworkUtils.isNetworkUp(context)) {
            return;
        }
        mProcessDialog = new CustomDialog(context, true);
        View view = View.inflate(context, R.layout.layout_dialog_new, null);
        positive = (RelativeLayout) view.findViewById(R.id.dialog_rl_positive);
        negative = (RelativeLayout) view.findViewById(R.id.dialog_rl_negative);
        mSpeed = (TextView) view.findViewById(R.id.download_speed);
        mPercent = (TextView) view.findViewById(R.id.download_percent);
        mProcessDialog.setCustomView(view);
        mProcess = (ProgressBar) view.findViewById(R.id.process);
        mProcessDialog.show();
        if (manager.getAllTask().size() == 0) {
            manager.addTask(GlobalConstant.APPUSER, url, GlobalConstant.UPDATEAPPNAME);
        } else {
            manager.getAllTask().get(0).setOnDownloading(true);
            manager.startTask(manager.getAllTask().get(0).getTaskID());
        }
//        mProcessDialog
//                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
////                        mProcessDialog.dismiss();
////                        manager.getAllTask().get(0).setOnDownloading(false);
////                        manager.stopTask(manager.getAllTask().get(0).getTaskID());
//                        Log.e("setOnDismissListener","setOnDismissListener+++++++++++++++++++++");
//                    }
//                });
        //后台下载操作
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProcessDialog.hide();
            }
        });
        //取消操作
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProcessDialog.dismiss();
                if (manager.getAllTask() != null && manager.getAllTask().size() > 0) {
                    manager.getAllTask().get(0).setOnDownloading(false);
                    manager.stopTask(manager.getAllTask().get(0).getTaskID());
                }

            }
        });
        mProcessDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProcessDialog.dismiss();
                if (manager.getAllTask() != null && manager.getAllTask().size() > 0) {
                    manager.getAllTask().get(0).setOnDownloading(false);
                    manager.stopTask(manager.getAllTask().get(0).getTaskID());
                }

            }
        });
        manager.setSingleTaskListener(GlobalConstant.APPUSER, new cn.cncgroup.tv.update.DownLoadListener() {
            @Override
            public void onStart(SQLDownLoadInfo sqlDownLoadInfo) {
            }

            @Override
            public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {


                manager.getAllTask().get(0).setDownFileSize(sqlDownLoadInfo.getDownloadSize());
                manager.getAllTask().get(0).setFileSize(sqlDownLoadInfo.getFileSize());
                mProcess.setMax((int) sqlDownLoadInfo.getFileSize());
                mProcess.setProgress((int) sqlDownLoadInfo.getDownloadSize());
                mPercent.setText(manager.getAllTask().get(0).getProgress() + "%");
                mSpeed.setText((sqlDownLoadInfo.getDownloadSize() - startSize) *1000/ (System.currentTimeMillis() - startTime)/1024+"K/S");
                startTime = System.currentTimeMillis();
                startSize = sqlDownLoadInfo.getDownloadSize();

            }

            @Override
            public void onStop(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
            }

            @Override
            public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
                manager.getAllTask().get(0).setOnDownloading(false);
            }

            @Override
            public void onSuccess(SQLDownLoadInfo sqlDownLoadInfo) {
                //根据监听到的信息查找列表相对应的任务，删除对应的任务
                //启动安装apk
                installApk(context);
                String taskID = manager.getAllTask().get(0).getTaskID();
                manager.deleteTask(taskID);

            }
        });
    }



    /**
     * 安装apk
     */
    private void installApk(Context context) {
        File apkfile = new File(FileHelper.getFileDefaultPath() + "/" + GlobalConstant.UPDATEAPPNAME);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
    }
}
