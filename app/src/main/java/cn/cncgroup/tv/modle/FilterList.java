package cn.cncgroup.tv.modle;

import java.util.ArrayList;

/**
 * Created by zhangbo on 15-6-18.
 */
public class FilterList
{
	private ArrayList<FilterList> result;
	private int id;
	private String name;
	private String upName;

	public String getUpName()
	{
		return upName;
	}

	public void setUpName(String upName)
	{
		this.upName = upName;
	}

	public ArrayList<FilterList> getResult()
	{
		return result;
	}

	public void setResult(ArrayList<FilterList> result)
	{
		this.result = result;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
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
}
