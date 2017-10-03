package cn.cncgroup.tv.modle;


/**
 * Created by zhangbo on 15-6-19.
 */
public class SearchData
{
	private boolean success;
	private SearchResult result;

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public SearchResult getResult()
	{
		return result;
	}

	public void setResult(SearchResult result)
	{
		this.result = result;
	}

	@Override
	public String toString() {
		return "SearchData{" +
				"success=" + success +
				", result=" + result +
				'}';
	}
}
