package cn.cncgroup.tv.modle;

import cn.cncgroup.tv.conf.Project;

/**
 * Created by zhangbo on 15-6-19.
 */
public class VideoSetListData
{
	private boolean success;
	private String currentUrl;
	private VideoSetListResult result;
	public String nextPageUrl;

	public String getCurrentUrl()
	{
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl)
	{
		this.currentUrl = currentUrl;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public VideoSetListResult getResult()
	{
		return result;
	}

	public void setResult(VideoSetListResult result)
	{
		this.result = result;
	}

	public String getNextPageUrl()
	{
		return Project.get().getConfig().getNextPageUrl(this);
	}

	@Override
	public String toString()
	{
		return "VideoSetListData{" + "success=" + success + ", currentUrl='"
		        + currentUrl + '\'' + ", result=" + result + '}';
	}
}
