package cn.cncgroup.tv.conf.model.domybox.modle;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/4/23.
 */

public class DomyVideo
{
	@SerializedName("channelId")
	public int cid;
	@SerializedName("channelName")
	public String cname;
	public int cp;
	/**
	 * 剧集简介
	 */
	public String desc;
	public String directors;
	/**
	 * 关注点
	 */
	@SerializedName("focus")
	public String epfocus;
	/**
	 * 时长
	 */
	@SerializedName("length")
	public int eplen;
	@SerializedName("name")
	public String epname;
	@SerializedName("index")
	public int eporder;
	/**
	 * 正片-片花区分标识
	 */
	@SerializedName("type")
	public int eptype;
	public String epvid;
	@SerializedName("actors")
	public String mainActors;
	/**
	 * 剧集图片地址
	 */
	@SerializedName("image")
	public String picurl;
	@SerializedName("id")
	public int videoId;
	@SerializedName("videoSetId")
	public int videosetId;
	/**
	 * 所属年份
	 */
	public String year;

	public String getVerticalPosterUrl()
	{
		if (TextUtils.isEmpty(picurl))
		{
			return "";
		}
		int pointIndex = picurl.lastIndexOf(".");
		return picurl.substring(0, pointIndex) + "_320_180"
		        + picurl.substring(pointIndex);
	}

	public String getHorizentalPosterUrl()
	{
		if (TextUtils.isEmpty(picurl))
		{
			return "";
		}
		int pointIndex = picurl.lastIndexOf(".");
		return picurl.substring(0, pointIndex) + "_260_360"
		        + picurl.substring(pointIndex);
	}
}
