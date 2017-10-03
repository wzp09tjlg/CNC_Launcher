package cn.cncgroup.tv.view.setting;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.view.setting.adapter.FeedBackAdapter;

/**
 * Created by zhangguang on 2015/11/25.
 */
public class SettingFeedBackFragment extends BaseFragment implements OnItemClickListener<FeedBackAdapter.FeedBackBean> {

    private static final String TAG = "SettingFeedBackFragment";
    private VerticalGridView mVerticalGridView;
    private ImageView mTwoCode;    //二维码图片

    @Override
    protected int getContentView() {
        return R.layout.activity_setting_feedback;
    }

    @Override
    protected void findView(View view) {
        mVerticalGridView = (VerticalGridView) view.findViewById(R.id.gridview);
        mTwoCode = (ImageView) view.findViewById(R.id.fk_imageView_twocode);
    }

    @Override
    protected void initView() {
        Log.i(TAG, "mVerticalGridView:" + mVerticalGridView);
        mVerticalGridView.setAdapter(new FeedBackAdapter(this));
        mTwoCode.setImageURI(Uri.parse(GlobalConstant.QR_CODE_HTTP));
    }

    @Override
    public void onItemClickLister(View view, int position, FeedBackAdapter.FeedBackBean feedBackBean) {
        Log.i(TAG, "onItemClickLister" + feedBackBean.desc);
        cheeckFeedBackType(position);
        GlobalToast.makeText(getActivity(), getActivity().getString(R.string.feedback_succeed), GlobalConstant.ToastShowTime).show();
    }

    private void cheeckFeedBackType(int position) {
        switch (position) {
            case 0:             //影片太少
                StatService.onEvent(getActivity(), "feedback_02", getActivity().getString(R.string.feedback0), 1);
                break;
            case 1:             //操作繁琐
                StatService.onEvent(getActivity(), "feedback_05", getActivity().getString(R.string.feedback1), 1);
                break;
            case 2:             //有闪退现象
                StatService.onEvent(getActivity(), "feedback_04", getActivity().getString(R.string.feedback2), 1);
                break;
            case 3:             //界面不美观
                StatService.onEvent(getActivity(), "feedback_03", getActivity().getString(R.string.feedback3), 1);
                break;
            case 4:            //想要的功能没有
                StatService.onEvent(getActivity(), "feedback_01", getActivity().getString(R.string.feedback4), 1);
                break;
        }
    }
}