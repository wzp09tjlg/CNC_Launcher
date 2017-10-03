package cn.cncgroup.tv.conf.model.voole.modle;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/9/22.
 */
public class VooleFilmDataForVideoList implements Cloneable
{
	public String filmClassName;
	public String status;
	public String filmClassID;
	public String filmCount;
	@SerializedName("total")
	public String allFilmCount;
	@SerializedName("pageNo")
	public String pageIndex;
	public String pageSize;
	public String pageCount;
	public String version;
	public String time;
	public String prevPageUrl;
	public String nextPageUrl;
	public String currentUrl;
	public VooleFilmDataForVideoList result;
	@SerializedName("content")
	public ArrayList<VooleFilm> film;

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
