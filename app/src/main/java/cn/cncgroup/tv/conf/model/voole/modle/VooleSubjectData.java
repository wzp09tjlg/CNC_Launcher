package cn.cncgroup.tv.conf.model.voole.modle;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/10/21.
 */
public class VooleSubjectData
{
	@SerializedName("title")
	public String filmClassName;
	public String status;
	public int filmClassID;
	@SerializedName("total")
	public int filmCount;
	@SerializedName("image")
	public String bigImgUrl;
	public String smallImgUrl;
	@SerializedName("pageNo")
	public int pageIndex;
	@SerializedName("pageCount")
	public int pageCount;
	@SerializedName("pageSize")
	public int pageSize;
	public String version;
	public String time;
	public String prevPageUrl;
	public String nextPageUrl;
	@SerializedName("result")
	public ArrayList<VooleSubject> film;
}
