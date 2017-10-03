package cn.cncgroup.tv.view.app;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.AppUnloadAdapter;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.bean.AppInfo;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.utils.SystemAndAppMsgUtil;

/**
 * 足迹 Fragmnt
 * Created by zhangguang on 2015/6/16.
 */
public class UnloadFragment extends BaseFragment implements OnItemClickListener<AppInfo>, OnItemFocusListener<AppInfo> {
	private static final String ATTR_PACKAGE_STATS = "PackageStats";
	private Handler cacheHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			String packageName = (String) msg.obj;
			if (msg.arg1 == 2)
			{
				getPkgInfo(packageName);
				showToast(
				        getResources().getString(
				                R.string.appset_cache_cleaned_succeed),
				        GlobalConstant.ToastShowTime);
			}
			else if (msg.arg1 == 1)
			{
				PackageStats newPs = msg.getData().getParcelable(
				        ATTR_PACKAGE_STATS);
				for (int i = 0; i < appLists.size(); i++)
				{
					if (packageName.equals(appLists.get(i).getPakageName()))
					{
						appLists.get(i).setCodesize(
						        Formatter.formatFileSize(getActivity(),
						                newPs.codeSize + newPs.dataSize));
						appLists.get(i).setCachesize(
						        Formatter.formatFileSize(getActivity(),
						                newPs.cacheSize));
						appUnloadAdapter.notifyDataSetChanged();
					}
				}
			}

		}
	};
	private static AppUnloadAdapter appUnloadAdapter;
    private ArrayList<AppInfo> appLists = new ArrayList<AppInfo>();
    private VerticalGridView appGridViewUnload;
    private Handler mHandler = new Handler(Looper.getMainLooper());
	private boolean firstIn = true;

	public void getPkgInfo(String pkg)
	{
		PackageManager pm = getActivity().getPackageManager();
		try
		{
			Method getPackageSizeInfo = pm.getClass().getMethod(
			        "getPackageSizeInfo", String.class,
			        IPackageStatsObserver.class);
			getPackageSizeInfo.invoke(pm, pkg, new PkgSizeObserver());
		} catch (Exception e)
		{
			// LogUtil.i(TAG, "没有拿到缓存数据");
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.app_set_fragment;
    }

    @Override
    protected void findView(View view) {
        appGridViewUnload = (VerticalGridView) view.findViewById(R.id.gv_appset_appunload);

    }

    @Override
    protected void initView() {
        appGridViewUnload.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_ITEM);
        appLists = new ArrayList<AppInfo>();


    }

    @Override
    public void onItemClickLister(View view, int position, AppInfo appMsgBean) {
        final String packageName = appLists.get(position).getPakageName();
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,packageURI);
        startActivity(uninstallIntent);
        Preferences.build(getActivity(), GlobalConstant.MYAPPSEQUENCE).removeKey(packageName);
    }

    @Override
    public void onItemFocusLister(View view, int position, AppInfo appInfo, boolean hasFocus) {
        if (hasFocus) {
            view.findViewById(R.id.rl_focus_msg).setVisibility(View.VISIBLE);
            view.setBackgroundResource(R.drawable.bgshap);
        } else {
            view.findViewById(R.id.rl_focus_msg).setVisibility(View.INVISIBLE);
            view.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        appLists = SystemAndAppMsgUtil.getAppsData(getActivity(), mHandler);
        if (null == appLists || appLists.size() == 0) {
			showToast("未查找到非系统应用", GlobalConstant.ToastShowTime);
        }
        for (int i = 0; i < appLists.size(); i++) {
            getPkgInfo(appLists.get(i).getPackageName());
        }
        appUnloadAdapter = new AppUnloadAdapter(appLists, this, this);
        appGridViewUnload.setAdapter(appUnloadAdapter);


        if (firstIn){
            firstIn = false;
        }else {
            appGridViewUnload.requestFocus();
        }

    }

	public class PkgSizeObserver extends IPackageStatsObserver.Stub
	{
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
		{
			Message msg = cacheHandler.obtainMessage();
			msg.obj = pStats.packageName;
			msg.arg1 = 1;
			Bundle data = new Bundle();
			data.putParcelable(ATTR_PACKAGE_STATS, pStats);
			msg.setData(data);
			cacheHandler.sendMessage(msg);
		}
	}
}
