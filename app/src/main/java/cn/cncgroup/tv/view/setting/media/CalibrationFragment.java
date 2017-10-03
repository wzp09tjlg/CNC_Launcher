package cn.cncgroup.tv.view.setting.media;

import android.view.View;
import android.widget.ImageView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;

/**
 * Created by zhang on 2015/11/26.
 */
public class CalibrationFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mEnter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_calibration;
    }

    @Override
    protected void findView(View view) {
        mEnter = (ImageView) view.findViewById(R.id.enter);
    }

    @Override
    protected void initView() {
        mEnter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        gotoActivity(CalibrationSettingActivity.class);
    }
}
