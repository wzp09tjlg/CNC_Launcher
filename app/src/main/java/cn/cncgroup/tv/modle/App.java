package cn.cncgroup.tv.modle;

import tv.tv9ikan.app.config.Apk;

import android.text.TextUtils;

/**
 * Created by zhang on 2015/5/27.
 */
public class App
{
	private int index;
	private Apk id;
	private String name;
	private String pic;
	private String url;

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public Apk getId()
	{
		return id;
	}

	public void setId(Apk id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPic()
	{
		return pic;
	}

	public void setPic(String pic)
	{
		this.pic = pic;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getPackageName()
	{
		if (id != null)
		{
			return id.pkgName;
		}
		if (!TextUtils.isEmpty(url))
		{
			return url.substring(url.lastIndexOf('/') + 1,
			        url.lastIndexOf('.'));
		}
		return "";
	}
}
