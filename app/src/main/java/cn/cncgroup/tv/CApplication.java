package cn.cncgroup.tv;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.baidu.mobstat.StatService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.InputStream;
import java.util.ArrayList;

import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.bean.AppInfo;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.conf.model.cibn.modle.CIBNXMLLoginData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.utils.NetUtil;
import cn.cncgroup.tv.utils.CrashHandler;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.utils.ThreadPool;
import cn.cncgroup.tv.utils.Utils;
import cn.cncgroup.tv.view.weather.Bean.WeatherBean;
import cn.cncgroup.tv.view.weather.city.AreaModel;
import cn.cncgroup.tv.view.weather.city.CitySaxParseHandler;
import cn.cncgroup.tv.view.weather.city.ProvicneModel;

/**
 * Created by zhang on 2015/4/27. 自定义Application
 */
public class CApplication extends Application {
    public static CIBNXMLLoginData sCIBNXMLLoginData;
    public static ArrayList<AppInfo> openHistoryLists = new ArrayList<AppInfo>();
    public static WeatherBean weatherBean = null;
    /**
     * 是否是launcher，以后通过配置文件修改
     */
    public static boolean isLauncher = false;
    public static Application mApplication;
    private static ArrayList<ProvicneModel> mProvicneModels;
    private static ArrayList<AreaModel> mAreaModels;
    private ArrayList<BaseActivity> mActivityList = new ArrayList<BaseActivity>();

    public static ArrayList<ProvicneModel> getProvicneModels() {
        return mProvicneModels;
    }

    /**
     * 首页推荐倾斜角度
     */
    public static final int CoverFlowVertical_angle_inclination = 30;
    public static final int CoverFlowHorizontal_angle_inclination = 0;

    /**
     * 添加打开应用历史记录数据
     */
    public static void addAppOpenLists(AppInfo app) {

        for (int i = 0; i < openHistoryLists.size(); i++) {
            if (openHistoryLists.get(i).getPackageName()
                    .equals(app.getPackageName()))
                openHistoryLists.remove(i);
        }
        if (openHistoryLists.size() >= 10) {
            for (int i = 9; i < openHistoryLists.size(); i++) {
                openHistoryLists.remove(i);
            }
        }
        openHistoryLists.add(0, app);
    }

    public static Application getInstance() {
        return mApplication;
    }

    public static RefWatcher getRefWatcher(Context context) {
        CApplication application = (CApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        // 初始化Fresco控件
        Fresco.initialize(this);
        // 开启Fresco的log
        // FLogDefaultLoggingDelegate.getInstance().setMinimumLoggingLevel(Log.VERBOSE);
        // 初始化Project
        Project.setupWithContext(this);
        // 初始化线程池
        ThreadPool.initialize();
        // 数据库的初始化
        ActiveAndroid.initialize(this);
        // 初始化缓存
        NetworkManager.getInstance().initDiskCache(getApplicationContext());
        // 初始化网络状态
        NetworkManager.CONNECTION_TYPE = NetUtil
                .getActiveNetwork(getApplicationContext());

        // 设置是否以mac为主key来统计用户数。
        StatService.setForTv(this, true);
        // 关闭日志
        // StatService.setDebugOn(false);
        //统计设置渠道商
        StatService.setAppChannel(this, Project.get().getConfig().getManufacturerModel(), true);
        // 设置异常处理handler
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));

        isLauncher = Project.get().getConfig().isLauncher();
        if (TextUtils.isEmpty(Preferences.build(getApplicationContext())
                .getString(GlobalConstant.WEATHERCITY, ""))) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String string = Utils.GetNetIp();
                    Preferences.build(getApplicationContext()).putString(
                            GlobalConstant.WEBNETIP, string);
                }
            }).start();
        }
        mAreaModels = new ArrayList<AreaModel>();
        initProvicneModels();
        mApplication = this;
    }

    /**
     * 初始化城市列表
     */
    private void initProvicneModels() {
        try {
            InputStream in = getAssets().open(GlobalConstant.FILE_CITY_NAME);
            mProvicneModels = CitySaxParseHandler.getProvicneModel(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addActivity(BaseActivity activity) {
        mActivityList.add(activity);
    }

    public void removeActivity(BaseActivity activity) {
        mActivityList.remove(activity);
    }

    public void finishAllActivity() {
        for (BaseActivity activity : mActivityList) {
            if (null != activity) {
                activity.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 数据库的退出
        ActiveAndroid.dispose();
    }
}
