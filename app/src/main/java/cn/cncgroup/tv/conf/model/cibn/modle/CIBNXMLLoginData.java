package cn.cncgroup.tv.conf.model.cibn.modle;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;

/**
 * Created by zhang on 2015/6/2.
 */
public class CIBNXMLLoginData extends CIBNXMLBaseData
{
	@Path("online")
	@Element(required = false)
	public String deviceId;
	@Path("online")
	@Element(required = false)
	public int resultCode;
	@Path("online")
	@Element(required = false)
	public String successStatus;
	@Path("online")
	@Element(required = false)
	public String accountId;
	@Path("online")
	@Element(required = false)
	public String userId;
	@Path("online")
	@Element(required = false)
	public String password;
	@Path("online")
	@Element(required = false)
	public String regionId;
	@Path("online")
	@Element(required = false)
	public String templateId;
	@Path("online")
	@Element(required = false)
	public int state;
	@Path("online")
	@Element(required = false)
	public String token;
	@Path("online")
	@Element(required = false)
	public String verificationCode;
	@Path("online")
	@Element(required = false)
	public String customerCategory;
	@Path("online")
	@Element(required = false)
	public String ipList;
	@Path("online")
	@Element(required = false)
	public int ipSize;
	@Path("online")
	@Element(required = false)
	public String platformId;
	@Path("online")
	@ElementList(required = false, type = Address.class)
	public ArrayList<Address> addressList;
	@Path("update")
	@Element(name = "state", required = false)
	public int updateState;
	@Path("update")
	@Element(name = "list", required = false)
	public String updateList;

	public static class Address
	{
		@Attribute(required = false)
		public int type;
		@Attribute(required = false)
		public String url;
	}
}
