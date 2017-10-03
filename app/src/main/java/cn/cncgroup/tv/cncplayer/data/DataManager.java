package cn.cncgroup.tv.cncplayer.data;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;

import cn.cncgroup.tv.cncplayer.PlayerConstants;
import cn.cncgroup.tv.cncplayer.bean.DefitionBean;
import cn.cncgroup.tv.cncplayer.bean.VideoInfo;
import cn.cncgroup.tv.cncplayer.callback.DefinitonListener;
import cn.cncgroup.tv.cncplayer.controller.IController;
import cn.cncgroup.tv.cncplayer.eventbus.DefinitionEvent;
import cn.cncgroup.tv.cncplayer.eventbus.FinishActivityEvent;
import cn.cncgroup.tv.cncplayer.eventbus.FocuseEvent;
import cn.cncgroup.tv.cncplayer.eventbus.PlayOverEvent;
import cn.cncgroup.tv.cncplayer.eventbus.PlayerEventBus;
import cn.cncgroup.tv.cncplayer.eventbus.VideoClickEvent;
import cn.cncgroup.tv.cncplayer.widget.selectview.Constant;
import cn.cncgroup.tv.modle.Video;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.player.controller.IVideoDefinitonCallback;
import cn.cncgroup.tv.player.controller.IVideoPlayInfoCallback;
import cn.cncgroup.tv.player.controller.VideoPlayInfoController;
import cn.cncgroup.tv.player.data.Definition;
import cn.cncgroup.tv.player.data.VideoPlayInfo;
import cn.cncgroup.tv.ui.widget.CustomToast;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.ListUtils;
import de.greenrobot.event.EventBus;

/**
 * Created by yqh on 2015/9/18.
 * 用于获取网络数据
 */
public class DataManager implements IVideoDefinitonCallback, PlayerEventBus.IPlayerEvent {
    private static final String TAG = "DataManager";
    private final DefinitonListener mDefinitonListener;
    private IVideoDefinitonCallback mDefinitonCallback;
    private Context mContext;
    private ArrayList<Definition> mDefinitonList;
    private ArrayList<DefitionBean> mDefitionBeanList;
    private IController mControllerManager;
    private String mCurrentDefiniID;
    private Video mVideo;
    private ArrayList<Video> mVideoList;
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private VideoSet mVideoSet;

    public DataManager(Context context, ArrayList<Video> videoList, IController manager, @NonNull DefinitonListener definitonListener) {
        mContext = context.getApplicationContext();
        mVideoList = videoList;
        mControllerManager = manager;
        mDefinitonListener = definitonListener;
        init();
    }

    private void init() {
        PlayerEventBus.getDefault().register(this);
        if (mVideoList != null && mControllerManager != null)
            mControllerManager.setVedioCount(mVideoList.size());
    }


    public void fetchUrl(Video video, VideoSet videoSet) {
        mVideo = video;
        mVideoSet = videoSet;
        mDefinitonCallback = this;
        VideoPlayInfoController.getVideoDefinition(mDefinitonCallback,
                String.valueOf(mVideo.getID()), mVideo.getEpvid());
       /* if (mVideoList != null && mVideo != null) {
            Log.i(TAG, "init pos:mCurrentVideoPos=" + Constant.CURRENT_INDEX);
        }*/
    }

    public void destoryManager() {
        PlayerEventBus.getDefault().unRegister();
    }

    @Override
    public void onDefinitionClick(DefinitionEvent event) {
        Log.d(TAG, "event=" + event.num + "--- Constant.CURRENT_INDEX:" + Constant.CURRENT_INDEX);
        if (mVideo == null) {
            GlobalToast.makeText(mContext, "can not play this video", GlobalConstant.ToastShowTime).show();
            return;
        }
        if (mControllerManager != null)
            mControllerManager.showDefitionTip();
        String videoId = String.valueOf(mVideo.getID());
        mCurrentDefiniID = mDefitionBeanList.get(event.num).defition;
        Log.d(TAG, "eqVid:" + mCurrentDefiniID);
        PlayerConstants.CURRENT_DEFINATION_INDEX = event.num;
        if (mControllerManager != null)
            mControllerManager.setCurrentDefinition(mDefitionBeanList.get(event.num).defitionName);
        PlayerConstants.CURRENT_DEFINATION_INDEX = event.num;
        VideoPlayInfoController.getVideoPlayInfo(mPlayerInfoCallBackListener, videoId, mVideo.getEpvid());
    }

    /**
     */
    @Override
    public void onVideoItemClick(VideoClickEvent event) {
        if (mControllerManager != null)
            mControllerManager.updateHistory();
        Log.d(TAG, "event=" + event.num);
        int index = event.num - 1;
        if (mVideoList == null) {
            Log.e(TAG, "mVideoList is NULL");
            return;
        }
        mVideo = mVideoList.get(index);
        if (mVideo == null) {
            GlobalToast.makeText(mContext, "cant play this video", GlobalConstant.ToastShowTime).show();
            return;
        }
        Constant.CURRENT_INDEX = index;
        String videoId = String.valueOf(mVideo.getID());
        Log.d(TAG, "mCurrentDefiniID:" + mCurrentDefiniID);
        VideoPlayInfoController.getVideoDefinition(mDefinitonCallback,
                videoId, mVideo.getEpvid());
    }

    /**
     */
    @Override
    public void onVideoItemFocuseChange(FocuseEvent event) {
        showFocuseTips(event);
    }

    /**
     */
    @Override
    public void onVideoItemPlayOver(PlayOverEvent event) {
        Log.d(TAG, "onVideoItemPlayOver");
        int size = mVideoList.size();
        int startPos = Constant.CURRENT_INDEX + 1;
        boolean isHasNext = false;
        for (int i = startPos; i < size; i++) {
            Constant.CURRENT_INDEX = i;
            if (mVideoList.get(Constant.CURRENT_INDEX) != null) {
                mVideo = mVideoList.get(Constant.CURRENT_INDEX);
                String videoId = String.valueOf(mVideoList.get(Constant.CURRENT_INDEX).getID());
                VideoPlayInfoController.getVideoDefinition(mDefinitonCallback,
                        videoId, mVideo.getEpvid());
                isHasNext = true;
                break;
            }
        }
        if (isHasNext) {
            Log.d(TAG, "have next video");
        } else {
            Log.d(TAG, "have nomore video");
            GlobalToast.makeText(mContext, "视频播放完毕", GlobalConstant.ToastShowTime).show();
            EventBus.getDefault().post(new FinishActivityEvent());
        }
    }

    private void showFocuseTips(final FocuseEvent event) {
        final boolean hasFocus = event.isShow;
        int pos = event.num - 1;
        mHander.removeCallbacksAndMessages(null);
        if (hasFocus) {
            final Video video = mVideoList.get(pos);
            final String focus = video == null ? null : video.getFocus();
            if (focus != null) {
                mHander.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CustomToast.showCustomToast(mContext, event.view, focus);
                    }
                }, 300);
            }
        } else {
            CustomToast.removeCustomToast();
        }
    }

    /**
     * 获取清晰度
     */
    @Override
    public void onSuccess(SparseArray<String> result) {
        Log.d(TAG, "onSuccess:" + result);
        mDefinitonList = mDefinitonListener.getDefitionList(mContext, result);
        mDefitionBeanList = mDefinitonListener.getDefitionListBean(mContext, result);
        if (!ListUtils.isEmpty(mDefinitonList)) {
            Definition defaultDefition = mDefinitonListener.getMostCloseDefinition(mDefinitonList);
            ArrayList<String> mDefitionNameList = getDefitionNameList(mDefitionBeanList);
            if (mControllerManager != null)
                mControllerManager.setBottomPannelViewDefitionResult(mDefitionNameList);
            for (int i = 0; i < mDefitionBeanList.size(); i++) {
                DefitionBean defitionBean1 = mDefitionBeanList.get(i);
                Log.d(TAG, "mDefitionBeanList:" + i + ":--" + defitionBean1.defition + "--defitionname:" + defitionBean1.defitionName);
                if (defitionBean1.resourceId == GlobalConstant.DEFAULT_DEFITION) {
                    mCurrentDefiniID = defitionBean1.defition;
                    VideoPlayInfoController.getVideoPlayInfo(mPlayerInfoCallBackListener,
                            String.valueOf(mVideo.getID()), defitionBean1.defition);
                    if (mControllerManager != null)
                        mControllerManager.setCurrentDefinition(defitionBean1.defitionName);
                    PlayerConstants.CURRENT_DEFINATION_INDEX = i;
                    Log.d(TAG, "DEFAULT_DEFITION listSize:" + mDefinitonList.size());
                } else {
                    if (Integer.parseInt(defaultDefition.getDefinitionString())
                            == defitionBean1.resourceId) {
                        PlayerConstants.CURRENT_DEFINATION_INDEX = i;
                        VideoPlayInfoController.getVideoPlayInfo(mPlayerInfoCallBackListener,
                                String.valueOf(mVideo.getID()), defitionBean1.defition);
                        if (mControllerManager != null)
                            mControllerManager.setCurrentDefinition(defitionBean1.defitionName);
                        Log.d(TAG, "NO DEFAULT_DEFITION listSize:" + mDefinitonList.size());
                    }
                }
            }
        }
    }

    private ArrayList<String> getDefitionNameList(ArrayList<DefitionBean> beanList) {
        ArrayList<String> names = new ArrayList<String>();
        for (DefitionBean bean : beanList) {
            names.add(bean.defitionName);
        }
        return names;
    }

    @Override
    public void onFailed(Exception e) {
    }

    private IVideoPlayInfoCallback mPlayerInfoCallBackListener = new IVideoPlayInfoCallback() {
        @Override
        public void onSuccess(VideoPlayInfo info) {
            final VideoInfo videoInfo = new VideoInfo();
            videoInfo.playInfo = info;
            videoInfo.name = mVideo.getName();
            videoInfo.focuse = mVideo.getFocus();
            videoInfo.id = mVideo.getID();
            videoInfo.url = info.getUrl();
            videoInfo.videoIndex = Constant.CURRENT_INDEX;
            videoInfo.videoSet = mVideoSet;
            Log.i(TAG, "mCurrentVideoPos:" + Constant.CURRENT_INDEX);
            Log.i(TAG, "mVideo.epvid:" + mVideo.getEpvid());
            // run in UI thread
            if (mControllerManager != null)
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        mControllerManager.startPlay(videoInfo);
                    }
                });
        }

        @Override
        public void onFailed(Exception e) {
            Log.e(TAG, "IVideoPlayInfoCallback onFailed:" + e.getMessage());
        }
    };
}
