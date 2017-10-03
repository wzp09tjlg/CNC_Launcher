package cn.cncgroup.tv.modle;

import java.util.ArrayList;

/**
 * Created by zhangbo on 15-6-18.
 */
public class FilterListData
{
	private boolean success;
	private ArrayList<FilterList> result;

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public ArrayList<FilterList> getResult()
	{
		return result;
	}

	public void setResult(ArrayList<FilterList> result)
	{
		this.result = result;
	}
}
