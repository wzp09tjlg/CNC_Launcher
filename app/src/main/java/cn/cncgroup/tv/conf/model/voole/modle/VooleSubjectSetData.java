package cn.cncgroup.tv.conf.model.voole.modle;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by zhang on 2015/10/21.
 */
public class VooleSubjectSetData {
    public String version;
    public String status;
    @SerializedName("total")
    public int columnCount;
    @SerializedName("pageSize")
    public int count;
    public String time;
    @SerializedName("pageNo")
    public int pageIndex;
    @SerializedName("pageCount")
    public int pageCount;
    @SerializedName("result")
    public ArrayList<VooleSubjectSet> filmClass;
}