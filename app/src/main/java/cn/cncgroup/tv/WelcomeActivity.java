package cn.cncgroup.tv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.squareup.okhttp.Request;

import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.modle.CategoryData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.view.homesecond.HomeActivity;
import cn.cncgroup.tv.view.homesecond.utils.CategoryUtils;

/**
 * Created by zhang on 2015/5/4.
 */
public class WelcomeActivity extends Activity {
    private static final String TAG = "WelcomActivity";
    private CategoryDataListener mCategoryDataListener = new CategoryDataListener();
    private TextView mVersion;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mVersion = (TextView) findViewById(R.id.version);
        mVersion.setText("V" + Project.get().getConfig().getRevision());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(getDataRunable, 300);
    }

    private Runnable getDataRunable = new Runnable() {
        @Override
        public void run() {
            NetworkManager.getInstance().getCategoryData(mCategoryDataListener, TAG, false, false);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        NetworkManager.cancelRequest(TAG);
        mHandler.removeCallbacks(getDataRunable);
    }

    private void checkDataLoad() {
        boolean flag = CategoryUtils.sCategoryData != null || mCategoryDataListener.isTry;
        if (flag) {
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
            finish();
        }
    }

    private class CategoryDataListener implements BaseRequest.Listener<CategoryData> {
        boolean isTry;

        @Override
        public void onResponse(CategoryData result, boolean isFromCache) {
            CategoryUtils.sCategoryData = result;
            checkDataLoad();
        }

        @Override
        public void onFailure(int errorCode, Request request) {
            if (!isTry) {
                NetworkManager.getInstance().getCategoryData(this, TAG, true, true);
                isTry = true;
            } else {
                checkDataLoad();
            }
        }
    }
}
