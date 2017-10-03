package cn.cncgroup.tv.view.setting;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.view.setting.adapter.SettingHomeAdapter;

/**
 * Created by zhang on 2015/11/25.
 */
public class SettingHomeActivity extends BaseActivity
        implements OnItemClickListener<SettingHomeAdapter.SettingItem>,
        OnItemFocusListener<SettingHomeAdapter.SettingItem> {
    private static final String TAG = "SettingHomeActivity";
    private VerticalGridView mGridView;
    private ShadowView mShadowView;
    private SettingHomeAdapter mAdapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_setting_home);
    }

    @Override
    protected void findView() {
        mGridView = (VerticalGridView) findViewById(R.id.gridview);
        mShadowView = (ShadowView) findViewById(R.id.shadow);
    }

    @Override
    protected void initView() {
        mAdapter = new SettingHomeAdapter(this, this);
        mGridView.setAdapter(mAdapter);
        mShadowView.init(25, 150);
    }

    @Override
    public void onItemClickLister(View view, int position, SettingHomeAdapter.SettingItem settingItem) {
        startActivity(new Intent(this, settingItem.getActivity()));
    }

    @Override
    public void onItemFocusLister(View view, int position, SettingHomeAdapter.SettingItem settingItem, boolean hasFocus) {
        if (hasFocus) {
            mShadowView.moveTo(view);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
    }
}
