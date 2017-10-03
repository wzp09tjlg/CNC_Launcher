package cn.cncgroup.tv.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * 应用程序设置信息。
 *
 * @author jinwenchao
 *         <p/>
 *         sample:
 *         //单例创建该类实例
 *         Preferences cncConfig =
 *         Preferences.build(context, GlobalConstant.CNC_GROUP);
 *         <p/>
 *         KEY_PUT为string常量
 *         <p/>
 *         获取数据：
 *         cncConfig.getInt(“KEY_PUT”, 0);
 *         添加数据：
 *         cncConfig.putInt(“KEY_PUT”, 0);
 */
public class Preferences {

    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor = null;


    /**
     * 私有构造器。
     *
     * @param context
     * @param cfgName
     * @version 1.0
     */
    private Preferences(Context context, String cfgName) {
        if (context == null)
            throw new IllegalArgumentException("the context is null");
        mSp = context.getSharedPreferences(cfgName, Context.MODE_PRIVATE);
    }

    /**
     * 构建 Preferences 实例。
     *
     * @param context Context
     * @param cfgName 配置文件名称
     * @return
     */
    public static Preferences build(Context context, String cfgName) {
        return new Preferences(context, cfgName);
    }

    /**
     * 使用默认名称("CNC_GROUP") 构建 Preferences 实例。
     *
     * @param context Context
     * @return
     */
    public static Preferences build(Context context) {
        return new Preferences(context, GlobalConstant.CNC_GROUP);
    }

    public boolean contains(String key) {
        return mSp.contains(key);
    }

    public int getInt(String key, int defValue) {
        int value = mSp.getInt(key, defValue);
        LogUtil.i("getInt");
        LogUtil.i("key:" + key);
        LogUtil.i("value:" + value);
        return value;
    }

    public String getString(String key, String defValue) {
        String value = mSp.getString(key, defValue);
        LogUtil.i("getString");
        LogUtil.i("key:" + key);
        LogUtil.i("value:" + value);
        return value;
    }

    public boolean getBoolean(String key, boolean defValue) {
        boolean value = mSp.getBoolean(key, defValue);
        LogUtil.i("getBoolean");
        LogUtil.i("key:" + key);
        LogUtil.i("value:" + value);
        return value;
    }

    public float getFloat(String key, float defValue) {
        float value = mSp.getFloat(key, defValue);
        LogUtil.i("getFloat");
        LogUtil.i("key:" + key);
        LogUtil.i("value:" + value);
        return value;
    }

    public long getLong(String key, long defValue) {
        long value = mSp.getLong(key, defValue);
        LogUtil.i("getLong");
        LogUtil.i("key:" + key);
        LogUtil.i("value:" + value);
        return value;
    }

    public boolean putInt(String key, int value) {
        mEditor = mSp.edit();
        mEditor.putInt(key, value);
        LogUtil.i("putInt");
        LogUtil.i("key:" + key);
        LogUtil.i("value:" + value);
        return mEditor.commit();
    }

    public boolean putLong(String key, long value) {
        mEditor = mSp.edit();
        mEditor.putLong(key, value);
        LogUtil.i("putLong");
        LogUtil.i("key:" + key);
        LogUtil.i("value:" + value);
        return mEditor.commit();
    }

    public boolean putBoolean(String key, boolean value) {
        mEditor = mSp.edit();
        mEditor.putBoolean(key, value);
        LogUtil.i("putBoolean");
        LogUtil.i("key:" + key);
        LogUtil.i("value:" + value);
        return mEditor.commit();
    }

    public boolean putFloat(String key, float value) {
        mEditor = mSp.edit();
        mEditor.putFloat(key, value);
        LogUtil.i("putFloat");
        LogUtil.i("key:" + key);
        LogUtil.i("value:" + value);
        return mEditor.commit();
    }

    public boolean putString(String key, String value) {
        mEditor = mSp.edit();
        mEditor.putString(key, value);
        LogUtil.i("putString");
        LogUtil.i("key:" + key);
        LogUtil.i("value:" + value);
        return mEditor.commit();
    }

    public boolean removeKey(String key) {
        mEditor = mSp.edit();
        mEditor.remove(key);
        return mEditor.commit();
    }

    public Map<String, ?> getAll() {
        return mSp.getAll();
    }

    private static Preferences instance = null;

    private Preferences() {

    }

    public boolean clear() {
        mEditor = mSp.edit();
        mEditor.clear();
        return mEditor.commit();
    }


}
