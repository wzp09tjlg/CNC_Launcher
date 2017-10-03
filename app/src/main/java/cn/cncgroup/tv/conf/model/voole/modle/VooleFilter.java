package cn.cncgroup.tv.conf.model.voole.modle;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by zhang on 2015/10/10.
 */
public class VooleFilter
{
	@SerializedName("name")
	public String lableDesc;
	public ArrayList<String> labelName;
	public ArrayList<VooleFilter> result;
	public String upName;
}
