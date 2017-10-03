package cn.cncgroup.tv.modle;

import java.io.Serializable;
import java.util.ArrayList;

import cn.cncgroup.tv.conf.Project;

/**
 * Created by zhang on 2015/5/27.
 */
public class Category implements Serializable
{
	private int index;
	private String id;
	private String name;
	private String pic;
	private ArrayList<Category> data;
	public String contentUrl;

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public String getContentUrl()
	{
		return Project.get().getConfig().getContentUrl(this);
	}

	public void setContentUrl(String contentUrl)
	{
		this.contentUrl = contentUrl;
	}

	public ArrayList<Category> getData()
	{
		return data;
	}

	public void setData(ArrayList<Category> data)
	{
		this.data = data;
	}

	public String getPic()
	{
		return pic;
	}

	public void setPic(String pic)
	{
		this.pic = pic;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		return "Category{" + "index=" + index + ", id='" + id + '\''
		        + ", name='" + name + '\'' + ", pic='" + pic + '\'' + ", data="
		        + data + ", contentUrl='" + contentUrl + '\'' + '}';
	}
}
