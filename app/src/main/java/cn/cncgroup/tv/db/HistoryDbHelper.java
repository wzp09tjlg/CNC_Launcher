package cn.cncgroup.tv.db;

/**
 * Created by Administrator on 2015/9/14.
 */
public class HistoryDbHelper {
    private HistoryDbHelper(){}
    public static void endPlay(VideoSetDao obj){
        DbUtil.addByObject(obj,true);
    }
    public static void middlePlay(VideoSetDao obj){
        DbUtil.addByObject(obj,true);
    }
}
