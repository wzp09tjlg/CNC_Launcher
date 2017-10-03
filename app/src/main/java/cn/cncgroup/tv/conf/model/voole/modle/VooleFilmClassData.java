package cn.cncgroup.tv.conf.model.voole.modle;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/9/21.
 */
public class VooleFilmClassData
{
	public String version;
	public String status;
	public int count;
	public String time;
	public String stTime;
	public int pageIndex;
	public int pageCount;
	@SerializedName("result")
	public ArrayList<VooleFilmClass> filmClass;
}
