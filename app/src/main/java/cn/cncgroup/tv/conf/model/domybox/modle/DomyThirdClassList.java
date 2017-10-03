package cn.cncgroup.tv.conf.model.domybox.modle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/4/23.
 */
public class DomyThirdClassList
{
	/**
	 * 三级分类标识
	 */
	@SerializedName("id")
	public int thirdclass_id;
	/**
	 * 三级分类名称，即标签
	 */
	@SerializedName("name")
	public String thirdclass_name;
}
