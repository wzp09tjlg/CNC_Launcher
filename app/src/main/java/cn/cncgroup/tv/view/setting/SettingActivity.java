package cn.cncgroup.tv.view.setting;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.mobstat.StatService;
import com.squareup.okhttp.Request;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.modle.UpdateData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.update.DownLoadService;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.VersionsInfo;
import cn.cncgroup.tv.view.weather.WeatherCityActivity;

public class SettingActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    UpdateData localUpdateData;
    private RelativeLayout mSetting_update;
    private RelativeLayout mSetting_weather;
    private ImageView mIv_update;
    private RelativeLayout mBoot_custom;
    private LocalUpateDataListener mLocalUpateDataListener = new LocalUpateDataListener();
    private String TAG = "SettingActivity";
    private boolean canUpdate = false;
    private ShadowView mShadowView;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void findView() {
        mSetting_update = (RelativeLayout) findViewById(R.id.setting_update);
        mSetting_weather = (RelativeLayout) findViewById(R.id.setting_weather);
        mIv_update = (ImageView) findViewById(R.id.iv_update);
        mBoot_custom = (RelativeLayout) findViewById(R.id.boot_custom);
        mShadowView = (ShadowView) findViewById(R.id.shadow);
        mSetting_weather.setOnClickListener(this);
        mSetting_weather.setOnFocusChangeListener(this);
        mSetting_update.setOnClickListener(this);
        mSetting_update.setOnFocusChangeListener(this);
        mBoot_custom.setOnClickListener(this);
        mBoot_custom.setOnFocusChangeListener(this);
        mIv_update.setVisibility(View.GONE);
    }

    @Override
    protected void initView() {
        mShadowView.init(25);
        //初始化进入该页面就开始检查是否有新版本
        NetworkManager.getInstance().checkUpdate(
                mLocalUpateDataListener, TAG);
        startService(new Intent(this, DownLoadService.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_update:
                //软件升级 添加统计
                StatService.onEvent(getActivity(), "App_updating", getActivity().getString(R.string.app_updating), 1);
                //当用户点击该按钮的时候要是有新版本就开始更新
                if (canUpdate) {
                    startUpdate();
                } else {
                    showToast(getResources().getString(R.string.no_update_app), GlobalConstant.ToastShowTime);
                }
                break;
            case R.id.setting_weather:
                gotoActivity(WeatherCityActivity.class);
//				overridePendingTransition(R.anim.push_up_in, R.anim.hold);
                //天气设置  增加统计
                StatService.onEvent(getActivity(), "Weather_setting", getActivity().getString(R.string.weather_setting), 1);
                break;
            case R.id.boot_custom:
                gotoActivity(SettingBootCustomActivity.class);
                break;
        }
    }

    private void startUpdate() {//更新提示相关的进度条提示框

        Intent intent = new Intent(this, SettingSoftwareUpgradeActivity.class);
        intent.putExtra("updateBean", localUpdateData);
        startActivity(intent);
        //下载完成以后的包解压，安装，重启等相关操作和提示
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mShadowView.moveTo(v);
        }
    }

    private void changeView(int ret) {
        if (ret == 1) {//请求成功
            //显示有更新的布局
            if (localUpdateData.getVersioncode() > VersionsInfo.getAppVersionCode(this, this.getPackageName())) {
                mIv_update.setVisibility(View.VISIBLE);
                canUpdate = true;
            } else {
                //不做任何更改
            }
        } else {//请求失败
            //不做任何更改
        }
    }

    private class LocalUpateDataListener implements
            BaseRequest.Listener<UpdateData> {
        @Override
        public void onResponse(UpdateData result, boolean isFromCache) {
            localUpdateData = result;
            changeView(localUpdateData.getRet());
        }

        @Override
        public void onFailure(int errorCode, Request request) {
            changeView(0);
        }
    }
}
