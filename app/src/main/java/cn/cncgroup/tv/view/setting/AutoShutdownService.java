package cn.cncgroup.tv.view.setting;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;

import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.utils.ThreadPool;

/**
 * Created by Wu on 2015/11/19.
 */
public class AutoShutdownService extends Service {
    private static final String TAG = "AutoShutdownService";
    //data
    private String sHour;
    private String sMinute;
    private boolean isShutdown = false;
    private Preferences shutdownPref;
    private AutoShutdownRunnable mAutoShutdownRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        shutdownPref = Preferences.build(this);
        isShutdown = shutdownPref.getBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, false);
        sHour = shutdownPref.getString(GlobalConstant.AUTO_SHUTDOWN_HOUR, "23");
        sMinute = shutdownPref.getString(GlobalConstant.AUTO_SHUTDOWN_MINUTE, "00");
        mAutoShutdownRunnable = null;
        mAutoShutdownRunnable = new AutoShutdownRunnable(sHour, sMinute);
        ThreadPool.execute(mAutoShutdownRunnable);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isShutdown = false;
    }

    class AutoShutdownRunnable implements Runnable {
        private String sHour;
        private String sMinute;

        public AutoShutdownRunnable(String hour, String minute) {
            sHour = hour;
            sMinute = minute;
        }

        @Override
        public void run() {
            int hour = 0;
            int minute = 0;
            Calendar calendar = null;
            Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
            while (isShutdown) {
                calendar = Calendar.getInstance();
                hour = calendar.getTime().getHours();
                minute = calendar.getTime().getMinutes();

                if (hour == Integer.parseInt(sHour) && minute == Integer.parseInt(sMinute)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);  //TODO 需要获得root权限才能调用
//                    ((PowerManager) getSystemService(Context.POWER_SERVICE))
//                            .goToSleep(SystemClock.uptimeMillis());
                    isShutdown = false;
                    stopSelf();
                    break;
                }
                try {
                    Thread.sleep(60 * 1000);
                } catch (Exception e) {
                }
            }
        }
    }
}
