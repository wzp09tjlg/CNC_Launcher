package cn.cncgroup.tv.conf.model.voole.modle;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/9/21.
 */
public class VooleCategoryData
{
	public String version;
	public String status;
	public int count;
	public String time;
	public String stTime;
	@SerializedName("data")
	public ArrayList<VooleCategory> filmClass;
}
