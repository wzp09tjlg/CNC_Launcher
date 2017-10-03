package cn.cncgroup.tv.conf.model.voole.modle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2015/10/21.
 */
public class VooleSubject {
    public int filmID;
    public int filmSize;
    @SerializedName("totalCount")
    public int contentCount;
    @SerializedName("updateCount")
    public int contentTrueCount;
    public String sourceID;
    public String area;
    public String filmFormat;
    public String imgUrl;
    @SerializedName("image")
    public String imgUrlB;
    @SerializedName("imageBig")
    public String imgUrlZ;
    public String language;
    public int stype;
    public String sourceName;
    public String sourceUrl;
    public String longTime;
    public String filmType;
    public int hd;
    public String year;
    @SerializedName("type")
    public int mtype;
    public String grade;
    @SerializedName("id")
    public int mid;
    public int previewLength;
    public int movieLevel;
    public int contentType;
    public String template;
    public int ispid;
    public int epgid;
    public String coderate;
    public String mediumtype;
    @SerializedName("name")
    public String filmName;
    public String mname;
    public String subject;
    public String mshowtime;
    public String introduction;
    @SerializedName("actors")
    public String actor;
    @SerializedName("directors")
    public String director;
    @SerializedName("focus")
    public String watchFocus;
    public EpgTopic epgTopic;
    public String mark;

    public static class EpgTopic {
        public int type;
        public String thirdpartyUrl;
        public String epgTpl;
        public String interfaceUrl;
        public String relateImgS;
        public String relateImgB;
        public String relateName;
        public String relateColumnImgS;
        public String relateColumnImgB;
    }
}
