package cn.cncgroup.tv.player.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;

import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.utils.ListUtils;


/**
 * 视频的码流信息类</br> </br> 包含该视频拥有的所有码流List, 提供获取最高码流，最低码流，码流列表等方法
 */
public class VideoDefinition {
    private static final String TAG = "VideoDefinition";
    private List<Definition> mDefinitions;
    /**
     * 通过一个以“，”分割的码流字符串，来获取该视频对应码流
     * 
     * @param definitionStr
     */
    public VideoDefinition(String definitionStr) {
        this(castDefinitionStr(definitionStr));
    }

    /**
     * 通过int数组获取视频对应的码流
     * 
     * @param definitions
     *            int array
     */
    public VideoDefinition(int[] definitions) {
        mDefinitions = new ArrayList<Definition>();
        if (definitions != null) {
            for (int definition : definitions) {
                Definition def = Definition.get(definition);
                if (def != null && (!mDefinitions.contains(def))) {
                    mDefinitions.add(def);
                }
            }
        }
        
        if (mDefinitions.size() == 0) {
            mDefinitions.add(Definition.DEFINITON_HIGH);
        }
        
        Log.d(TAG, "VideoDefinition.<init>: def list=" + this.toString());
    }
    
    public String getDefinitionString() {
        if (ListUtils.isEmpty(mDefinitions)) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        for (Definition def : mDefinitions) {
            sb.append(def.getValue());
            if (!(mDefinitions.lastIndexOf(def) == mDefinitions.size() - 1)) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public List<Definition> getDefinitions() {
        return mDefinitions;
    }

    public boolean isEmpty() {
        return (mDefinitions == null || mDefinitions.size() < 1);
    }

    private static int[] castDefinitionStr(String definitionStr) {
        if (TextUtils.isEmpty(definitionStr)) {
            return null;
        }
        
        String[] strTmp = definitionStr.split(",");
        if (strTmp != null && strTmp.length > 0) {
            int[] definitions = new int[strTmp.length];
            for (int i = 0; i < strTmp.length; i++) {
                definitions[i] = getInteger(strTmp[i], -1);
            }
            Arrays.sort(definitions);
            return definitions;
        }
        
        return null;
    }

    private static int getInteger(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
        }
        
        return defaultValue;
    }

    //choose the most close definition by setting weight.
    public static  Definition getMostCloseDefinition(List<Definition> defList, Definition setting) {
        int diff = Integer.MAX_VALUE;
        if(ListUtils.isEmpty(defList)) {
        	return null;
        }
        Definition ret = null;
        for (Definition def : defList) {
            if (def == setting) {
                ret = def;
                break;
            } else {
                int temp = Math.abs(def.getSettingWeight() - setting.getSettingWeight());
                if (temp < diff) {
                    diff = temp;
                    ret = def;
                }
            }
        }
        return ret;
    }
    
    //
	public Definition getRelatedDefinition(Definition closestNormal,
			Definition closestDolby, Definition closestH265, Definition setting) {
		Definition ret = null;
		if (setting.isDolby()) {
			if (closestDolby != null) {
				ret = closestDolby;
			} else if (closestNormal != null) {
				ret = closestNormal;
			} else {
				ret = closestH265;
			}
		} else if (setting.isH265()) {
			if (closestH265 != null) {
				ret = closestH265;
			} else if (closestNormal != null) {
				ret = closestNormal;
			} else {
				ret = closestDolby;
			}
		} else {
			if (closestNormal != null) {
				ret = closestNormal;
			} else if (closestDolby != null) {
				ret = closestDolby;
			} else {
				ret = closestH265;
			}
		}
		Log.d(TAG, "closestNormal = " + closestNormal + ",closestDolby="
				+ closestDolby + ",closestH265=" + closestH265 + ",setting="
				+ setting + ",ret=" + ret);
		return ret;
	}
    public Definition getMostSuitableDefinition(Definition setting) {
        Definition ret = null;
        List<Definition> defList = getDefinitions();
        Log.d(TAG, ">> getMostSuitableDefinition: all="
                + this.toString() + ", setting=" + setting.getSettingWeight());

        if(ListUtils.isEmpty(defList)) {
        	Log.e(TAG, "definition list is empty!!");
        	return null;
        }
        List<Definition> defListNormal = new ArrayList<Definition>();
        List<Definition> defListDolby = new ArrayList<Definition>();
        List<Definition> defListH265 = new ArrayList<Definition>();
        
        for(Definition def: defList) {
        	if(def.isDolby()) {
        		defListDolby.add(def);
        	} else if(def.isH265()) {
        		defListH265.add(def);
        	} else {
        		defListNormal.add(def);
        	}
        }
        
        Definition closestNormal = getMostCloseDefinition(defListNormal, setting);
        Definition closestDolby = getMostCloseDefinition(defListDolby, setting);
        Definition closestH265 = getMostCloseDefinition(defListH265, setting);
        ret = getRelatedDefinition(closestNormal, closestDolby, closestH265, setting);
        Log.d(TAG, "<< getMostSuitableDefinition: mostSuitable=" + ret);
        return ret;
    }

    public static Definition getDefinitionSetting() {
        int defSetting = Project.get().getConfig().getDefaultDefinitonValue();
        return Definition.get(defSetting);
    }
    
    public static Definition getDefaultDefinition() {
        return Definition.DEFINITON_720P;
    }

    public void addDefinition(VideoDefinition definitions) {
        if (definitions == null) {
            if (ListUtils.isEmpty(mDefinitions)) {
                mDefinitions = new ArrayList<Definition>();
                mDefinitions.add(Definition.DEFINITON_HIGH);
            }
            return;
        }
        if (Project.get().getConfig().isEnableDolby()) {
            List<Definition> definitionList = mergeDefinition(definitions.getDefinitions(),
                    mDefinitions);
            mDefinitions = definitionList;
        }
        
        mDefinitions = sortDefinition(Project.get().getConfig().isEnableDolby(),
                Project.get().getConfig().isEnableH265(), definitions.getDefinitions());

        // if the definition above 6, remove the lower definition
        if (mDefinitions != null && mDefinitions.size() > 6) {
            int size = mDefinitions.size();
            mDefinitions = mDefinitions.subList(size - 6, size);
        }
    }
    
    private static String defListToString(List<Definition> defList) {
        StringBuilder sb = new StringBuilder();
        if(ListUtils.isEmpty(defList)) {
            sb.append("definition list is null");
        } else {
            for(Definition df : defList) {
                sb.append(df.toString());
            }
        }
        return sb.toString();
    }
    
    private static List<Definition> sortDefinition(boolean enableDolby,
            boolean enableH265, List<Definition> definitions) {
        Log.d(TAG, "sortDefinition, enableDolby=" + enableDolby
                + ",enableH265=" + enableH265 + "before = "
                + defListToString(definitions));
        List<Definition> listDefinition;
        List<Definition> all = new ArrayList<Definition>();

        for (Definition def : Definition.values()) {
            if ((!enableDolby && def.isDolby())
                    || (!enableH265 && def.isH265())) {
                continue;
            }
            all.add(def);
        }

        listDefinition = new ArrayList<Definition>(all);
        for (Definition def : all) {
            if (!definitions.contains(def)) {
                listDefinition.remove(def);
            }
        }
        
        Log.d(TAG, "sortDefinition, result = " + defListToString(listDefinition));
        return listDefinition;
    }

    private static List<Definition> mergeDefinition(List<Definition> definitionList,
            List<Definition> mdefinitions) {
        if (ListUtils.isEmpty(definitionList)) {
            return mdefinitions;
        } else if (ListUtils.isEmpty(mdefinitions)) {
            return definitionList;
        } else {
            for (Definition def : mdefinitions) {
                if (!definitionList.contains(def) && def.isDolby()) {
                    definitionList.add(def);
                }
            }
            
            return definitionList;
        }
    }

    public void setAutoDefinition(Boolean auto) {
        Log.d(TAG, "setAutoDefinition(" + auto + ")");
    }
    
    public Definition getLowestDefinition() {
        if (ListUtils.isEmpty(mDefinitions)) {
            return null;
        }
        
        Definition lowestDolby = null;
        Definition lowestNormal = null;
        for (Definition def : mDefinitions) {
            if (def.isDolby()) {
                lowestDolby =
                        lowestDolby == null ? def :
                            (def.getSettingWeight() < lowestDolby.getSettingWeight() ? def : lowestDolby);
            } else {
                lowestNormal =
                        lowestNormal == null ? def :
                            (def.getSettingWeight() < lowestNormal.getSettingWeight() ? def : lowestNormal);
            }
        }
        
        Definition lowestDef = null;
        if (lowestNormal != null && lowestDolby != null) {
            Definition correspondingNormalDef = null;
            switch (lowestDolby) {
            case DEFINITON_720P_DOLBY:
                correspondingNormalDef = Definition.DEFINITON_720P;
                break;
            case DEFINITON_1080P_DOLBY:
                correspondingNormalDef = Definition.DEFINITON_1080P;
                break;
            case DEFINITON_4K_DOLBY:
                correspondingNormalDef = Definition.DEFINITON_4K;
                break;
            }
            
            if (correspondingNormalDef != null) {
                lowestDef = correspondingNormalDef.getSettingWeight() < lowestNormal.getSettingWeight() ? lowestDolby : lowestNormal;
            }
        } else {
            lowestDef = lowestNormal != null ? lowestNormal : lowestDolby;
        }
        
        Log.d(TAG, "getLowestDefinition: lowest definition=" + lowestDef);
        return lowestDef;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VideoDefinition{");
        if (mDefinitions != null) {
            for (Definition def : mDefinitions) {
                builder.append(def.toString() + ", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }
    
    public boolean contains(Definition definition) {
        if (mDefinitions != null) {
            return mDefinitions.contains(definition);
        }
        return false;
    }

}
