package cn.cncgroup.tv.cncplayer.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.bean.DefitionBean;
import cn.cncgroup.tv.player.data.Definition;
import cn.cncgroup.tv.utils.GlobalConstant;

/**关于播放的一些工具类
 * Created by zhangguang on 2015/9/1.
 */
public class CNCPlayerUtil {
    /**
     * 得到该剧所有分辨率信息（ArrayList<Defition>枚举类型）
     * @param mContext
     * @param defitionList
     * @return
     */
    public static ArrayList<Definition> getDefitionList(Context mContext, SparseArray<String> defitionList){
        ArrayList<Definition> defitionLists = new ArrayList<Definition>();
        Definition defition = null;
        String value = null;
        for(int i = 0 ; i<defitionList.size() ; i++){
            int defaultKey = defitionList.keyAt(i);  //1 bc20d9ee04c9c54b05d9bb9038341472
            int key = colateDefition(defaultKey);
            if(key != GlobalConstant.DOMY_TYPE_TEST){
                defition = Definition.get(key);
//                value = defitionList.get(key);
//                defition = new Defition();
//                defition.resourceId = key;
//                defition.defitionName = getDefitionName(mContext,key);
//                defition.defition = value;
                defitionLists.add(defition);
            }
        }
       return  defitionLists;
    }

    /**
     * 得到该剧所有分辨率信息（ArrayList<Defition>Bean类型）
     * @param mContext
     * @param defitionList
     * @return
     */
    public static ArrayList<DefitionBean> getDefitionListBean(Context mContext, SparseArray<String> defitionList){
        ArrayList<DefitionBean> defitionLists = new ArrayList<DefitionBean>();
        DefitionBean defition = null;
        String value = null;
        for(int i = 0 ; i<defitionList.size() ; i++){
            int defaultKey = defitionList.keyAt(i);  //1 bc20d9ee04c9c54b05d9bb9038341472
            int key = colateDefition(defaultKey);
            if(key != GlobalConstant.DOMY_TYPE_TEST){
                value = defitionList.get(key);
                defition = new DefitionBean();
                defition.resourceId = key;
                defition.defitionName = getDefitionName(mContext,key);
                defition.defition = value;
                defitionLists.add(defition);
            }
        }
        return  defitionLists;
    }


    /**
     * 判断是否是可用分辨率
     * @param defition
     * @return
     */
    private static int colateDefition(int defition){
       if(defition == GlobalConstant.DEFITION_2_360 || defition == GlobalConstant.DEFITION_4_720 ||
               defition == GlobalConstant.DEFITION_5_1080 || defition == GlobalConstant.DEFITION_10_4k||
               defition ==  GlobalConstant.DEFITION_14_dolby720P || defition == GlobalConstant.DEFITION_17_h265_720P||
               defition == GlobalConstant.DEFITION_15_dolby1080P || defition == GlobalConstant.DEFITION_16_dolby4K||
               defition == GlobalConstant.DEFITION_19_h265_4K || defition == GlobalConstant.DEFITION_18_h265_1080P){
          return defition;
       }else {
         return GlobalConstant.DOMY_TYPE_TEST;      //57
       }
    }
    /**
     * 根据分辨率Type得到分辨率名称
     * @param defitionType
     */
    private static String getDefitionName(Context mContext,int defitionType){
        String defitionName = null;
        switch (defitionType){
            case GlobalConstant.DEFITION_2_360:
                defitionName = mContext.getResources().getString(R.string.definition_high);
                break;
            case GlobalConstant.DEFITION_4_720:
                defitionName = mContext.getResources().getString(R.string.definition_720P);
                break;
            case GlobalConstant.DEFITION_5_1080:
                defitionName = mContext.getResources().getString(R.string.definition_1080P);
                break;
            case GlobalConstant.DEFITION_10_4k:
                defitionName = mContext.getResources().getString(R.string.definition_4K);
                break;
            case GlobalConstant.DEFITION_14_dolby720P:
                defitionName = mContext.getResources().getString(R.string.definition_720p_dolby);
                break;
            case GlobalConstant.DEFITION_15_dolby1080P:
                defitionName = mContext.getResources().getString(R.string.definition_1080p_dolby);
                break;
            case GlobalConstant.DEFITION_16_dolby4K:
                defitionName = mContext.getResources().getString(R.string.definition_4k_dolby);
                break;
            case GlobalConstant.DEFITION_17_h265_720P:
                defitionName = mContext.getResources().getString(R.string.definition_720p_h265);
                break;
            case GlobalConstant.DEFITION_18_h265_1080P:
                defitionName = mContext.getResources().getString(R.string.definition_1080p_h265);
                break;
            case GlobalConstant.DEFITION_19_h265_4K:
                defitionName = mContext.getResources().getString(R.string.definition_4k_h265);
                break;
        }
        return defitionName;
    }

    public static String checkIsEmpty(String str){
        return  TextUtils.isEmpty(str)? "无":str;
    }
}
