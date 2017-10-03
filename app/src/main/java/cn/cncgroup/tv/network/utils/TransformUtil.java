package cn.cncgroup.tv.network.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

/**
 * Created by zhang on 2015/4/21. 模型类转换工具类，步骤为：
 * 1、首先用fastjson将json数据映射为原始模型类
 * 2、再用gson将模型类序列化为json数据，因为在原始模型类中，加入了gson的转换注解，gson会根据注解，自动修改字段名称
 * 3、将已经修改好字段名的json数据，映射到目标模型类中 为了成功转换模型类，需要根据规则执行以下步骤：
 * 1)、确定程序需要模型类结构，确定字段名称
 * 2)、确定原始数据模型类的结构，确定那些字段是程序需要模型类的字段。
 * 3)、在原始模型类中需要的字段上加入注解：@SerializedName("targentname")
 * 。targetname为程序需要模型类字段名称//或者通过public字段和get,set方法的差异做变换
 * 4)、继承TransformRequest写一个特定的转换Request，如果结构有差异需重写preParseJson（String json）函数做预处理
 * 5)、在NetworkManager中定义自己需要用到的访问函数
 */
public class TransformUtil
{

	public static <E> E getObjectFromJSON(String json, Class<E> from)
	{
		return JSON.parseObject(json, from);
	}

	public static <E> String setObjectToJSON(E object, Gson gson)
	{
		return gson.toJson(object);
	}

	public static <T> T getObjectFromJSON(Gson gson, String json, Class<T> to)
	{
		return gson.fromJson(json, to);
	}

	public static <T, E> T transformModle(String json, Gson gson,
	        Class<E> from, Class<T> to)
	{
		return getObjectFromJSON(gson,
		        setObjectToJSON(getObjectFromJSON(json, from), gson), to);
		// return gson.fromJson(gson.toJson(JSON.parseObject(json, from)), to);
	}
}
