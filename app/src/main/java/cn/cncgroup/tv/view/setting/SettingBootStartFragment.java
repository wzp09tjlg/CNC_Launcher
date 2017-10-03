package cn.cncgroup.tv.view.setting;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.settings.BaseSettingActivity;
import cn.cncgroup.tv.utils.Preferences;

/**
 * Created by zhangtao on 2015/11/3.
 */
public class SettingBootStartFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "SettingBootStartFragment";
    private RelativeLayout mBg_boot_start;
    private ImageView mBg_boot_start_imageview;
    private TextView mOn_textview;
    private TextView mOff_textview;
    private boolean ischeck = false;
    private static final String KEY_CHECK = "keycheck";
    private BaseSettingActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseSettingActivity) activity;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_setting_boot_start;
    }

    @Override
    protected void findView(View view) {
        mBg_boot_start = (RelativeLayout) view.findViewById(R.id.bg_boot_start);
        mBg_boot_start_imageview = (ImageView) view.findViewById(R.id.bg_boot_start_imageview);
        mOn_textview = (TextView) view.findViewById(R.id.on_textview);
        mOff_textview = (TextView) view.findViewById(R.id.off_textview);
    }

    protected void initView() {
        mBg_boot_start.setOnClickListener(this);
        if (Preferences.build(getActivity()).getBoolean(KEY_CHECK, ischeck)) {
            mBg_boot_start_imageview.setImageResource(R.drawable.boot_btn_on);
            mOn_textview.setTextColor(getResources().getColor(R.color.textview_bright));
            mOff_textview.setTextColor(getResources().getColor(R.color.textview_dark));
            ischeck = true;
        } else {
            mBg_boot_start_imageview.setImageResource(R.drawable.boot_btn_off);
            mOn_textview.setTextColor(getResources().getColor(R.color.textview_dark));
            mOff_textview.setTextColor(getResources().getColor(R.color.textview_bright));
            ischeck = false;
        }
    }

    /**
     * ischeck的值默认是false，点击之后值改变，判断ischeck的值，用来设置字体颜色和图片显示效果。
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bg_boot_start:
                if (ischeck) {
                    mBg_boot_start_imageview.setImageResource(R.drawable.boot_btn_off);
                    mOn_textview.setTextColor(getResources().getColor(R.color.textview_dark));
                    mOff_textview.setTextColor(getResources().getColor(R.color.textview_bright));
                    ischeck = !ischeck;
                    //将ischeck值存入Preferences中
                    Preferences.build(getActivity()).putBoolean(KEY_CHECK, ischeck);
                } else {
                    mBg_boot_start_imageview.setImageResource(R.drawable.boot_btn_on);
                    mOn_textview.setTextColor(getResources().getColor(R.color.textview_bright));
                    mOff_textview.setTextColor(getResources().getColor(R.color.textview_dark));
                    ischeck = !ischeck;
                    Preferences.build(getActivity()).putBoolean(KEY_CHECK, ischeck);
                }
                break;
            default:
                break;
        }
    }
}