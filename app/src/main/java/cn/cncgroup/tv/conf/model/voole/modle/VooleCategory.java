package cn.cncgroup.tv.conf.model.voole.modle;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/9/21.
 */
public class VooleCategory
{
	public int filmClassID;
	@SerializedName("id")
	public String mtype;
	public String keyValue;
	@SerializedName("name")
	public String filmClassName;
	public int filmCount;
	public int childrenCount;
	@SerializedName("contentUrl")
	public String filmClassUrl;
	public int pageCount;
	@SerializedName("pic")
	public String icoUrl;
	public String icoID;
	public String secIcoUrl;
	public String smallImgUrl;
	public String bigImgUrl;
	public int channelId;
	public String channelCode;
	public int count;
	public String template;
	public int contentType;
	public String intro;
	@SerializedName("data")
	public ArrayList<VooleCategory> filmClass;
}
