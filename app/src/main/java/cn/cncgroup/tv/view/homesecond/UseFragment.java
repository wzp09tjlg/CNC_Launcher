package cn.cncgroup.tv.view.homesecond;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.R;
import cn.cncgroup.tv.bean.AppInfo;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.ui.widget.itemanimator.ItemAppAddOrDelAnimator;
import cn.cncgroup.tv.utils.AppInfoUtils;
import cn.cncgroup.tv.utils.DataComparator;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.utils.Utils;
import cn.cncgroup.tv.view.app.AppShowAllActivity;
import cn.cncgroup.tv.view.homesecond.adapter.UseAdapter;

/**
 * Created by futuo on 2015/8/27.
 */
public class UseFragment extends BaseHomeFragment implements
        OnItemClickListener<AppInfo>,
        AppUninstallBroadcast.AppUninstallListener,
        OnItemFocusListener<AppInfo>, UseAdapter.DoTopOrDelOperator {
    private static final String TAG = "UseFragment";
    private static final String HASTITLE = "HASTITLE";
    private static final String DATAPACKNAME = "datapackname";
    private static final String TOP_QUEUE = "TOP_QUEUE";
    private static final String TOP_QUEUE_NAME = "TOP_QUEUE_NAME";
    private static final String FILEMANAGER_PACKAGE = "cn.cncgroup.filemanager";
    private final String OPEN_APP_ERROR = "打开应用失败!";
    private final int TOTAL_TOP_COUNT = 8;
    private final int USEDATA = 100;
    // widget
    private VerticalGridView mUseGridview;
    // data
    private PackageInfo packInfo;
    private PackageManager pManager;
    private SharedPreferences sharedPreAppTop;

    private Context mContext;
    private boolean firstInit = true;
    private List<AppInfo> mList;
    private DataComparator<AppInfo> mDataComparator;
    private UseAdapter mUseAdapter;

    private Handler mHandler = new Handler();
    private ShadowView mShadowView;
    private Runnable mHideRunable = new Runnable() {
        @Override
        public void run() {
            mShadowView.setVisibility(View.INVISIBLE);
        }
    };

    private boolean isDisplayFileManager = false;

    @Override
    public void onItemClickLister(View view, int position, AppInfo appInfo) {
        ArrayList<AppInfo> appList = AppInfoUtils.getAppsData(mContext);
        Intent intent = new Intent();
        if (position == 6) // Setting
        {
            StatService.onEvent(mContext, "Settings", getActivity().getString(R.string.settings), 1);
            Project.get().getConfig().openSettingHome(getActivity());
        } else if (position == 5) { // All App
            if (appList.size() <= 0) {
                showToast(
                        mContext.getResources().getString(
                                R.string.not_found_app),
                        GlobalConstant.ToastShowTime);
            } else {
                intent.putExtra(HASTITLE, true);
                intent.setClass(getActivity(), AppShowAllActivity.class);
                startActivityForResult(intent, USEDATA);
            }
        } else if (isDisplayFileManager && position == 7) {
            try {
                Utils.startAppByPackageName(getActivity(), FILEMANAGER_PACKAGE);
            } catch (Exception e) {
                showToast(OPEN_APP_ERROR, GlobalConstant.ToastShowTime);
            }
        } else { // someone App
            int indexPosition;
            if (position <= 4)
                indexPosition = position;
            else {
                if (isDisplayFileManager)
                    indexPosition = position - 3;
                else
                    indexPosition = position - 2;
            }

            if (indexPosition < mList.size()) {
                try {
                    Utils.startAppByPackageName(getActivity(),
                            mList.get(indexPosition).getPackageName());
                } catch (Exception e) {
                    showToast(OPEN_APP_ERROR, GlobalConstant.ToastShowTime);
                }
            } else { // to Enter All
                if (appList.size() <= 0) {
                    showToast(
                            mContext.getResources().getString(
                                    R.string.not_found_app),
                            GlobalConstant.ToastShowTime);
                } else {
                    StatService.onEvent(mContext, "App_all", getActivity().getString(R.string.app_all), 1);
                    intent.putExtra(HASTITLE, false);
                    intent.setClass(getActivity(), AppShowAllActivity.class);
                    startActivityForResult(intent, USEDATA);
                }
            }
        }
    }

    @Override
    public void doTopOrDelOperator(int position, int type) {
        if (type == 0) { // do Delete
            mList.remove(position);
            mUseAdapter.deleteAppChange(position);
            doSaveAppTopQueue();
        } else { // do Top
            String packageName = mList.get(position).getPackageName();
            Iterator<AppInfo> iterator = mList.iterator();
            while (iterator.hasNext()) {
                AppInfo appInfo = iterator.next();
                if (appInfo.getPackageName().equals(packageName)) {
                    appInfo.setTag(-1);
                    break;
                }
            }

            Collections.sort(mList, mDataComparator);// 排序

            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setTag(i);
            }
            doSaveAppTopQueue();
            mUseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemFocusLister(View view, int position, AppInfo appInfo, boolean hasFocus) {
        if (hasFocus) {
            if (mUseGridview.getFocusedChild() != null) {
                mHandler.removeCallbacks(mHideRunable);
                mShadowView.moveTo(view);
            }
        } else {
            mHandler.postDelayed(mHideRunable, 16);
        }
        if (position != 0 || !hasFocus)
            return;
        // 检测到用户添加应用的数量≥1时 弹Toast提示
        long toastLastShowTime = Preferences.build(CApplication.getInstance()).getLong(GlobalConstant.LASTSHOWTIM, 1000);
        // 当第一个获得焦点且第一个Item不为空且每天只提示一次
        if (mList.size() > 0 && toastLastShowTime + (24 * 60 * 60 * 1000) < System.currentTimeMillis()) {
            toastLastShowTime = System.currentTimeMillis();
            showToast(
                    mContext.getResources().getString(R.string.use_toast_msg),
                    GlobalConstant.ToastShowTime);
            boolean isSaveSuccess = Preferences.build(CApplication.getInstance()).putLong(GlobalConstant.LASTSHOWTIM, toastLastShowTime);
            if (!isSaveSuccess)
                Preferences.build(CApplication.getInstance()).putLong(GlobalConstant.LASTSHOWTIM, toastLastShowTime);
        }
    }

    @Override
    public void doAppUninstallListener(String packageName) // 判断删除的应用是不是在topQueue中，如果在，就处理，不在就不处理
    {
        if (checkUninstallAppInTopQueue(packageName) > -1) {
            AppInfoUtils.getAppsData(getActivity());
            reSetAppTopQueueData(packageName);
        }
    }

    @Override
    public void doAppInstallListener(String namePackage) {
        // there is no need to do something
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUninstallBroadcast.addUninstallListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_app;
    }

    @Override
    protected void findView(View view) {
        mUseGridview = (VerticalGridView) view.findViewById(R.id.gridview);
        mShadowView = (ShadowView) view.findViewById(R.id.shadow);
    }


    @Override
    protected void initView() {
        mContext = getActivity();
        pManager = getActivity().getPackageManager();
        mList = new ArrayList<AppInfo>();
        mDataComparator = new DataComparator<AppInfo>();
        packInfo = new PackageInfo();
        isDisplayFileManager = checkExistFileManager();
        mUseGridview.setItemAnimator(new ItemAppAddOrDelAnimator());
        mUseAdapter = new UseAdapter(mList, mContext, this, this, this, isDisplayFileManager);
        mUseGridview.setAdapter(mUseAdapter);
        mShadowView.init(25);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppInfoUtils.getAppsData(getActivity());
        if (firstInit) {
            getAppTopQueue();
            firstInit = false;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        hindToast();
        AppUninstallBroadcast.deleteUninstallListener(this);
    }

    @Override
    public void onPause() {
        GlobalToast.cancel();
        super.onPause();
        doSaveAppTopQueue();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == USEDATA) {
            String strPackName = "";
            try {
                strPackName = data.getStringExtra(DATAPACKNAME);
                if (TextUtils.isEmpty(strPackName)) {
                    GlobalToast.makeText(getActivity(), "已存在该应用", GlobalConstant.ToastShowTime).show();
                    return;
                }
                packInfo = getActivity().getPackageManager().getPackageInfo(
                        strPackName, 0);
            } catch (PackageManager.NameNotFoundException e) {
            }
            boolean isExist = false;
            Iterator<AppInfo> iterator = mList.iterator();
            while (iterator.hasNext()) {
                AppInfo appInfo = iterator.next();
                if (appInfo.getPackageName().equals(strPackName)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                addInfo();
            }
        }
    }

    private void addInfo() {
        int tag = mList.size();
        AppInfo appInfo = new AppInfo();
        appInfo.setAppName(pManager.getApplicationLabel(
                packInfo.applicationInfo).toString());
        appInfo.setPackageName(packInfo.packageName);
        appInfo.setIcon(pManager.getApplicationIcon(packInfo.applicationInfo));
        appInfo.setTag(tag);
        mList.add(appInfo);
        mUseAdapter.addAppChange(tag);
        doSaveAppTopQueue();
    }

    private void doSaveAppTopQueue() {
        sharedPreAppTop = getActivity().getSharedPreferences(TOP_QUEUE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreAppTop.edit();
        for (int i = 0; i < TOTAL_TOP_COUNT; i++) {
            editor.putString(TOP_QUEUE_NAME + "" + i, "");// 首先清空一下之前保存的列表
            editor.commit();
        }
        if (mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                editor.putString(TOP_QUEUE_NAME + "" + i, mList.get(i)
                        .getPackageName());
                editor.commit();
            }
        }
    }

    private void getAppTopQueue() {
        sharedPreAppTop = getActivity().getSharedPreferences(TOP_QUEUE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreAppTop.edit();
        String appPackageName = "";
        int tempSize = 0;
        if (isDisplayFileManager)
            tempSize = 7;
        else
            tempSize = 8;
        for (int i = 0; i < tempSize; i++) {
            if (sharedPreAppTop.contains(TOP_QUEUE_NAME + "" + i)) {
                appPackageName = sharedPreAppTop.getString(TOP_QUEUE_NAME + ""
                        + i, "");
                AppInfo appInfo = AppInfoUtils.getAppInfo(appPackageName);
                if (appInfo != null) {
                    mList.add(appInfo);
                }
            } else {
                editor.putString(TOP_QUEUE_NAME + "" + i, "");
                editor.commit();
            }
        }
    }

    private int checkUninstallAppInTopQueue(String packageName) { // to check TopQueue exist the delete's App.
        sharedPreAppTop = getActivity().getSharedPreferences(TOP_QUEUE,Context.MODE_PRIVATE);
        String topAppPackageName = "";
        for (int i = 0; i < TOTAL_TOP_COUNT; i++) {
            topAppPackageName = sharedPreAppTop.getString(TOP_QUEUE_NAME + ""
                    + i, "");
            if (packageName.equals(topAppPackageName)) {
                return i;
            }
        }
        return -1;
    }

    private void reSetAppTopQueueData(String packageName) {
        Iterator<AppInfo> iterator = mList.iterator();
        while (iterator.hasNext()) {
            AppInfo appInfo = iterator.next();
            if (appInfo.getPackageName().equals(packageName)) {
                iterator.remove();
            }
        }
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setTag(i);
        }
        doSaveAppTopQueue();
        mUseAdapter.notifyDataSetChanged();
    }

    @Override
    void arrowScroll(boolean isLeft, boolean isTop) {
        mUseGridview.findViewHolderForAdapterPosition(0).itemView.requestFocus();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
    }

    public void clearFouse() {
        if (getView() != null) {
            getView().requestFocus();
        }
    }

    private boolean checkExistFileManager() {
        if (AppInfoUtils.getAppInfo(FILEMANAGER_PACKAGE) != null)
            return true;
        else
            return false;
    }
}