package cn.cncgroup.tv.conf.model.voole.modle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/10/12.
 */
public class VooleDetailData
{
	public String filmSetName;
	public String status;
	public int contentCount;
	@SerializedName("result")
	public VooleFilm film;
	public VooleCp cp;
	public VooleContentSet contentSet;
}
