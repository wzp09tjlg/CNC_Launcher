package cn.cncgroup.tv.cncplayer.localplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2015/10/10.
 * 接收外部存储拔掉的广播
 */
public class ExternalStorageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED))
        if (intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED)) {
            LocalPlayerHelper.getInstance(context).clearHistory();
        }
    }
}
