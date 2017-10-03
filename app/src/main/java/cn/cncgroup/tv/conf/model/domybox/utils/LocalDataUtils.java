package cn.cncgroup.tv.conf.model.domybox.utils;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.modle.CategoryData;

/**
 * Created by zhang on 2015/9/29
 * .
 */
public class LocalDataUtils
{
	public static CategoryData getDomyboxCategory()
	{
		CategoryData categoryData = null;
		try
		{
			BufferedReader reader = new BufferedReader(
			        new InputStreamReader(CApplication.getInstance().getAssets()
			                .open("domybox_category.json")));
			StringBuilder builder = new StringBuilder();
			String buffer;
			while ((buffer = reader.readLine()) != null)
			{
				builder.append(buffer);
			}
			categoryData = JSON.parseObject(builder.toString(),
			        CategoryData.class);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return categoryData;
	}
}
