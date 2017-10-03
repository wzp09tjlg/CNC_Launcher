package cn.cncgroup.tv.conf.model.domybox.modle;

import java.io.Serializable;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/4/21.
 */
public class DomyVideoSet implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**
	 * 专辑频道ID
	 */
	@SerializedName("channelId")
	public int cid;
	/**
	 * 专辑频道
	 */
	@SerializedName("channelName")
	public String cname;
	/**
	 * 0:自有内容 1:奇艺
	 */
	public String cp;
	/**
	 * 当前更新到的集数
	 */
	@SerializedName("count")
	public int currCount;
	/**
	 * 专辑简介
	 */
	public String desc;
	/**
	 * 导演
	 */
	public String directors;
	/**
	 * 时长
	 */
	@SerializedName("length")
	public int eqLen;
	public String eqVid;
	/**
	 * 一句话看点
	 */
	@SerializedName("focus")
	public String focusName;
	/**
	 * 专辑唯一标识
	 */
	public int id;
	/**
	 * 首次上线时间
	 */
	public String initIssueTime;
	/**
	 * 演员列表
	 */
	@SerializedName("actors")
	public String mainActors;
	/**
	 * 专辑名称
	 */
	public String name;
	/**
	 * 播放次数
	 */
	public int playCount;
	/**
	 * 海报图
	 */
	@SerializedName("image")
	public String posterUrl;
	/**
	 * 评分
	 */
	public Float score;
	/**
	 * 0:单集,1:多集
	 */
	public int seriesType;
	public String streams;
	/**
	 * 标签列表
	 */
	@SerializedName("tags")
	public String tagNames;
	/**
	 * 发行时间
	 */
	public String time;
	/**
	 * 系列片数
	 */
	public int total;
	/**
	 * 0:非3D,1:3D
	 */
	public int type3d;

	public String getVerticalPosterUrl()
	{
		if (TextUtils.isEmpty(posterUrl))
		{
			return "";
		}
		int pointIndex = posterUrl.lastIndexOf(".");
		return posterUrl.substring(0, pointIndex) + "_320_180"
		        + posterUrl.substring(pointIndex);
	}

	public String getHorizentalPosterUrl()
	{
		if (TextUtils.isEmpty(posterUrl))
		{
			return "";
		}
		int pointIndex = posterUrl.lastIndexOf(".");
		return posterUrl.substring(0, pointIndex) + "_260_360"
		        + posterUrl.substring(pointIndex);
	}
}
