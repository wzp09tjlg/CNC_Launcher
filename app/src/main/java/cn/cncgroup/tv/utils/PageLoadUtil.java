package cn.cncgroup.tv.utils;

import cn.cncgroup.tv.ui.listener.IPageLoader;

/**
 * Created by zhang on 2015/7/2.
 */
public class PageLoadUtil
{
	public static void loadPage(int position, int pageSize, int count,
	        IPageLoader loader)
	{
		int page = position / pageSize + 1;
		int pageStart = position / pageSize * pageSize;
		int nextPageStart = pageStart + pageSize;
		if (nextPageStart < count)
		{
			loader.loadPage(page + 1);
		}
	}
}
