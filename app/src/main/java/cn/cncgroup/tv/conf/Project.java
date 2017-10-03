package cn.cncgroup.tv.conf;

import android.content.Context;
import android.util.Log;

import java.io.Closeable;
import java.io.InputStream;
import java.util.Properties;

import cn.cncgroup.tv.utils.LogUtil;

/**
 * Created by JIF on 2015/6/1.
 */
public class Project
{
	private static Project sInstance = null;
	private IAppConfig appConfig = null;
	private Context mContext = null;

	private Project(Context context)
	{
		mContext = context;
	}

	public static Project get()
	{
		return sInstance;
	}

	public synchronized static void setupWithContext(Context context)
	{
		sInstance = new Project(context);
		sInstance.setup();
	}

	private static String getConfigClassName(String model)
	{
		StringBuilder builder = new StringBuilder("cn.cncgroup.tv.conf.model.");
		builder.append(model).append(".AppConfig");
		Log.i("Project", "getConfigClassName " + builder.toString());
		return builder.toString();
	}

	private static void safeClose(Closeable closeable)
	{
		try
		{
			if (closeable != null)
			{
				closeable.close();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public IAppConfig getConfig()
	{
		if (appConfig == null)
		{
			throw new IllegalStateException(
			        "The Project instance has not been setupped.");
		}
		return appConfig;
	}

	private void setup()
	{
		InputStream stream = null;
		Properties props = new Properties();
		try
		{
			stream = mContext.getAssets().open("app.cfg");
			props.load(stream);
			LogUtil.i("App config:", props.toString());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		safeClose(stream);
		config(mContext, props);
	}

	private boolean config(Context context, Properties props)
	{
		checkConfigArguments(context, props);
		// 针对各个设备的差异化，进行各自的初始化
		String defaultModel = "cibn";
		String customer = props.getProperty("APK_MODEL", defaultModel);
		try
		{
			appConfig = (IAppConfig) Class
			        .forName(getConfigClassName(customer)).newInstance();
		} catch (Exception e)
		{
			try
			{
				appConfig = (IAppConfig) Class.forName(
				        getConfigClassName(defaultModel)).newInstance();
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}

		return appConfig.initialize(context, props);
	}

	private void checkConfigArguments(Context context, Properties props)
	{
		if ((context == null) || (props == null))
		{
			throw new NullPointerException(
			        "The config properties should not be null.");
		}
		else if (appConfig != null)
		{
			throw new IllegalStateException(
			        "The config object has been already initialized.");
		}
	}

}
