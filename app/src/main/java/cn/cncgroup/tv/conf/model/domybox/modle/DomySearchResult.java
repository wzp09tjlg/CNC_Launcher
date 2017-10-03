package cn.cncgroup.tv.conf.model.domybox.modle;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/4/23.
 */
public class DomySearchResult extends DomyBaseResult
{
	@SerializedName("content")
	public ArrayList<DomySearchRecord> pageContent;
}
