package cn.cncgroup.tv.settings;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.OnChildViewHolderSelectedListener;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * Created by zhang on 2015/11/20.
 */
public abstract class BaseSettingActivity extends BaseActivity {
    protected VerticalGridView mMenu;
    protected TextView mTitle;
    protected ShadowView mShadow;

    private OnChildViewHolderSelectedListener mSelectListener = new OnChildViewHolderSelectedListener() {
        @Override
        public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
            onMenuSelected(parent, child, position);
        }
    };

    private ViewTreeObserver.OnGlobalFocusChangeListener mFocusListener = new ViewTreeObserver.OnGlobalFocusChangeListener() {
        @Override
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            moveTo(newFocus);
        }
    };

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_base_setting);
    }

    @Override
    protected void findView() {
        mMenu = (VerticalGridView) findViewById(R.id.menu);
        mTitle = (TextView) findViewById(R.id.title);
        mShadow = (ShadowView) findViewById(R.id.shadow);
    }

    @Override
    protected void initView() {
        mTitle.setText(getSettingTitle());
        mMenu.setOnChildViewHolderSelectedListener(mSelectListener);
        mMenu.setAdapter(getMenuAdapter());
        mShadow.init(25, 150);
        mMenu.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        mMenu.getViewTreeObserver().addOnGlobalFocusChangeListener(mFocusListener);
    }

    protected abstract String getSettingTitle();

    abstract RecyclerView.Adapter getMenuAdapter();

    abstract void onMenuSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position);

    public void moveTo(View view) {
        mShadow.moveTo(view);
    }

    public void cancelMove() {
        mShadow.cancelMove();
    }
}
