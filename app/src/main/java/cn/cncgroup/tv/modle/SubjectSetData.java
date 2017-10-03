package cn.cncgroup.tv.modle;

import java.util.ArrayList;

/**
 * Created by zhang on 2015/10/21.
 */
public class SubjectSetData
{
	private int total;
	private int pageNo;
	private int pageCount;
	private int pageSize;
	private ArrayList<SubjectSet> result;

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public int getPageNo()
	{
		return pageNo;
	}

	public void setPageNo(int pageNo)
	{
		this.pageNo = pageNo;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public ArrayList<SubjectSet> getResult()
	{
		return result;
	}

	public void setResult(ArrayList<SubjectSet> result)
	{
		this.result = result;
	}

	@Override
	public String toString() {
		return "SubjectSetData{" +
				"total=" + total +
				", pageNo=" + pageNo +
				", pageCount=" + pageCount +
				", pageSize=" + pageSize +
				", result=" + result +
				'}';
	}
}
