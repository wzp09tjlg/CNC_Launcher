package cn.cncgroup.tv.modle;

import java.util.ArrayList;

/**
 * Created by zhangbo on 15-6-19.
 */
public class SearchResult
{
	private int pageCount; //服务器一共几页
	private int pageNo;    //返回的第几页
	private int pageSize;  //请求每页多少个
	private int total;   //服务器一共多少个
	private ArrayList<Search> content;

	public int getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
	}

	public int getPageNo()
	{
		return pageNo;
	}

	public void setPageNo(int pageNo)
	{
		this.pageNo = pageNo;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public ArrayList<Search> getContent()
	{
		return content;
	}

	public void setContent(ArrayList<Search> content)
	{
		this.content = content;
	}

	@Override
	public String toString() {
		return "SearchResult{" +
				"pageCount=" + pageCount +
				", pageNo=" + pageNo +
				", pageSize=" + pageSize +
				", total=" + total +
				", content=" + content +
				'}';
	}
}