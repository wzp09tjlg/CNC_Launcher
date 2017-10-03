package cn.cncgroup.tv.conf.model.voole.modle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/10/13.
 */
public class VooleRecommendVideoSet
{
	public String stamp;
	public String seqno;
	@SerializedName("id")
	public int mid;
	@SerializedName("channelId")
	public int mtype;
	public int mtype2;
	public String formalflag;
	@SerializedName("total")
	public int series;
	public String copyright;
	@SerializedName("name")
	public String cnname;
	public String enname;
	public String cnabbr;
	public String pyname;
	public String pyacronym;
	public String country;
	public String studio;
	@SerializedName("time")
	public String years;
	@SerializedName("tags")
	public String genre;
	@SerializedName("directors")
	public String director;
	@SerializedName("actors")
	public String actor;
	public String showtime;
	public String duration;
	public String language;
	@SerializedName("focus")
	public String watchfocus;
	public String summary;
	@SerializedName("desc")
	public String storyintro;
	public String viewnum;
	public String lineflag;
	public String create_time;
	public String hotkeyword;
	public String concernstatus;
	public String gradeByEditor;
	public String gradeByUser;
	public String endGrade;
	public String gradeRate;
	public String topleft;
	public String topright;
	public String bottomleft;
	public String bottomright;
	public String toplefttime;
	public String toprighttime;
	public String bottomlefttime;
	public String bottomrighttime;
	public String movielevel;
	public String mname;
	public String subject;
	public String mshowtime;
	public String stype;
	public String serieslist;
	public String download;
	public String previewtime;
	public String viewtype;
	@SerializedName("image")
	public String bigposterurl;
	public String smallposterurl;
}