package cn.cncgroup.tv.bean;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

/**
 * Created by jinwenchao123 on 15/5/13.
 */
public class AppInfo implements Serializable
{
	public String appName; // 应用的名字
	public String packageName; // 应用的包名
	public String versionName; // 版本名字
	public String versionCode; // 版本号
	public int tag; // tag值
	public Drawable icon; // 图标
	public String codesize;
	public String cachesize;
	public long cachesizeLong;

	public AppInfo()
	{
	}

	public AppInfo(String _appName, String _packageName, String _versionName,
	        String _versionCode, int _tag, Drawable _icon)
	{
		this.appName = _appName;
		this.packageName = _packageName;
		this.versionName = _versionName;
		this.versionCode = _versionCode;
		this.tag = _tag;
		this.icon = _icon;
	}

	public AppInfo(AppInfo appInfo)
	{
		this.appName = appInfo.appName;
		this.packageName = appInfo.packageName;
		this.versionName = appInfo.versionName;
		this.versionCode = appInfo.versionCode;
		this.tag = appInfo.tag;
		this.icon = appInfo.icon;
	}

	public long getCachesizeLong()
	{
		return cachesizeLong;
	}

	public void setCachesizeLong(long cachesizeLong)
	{
		this.cachesizeLong = cachesizeLong;
	}

	public String getCachesize()
	{
		return cachesize;
	}

	public void setCachesize(String cachesize)
	{
		this.cachesize = cachesize;
	}

	public String getCodesize()
	{
		return codesize;
	}

	public void setCodesize(String codesize)
	{
		this.codesize = codesize;
	}

	public String getAppName()
	{
		return appName;
	}

	public void setAppName(String _appName)
	{
		this.appName = ("".equals(_appName) || _appName == null) ? ""
		        : _appName;
	}

	public String getPakageName()
	{
		return packageName;
	}

	public void setPakageName(String _pakageName)
	{
		this.packageName = ("".equals(_pakageName) || _pakageName == null) ? ""
		        : _pakageName;
	}

	public String getVersionName()
	{
		return versionName;
	}

	public void setVersionName(String _versionName)
	{
		this.versionName = ("".equals(_versionName) || _versionName == null) ? ""
		        : _versionName;
	}

	public String getVersionCode()
	{
		return versionCode;
	}

	public void setVersionCode(String _versionCode)
	{
		this.versionName = ("".equals(_versionCode) || _versionCode == null) ? ""
		        : _versionCode;
	}

	public Drawable getIcon()
	{
		return icon;
	}

	public void setIcon(Drawable _icon)
	{
		this.icon = _icon;
	}

	public int getTag()
	{
		return tag;
	}

	public void setTag(int tag)
	{
		this.tag = tag;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

}
