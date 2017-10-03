package cn.cncgroup.tv.conf.model.voole.modle;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by zhang on 2015/10/10.
 */
public class VooleFilterData
{
	public String status;
	public int labelCount;
	public String labelClassName;
	@SerializedName("result")
	public ArrayList<VooleFilter> label;
}
