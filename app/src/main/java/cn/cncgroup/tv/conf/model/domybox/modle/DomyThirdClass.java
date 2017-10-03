package cn.cncgroup.tv.conf.model.domybox.modle;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/4/23.
 */
public class DomyThirdClass
{
	/**
	 * 一级分类标识
	 */
	public int firstclass_id;
	/**
	 * 二级分类标识
	 */
	@SerializedName("id")
	public int secondclass_id;
	/**
	 * 二级分类名称
	 */
	@SerializedName("name")
	public String secondclass_name;
	@SerializedName("result")
	public ArrayList<DomyThirdClassList> classThirdList;
	@SerializedName("filter")
	public DomyThirdClassList filter;
}
