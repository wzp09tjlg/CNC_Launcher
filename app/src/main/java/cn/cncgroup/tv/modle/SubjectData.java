package cn.cncgroup.tv.modle;

import java.util.ArrayList;

/**
 * Created by zhang on 2015/10/21.
 */
public class SubjectData
{
	private String title;
	private String image;
	private int total;
	private int pageNo;
	private int pageSize;
	private int pageCount;
	private String prevPageUrl;
	private String nextPageUrl;
	private ArrayList<Subject> result;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

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

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
	}

	public String getPrevPageUrl()
	{
		return prevPageUrl;
	}

	public void setPrevPageUrl(String prevPageUrl)
	{
		this.prevPageUrl = prevPageUrl;
	}

	public String getNextPageUrl()
	{
		return nextPageUrl;
	}

	public void setNextPageUrl(String nextPageUrl)
	{
		this.nextPageUrl = nextPageUrl;
	}

	public ArrayList<Subject> getResult()
	{
		return result;
	}

	public void setResult(ArrayList<Subject> result)
	{
		this.result = result;
	}
}