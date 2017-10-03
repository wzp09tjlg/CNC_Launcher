package cn.cncgroup.tv.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;

import cn.cncgroup.tv.db.DbUtil;
import cn.cncgroup.tv.db.VideoSetDao;
import cn.cncgroup.tv.utils.GlobalConstant;

/**
 * Created by jinwenchao123 on 15/10/29.
 */
public class YoupengHistoryReceiver extends BroadcastReceiver {
    private static final String TAG = "youpengHistoryReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //		Log.i(TAG, "onReceive action = " + intent.getAction());
        try {
//            Log.e(TAG, "playorder::::time = " + intent.getIntExtra("time", 0));
//            Log.e(TAG, "playorder :::::duration= " + intent.getIntExtra("duration", 0));
            ArrayList<VideoSetDao> historyData = (ArrayList<VideoSetDao>) DbUtil
                    .queryAll(GlobalConstant.ISHISTORY);
            VideoSetDao videoSetDao = historyData.get(0);
            videoSetDao.startTime = intent.getIntExtra("time", 0);
            videoSetDao.length = intent.getIntExtra("duration", 0);
            DbUtil.addByObject(videoSetDao,true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
