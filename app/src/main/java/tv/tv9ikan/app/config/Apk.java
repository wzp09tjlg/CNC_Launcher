package tv.tv9ikan.app.config;

import java.io.Serializable;

public class Apk implements Serializable
{
	private static final long serialVersionUID = 2L;
	/**
	 * apk类型ID
	 */
	public Integer apkCategoryId;
	/**
	 * apk类型名
	 */
	public String apkCategoryName;
	/**
	 * apk 的icon下载地址
	 */
	public String apkIconURL;
	/**
	 * apk安装包的名字
	 */
	public String apkName;
	/**
	 * 安装包的地址下载地址
	 */
	public String apkUrl;
	/**
	 * apk文件大小 （如：956B）
	 */
	public Long apksize;
	/**
	 * apk作者
	 */
	public String author;
	/**
	 * apk创建时间
	 */
	public Long createTime;
	/**
	 * 创建者
	 */
	public String creator;
	/**
	 * 用户ID
	 */
	public String devId;
	/**
	 * 下载次数
	 */
	public Integer downloadedTimes;
	/**
	 * 自定义的所属分类ID
	 */
	public String id;
	/**
	 * 图片的下载地址
	 */
	public String imageUrl;
	/**
	 * 是否独家
	 */
	public Integer isAlone;
	/**
	 * 是否首发
	 */
	public Integer isFirst;
	/**
	 * 是否安全
	 */
	public Integer isSecurity;
	/**
	 * 是否虚拟星级
	 */
	public Integer is_virtual_star;
	/**
	 * boolean 是否独家
	 */
	public boolean isalone;
	/**
	 * boolean 是否首发
	 */
	public boolean isfirst;
	/**
	 * boolean 是否安全，false：不安全，true：安全
	 */
	public boolean issecurity;
	/**
	 * 最后更新时间
	 */
	public Long lastUpdateTime;
	/**
	 * 最后更新人
	 */
	public String lastUpdator;
	/**
	 * apk产品名
	 */
	public String name;
	/**
	 * 经营者
	 */
	public String operator;
	/**
	 * 包名
	 */
	public String pkgName;
	/**
	 * 广告连接
	 */
	public String poster;
	/**
	 * apk介绍
	 */
	public String presentation;
	/**
	 * 产品名
	 */
	public String productName;
	/**
	 * 下载所需要的积分或者增送的积分
	 */
	public Integer score;
	/**
	 * 1为获得，2为消耗
	 */
	public Integer scoreType;
	/**
	 * 适配设备 （1-7个星）
	 */
	public Integer starNumber;
	/**
	 * 总星级
	 */
	public Integer total_star;
	/**
	 * 游戏唯一ID. 每一个游戏有一个唯一的ID
	 */
	public Integer uId;
	/**
	 * 更新简介
	 */
	public String updateTip;
	/**
	 * uuid值
	 */
	public String uuid;
	/**
	 * 游戏版本号，用于判断是否要版本升级
	 */
	public String versionCode;
	/**
	 * 游戏版本号，用来显示游戏版本信息
	 */
	public String versionName;
	/**
	 * 实际星级
	 */
	public Integer virtual_star;
	/**
	 * 存放sqllite中的id值
	 */
	private Integer _id;

	public String getId()
	{
		return this.id;
	}

	public void setId(String paramString)
	{
		this.id = paramString;
	}
}