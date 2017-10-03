package cn.cncgroup.tv.conf.model.domybox.modle;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by zhang on 2015/4/21.
 */
public class DomyVideoSetListResult extends DomyBaseResult
{
	@SerializedName("content")
	public ArrayList<DomyVideoSet> pageContent;
}
