package cn.cncgroup.tv.conf.model.domybox.modle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/4/23.
 */
public class DomySearchRecord
{
	/**
	 * 搜索结果id
	 */
	@SerializedName("id")
	public int record_id;
	/**
	 * 搜索结果图片
	 */
	@SerializedName("image")
	public String record_img;
	/**
	 * 搜索结果类型
	 */
	@SerializedName("channelId")
	public int record_type;
	/**
	 * 搜索结果名称
	 */
	@SerializedName("name")
	public String record_name;
	/**
	 * 搜索结果竖图
	 */
	@SerializedName("verticalImage")
	public String record_tv_img;
	/**
	 * 搜索结果时长
	 */
	@SerializedName("length")
	public String record_time_length;
	/**
	 * 搜索结果更新至多少集
	 */
	@SerializedName("update")
	public String record_update;
	/**
	 * 一共多少集
	 */
	@SerializedName("total")
	public String record_total;
}
