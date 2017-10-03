package cn.cncgroup.tv.conf.model.cibn.modle;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by zhang on 2015/6/2.
 */
@Root(name = "Service")
public class CIBNXMLBaseData
{
	@Attribute(required = false)
	public String id;
}
