package cn.cncgroup.tv.view.detail;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.okhttp.Request;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.VarietyDetailAdapter;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.cncplayer.PlayerConstants;
import cn.cncgroup.tv.cncplayer.PlayerFragment;
import cn.cncgroup.tv.cncplayer.eventbus.PlayOverEvent;
import cn.cncgroup.tv.cncplayer.eventbus.PlayerEventBus;
import cn.cncgroup.tv.cncplayer.eventbus.VideoClickEvent;
import cn.cncgroup.tv.cncplayer.eventbus.VideoSizeEvent;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.db.DbUtil;
import cn.cncgroup.tv.db.VideoSetDao;
import cn.cncgroup.tv.modle.Video;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.LogUtil;
import de.greenrobot.event.EventBus;

/**
 * 综艺详情Fragment Created by zhangguang on 2015/6/24.
 */
public class VarietyDetailFragment extends BaseFragment implements OnItemClickListener<Video>, OnItemFocusListener<Video>, BaseRequest.Listener<VideoListData>, View.OnClickListener {
    private final String TAG = "VarietyDetailActivity";
    // widget
    private View mPlayerContener; // 名字图片
    private TextView mName, mMaster, mDesc; // 节目名称，主持人，简介
    private TextView gather_textView;
    private ImageView gather_imageView;
    private VerticalGridView mGridView; // 选集
    private ImageView mCollect; // 收藏
    // data
    private VarietyDetailAdapter mAdapter;
    private VideoSetDao mVideoSetDao;// 是否收藏
    private VideoSet mVideoSet;
    private Video mVideo;
    private ImageView imageHint;
    private RelativeLayout rl_gather;

    private ShadowView mShadowView;
    private View mFocusedView;
    private boolean gatherIsChek = true; // 选集是否显示,是否收藏（数据暂时先这样设置）
    private Handler mHandler = new Handler();
    private ArrayList<Video> videoList;
    private Runnable mHideRunable = new Runnable() {
        @Override
        public void run() {
            mShadowView.setVisibility(View.INVISIBLE);
        }
    };
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mFocusedView != null) {
                mShadowView.moveTo(mFocusedView);
            }
        }
    };
    private ViewGroup mOther;
    /**
     * PlayerEventBus的回调，用于小窗口播放
     */
    private PlayerEventBus.FragmentVideoListener mVideoListener = new PlayerEventBus.FragmentVideoListener() {
        @Override
        public void onVideoPlayeOver(PlayOverEvent event) {
            if (event.pos == mGridView.getChildCount()) {
                changeToSmallScreen();
                mPlayerContener.setVisibility(View.GONE);
                return;
            }
            resetSelectedState(event.pos + 1);
        }

        @Override
        public void onVideoItemClick(VideoClickEvent event) {
            resetSelectedState(event.num - 1);
        }
    };
//    private SmallPlayerFragment mSmallPlayerFragment;

    /**
     * 刷新右侧状态
     *
     * @param index 下标
     */
    private void resetSelectedState(int index) {
        mCurrentPos = index;
        Log.d(TAG, "getLastVisibleIndex=" + mGridView.getLastVisibleIndex() + "---index=" + index);
        View current = mGridView.getChildAt(index);
        if (PlayerConstants.SCREEN_TYPE == PlayerConstants.FRAGMENT_SMALL) {
            current.requestFocus();
        }
        mAdapter.setSelectedPos(current, index);
    }

    private PlayerFragment mPlayerFragment;
    private SimpleDraweeView mPoster;
    private boolean isShouldPlay; //是否应该播放,Config中获取，
    private int mCurrentPos = 0; //当前播放的位置

    /**
     * 综艺剧集Item回调
     */
    @Override
    public void onResponse(final VideoListData result, boolean isFromCache) {
        if (getActivity() == null) {
            return;
        }
        if (result.result == null) {
            Project.get().getConfig().playVideo(getActivity(), mVideoSet.id, mVideoSet.id, "" + 1, 0, mVideoSet.getName(), mVideoSet.getImage(), mVideoSet.getChannelId());
            getActivity().finish();
            return;
        }
        if (result.result.size() > GlobalConstant.VARIETYIMAGEHINT) {
            imageHint.setVisibility(View.VISIBLE);
        }
        videoList = result.result;
        Log.i(TAG, "ListSize:" + result.result.size());
        mAdapter.addData(result);
        mGridView.post(new Runnable() {
            @Override
            public void run() {
                mGridView.requestFocus();
            }
        });
        showContent();
        hideLoading();
        if (isShouldPlay) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    initPlayer(result.result.get(0), 1);
                }
            });
        }
    }

    //初始化播放器
    private void initPlayer(Video video, int pos) {
        mCurrentPos = pos - 1;
        PlayerConstants.SCREEN_TYPE = PlayerConstants.FRAGMENT_SMALL;
        Bundle bundle = new Bundle();
        bundle.putString("id", mVideoSet.id);
        bundle.putString("serialNumber", "" + pos);
        bundle.putInt("playTime", 0);
        if (mPlayerFragment == null) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            mPlayerFragment = PlayerFragment.newInstance(bundle);
            transaction.replace(R.id.player_contener, mPlayerFragment, "PlayerFragment");
//            mSmallPlayerFragment = SmallPlayerFragment.newInstance(bundle);
//            transaction.replace(R.id.player_contener, mSmallPlayerFragment, "PlayerFragment");
            transaction.commit();
        } else {
            mPlayerFragment.initData(bundle);
        }
    }

    @Override
    public void onFailure(int errorCode, Request request) {
        hideContent();
        hideLoading();
        showError();
    }

    @Override
    public void onItemFocusLister(View view, int position, Video video, boolean hasFocus) {
        if (position == 0) {
            mVideo = video;
            if (TextUtils.isEmpty(mVideoSet.getImage())) {
                mPoster.setImageURI(Uri.parse(mVideo.image));
            }
        }
        if (hasFocus) {
            mHandler.removeCallbacks(mHideRunable);
            mFocusedView = view;
            mShadowView.moveTo(mFocusedView);
            if (position >= videoList.size() - 4)
                imageHint.setVisibility(View.INVISIBLE);
            else if (position <= videoList.size() - 4 && videoList.size() > GlobalConstant.VARIETYIMAGEHINT)
                imageHint.setVisibility(View.VISIBLE);
        } else {
            mHandler.postDelayed(mHideRunable, 16);
        }
    }

    @Override
    public void onItemClickLister(View view, int position, Video video) {
        Log.d(TAG, "postion = " + position + "---mCurrentPos=" + mCurrentPos);
        if (mPlayerContener.getVisibility() != View.VISIBLE)
            mPlayerContener.setVisibility(View.VISIBLE);
        if (isShouldPlay) {
            if (position == mCurrentPos)
                changeToFullScreen();
            else
                initPlayer(video, position + 1);
        } else
            Project.get().getConfig().playVideo(getActivity().getApplicationContext(), mVideoSet.id, video.id, Project.get().getConfig().getVideoPos(video, String.valueOf(position + 1)), 0, video.getName(), video.getImage(), video.getChannelId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_gather:
                if (!gatherIsChek) { // 已收藏
                    notTocollect();
                } else {
                    collect();
                }
                if (!mCollect.isSelected()) {
                    //收藏成功 添加统计
                    StatService.onEvent(getActivity().getApplicationContext(), "Collection_button", getString(R.string.collection_button), 1);
                    mCollect.setSelected(true);
                    doCollect();
                } else {
                    mCollect.setSelected(false);
                    unDoCollect();
                }
                break;
            case R.id.player_contener:
                changeToFullScreen();
                break;
        }
    }

    /**
     * 切换为全屏播放
     */
    private void changeToFullScreen() {
        videoViewFocuse(false);
        mPlayerFragment.getControllerManager().enableEvent(true);
        PlayerConstants.SCREEN_TYPE = PlayerConstants.FRAGMENT_FULL;
        getActivity().getWindow().getDecorView().setBackgroundResource(0);
        mOther.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerContener.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.setMargins(0, 0, 0, 0);
        mPlayerContener.setLayoutParams(params);
    }

    /**
     * 切换为小屏播放
     */
    private void changeToSmallScreen() {
        mPlayerFragment.getControllerManager().enableEvent(false);
        PlayerConstants.SCREEN_TYPE = PlayerConstants.FRAGMENT_SMALL;
        FrameLayout.LayoutParams mDefaultParams;
        mDefaultParams = new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.dimen_784dp), getResources().getDimensionPixelSize(R.dimen.dimen_435dp));
        mDefaultParams.setMargins(getResources().getDimensionPixelSize(R.dimen.dimen_96dp), getResources().getDimensionPixelSize(R.dimen.dimen_96dp), 0, 0);
        Log.i(TAG, "mCurrentPos=" + mCurrentPos);
        getActivity().getWindow().getDecorView().setBackgroundResource(R.drawable.recommend_back);
        mPlayerContener.setLayoutParams(mDefaultParams);
        mOther.setVisibility(View.VISIBLE);
        mGridView.setSelectedPosition(mCurrentPos);
        videoViewFocuse(true);

    }

    @Override
    public void onResume() {
        //Todo  优朋播放时长统计待优化
        //优朋结束统计播放时长
        StatService.onEventEnd(getActivity().getApplicationContext(), "PlayTime", getActivity().getString(R.string.playtime));
        super.onResume();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_detail_variety;
    }

    @Override
    protected void findView(View view) {
        mPlayerContener = view.findViewById(R.id.player_contener);
        mPoster = (SimpleDraweeView) view.findViewById(R.id.poster);
        mOther = (ViewGroup) view.findViewById(R.id.other_contener);
        mName = (TextView) view.findViewById(R.id.variety_detail_tv_title);
        mMaster = (TextView) view.findViewById(R.id.variety_detail_tv_presenter);
        mDesc = (TextView) view.findViewById(R.id.variety_detail_desc);
        mCollect = (ImageView) view.findViewById(R.id.variety_detail_img_collect);
        mGridView = (VerticalGridView) view.findViewById(R.id.variety_detail_gridiew);
        gather_textView = (TextView) view.findViewById(R.id.tv_gather);
        gather_imageView = (ImageView) view.findViewById(R.id.iv_gather);
        mShadowView = (ShadowView) view.findViewById(R.id.shadow);
        rl_gather = (RelativeLayout) view.findViewById(R.id.rl_gather);
        imageHint = (ImageView) view.findViewById(R.id.variety_detail_image_hint);
        rl_gather.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        isShouldPlay = Project.get().getConfig().isShouldPlay();
        if (isShouldPlay) {
            mPlayerContener.setVisibility(View.VISIBLE);
        }
        EventBus.getDefault().register(this);
        PlayerEventBus.getDefault().setOnFragmentVideoListner(mVideoListener);
        mVideoSet = (VideoSet) getArguments().getSerializable(GlobalConstant.KEY_VIDEOSET);
        setDetailValue(mVideoSet);
        initAdapter();
        checkCollected();
        mCollect.setOnClickListener(this);
        rl_gather.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (!isShouldPlay) return false;
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    videoViewFocuse(true);
                }
                return false;
            }
        });
        mShadowView.init(25);
        mGridView.addOnScrollListener(mScrollListener);
        mPlayerContener.setOnClickListener(this);
        mPlayerContener.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mHandler.removeCallbacks(mHideRunable);
                    mShadowView.moveTo(v);
                } else {
                    mHandler.postDelayed(mHideRunable, 16);
                }
            }
        });
    }

    /**
     * 设置放置视频Fragment是否可获取焦点
     */
    private void videoViewFocuse(boolean able) {
        Log.i(TAG, "videoViewFocuse");
        if (!isShouldPlay) return;
        mPlayerContener.setFocusable(able);
        mPlayerContener.setClickable(able);
        if (able)
            mPlayerContener.requestFocus();
    }

    /**
     * 大小屏改变
     */
    public void onEventMainThread(VideoSizeEvent event) {
//        Log.i(TAG, "onEventMainThread=" + event);
        if (event.isFullScreen())
            changeToFullScreen();
        else
            changeToSmallScreen();
    }

    /**
     * 单集播放完
     */
    public void onEventMainThread(PlayOverEvent event) {
//        Log.i(TAG, "onEventMainThread pos=" + event.pos);
        mCurrentPos = event.pos;
        if (event.pos == mGridView.getChildCount() - 1) {
            changeToSmallScreen();
            mPlayerContener.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        PlayerEventBus.getDefault().setOnFragmentVideoListner(null);
        super.onPause();
        NetworkManager.cancelRequest(TAG);
    }

    private void checkCollected() {
        try {
            mVideoSetDao = DbUtil.queryById(mVideoSet.id); // 查看是否收藏 第一次是空值
            if (mVideoSetDao != null) {
                if (mVideoSetDao.collect == 1) {
                    collect();
                    mCollect.setSelected(true);
                } else {
                    mCollect.setSelected(false);
                    notTocollect();
                }
            } else {
                mCollect.setSelected(false);
                notTocollect();
            }
        } catch (Exception e) {
        }
    }

    private void initAdapter() {
        Log.i(TAG, " mVideoSet.getCount()=" + mVideoSet.getCount());
        mAdapter = new VarietyDetailAdapter(getActivity().getApplicationContext(), mVideoSet.getCount(), this, this);
        mGridView.setAdapter(mAdapter);
        mAdapter.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (!isShouldPlay) return false;
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    videoViewFocuse(true);
                }
                return false;
            }
        });
    }

    /**
     * 设置详情页 详情数据
     */
    public void setDetailValue(VideoSet videoSet) {
        if (!TextUtils.isEmpty(videoSet.getImage())) {
            mPoster.setImageURI(Uri.parse(videoSet.getVerticalPosterUrl()));
        }
        this.mVideoSet = videoSet;
        mName.setText(videoSet.name);
        mDesc.setText(videoSet.desc);
        mMaster.setText(videoSet.actors.replace("|", " / "));
        NetworkManager.getInstance().getVideoList(videoSet.getVideoListUrl(), this, TAG);
    }

    private void doCollect() {
        DbUtil.addCollect(mVideoSet.getId(), mVideo.image, mVideoSet.getName());
    }

    private void unDoCollect() {
        DbUtil.deleteCollect(mVideoSet.getId());
    }

    /**
     * 未收藏
     */
    private void notTocollect() {
        gather_textView.setText(getString(R.string.ungather));
        gather_imageView.setImageResource(R.drawable.ungather_icon);
        gatherIsChek = true;
    }

    /**
     * 已收藏
     */
    private void collect() {
        gather_textView.setText(getString(R.string.gather));
        gather_imageView.setImageResource(R.drawable.gather_icon);
        gatherIsChek = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        gather_imageView.setImageResource(0);
        mCollect.setOnClickListener(null);
    }
}
