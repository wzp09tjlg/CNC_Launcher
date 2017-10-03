package cn.cncgroup.tv.conf.model.cibn;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.okhttp.Request;

import java.util.ArrayList;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.R;
import cn.cncgroup.tv.conf.AbsAppConfig;
import cn.cncgroup.tv.conf.model.cibn.modle.CIBNXMLActiveData;
import cn.cncgroup.tv.conf.model.cibn.modle.CIBNXMLLoginData;
import cn.cncgroup.tv.conf.model.cibn.utils.CIBNUtils;
import cn.cncgroup.tv.modle.Category;
import cn.cncgroup.tv.modle.CategoryData;
import cn.cncgroup.tv.modle.FilterList;
import cn.cncgroup.tv.modle.FilterListData;
import cn.cncgroup.tv.modle.RecommendData;
import cn.cncgroup.tv.modle.RecommendVideoSetListData;
import cn.cncgroup.tv.modle.SearchData;
import cn.cncgroup.tv.modle.SubjectData;
import cn.cncgroup.tv.modle.SubjectSetData;
import cn.cncgroup.tv.modle.Video;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.modle.VideoSetDetailData;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.network.request.JsonRequest;
import cn.cncgroup.tv.network.utils.LocalURLUtil;
import cn.cncgroup.tv.ui.listener.ILoginCallback;
import cn.cncgroup.tv.ui.widget.CustomDialog;
import cn.cncgroup.tv.utils.Preferences;

/**
 * Created by JIF on 2015/6/1.
 */
public class AppConfig extends AbsAppConfig {
    public static final String CIBN_DEVICE_ID = "cibn_device_id";

    @Override
    public void openCategory(Context context, Object object) {
        CIBNUtils.openCIBNEPGPage(context, CIBNUtils.ACTION_PROGRAM_LIST,
                CIBNUtils.PARAM_PROGRAM_LIST);
    }

    @Override
    public void openSubject(Context context) {

    }

    @Override
    public void openSubjectDetail(Context context, Object object) {

    }

    @Override
    public void openVideoDetail(Context context, VideoSet set) {
        CIBNUtils.openCIBNEPGPage(context, CIBNUtils.ACTION_DETAIL,
                CIBNUtils.PARAM_DETAIL);
    }

    @Override
    public void playVideo(Context context, String videoSetId, String videoId,String position,
                          int startTime, String name, String image,String channelId) {

    }

    @Override
    public void openHistory(Context context) {
        CIBNUtils.openCIBNEPGPage(context, CIBNUtils.ACTION_HISTORY);
    }

    @Override
    public void openCollect(Context context) {
        CIBNUtils.openCIBNEPGPage(context, CIBNUtils.ACTION_FAVOR);
    }

    @Override
    public void openSearch(Context context) {
        CIBNUtils.openCIBNEPGPage(context, CIBNUtils.ACTION_SEARCH);
    }

    @Override
    public void getRecommendData(BaseRequest.Listener<RecommendData> listener,
                                 Object tag, boolean isUseCache, boolean isOnlyUseCache) {
        new JsonRequest(LocalURLUtil.getLocalCIBNRecommendUrl(), tag, listener,
                true, RecommendData.class).performNetwork(
                BaseRequest.Method.GET, isUseCache, isOnlyUseCache);
    }

    @Override
    public void getCategoryData(BaseRequest.Listener<CategoryData> listener,
                                Object tag, boolean isUseCache, boolean isOnlyUseCache) {
        new JsonRequest(LocalURLUtil.getLocalCIBNCategoryUrl(), tag, listener,
                true, CategoryData.class).performNetwork(BaseRequest.Method.GET,
                isUseCache, isOnlyUseCache);
    }

    @Override
    public void getVideoSetList(String url, BaseRequest.Listener<VideoSetListData> listener, Object tag, int pageSize) {

    }

    @Override
    public void getHotVideoSetList(String type, int pageSize, int pageNo, BaseRequest.Listener<VideoSetListData> listener, Object tag) {

    }

    @Override
    public void getNewVideoSetList(String type, int pageSize, int pageNo, BaseRequest.Listener<VideoSetListData> listener, Object tag) {

    }

    @Override
    public void getTagFilter(String type, BaseRequest.Listener<FilterListData> listener, Object tag) {

    }

    @Override
    public void getVideoSetDetail(String url, BaseRequest.Listener<VideoSetDetailData> listener, Object tag) {

    }

    @Override
    public void getRecommendVideoSetList(String url, BaseRequest.Listener<RecommendVideoSetListData> listener, Object tag) {

    }

    @Override
    public void getVideoList(String url, BaseRequest.Listener<VideoListData> listener, Object tag) {

    }

    @Override
    public void getSearchResult(String keyword, int pageNo, int pageSize, BaseRequest.Listener<SearchData> listener, Object tag) {

    }

    @Override
    public void getSubjectSetList(String url, BaseRequest.Listener<SubjectSetData> listener, Object tag) {

    }

    @Override
    public void getSubjectList(String url, BaseRequest.Listener<SubjectData> listener, Object tag) {

    }

    @Override
    public String getNextPageUrl(VideoSetListData data) {
        return null;
    }

    @Override
    public String getHorizentalPosterUrl(VideoSet set) {
        return null;
    }

    @Override
    public String getVerticalPosterUrl(VideoSet set) {
        return null;
    }

    @Override
    public String getFilterResultUrl(String type, ArrayList<FilterList> filters) {
        return null;
    }

    @Override
    public String getContentUrl(Category category) {
        return null;
    }

    @Override
    public String getVideoSetSourceUrl(VideoSet set) {
        return null;
    }

    @Override
    public String getVideoSetRecommendUrl(VideoSet set) {
        return null;
    }

    @Override
    public String getVideoListUrl(VideoSet set) {
        return null;
    }

    @Override
    public String getVarietyYear(Video video) {
        return null;
    }

    @Override
    public String getPlayId(VideoSet set, Video video) {
        return null;
    }

    @Override
    public String getPlayLength(VideoSet set) {
        return null;
    }

    @Override
    public String getIssueTime(VideoSet set) {
        return null;
    }

    @Override
    public Fragment getSearchRecommendFragment() {
        return null;
    }

    @Override
    public void getSearchResult(String keyword, String mtype, int pageNo, int pageSize, BaseRequest.Listener<SearchData> listener, Object tag) {
    }

    @Override
    public String getVideoPos(Video video,String pos) {
        return pos;
    }

    @Override
    public boolean isShouldPlay() {
        return false;
    }

    public void showCustomDialog(Context context, int res) {
        final CustomDialog dialog = new CustomDialog(context);
        dialog.setMessage(context.getText(res));
        dialog.setPositiveButton(context.getText(R.string.confirm),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private class CIBNActiveListener
            implements BaseRequest.Listener<CIBNXMLActiveData> {
        Context mContext;
        String TAG;
        String MAC;
        CIBNLoginListener mListener;
        ILoginCallback mCallback;

        CIBNActiveListener(Context context, String TAG, String MAC,
                           CIBNLoginListener listener, ILoginCallback callback) {
            mContext = context;
            this.TAG = TAG;
            this.MAC = MAC;
            mListener = listener;
            mCallback = callback;
        }

        @Override
        public void onResponse(CIBNXMLActiveData result,boolean isFromCache) {
            // 这个接口resultCode==1时通过
            if (result.resultCode == 1) {
                Preferences.build(mContext).putString(CIBN_DEVICE_ID,
                        result.deviceId);
                NetworkManager.getInstance().loginCIBNTerminal(mListener, TAG,
                        MAC, result.deviceId, false, false);
            } else {
                NetworkManager.cancelRequest(TAG);
                showCustomDialog(mContext,
                        R.string.alert_dialog_message_active_error);
                mCallback.onLoginFailure();
            }
        }

        @Override
        public void onFailure(int errorCode,Request request) {
            mCallback.onLoginFailure();
            if (errorCode == BaseRequest.ERROR_ACTIVE_NETWORK) {
                showCustomDialog(mContext,
                        R.string.alert_dialog_message_network_error);
            } else {
                NetworkManager.cancelRequest(TAG);
                showCustomDialog(mContext,
                        R.string.alert_dialog_message_active_error);
            }
        }
    }

    private class CIBNLoginListener
            implements BaseRequest.Listener<CIBNXMLLoginData> {
        boolean isTry;
        Context mContext;
        String TAG;
        String MAC;
        ILoginCallback mCallback;

        CIBNLoginListener(Context context, String TAG, String MAC,
                          ILoginCallback callback) {
            mContext = context;
            this.TAG = TAG;
            this.MAC = MAC;
            mCallback = callback;
        }

        @Override
        public void onResponse(CIBNXMLLoginData result,boolean isFromCache) {
            // 这个接口resultCode==0时通过
            if (result.resultCode == 0 && result.state == 111) {
                CApplication.sCIBNXMLLoginData = result;
                mCallback.onLoginResponse();
            } else {
                NetworkManager.cancelRequest(TAG);
                showCustomDialog(mContext,
                        R.string.alert_dialog_message_login_error);
                mCallback.onLoginFailure();
            }
        }

        @Override
        public void onFailure(int errorCode,Request request) {
            if (errorCode == BaseRequest.ERROR_ACTIVE_NETWORK) {
                showCustomDialog(mContext,
                        R.string.alert_dialog_message_network_error);
                mCallback.onLoginFailure();
            } else {
                if (!isTry) {
                    String id = Preferences.build(context)
                            .getString(CIBN_DEVICE_ID, null);
                    NetworkManager.getInstance().loginCIBNTerminal(this, TAG,
                            MAC, id, true, true);
                    isTry = true;
                } else {
                    NetworkManager.cancelRequest(TAG);
                    showCustomDialog(mContext,
                            R.string.alert_dialog_message_login_error);
                    mCallback.onLoginFailure();
                }
            }
        }
    }
}
