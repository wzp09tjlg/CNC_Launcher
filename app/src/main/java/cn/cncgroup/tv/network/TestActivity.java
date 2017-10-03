package cn.cncgroup.tv.network;

import android.app.Activity;
import android.os.Bundle;

import cn.cncgroup.tv.R;

/**
 * Created by zhang on 2015/4/23.
 */
public class TestActivity extends Activity {
    public static final String TAG = "TestActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
