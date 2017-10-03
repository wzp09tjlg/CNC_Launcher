package cn.cncgroup.tv.modle;

import java.util.ArrayList;

/**
 * Created by zhang on 2015/5/27.
 */
public class CategoryData
{
	private ArrayList<Category> data;
	private int count;

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public ArrayList<Category> getData()
	{
		return data;
	}

	public void setData(ArrayList<Category> data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		return "CategoryData{" + "data=" + data + ", count=" + count + '}';
	}
}
