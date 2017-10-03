package cn.cncgroup.tv.view.setting;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.ui.widget.CustomDialog;
import cn.cncgroup.tv.utils.DisplayUtil;

/**
 * Created by zhangtao on 2015/11/23.
 */
public class RestoreFactorySetting extends BaseFragment implements View.OnClickListener {
    private TextView mCancel_textview;
    private TextView mContinue_textview;

    @Override
    protected int getContentView() {
        return R.layout.restore_factory_settings;
    }

    @Override
    protected void findView(View view) {
        mCancel_textview = (TextView) view.findViewById(R.id.cancel_textview);
        mContinue_textview = (TextView) view.findViewById(R.id.continue_textview);
    }

    @Override
    protected void initView() {
        mCancel_textview.setOnClickListener(this);
        mContinue_textview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_textview:
                getActivity().onBackPressed();
                break;
            case R.id.continue_textview:
                showRestoreDialog();
                break;
            default:
                break;
        }
    }

    private void showRestoreDialog() {
        CustomDialog dialog = new CustomDialog(getActivity(), false);
        dialog.setTextSize(DisplayUtil.px2sp(getActivity(),
                getActivity().getResources().getDimensionPixelOffset(R.dimen.dimen_36sp)));
        dialog.setMessage(getString(R.string.restore_tips));
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        dialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
            }
        });
        dialog.show();
    }
}
