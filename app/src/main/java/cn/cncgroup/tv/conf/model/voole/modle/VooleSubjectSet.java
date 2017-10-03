package cn.cncgroup.tv.conf.model.voole.modle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/10/21.
 */
public class VooleSubjectSet
{
	public int filmClassID;
	@SerializedName("type")
	public String mtype;
	public String keyValue;
	@SerializedName("name")
	public String filmClassName;
	@SerializedName("count")
	public int filmCount;
	public int childrenCount;
	@SerializedName("sourceUrl")
	public String filmClassUrl;
	public int pageCount;
	public String icoUrl;
	public String icoID;
	public String secIcoUrl;
	public String smallImgUrl;
	@SerializedName("image")
	public String bigImgUrl;
	public int channelId;
	public String channelCode;
	@SerializedName("no_use")
	public String count;
	public String template;
	public int contentType;
	public String intro;
}
