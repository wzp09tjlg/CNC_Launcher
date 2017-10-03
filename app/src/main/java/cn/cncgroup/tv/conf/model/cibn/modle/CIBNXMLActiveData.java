package cn.cncgroup.tv.conf.model.cibn.modle;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;

/**
 * Created by zhang on 2015/6/2.
 */
public class CIBNXMLActiveData extends CIBNXMLBaseData
{
	@Path("online")
	@Element(required = false)
	public String agentvendorID;
	@Path("online")
	@Element(required = false)
	public String deviceId;
	@Path("online")
	@Element(required = false)
	public int resultCode;
	@Path("online")
	@Element(required = false)
	public String successStatus;
}
