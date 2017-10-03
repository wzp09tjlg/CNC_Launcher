package cn.cncgroup.tv.conf;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Properties;

import cn.cncgroup.tv.utils.VersionsInfo;
import cn.cncgroup.tv.view.setting.SettingActivity;
import cn.cncgroup.tv.view.setting.SettingHomeActivity;

/**
 * Created by JIF on 2015/6/1.
 */
public abstract class AbsAppConfig implements IAppConfig {
    private static final String TAG = "AbsAppConfig";
    protected boolean hasSystemPermission = false;
    private String svnRevision = "0";
    protected String model = "cibn";
    protected Context context;
    protected boolean isLauncher = false;

    private static boolean getBoolProperty(Properties props, String name,
                                           boolean defValue) {
        String TRUE = "true";
        return TRUE.equalsIgnoreCase(
                props.getProperty(name, defValue ? TRUE : "false"));
    }

    @Override
    public boolean initialize(Context context, Properties props) {
        this.context = context;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver()
                    .query(Uri
                                    .parse("content://android.provider.calibration/calibration_horizontal"),
                            null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                hasSystemPermission = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            hasSystemPermission = false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        model = props.getProperty("APK_MODEL", model);
        svnRevision = props.getProperty("APK_SVNREVISION", svnRevision);
        isLauncher = props.getProperty("APK_ISLAUNCHER", "false").equals("true");
        return true;
    }

    @Override
    public boolean hasSystemPermission() {
        return hasSystemPermission;
    }

    @Override
    public boolean isLauncher() {
        return isLauncher;
    }

    @Override
    public String getRevision() {
        String revision = "1.0";
        String versionName = VersionsInfo.getAppVsersionName(context,
                context.getPackageName());
        Log.i("AbsAppConfig", "versionName = " + versionName);
        revision = versionName + "." + svnRevision;
        Log.i("AbsAppConfig", "revision = " + revision);
        return revision;
    }

    @Override
    public boolean isEnableDolby() {
        return false;
    }

    @Override
    public boolean isEnableH265() {
        return false;
    }

    @Override
    public int getDefaultDefinitonValue() {
        return 4; // 默认720P
    }

    @Override
    public String getManufacturerModel() {
        return model;
    }

    @Override
    public void openSettingHome(Context context) {
        if (isLauncher) {
            context.startActivity(new Intent(context, SettingHomeActivity.class));
        } else {
            context.startActivity(new Intent(context, SettingActivity.class));
        }
    }
}
