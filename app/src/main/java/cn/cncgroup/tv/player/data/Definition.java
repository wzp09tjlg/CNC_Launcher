package cn.cncgroup.tv.player.data;

import android.content.Context;
import android.util.Log;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.utils.GlobalConstant;

public enum Definition implements IDefinition {
    DEFINITON_HIGH(R.string.definition_high, 2, GlobalConstant.DEFINITION_SETTINGS_WEIGHT_HIGH, false, false),
    
    DEFINITON_720P(R.string.definition_720P, 4, GlobalConstant.DEFINITION_SETTINGS_WEIGHT_720P, false, false),
    DEFINITON_720P_DOLBY(R.string.definition_720p_dolby, 14, GlobalConstant.DEFINITION_SETTINGS_WEIGHT_720P, true, false),
    DEFINITON_720P_H265(R.string.definition_720p_h265, 17, GlobalConstant.DEFINITION_SETTINGS_WEIGHT_720P,false, true),

    DEFINITON_1080P(R.string.definition_1080P, 5, GlobalConstant.DEFINITION_SETTINGS_WEIGHT_1080P, false, false),
    DEFINITON_1080P_DOLBY(R.string.definition_1080p_dolby, 15, GlobalConstant.DEFINITION_SETTINGS_WEIGHT_1080P, true, false),
    DEFINITON_1080P_H265(R.string.definition_1080p_h265, 18, GlobalConstant.DEFINITION_SETTINGS_WEIGHT_1080P, false, true),

    DEFINITON_4K(R.string.definition_4K, 10, GlobalConstant.DEFINITION_SETTING_WEIGHT_4K, false, false),
    DEFINITON_4K_DOLBY(R.string.definition_4k_dolby, 16, GlobalConstant.DEFINITION_SETTING_WEIGHT_4K, true, false),
    DEFINITON_4K_H265(R.string.definition_4k_h265, 19, GlobalConstant.DEFINITION_SETTING_WEIGHT_4K, false, true);

    private static final String TAG = "Definition";

    private int mDefinition;
    private final int mSettingWeightingFactor;
    private String mName;
    private int mNameResourceId;
    private boolean mIsDolby;
    private boolean mIsH265;
    
    Definition(int resourceId, int definition, int settingWeight, boolean dolby, boolean h265) {
        mNameResourceId = resourceId;
        mDefinition = definition;
        mSettingWeightingFactor = settingWeight;
        mIsDolby = dolby;
        mIsH265 = h265;
    }
    
    @Override
    public boolean isDolby() {
        return mIsDolby;
    }
    
    @Override
    public boolean isH265() {
        return mIsH265;
    }
    
    @Override
    public boolean is4K() {
        return (mDefinition == DEFINITON_4K.getValue()
                || mDefinition == DEFINITON_4K_DOLBY.getValue()
                || mDefinition == DEFINITON_4K_H265.getValue());
    }
    
    @Override
    public String getName(Context context) {
        if (mName == null) {
            mName = context.getString(mNameResourceId);
        }
        return mName;
    }
    
    @Override
    public int getValue() {
        return mDefinition;
    }
    
    @Override
    public int getSettingWeight() {
        return mSettingWeightingFactor;
    }

    @Override
    public String getDefinitionString() {
        return String.valueOf(mDefinition);
    }

    // TODO consider adding "definition group" to simplify such judgement
    private static boolean isDefinitionSupported(int definition) {
        boolean is4k = DEFINITON_4K.getValue() == definition
                || DEFINITON_4K_DOLBY.getValue() == definition
                || DEFINITON_4K_H265.getValue() == definition;
        boolean isConfigSupport4k = false;//Project.get().getConfig().is4kStreamSupported();
        
        boolean is4kH265 = DEFINITON_4K_H265.getValue() == definition;
        boolean isConfigSupport4kH265 = false;//Project.get().getConfig().is4kH265StreamSupported();
        
        boolean isConfigSupported = !(is4k && !isConfigSupport4k || is4kH265 && !isConfigSupport4kH265);
        Log.d(TAG, "isDefinitionSupported(" + definition + ") return " + isConfigSupported);
        return isConfigSupported;
    }
    
    public static Definition get(int definition) {
        Log.d(TAG, "get(" + definition + ")");
        for (Definition vd : Definition.values()) {
            if (vd.getValue() == definition
                    && isDefinitionSupported(definition)) {
                return vd;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append("Definition[value:")
                .append(mDefinition)
                .append(", ")
                .append(mName)
                 .append(", ")
                .append("weight:")
                .append(mSettingWeightingFactor)
                .append("]")
                .toString();
    }
}