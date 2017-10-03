package cn.cncgroup.tv.cncplayer;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.eventbus.FinishActivityEvent;
import de.greenrobot.event.EventBus;

public class PlayerContainerActivity extends FragmentActivity{
    private static final String TAG = "PlayerContainerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_container);
        EventBus.getDefault().register(this);
        Log.i(TAG, "onCreate");
        init();
    }

    private void init() {
        PlayerConstants.SCREEN_TYPE=PlayerConstants.ACTIVITY_FULL;
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String serialNumber = bundle.getString("serialNumber");
        int mPlayTime = bundle.getInt("playTime");
        Log.i(TAG,"id="+id+"--serialNumber="+serialNumber+"--mPlayTime="+mPlayTime);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        PlayerFragment instance = PlayerFragment.newInstance(getIntent().getExtras());
        transaction.replace(R.id.player,instance,"PlayerFragment");
        transaction.commit();
    }

    public void onEventMainThread(FinishActivityEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
