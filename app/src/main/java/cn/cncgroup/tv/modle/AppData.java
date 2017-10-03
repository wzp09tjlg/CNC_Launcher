package cn.cncgroup.tv.modle;

import java.util.ArrayList;

/**
 * Created by zhang on 2015/5/27.
 */
public class AppData
{
	private ArrayList<App> data;
	private int count;

	public ArrayList<App> getData()
	{
		return data;
	}

	public void setData(ArrayList<App> data)
	{
		this.data = data;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
}
