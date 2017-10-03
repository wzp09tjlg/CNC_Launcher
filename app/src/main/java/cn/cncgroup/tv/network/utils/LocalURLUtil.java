package cn.cncgroup.tv.network.utils;

/**
 * Created by zhang on 2015/6/2.
 */
public class LocalURLUtil
{
	public static final String LOCAL_DOMY_DOMAIN = "http://192.168.1.200:8080/domybox/api";
	public static final String LOCAL_CIBN_DOMAIN = "http://192.168.1.200:8080/cibn/api";

	public static String getLocalCIBNRecommendUrl()
	{
		return  LOCAL_CIBN_DOMAIN + "/recommend.json";
	}

	public static String getLocalCIBNCategoryUrl()
	{
		return LOCAL_CIBN_DOMAIN + "/category.json";
	}

	public static String getLocalAppUrl()
	{
		return "http://192.168.1.200:8080/cnc-cms/getInfo.do?method=getApp";
	}

	public static String getLocalUpdateUrl()
	{
//		return LOCAL_CIBN_DOMAIN + "/update.json";
		return "http://192.168.1.200:8080/cnc-cms/getInfo.do?method=getAppUpdate";
	}

	public static String getLocalDOMYRecommendUrl()
	{
		return "http://192.168.1.200:8080/cnc-cms/getInfo.do?method=getRecommend";
	}

	public static String getLocalDOMYCategoryUrl()
	{
		return "http://192.168.1.200:8080/cnc-cms/getInfo.do?method=getCategory";
	}

}
