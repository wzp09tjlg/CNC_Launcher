package cn.cncgroup.tv.view.setting;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.modle.UpdateData;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.update.DownloadUtil;

/**
 * Created by zhangtao on 2015/11/12.
 */
public class SettingSoftwareUpgradeActivity extends BaseActivity  implements View.OnFocusChangeListener{
    private TextView mVersion_textview;
    private TextView mUpdate_content_textview;
    private TextView mLmmediate_update_textview;
    private TextView mVersion_data_textview;
    UpdateData localUpdateData;
    private ScrollView mDescContainer;
    private ObjectAnimator mDescAnimator;
    private ShadowView mShadowView;
    DownloadUtil mDialogUtil = new DownloadUtil();
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_setting_boot_update);
    }

    @Override
    protected void findView() {

        localUpdateData = (UpdateData)getIntent().getSerializableExtra("updateBean");
        mShadowView = (ShadowView) findViewById(R.id.shadow);
        mVersion_textview = (TextView) findViewById(R.id.version_textview);
        mVersion_data_textview = (TextView) findViewById(R.id.version_data_textview);
        mVersion_data_textview.setText("（"+localUpdateData.getReleasedata().replace("-","")+"）");
        mVersion_textview.setText("最新版本 "+"V "+localUpdateData.getVersion());
        mUpdate_content_textview = (TextView) findViewById(R.id.update_content_textview);

        mDescContainer = (ScrollView) findViewById(R.id.desc_container);
        mDescContainer.setFocusable(false);
        mDescContainer.setSelected(false);
        mDescContainer.setClickable(false);
        mLmmediate_update_textview = (TextView)findViewById(R.id.lmmediate_update_textview);
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

        mLmmediate_update_textview.setOnFocusChangeListener(this);
        mLmmediate_update_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdate();
            }
        });
    }


    private void startUpdate() {//更新提示相关的进度条提示框

        mDialogUtil.checkUpdata(this, localUpdateData.getUrl(), this.getPackageName(), localUpdateData.getVersion());
        //下载完成以后的包解压，安装，重启等相关操作和提示
    }
    @Override
    protected void initView() {
        mShadowView.init(25);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            mShadowView.moveTo(v);
        }
    }
    @Override
    public void onStop() {
        if (mDescAnimator != null) {
            mDescAnimator.cancel();
        }
        super.onStop();
    }
}
