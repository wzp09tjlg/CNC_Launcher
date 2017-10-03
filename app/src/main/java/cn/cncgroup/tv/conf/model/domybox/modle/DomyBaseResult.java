package cn.cncgroup.tv.conf.model.domybox.modle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangbo on 15-6-19.
 */
public class DomyBaseResult
{
	public int pageCount;
	public int pageNo;
	public int pageSize;
	@SerializedName("total")
	public int recCount;
}
