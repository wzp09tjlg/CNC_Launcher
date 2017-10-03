package cn.cncgroup.tv.utils;

/**
 * Created by Wuzhenpeng on 2015/6/23.
 */
public class StringUtil
{

	// 中文字符中间插入空格的处理
	public static String insertBlankToStr(String str)
	{
		String temp = "";
		if (str.length() == 2)
		{
			temp = String.copyValueOf(str.toCharArray(), 0, 1);
			temp = temp + "  ";
			temp = temp + String.copyValueOf(str.toCharArray(), 1, 1);
		}
		else if (str.length() == 3)
		{
			temp = String.copyValueOf(str.toCharArray(), 0, 1);
			temp = temp + " ";
			temp = temp + String.copyValueOf(str.toCharArray(), 1, 1);
			temp = temp + " ";
			temp = temp + String.copyValueOf(str.toCharArray(), 2, 1);
		}
		else if (str.length() >= 4)
		{
			temp = str;
		}
		return temp;
	}

	public static String insertBlankSToStr(String str)
	{
		String temp = "";
		if (str.length() == 2)
		{
			temp = String.copyValueOf(str.toCharArray(), 0, 1);
			temp = temp + "        ";
			temp = temp + String.copyValueOf(str.toCharArray(), 1, 1);
			temp = temp + " :";
		}
		else if (str.length() == 3)
		{
			temp = String.copyValueOf(str.toCharArray(), 0, 1);
			temp = temp + "  ";
			temp = temp + String.copyValueOf(str.toCharArray(), 1, 1);
			temp = temp + "  ";
			temp = temp + String.copyValueOf(str.toCharArray(), 2, 1);
			temp = temp + " :";
		}
		else if (str.length() >= 4)
		{
			temp = String.copyValueOf(str.toCharArray(), 0, 4) + " :";
		}
		return temp;
	}

	public static String checkStrEmpty(String value)
	{
		if (value == null)
			return "无";
		else if (value.trim() == "")
			return "无";
		else
			return value;

	}
}
