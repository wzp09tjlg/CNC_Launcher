package cn.cncgroup.tv.conf.model.voole.modle;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/10/14.
 */
public class VooleSearchResult
{
	@SerializedName("pageNo")
	public int page;
	@SerializedName("pageSize")
	public int pagesize;
	@SerializedName("total")
	public int count;
	public int dbtotal;
	@SerializedName("content")
	public ArrayList<VooleSearch> data;
}
