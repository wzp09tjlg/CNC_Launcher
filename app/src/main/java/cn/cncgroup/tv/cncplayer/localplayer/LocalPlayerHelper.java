package cn.cncgroup.tv.cncplayer.localplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Administrator on 2015/10/10.
 */
public class LocalPlayerHelper {
    private static final String SP_NAME = "localhistory";
    private static final String TAG = "LocalPlayerHelper";
    private static SharedPreferences mSp;
    private static LocalPlayerHelper mHelper;

    private LocalPlayerHelper() {
    }

    public static LocalPlayerHelper getInstance(Context context) {
        if (mHelper == null)
            mHelper = new LocalPlayerHelper();
        if (mSp == null)
            mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return mHelper;
    }

    public void putHistory(String uri, int time) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(uri, time);
        editor.apply();
    }

    public void clearHistory() {
        Log.i(TAG,"clear local player History");
        SharedPreferences.Editor editor = mSp.edit();
        editor.clear();
        editor.apply();
    }

    public void removeHistory(String uri) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.remove(uri);
        editor.apply();
    }

    public int getHistory(String uri) {
       return mSp.getInt(uri,-1);
    }
}
