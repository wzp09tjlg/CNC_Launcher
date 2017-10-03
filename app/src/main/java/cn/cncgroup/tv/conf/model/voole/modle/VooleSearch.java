package cn.cncgroup.tv.conf.model.voole.modle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/10/14.
 */
public class VooleSearch
{
	public int index;
	public String categoryid;
	public String ispid;
	public String epgid;
	@SerializedName("notuse")
	public String id;
	@SerializedName("id")
	public String mid;
	@SerializedName("channelId")
	public String mtype;
	public String stype;
	@SerializedName("name")
	public String cnname;
	public String pyname;
	public String pyacronym;
	public String country;
	public String years;
	public String genre;
	public String director;
	public String actor;
	public String watchfocus;
	public String storyintro;
	public String hotkeyword;
	@SerializedName("image")
	public String bigposterurl;
	public String smallposterurl;
	public String endGrade;
	@SerializedName("total")
	public String series;
	public String duration;
	public String studio;
	public String enname;
	public String cnabbr;
	public String movielevel;
	public String language;
	public String mname;
	public String subject;
	public String mshowtime;
	public String viewtype;
	public String previewtime;
}
