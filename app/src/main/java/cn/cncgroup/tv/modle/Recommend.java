package cn.cncgroup.tv.modle;

import android.text.TextUtils;

/**
 * Created by zhang on 2015/5/27.
 */
public class Recommend
{
	private int index;
	private String id;
	private String name;
	private String type;
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

	public String getId()
	{
		return id;
	}

	public void setId(String id)
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

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
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
		if (!TextUtils.isEmpty(url))
		{
			return url.substring(url.lastIndexOf('/') + 1,
			        url.lastIndexOf('.'));
		}
		return "";
	}
}
