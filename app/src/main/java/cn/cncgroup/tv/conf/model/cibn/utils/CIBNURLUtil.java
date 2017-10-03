package cn.cncgroup.tv.conf.model.cibn.utils;

/**
 * Created by zhang on 2015/6/2.
 */
public class CIBNURLUtil
{
	/**
	 * CIBN认证地址
	 */
	public static final String AUTHENTIATION_DOMAIN = "http://114.247.94.105";

	/**
	 * 代理商ID
	 */
	public static final String AGENT_VENDER_ID = "00940000000000000000000000000038";

	/**
	 * 静态激活
	 */
	public static String getCIBNStaticTerminalActiveUrl(String mac)
	{
		return AUTHENTIATION_DOMAIN
		        + "/userCenter/tms/deviceInit.action?mac=" + mac;
	}

	/**
	 * 动态激活
	 */
	public static String getCIBNDynamicTerminalActiveUrl(String mac,
	        int areaId, int type)
	{
		return AUTHENTIATION_DOMAIN
		        + "/userCenter/tms/cibn/deviceEntry.action?mac=" + mac
		        + "&agentvendorId=" + AGENT_VENDER_ID + "&areaId="
		        + areaId + "&type=" + type;
	}

	/**
	 * 静态登录
	 */
	public static String getCIBNStaticTerminalLoginUrl(String mac,
	        String diviceId)
	{
		return AUTHENTIATION_DOMAIN
		        + "/userCenter/tms/cibn/device!deviceLogin.action?deviceId="
		        + diviceId + "&mac=" + mac;
	}

	/**
	 * 动态登录
	 */
	public static String getCIBNDynamicTerminalLoginUrl(String mac,
	        String deviceId)
	{
		return AUTHENTIATION_DOMAIN
		        + "/userCenter/tms/cibn/device!deviceLogin.action?mac=" + mac
		        + "&deviceId=" + deviceId;
	}
}
