package cn.cncgroup.tv.view.homesecond;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.view.homesecond.adapter.CustomTabAdapter;
import cn.cncgroup.tv.view.homesecond.utils.HomeTab;
import cn.cncgroup.tv.view.homesecond.utils.HomeTabManager;

/**
 * Created by zhang on 2015/11/16.
 */
public class CustomTabDialog extends DialogFragment implements OnItemFocusListener<HomeTab> {
    private VerticalGridView mGridView;
    private CustomTabAdapter mAdapter;
    private ShadowView mShadowView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomTabDialog);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(
                getResources().getDimensionPixelSize(R.dimen.dimen_1200dp),
                getResources().getDimensionPixelSize(R.dimen.dimen_630dp));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_custom_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mShadowView = (ShadowView) view.findViewById(R.id.shadow);
        mShadowView.init(26);
        mGridView = (VerticalGridView) view.findViewById(R.id.gridview);
        mAdapter = new CustomTabAdapter(this);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void onItemFocusLister(View view, int position, HomeTab tab, boolean hasFocus) {
        if (hasFocus) {
            mShadowView.moveTo(view);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        HomeTabManager.getInstance().resetCustomTab(mAdapter.getChoicePosition());
        super.onDismiss(dialog);
    }
}