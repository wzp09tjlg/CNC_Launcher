package cn.cncgroup.tv.utils;

import java.util.Comparator;

import cn.cncgroup.tv.bean.AppInfo;

/**
 * Created by Wu on 2015/11/2.
 */
public class DataComparator<T> implements Comparator<T>
{

	@Override
	public int compare(T lhs, T rhs)
	{
		AppInfo app1 = (AppInfo) lhs;
		AppInfo app2 = (AppInfo) rhs;
		return app1.tag < app2.tag ? -1 : 1;
	}
}
