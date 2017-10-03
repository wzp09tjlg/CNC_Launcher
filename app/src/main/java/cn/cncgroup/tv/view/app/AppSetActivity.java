package cn.cncgroup.tv.view.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.AppSetMenuAdapter;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.SystemAndAppMsgUtil;

public class AppSetActivity extends BaseActivity
        implements OnItemFocusListener<AppSetMenuAdapter.AppSetMenuItem> {
    private TextView tv_sdcard_size;
    private ProgressBar pb_sdcard_used;
    private Fragment fragment;
    private VerticalGridView mMenu;
    private AppSetMenuAdapter mAdapter;
    private static String TAG = "AppSetActivity";

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_app_set);
    }

    @Override
    protected void findView() {
        tv_sdcard_size = (TextView) findViewById(R.id.tv_sdcard_size);
        pb_sdcard_used = (ProgressBar) findViewById(R.id.pb_sdcard_used);
        mMenu = (VerticalGridView) findViewById(R.id.appset_menu);
    }

    @Override
    protected void initView() {
        mAdapter = new AppSetMenuAdapter(this);
        mMenu.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String available = SystemAndAppMsgUtil.getSDAvailableSize(this);
        String total = SystemAndAppMsgUtil.getSDTotalSize(this);
        tv_sdcard_size.setText(available + getResources().getString(R.string.sd_card_msg) + total);
        pb_sdcard_used.setProgress(SystemAndAppMsgUtil.getSDcardPregress(this));
        //调用fragment方法，实现刷新adapter
    }

    @Override
    public void onItemFocusLister(View view, int position, AppSetMenuAdapter.AppSetMenuItem appSetMenuItem, boolean hasFocus) {
        if (hasFocus && position != mAdapter.getSelectedPosition()) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            fragment = Fragment.instantiate(this, appSetMenuItem.fragment.getName());
            transaction.replace(R.id.app_set_content, fragment);
            transaction.commit();
            mAdapter.setSelectedPosition(position);
        }
    }
}
