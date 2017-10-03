package cn.cncgroup.tv.conf.model.voole.modle;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by zhang on 2015/10/13.
 */
public class VooleRecommendVideoSetListData
{
	public int count;
	@SerializedName("result")
	public ArrayList<VooleRecommendVideoSet> data;
}