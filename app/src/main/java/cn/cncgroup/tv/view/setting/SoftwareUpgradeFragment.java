package cn.cncgroup.tv.view.setting;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Request;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.modle.UpdateData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.update.DownLoadService;
import cn.cncgroup.tv.update.DownloadUtil;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.utils.VersionsInfo;

/**
 * Created by zhangtao on 2015/11/26.
 */
public class SoftwareUpgradeFragment extends BaseFragment implements View.OnFocusChangeListener {
    private static final String TAG = "SoftwareUpgradeFragment";
    private TextView mVersion_textview;
    private TextView mVersion_data_textview;
    private TextView mUpdate_content_textview;
    private LocalUpateDataListener mLocalUpateDataListener = new LocalUpateDataListener();
    private TextView mLmmediate_update_textview;
    private ScrollView mDescContainer;
    private ShadowView mShadowView;
    UpdateData localUpdateData;
    private ObjectAnimator mDescAnimator;
    DownloadUtil mDialogUtil = new DownloadUtil();
    private boolean isLatestversion;

    @Override
    protected int getContentView() {
        return R.layout.setting_software_upgrade;
    }

    @Override
    protected void findView(View view) {

        mShadowView = (ShadowView) view.findViewById(R.id.shadow);
        mVersion_data_textview = (TextView) view.findViewById(R.id.version_data_textview);
        mVersion_textview = (TextView) view.findViewById(R.id.version_textview);
        mLmmediate_update_textview = (TextView) view.findViewById(R.id.lmmediate_update_textview);
        mUpdate_content_textview = (TextView) view.findViewById(R.id.update_content_textview);
        mDescContainer = (ScrollView) view.findViewById(R.id.desc_container);
    }

    @Override
    protected void initView() {
        NetworkManager.getInstance().checkUpdate(mLocalUpateDataListener, TAG);
//        Intent intent = new Intent(getActivity(), DownLoadService.class);
//        getActivity().startService(intent);
        Intent intent = new Intent(getActivity(), DownLoadService.class);
        intent.putExtra("updateBean", localUpdateData);
        getActivity().startService(intent);
        mShadowView.init(25);
    }

    private class LocalUpateDataListener implements
            BaseRequest.Listener<UpdateData> {
        @Override
        public void onResponse(final UpdateData result, boolean isFromCache) {
            localUpdateData = result;
            mDescContainer.setFocusable(false);
            mDescContainer.setSelected(false);
            mDescContainer.setClickable(false);
            if (localUpdateData == null) {
                return;
            }
            Log.i("mVersion_textview", "mVersion_textview=" + localUpdateData.getVersioncode()+"~~~~~~~~~~~~~~~"
                    +VersionsInfo.getAppVersionCode(getActivity(), getActivity().getPackageName()));
            if (localUpdateData.getVersioncode() > VersionsInfo.getAppVersionCode(getActivity(), getActivity().getPackageName())){
                mVersion_textview.setText("最新版本" + "V" + localUpdateData.getVersion());
                mVersion_data_textview.setText("（" + localUpdateData.getReleasedata().replace("-", "") + "）");
                mLmmediate_update_textview.setFocusable(true);
                mLmmediate_update_textview.setVisibility(View.VISIBLE);
                mLmmediate_update_textview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {

                    }
                });
                mLmmediate_update_textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogUtil.checkUpdata(getActivity(), localUpdateData.getUrl(),
                                getActivity().getPackageName(), localUpdateData.getVersion());
                        //下载完成以后的包解压，安装，重启等相关操作和提示
                    }
                });
                isLatestversion  = false;
            } else {
                mVersion_textview.setText("当前已是最新版本 " + "V" + localUpdateData.getVersion());
                mVersion_data_textview.setText("");
                mLmmediate_update_textview.setFocusable(false);
                mLmmediate_update_textview.setVisibility(View.GONE);
                isLatestversion  = true;
            }
            Preferences.build(getActivity()).putBoolean("isLatestversion", isLatestversion);

            //从数据库读取的文本\n无法转译出来，需要在代码中替换掉
            mUpdate_content_textview.setText(localUpdateData.getUpdateinfo().replace("\\n", "\n"));
            mUpdate_content_textview.setFocusable(false);
            mUpdate_content_textview.setSelected(false);
            mUpdate_content_textview.setClickable(false);
            mUpdate_content_textview.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            int height = mUpdate_content_textview.getMeasuredHeight();
                            int containerHeight = mDescContainer
                                    .getMeasuredHeight();
                            int offset = height - containerHeight;
                            if (mDescAnimator == null && offset > 0) {
                                int duration = height * 100;
                                mDescAnimator = ObjectAnimator
                                        .ofInt(mDescContainer, "scrollY", 0, offset)
                                        .setDuration(duration);
                                mDescAnimator
                                        .setRepeatCount(ValueAnimator.INFINITE);
                                mDescAnimator.setRepeatMode(ValueAnimator.REVERSE);
                                mDescAnimator.start();
                            }
                        }
                    });
        }

        @Override
        public void onFailure(int errorCode, Request response) {

        }
    }
    @Override
    public void onFocusChange(View view, boolean b) {

    }
}
