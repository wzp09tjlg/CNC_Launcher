package cn.cncgroup.tv.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import cn.cncgroup.tv.R;

/**
 * 详情页接口数据截取 Created by zhangguang on 2015/6/10.
 */
public class DetailStringUtil
{

	/**
	 * 上映时间
	 * 
	 * @param str
	 * @return
	 */
	public static String getIssueTime(String str, Context mContext)
	{
		String regEx = "/^([\\d]{4}).*/"; // TODO 待验证则表达式是否可行
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find())
			return str.substring(0, 4)
			        + mContext.getResources().getString(R.string.year);
		else
			return str;
	}

	/**
	 * 类型
	 * 
	 * @param str
	 * @return
	 */
	public static String getType(String str)
	{
		String Str = str.replace(",", "/");
		return getStr(Str, 4);
	}

	/**
	 * 演员
	 * 
	 * @param str
	 * @return
	 */
	public static String getActress(String str)
	{
		String Str = str.replace(",", "/");
		return getStr(Str, 4);
	}

	public static String removerBlank(String str)
	{
		return str.replace(" ", "");
	}

	/**
	 * 截取字符串 "/" 第几次出现之前的字符 如"国语/内地/喜剧/家庭剧/言情剧" index 为6就是截取第三次出现之前的字符"国语/内地/喜剧"
	 * 
	 * @param str
	 * @param index
	 * @return
	 */
	public static String getIndex(String str, int index)
	{
		int x = 0;
		for (int i = 0; i < str.length(); i++)
		{
			String getstr = str.substring(0, i + 1);
			if (getstr.contains("/"))
			{
				x++;
			}
			if (x == index)
			{
				return getstr;
			}
		}
		return null;
	}

	/**
	 * 截取字符串 第四个/前面的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getStr(String str, int index)
	{
		String b = str.replace(",", "/");
		int d = 0;
		String value = "";
		for (int i = 0; i < b.length(); i++)
		{
			char ch = b.charAt(i);
			if (ch == '/')
			{
				d++;
			}
			value = value + ch;
			if (d == index)
			{
				return value.substring(0, value.length() - 1);
			}
		}
		return b;
	}

	/**
	 * 综艺详情的期数
	 * 
	 * @return
	 */
	// public static String detailStage(String str){
	// if(!TextUtils.isEmpty(str)){
	// return
	// (str.substring(0,4)+"-"+str.substring(4,6)+"-"+str.substring(6,8));
	// }
	// return "";
	// }
}
