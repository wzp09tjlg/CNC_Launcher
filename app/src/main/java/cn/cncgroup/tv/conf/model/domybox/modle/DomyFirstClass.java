package cn.cncgroup.tv.conf.model.domybox.modle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/4/23.
 */
public class DomyFirstClass
{
	public String backImg;
	@SerializedName("type")
	public int classfirstType;
	public String cp_video_id;
	/**
	 * 频道唯一标识
	 */
	@SerializedName("id")
	public int firstclassId;
	/**
	 * 频道名称
	 */
	@SerializedName("name")
	public String firstclassName;
	/**
	 * 频道运营图
	 */
	@SerializedName("image")
	public String iconImg;
	public int isEffective;
	/**
	 * 频道图片
	 */
	@SerializedName("picture")
	public String pic;
	public String picUrl;
	public String rowVersion;
	public int seq;
	public int showType;
	public int specType;
}
