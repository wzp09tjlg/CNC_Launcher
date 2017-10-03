package cn.cncgroup.tv.view.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.cncgroup.tv.WelcomeActivity;
import cn.cncgroup.tv.utils.Preferences;

/**
 * 构造广播接收者，用来监听从Preferences中取出ischeck的值，判断是否开机自启
 * Created by zhangtao on 2015/11/6.
 */
public class AppStartReceiver extends BroadcastReceiver {
    private static final String KEY_CHECK = "keycheck";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Preferences.build(context).getBoolean(KEY_CHECK, false)) {
            Intent i = new Intent(context, WelcomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
