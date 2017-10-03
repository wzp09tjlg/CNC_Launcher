package cn.cncgroup.tv.modle;

import java.util.ArrayList;

/**
 * Created by zhangbo on 15-6-19.
 */
public class VideoSetListResult
{
	private int pageCount;
	private int pageNo;
	private int pageSize;
	private int total;
	private ArrayList<VideoSet> content;

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

	public ArrayList<VideoSet> getContent()
	{
		return content;
	}

	public void setContent(ArrayList<VideoSet> content)
	{
		this.content = content;
	}

	@Override
	public String toString()
	{
		return "VideoSetListResult{" + "pageCount=" + pageCount + ", pageNo="
		        + pageNo + ", pageSize=" + pageSize + ", total=" + total
		        + ", content=" + content + '}';
	}
}
