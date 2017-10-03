package cn.cncgroup.tv.cncplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.Timer;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.bean.DefitionBean;
import cn.cncgroup.tv.cncplayer.callback.DefinitonListener;
import cn.cncgroup.tv.cncplayer.controller.ControllerManager;
import cn.cncgroup.tv.cncplayer.data.DataManager;
import cn.cncgroup.tv.cncplayer.utils.CNCPlayerUtil;
import cn.cncgroup.tv.cncplayer.widget.selectview.Constant;
import cn.cncgroup.tv.modle.Video;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.modle.VideoSetDetailData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.player.data.Definition;
import cn.cncgroup.tv.player.data.VideoDefinition;
import cn.cncgroup.tv.utils.GlobalConstant;

public class PlayerFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "PlayerFragment";
    private static final String ARG_ID = "id";
    private static final String ARG_POS = "serialNumber";
    private static final String ARG_TIME = "playTime";
    private OnFragmentInteractionListener mListener;
    private int mPlayTime = 0;
    private Context mContext;
    private VideoSet mVideoSet;
    private DataManager mDataManager;
    private ControllerManager mControllerManager;
    private TextView tv_currenTime; //整点报时
    private Timer mTimer;
    private TimeSetBroadCastReceiver timeSetBroadCastReceiver = new TimeSetBroadCastReceiver();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlayerFragment.
     */
    public static PlayerFragment newInstance(Bundle bundle) {
        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public PlayerFragment() {
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case PlayerConstants.ACTIVITY_FULL:          //1 代表是整点、半点
                    tv_currenTime.setVisibility(View.VISIBLE);
                    tv_currenTime.setText((String) message.obj);
                    changeTime();
                    break;
            }
        }
    };

    public void initData(Bundle bundle) {
        String id = bundle.getString(ARG_ID);
        String serialNumber = bundle.getString(ARG_POS);
        Log.i(TAG, "id=" + id + "--serialNumber=" + serialNumber + "--mPlayTime=" + mPlayTime);
        Constant.CURRENT_INDEX = Integer.valueOf(serialNumber) - 1;
        mPlayTime = bundle.getInt(ARG_TIME);
        VideoSet videoSet = new VideoSet();
        videoSet.setId(id);
        Log.i(TAG, "id:" + id + "--serialNumber:" + serialNumber + "--playTime:" + mPlayTime);
        if (!TextUtils.isEmpty(serialNumber)) {
            Log.i(TAG, "videoSet.getVideoListUrl():" + videoSet.getVideoListUrl());
            NetworkManager.getInstance().getVideoSetDetail(videoSet.getSourceUrl(), new VideoSetDataListener(), TAG);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_player, container, false);
        mContext = getActivity().getApplicationContext();
        inflate.requestFocus();
        if (getArguments() != null) {
            initData(getArguments());
        }
        mControllerManager = (ControllerManager) inflate.findViewById(R.id.baseController);
        tv_currenTime = (TextView) inflate.findViewById(R.id.tv_currentTime);
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), GlobalConstant.DS_DIGI);
        tv_currenTime.setTypeface(typeface);
        return inflate;
    }

    public ControllerManager getControllerManager() {
        return mControllerManager;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private DefinitonListener mDefinitonListener = new DefinitonListener() {
        @Override
        public ArrayList<Definition> getDefitionList(Context context, SparseArray<String> result) {
            return CNCPlayerUtil.getDefitionList(mContext, result);
        }

        @Override
        public ArrayList<DefitionBean> getDefitionListBean(Context context, SparseArray<String> result) {
            return CNCPlayerUtil.getDefitionListBean(mContext, result);
        }

        @Override
        public Definition getMostCloseDefinition(ArrayList<Definition> mDefinitonList) {
            return VideoDefinition.getMostCloseDefinition(mDefinitonList, Definition.DEFINITON_720P);
        }
    };

    class VideoSetDataListener implements BaseRequest.Listener<VideoSetDetailData> {

        @Override
        public void onResponse(VideoSetDetailData result, boolean isFromCache) {
            mVideoSet = result.result;
            Log.i(TAG, "result:" + result.success + "--result=" + result + "--mVideoSet=" + mVideoSet);
            if (mVideoSet.seriesType == 0)//电影//
            {
                ArrayList<Video> videoList = new ArrayList<Video>();
                Video vide = new Video();
                vide.channelId = mVideoSet.channelId;
                vide.epvid = mVideoSet.eqVid;
                vide.id = mVideoSet.id;
                vide.name = mVideoSet.name;
                vide.focus = mVideoSet.focus;
                vide.length = mVideoSet.getLength();
                vide.image = mVideoSet.getImage();
                vide.startTime = mPlayTime;
                Log.i(TAG, "Video:" + vide);
                videoList.add(vide);
                mDataManager = new DataManager(mContext, videoList, mControllerManager, mDefinitonListener);
                mDataManager.fetchUrl(videoList.get(Constant.CURRENT_INDEX), mVideoSet);
            } else {
                Log.i(TAG, "videoSet.getVideoListUrl():" + mVideoSet.getVideoListUrl());
                NetworkManager.getInstance().getVideoList(mVideoSet.getVideoListUrl(), new VideoListDataListener(), TAG);
            }
        }

        @Override
        public void onFailure(int errorCode, Request request) {
            Log.e(TAG, "onFailure:BaseRequest.Listener<VideoSetDetailData>-errorCode" + errorCode);
        }
    }

    class VideoListDataListener implements BaseRequest.Listener<VideoListData> {

        @Override
        public void onResponse(VideoListData result, boolean isFromCache) {
            ArrayList<Video> videoList = result.result;
            Video video = videoList.get(Constant.CURRENT_INDEX);
            video.startTime = mPlayTime;
            mDataManager = new DataManager(mContext, videoList, mControllerManager, mDefinitonListener);
            mDataManager.fetchUrl(video, mVideoSet);
        }

        @Override
        public void onFailure(int errorCode, Request request) {
            Log.e(TAG, "onFailure:BaseRequest.Listener<VideoListData>-errorCode" + errorCode);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        //播放 开始统计
        StatService.onEventStart(mContext, "PlayTime", mContext.getString(R.string.playtime));
        //注册时间改变的广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        mContext.registerReceiver(timeSetBroadCastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mControllerManager == null) {
            return;
        }
        mControllerManager.updateHistory();
        mControllerManager.destroyPlay();
        StatService.onEventEnd(mContext, "PlayTime", mContext.getString(R.string.playtime));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDataManager != null) {
            mDataManager.destoryManager();
        }
        NetworkManager.cancelRequest(TAG);
        mContext.unregisterReceiver(timeSetBroadCastReceiver);
        mHandler.removeCallbacksAndMessages(null);
    }


    /**
     * 时间改变广播，每一分钟接收一次
     */
    private class TimeSetBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                CharSequence currentMin = DateFormat.format("mm", System.currentTimeMillis()).toString();
                Log.i(TAG, "onReceive() currentMin:" + currentMin);
                if (currentMin.equals(PlayerConstants.TIME29) || currentMin.equals(PlayerConstants.TIME59)) {
//                        if(currentMin.equals("59") || currentMin.equals("")){
                    final CharSequence currenTime = DateFormat.format("HH:mm", System.currentTimeMillis()).toString();
                    final Message message = new Message();
                    message.what = PlayerConstants.ACTIVITY_FULL;
                    message.obj = currenTime;
                    mHandler.postDelayed(new Runnable() {  //获取整点时间后延迟30秒发送Handler
                        @Override
                        public void run() {
                            mHandler.sendMessage(message);
                        }
                    }, 1000 * 30);
                }
            }
        }
    }

    /**
     * 整、半点时间的改变
     */
    private void changeTime() {
        mHandler.postDelayed(new Runnable() {   //30秒后更新获取系统时间
            @Override
            public void run() {
                long sysTime = System.currentTimeMillis();
                CharSequence currentTime = DateFormat.format("HH:mm", sysTime).toString();
                tv_currenTime.setText(currentTime);
            }
        }, 1000 * 30);
        mHandler.postDelayed(new Runnable() {  //60后隐藏
            @Override
            public void run() {
                tv_currenTime.setVisibility(View.INVISIBLE);
            }
        }, 1000 * 60);
    }
}
