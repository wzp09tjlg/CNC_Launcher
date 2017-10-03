package cn.cncgroup.tv.modle;

import android.text.TextUtils;

/**
 * Created by zhangbo on 15-6-19.
 */
public class Search
{
	/**
	 * 搜索结果id
	 */
	private String id;
	/**
	 * 搜索结果图片
	 */
	private String image;
	/**
	 * 搜索结果类型
	 */
	private int channelId;
	/**
	 * 搜索结果名称
	 */
	private String name;
	/**
	 * 搜索结果竖图
	 */
	private String verticalImage;
	/**
	 * 搜索结果时长
	 */
	private String length;
	/**
	 * 搜索结果更新至多少集
	 */
	private String update;
	/**
	 * 一共多少集
	 */
	private String total;

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	private String stype;
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public int getChannelId()
	{
		return channelId;
	}

	public void setChannelId(int type)
	{
		this.channelId = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getVerticalImage()
	{
		return verticalImage;
	}

	public void setVerticalImage(String verticalImage)
	{
		this.verticalImage = verticalImage;
	}

	public String getLength()
	{
		return length;
	}

	public void setLength(String length)
	{
		this.length = length;
	}

	public String getUpdate()
	{
		return update;
	}

	public void setUpdate(String update)
	{
		this.update = update;
	}

	public String getTotal()
	{
		return total;
	}

	public void setTotal(String total)
	{
		this.total = total;
	}

	public String getVerticalPosterUrl()
	{
		if (TextUtils.isEmpty(image))
		{
			return "";
		}
		int pointIndex = image.lastIndexOf(".");
		return image.substring(0, pointIndex) + "_320_180"
		        + image.substring(pointIndex);
	}

	public String getHorizentalPosterUrl()
	{
		if (TextUtils.isEmpty(image))
		{
			return "";
		}
		int pointIndex = image.lastIndexOf(".");
		return image.substring(0, pointIndex) + "_260_360"
		        + image.substring(pointIndex);
	}

	@Override
	public String toString() {
		return "Search{" +
				"id='" + id + '\'' +
				", image='" + image + '\'' +
				", channelId=" + channelId +
				", name='" + name + '\'' +
				", verticalImage='" + verticalImage + '\'' +
				", length='" + length + '\'' +
				", update='" + update + '\'' +
				", total='" + total + '\'' +
				'}';
	}
}
