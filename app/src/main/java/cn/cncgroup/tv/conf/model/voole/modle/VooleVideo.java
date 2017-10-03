package cn.cncgroup.tv.conf.model.voole.modle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/9/22.
 */
public class VooleVideo {
    public String filmID;
    public String filmSize;
    @SerializedName("total")
    public int contentCount;
    @SerializedName("count")
    public int contentTrueCount;
    public String sourceID;
    public String area;
    public String filmFormat;
    public String imgUrl;
    @SerializedName("image")
    public String imgUrlB;
    public String language;
    public String sourceName;
    public String sourceUrl;
    @SerializedName("length")
    public String longTime;
    public String filmType;
    public String hd;
    public String year;
    @SerializedName("channelId")
    public String mtype;
    public String stype;
    @SerializedName("id")
    public String mid;
    public String skipTime;
    public String grade;
    public String previewLength;
    public String movieLevel;
    public String ispid;
    public String epgid;
    public String coderate;
    public String mediumtype;
    public String isdownload;
    @SerializedName("desc")
    public String filmName;
    public String filmClassId;
    public String filmClassCode;
    public String filmClassName;
    @SerializedName("name")
    public String mname;
    public String subject;
    @SerializedName("showTime")
    public String mshowtime;
    @SerializedName("actors")
    public String actor;
    @SerializedName("directors")
    public String director;
    @SerializedName("focus")
    public String watchFocus;
    public String introduction;
    @SerializedName("tags")
    public String relatedTag;
    @SerializedName("score")
    public String mark;
    @SerializedName("seriesType")
    public int seriesType;
    @SerializedName("videoListUrl")
    public String videoListUrl;
}
