package cn.cncgroup.tv.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.WelcomeActivity;

/**
 * Created by zhang on 2015/5/28.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler
{

	private CApplication mCApplication;

	public CrashHandler(CApplication application)
	{
		mCApplication = application;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable)
	{
		throwable.printStackTrace();
		if (CApplication.isLauncher)
		{
			Intent intent = new Intent(mCApplication.getApplicationContext(),
					WelcomeActivity.class);
			PendingIntent restartIntent = PendingIntent.getActivity(
			        mCApplication.getApplicationContext(), 0, intent,
			        PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager mgr = (AlarmManager) mCApplication
			        .getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent);
		}
		mCApplication.finishAllActivity();
	}

}
