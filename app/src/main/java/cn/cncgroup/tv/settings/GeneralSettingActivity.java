package cn.cncgroup.tv.settings;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**
 * Created by zhang on 2015/11/20.
 */
public abstract class GeneralSettingActivity extends BaseSettingActivity {
    private GeneralMenuAdapter mAdapter;

    @Override
    RecyclerView.Adapter getMenuAdapter() {
        mAdapter = new GeneralMenuAdapter(getMenuList());
        return mAdapter;
    }

    @Override
    void onMenuSelected(RecyclerView parent, RecyclerView.ViewHolder child, final int position) {
        GeneralMenu menu = mAdapter.getItem(position);
        mMenu.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.setSelectPosition(position);
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = Fragment.instantiate(this, menu.getFragment().getName());
        transaction.replace(R.id.content, fragment, "" + position);
        transaction.commit();
    }

    protected abstract ArrayList<GeneralMenu> getMenuList();
}
