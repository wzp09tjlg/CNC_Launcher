package cn.cncgroup.tv.modle;

import java.io.Serializable;

import android.text.TextUtils;

import cn.cncgroup.tv.conf.Project;

/**
 * Created by zhangbo on 15-6-18.
 */
public class VideoSet implements Serializable
{
	public String channelId;
	public String channelName;
	/**
	 * 影视id
	 */
	public String id;
	/**
	 * 影视名称
	 */
	public String name;
	/**
	 * 简介
	 */
	public String desc;
	/**
	 * 导演
	 */
	public String directors;
	/**
	 * 演员
	 */
	public String actors;
	/**
	 * 标签
	 */
	public String tags;
	/**
	 * 影视图片
	 */
	private String image;
	/**
	 * 影视追剧集数
	 */
	public int count;
	/**
	 * 影视发行时间
	 */
	private String time;
	private String showTime;
	/**
	 * 评分
	 */
	public String score;
	/**
	 * 时长
	 */
	private int length;
	/**
	 * 一句话看点
	 */
	public String focus;
	/**
	 * 0:单集,1:多集
	 */
	public int seriesType;
	/**
	 * 分辨率信息
	 */
	public String eqVid;
	public int total;
	public String sourceUrl;

	private String area;
	private String subject;

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getArea()
	{
		return area;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public String getRecommendUrl()
	{
		return Project.get().getConfig().getVideoSetRecommendUrl(this);
	}

	public String getSourceUrl()
	{
		return Project.get().getConfig().getVideoSetSourceUrl(this);
	}

	public String getVerticalPosterUrl()
	{
		return Project.get().getConfig().getVerticalPosterUrl(this);
	}

	public String getHorizentalPosterUrl()
	{
		return Project.get().getConfig().getHorizentalPosterUrl(this);
	}

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setSourceUrl(String sourceUrl)
	{
		this.sourceUrl = sourceUrl;
	}

	public String getChannelName()
	{
		return channelName;
	}

	public void setChannelName(String channelName)
	{
		this.channelName = channelName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getDirectors()
	{
		return directors;
	}

	public void setDirectors(String directors)
	{
		this.directors = directors;
	}

	public String getActors()
	{
		return actors;
	}

	public void setActors(String actors)
	{
		this.actors = actors;
	}

	public String getTags()
	{
		return tags;
	}

	public void setTags(String tags)
	{
		this.tags = tags;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public String getTime()
	{
		return time;
	}

	public String getIssueTime()
	{
		return Project.get().getConfig().getIssueTime(this);
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getScore()
	{
		return score;
	}

	public void setScore(String score)
	{
		this.score = score;
	}

	public String getPlayLength()
	{
		return Project.get().getConfig().getPlayLength(this);
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public String getFocus()
	{
		return focus;
	}

	public void setFocus(String focus)
	{
		this.focus = focus;
	}

	public int getSeriesType()
	{
		return seriesType;
	}

	public void setSeriesType(int seriesType)
	{
		this.seriesType = seriesType;
	}

	public String getEqVid()
	{
		return eqVid;
	}

	public void setEqVid(String eqVid)
	{
		this.eqVid = eqVid;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public String videoListUrl;

	private void setVideoListUrl(String videoListUrl)
	{
		this.videoListUrl = videoListUrl;
	}

	public String getVideoListUrl()
	{
		return Project.get().getConfig().getVideoListUrl(this);
	}

	public String getShowTime()
	{
		return showTime;
	}

	public void setShowTime(String showTime)
	{
		this.showTime = showTime;
	}

	@Override
	public String toString()
	{
		return "VideoSet{" + "channelId=" + channelId + ", channelName='"
		        + channelName + '\'' + ", id=" + id + ", name='" + name + '\''
		        + ", desc='" + desc + '\'' + ", directors='" + directors + '\''
		        + ", actors='" + actors + '\'' + ", tags='" + tags + '\''
		        + ", image='" + image + '\'' + ", count=" + count + ", time='"
		        + time + '\'' + ", score=" + score + ", length=" + length
		        + ", focus='" + focus + '\'' + ", seriesType=" + seriesType
		        + ", eqVid='" + eqVid + '\'' + ", total=" + total + '}';
	}
}