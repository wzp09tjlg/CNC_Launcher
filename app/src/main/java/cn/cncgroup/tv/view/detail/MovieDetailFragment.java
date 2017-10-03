package cn.cncgroup.tv.view.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.MovieDetailRecomAdapter;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.db.DbUtil;
import cn.cncgroup.tv.db.VideoSetDao;
import cn.cncgroup.tv.modle.RecommendVideoSetListData;
import cn.cncgroup.tv.modle.Video;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.modle.VideoSetDetailData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.CustomToast;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.HorizontalGridView;
import cn.cncgroup.tv.ui.widget.selectview.ItemClickListener;
import cn.cncgroup.tv.ui.widget.selectview.ItemFocusListener;
import cn.cncgroup.tv.ui.widget.selectview.SelectView;
import cn.cncgroup.tv.utils.DetailStringUtil;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.ImageReflectedUtil;
import cn.cncgroup.tv.utils.UIUtils;
import cn.cncgroup.tv.view.homesecond.utils.CategoryUtils;

/**
 * 影视详情Fragment Created by ZG 2015/6/24.
 */
public class MovieDetailFragment extends BaseFragment implements
        BaseRequest.Listener<VideoSetDetailData>, View.OnClickListener,
        ItemClickListener, ItemFocusListener, View.OnFocusChangeListener {
    public static final String TAG = "DetialsActivity";
    private String emptyString = null; // 字符串“无”
    private TextView tv_details_name, tv_details_collect, tv_details_issuetime, tv_details_director, tv_details_actress, mDesc;

    private SimpleDraweeView iv_photo;
    private RelativeLayout rl_feedback, rl_play, rl_collect, rl_gather; // 选集的布局(电影详情页不需要显示),未收藏layout
    private LinearLayout rl_err, collect_widget; // 相关推荐异常，mHorizontalPageView父布局,选集控件
    private TextView gather_textView, tv_play_text; // 是否收藏文字,显示播放（电影）还是显示播放至多少集（电视剧）
    private ImageView gather_imageView; // 是否收藏图片
    private ImageView iv_feedback;
    private LinearLayout hint, hint_feedback;
    private List<RecommendVideoSetListData> itemDataScolls;
    private HorizontalGridView horgridview_detail;
    private SelectView mSelectView;
    private Context mContext;
    private Handler handler = new Handler(Looper.getMainLooper());
    private VideoSet videoSet, playVideoSet; // 详情页Bean
    private VideoSetDao videoSetDao;// 是否收藏和播放至多少集
    private Video[] mVideoArray;
    /**
     * 选集回调
     */
    private BaseRequest.Listener<VideoListData> videoListListener = new BaseRequest.Listener<VideoListData>() {
        @Override
        public void onResponse(VideoListData result, boolean isFromCache) {
            if (getActivity() == null) {
                return;
            }
            if (result.result == null) {
                return;
            }
            int videoListLength = mVideoArray.length;
            for (Video video : result.result) {
                if (result.result.size() == 1) {
                    mVideoArray[0] = video;
                } else {
                    if (video.index - 1 < videoListLength) {
                        mVideoArray[video.index - 1] = video;
                    }
                }
            }
        }

        @Override
        public void onFailure(int errorCode, Request request) {

        }
    };
    private BaseActivity mActivity;
    /**
     * 影视相关推荐回调
     */
    private BaseRequest.Listener<RecommendVideoSetListData> favorListContentListener = new BaseRequest.Listener<RecommendVideoSetListData>() {
        @Override
        public void onResponse(RecommendVideoSetListData result, boolean isFromCache) {
            if (getActivity() == null) {
                return;
            }
            if (result.result != null && result.result.size() != 0) {
                mActivity.hideLoading();
                mActivity.showContent();
                itemDataScolls = new ArrayList<RecommendVideoSetListData>();
                itemDataScolls.add(result);
                horgridview_detail.setAdapter(new MovieDetailRecomAdapter(result, new RecommendOnItemClickListener(), new RecommendOnItemFocuseListener()));
            } else {
                mActivity.hideLoading();
                mActivity.showContent();
                horgridview_detail.setVisibility(View.GONE);
                rl_err.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int errorCode, Request request) {
            if (getActivity() == null) {
                return;
            }
            mActivity.showContent();
            horgridview_detail.setVisibility(View.GONE);
            rl_err.setVisibility(View.VISIBLE);
        }
    };
    private int animHeight; // 选集控件的高
    private boolean collectIsCheck, gatherIsChek = true; // 选集是否显示,是否收藏（数据暂时先这样设置）
    private ScrollView mDescContainer;
    private ObjectAnimator mDescAnimator;
    private ImageView detail_img_reflect;

    private ShadowView mShadowView;
    private View mFocusedView;
    private Runnable mHideRunable = new Runnable() {
        @Override
        public void run() {
            mFocusedView = null;
            mShadowView.setVisibility(View.INVISIBLE);
        }
    };
    private Handler mHandler = new Handler();
    private String mAreal;
    private String mPlayyear;
    private ImageView mQr_code_iv;
    private RelativeLayout mQr_iamge_rl;

    /**
     * 详情页信息回调
     *
     * @param result
     */
    @Override
    public void onResponse(VideoSetDetailData result, boolean isFromCache) {
        if (getActivity() == null) {
            return;
        }
        setDetailValue(result.result);
    }

    @Override
    public void onFailure(int errorCode, Request request) {

    }

    @Override
    protected int getContentView() {
        mActivity = (BaseActivity) getActivity();
        return R.layout.activity_movevideo_details;
    }

    @Override
    protected void findView(View view) {
        detail_img_reflect = (ImageView) view.findViewById(R.id.detail_img_reflect);
        mDescContainer = (ScrollView) view.findViewById(R.id.desc_container);
        mDescContainer.setFocusable(false);
        mDescContainer.setSelected(false);
        mDescContainer.setClickable(false);
        mDesc = (TextView) view.findViewById(R.id.desc);
        mDesc.setFocusable(false);
        mDesc.setSelected(false);
        mDesc.setClickable(false);
        tv_details_name = (TextView) view.findViewById(+R.id.telepaly_details_tv_titele);
        tv_details_collect = (TextView) view.findViewById(R.id.telepaly_details_tv_collect);

        tv_details_issuetime = (TextView) view.findViewById(R.id.telepaly_details_tv_issuetime);
        tv_details_director = (TextView) view.findViewById(R.id.telepaly_details_tv_director);
        tv_details_actress = (TextView) view.findViewById(R.id.telepaly_details_tv_actress);
        tv_play_text = (TextView) view.findViewById(R.id.teleplay_details_tv_play);
        mSelectView = (SelectView) view.findViewById(R.id.selectview);
        iv_photo = (SimpleDraweeView) view.findViewById(R.id.teleplay_details_iv_photo);
        horgridview_detail = (HorizontalGridView) view.findViewById(R.id.horgridview_detail);
        rl_feedback = (RelativeLayout) view.findViewById(R.id.rl_feedback);
        rl_play = (RelativeLayout) view.findViewById(R.id.rl_play);
        rl_collect = (RelativeLayout) view.findViewById(R.id.rl_collect);
        collect_widget = (LinearLayout) view.findViewById(R.id.rl_widget_collect);
        rl_gather = (RelativeLayout) view.findViewById(R.id.rl_gather);
        rl_feedback.setNextFocusUpId(0);
        rl_play.setNextFocusUpId(0);
        rl_collect.setNextFocusUpId(0);
        rl_gather.setNextFocusUpId(0);

        hint_feedback = (LinearLayout) view.findViewById(R.id.hint_feedback);
        iv_feedback = (ImageView) view.findViewById(R.id.iv_feedback);
        hint = (LinearLayout) view.findViewById(R.id.hint);
        horgridview_detail = (HorizontalGridView) view.findViewById(R.id.horgridview_detail);

        gather_textView = (TextView) view.findViewById(R.id.tv_gather);
        gather_imageView = (ImageView) view.findViewById(R.id.iv_gather);
        rl_err = (LinearLayout) view.findViewById(R.id.rl_err);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.detail_rls);
        for (int i = 0; i < typedArray.length(); i++) {
            RelativeLayout linearLayout = (RelativeLayout) view.findViewById(typedArray.getResourceId(i, 0));
            linearLayout.setOnClickListener(this);
        }
        typedArray.recycle();
        mSelectView.initView(getActivity().getSupportFragmentManager(), this, this);
        mContext = getActivity();
        emptyString = mContext.getResources().getString(R.string.nulls);
        animHeight = (int) mContext.getResources().getDimension(R.dimen.dimen_330dp);
        mShadowView = (ShadowView) view.findViewById(R.id.shadow);
        horgridview_detail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mFocusedView != null) {
                    mShadowView.moveTo(mFocusedView);
                }
            }
        });
        mQr_iamge_rl = (RelativeLayout) view.findViewById(R.id.qr_iamge_rl);
        mQr_code_iv = (ImageView) view.findViewById(R.id.qr_code_iv);
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            videoSet = (VideoSet) bundle.getSerializable(GlobalConstant.KEY_VIDEOSET);
        }
        if (videoSet != null) {
            setDetailValue(videoSet);
        }
        mQr_code_iv.setImageURI(Uri.parse(GlobalConstant.QR_CODE_HTTP));

        rl_collect.setOnFocusChangeListener(this);
        rl_gather.setOnFocusChangeListener(this);
        rl_play.setOnFocusChangeListener(this);
        rl_feedback.setOnFocusChangeListener(this);
        mShadowView.init(25);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        int viewId = view.getId();
        if (b) {
            handler.removeCallbacksAndMessages(null);
            if (viewId == R.id.rl_play || viewId == R.id.rl_gather) { // 播放和收藏
                if (itemDataScolls == null || itemDataScolls.size() <= 0) {
                    rl_err.setVisibility(View.VISIBLE);
                    horgridview_detail.setVisibility(View.INVISIBLE);
                    hint.setVisibility(View.VISIBLE);
                    hint_feedback.setVisibility(View.INVISIBLE);
                    iv_feedback.setVisibility(View.INVISIBLE);
                    mQr_iamge_rl.setVisibility(View.INVISIBLE);
                    mQr_code_iv.setVisibility(View.INVISIBLE);
                } else {
                    rl_err.setVisibility(View.INVISIBLE);
                    horgridview_detail.setVisibility(View.VISIBLE);
                    hint.setVisibility(View.VISIBLE);
                    hint_feedback.setVisibility(View.INVISIBLE);
                    iv_feedback.setVisibility(View.INVISIBLE);
                    mQr_iamge_rl.setVisibility(View.INVISIBLE);
                    mQr_code_iv.setVisibility(View.INVISIBLE);
                }
                if (collectIsCheck) {
                    collectIsCheck = false;
                    collectAnim(animHeight, 0, new CollectAnimatorListener());
                    rl_collect.setSelected(false);
                }
            } else if (viewId == R.id.rl_feedback) {
                horgridview_detail.setVisibility(View.INVISIBLE);
                hint.setVisibility(View.INVISIBLE);
                rl_err.setVisibility(View.INVISIBLE);
                hint_feedback.setVisibility(View.VISIBLE);
                iv_feedback.setVisibility(View.VISIBLE);
                mQr_iamge_rl.setVisibility(View.VISIBLE);
                mQr_code_iv.setVisibility(View.VISIBLE);
            }
            if (viewId == R.id.rl_collect) { // 选集
                if (itemDataScolls == null || itemDataScolls.size() <= 0) {
                    rl_err.setVisibility(View.VISIBLE);
                }
                if (!collectIsCheck) {
                    collect_widget.setVisibility(View.VISIBLE);
                    collectIsCheck = true;
                    collectAnim(0, animHeight, null);
                    rl_collect.setSelected(true);
//                    tv_details_collect_color.setTextColor(getResources().getColor(R.color.detail_btn_bg));
                    if (videoSetDao != null) {
                        mSelectView.post(new Runnable() {
                            @Override
                            public void run() {
                                mSelectView.onItemFocusListener(null, videoSetDao.playOrder / 10, true);
                            }
                        });
                    }

                }
            }
        }
    }

    private String checkEmpty(String source) {
        return TextUtils.isEmpty(source) ? emptyString : DetailStringUtil.getType(source);
    }

    private String checkEmptys(String source) {
        return TextUtils.isEmpty(source) ? emptyString : source;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_play: // 第几集播放,电影的话就是播放当前电影
                if (playVideoSet.seriesType == 0) {
                    int time = 0;
                    if (videoSetDao != null) {
                        time = videoSetDao.startTime;
                    }
                    Project.get().getConfig().playVideo(getActivity(), playVideoSet.getId(), playVideoSet.getId(), "1", time, playVideoSet.getName(), playVideoSet.getImage(), playVideoSet.getChannelId());
                } else {
                    int time = 0;
                    int pos = 1;
                    if (videoSetDao != null) {
                        pos = videoSetDao.count;
                        time = videoSetDao.startTime;
                    }
                    if (pos == 0) {
                        pos = 1;
                    }
                    Project.get().getConfig().playVideo(getActivity(), videoSet.getId(), playVideoSet.getId(), String.valueOf(pos), time, videoSet.getName(), videoSet.getImage(), videoSet.getChannelId());
                }
                break;
            case R.id.rl_gather: // 收藏
                if (!gatherIsChek) { // 已收藏
                    DbUtil.deleteCollect(playVideoSet.id);
                    notTocollect();
                } else { // 收藏
                    //收藏成功 添加统计
                    StatService.onEvent(mContext, "Collection_button", mContext.getString(R.string.collection_button), 1);
                    if (playVideoSet.seriesType == 0 || playVideoSet.count == playVideoSet.total) {
                        DbUtil.addCollect(playVideoSet.id, playVideoSet.getImage(), playVideoSet.name);
                    } else {
                        DbUtil.addCollect(playVideoSet.id, playVideoSet.getImage(), playVideoSet.name, playVideoSet.count, playVideoSet.total);
                    }
                    collect();
                }
                break;
        }
    }

    @Override
    public void onItemClickListener(View view, int position) {
        Project.get().getConfig().playVideo(getActivity(), videoSet.getId(), playVideoSet.getId(),
                String.valueOf(position), 0, videoSet.getName(), videoSet.getImage(), videoSet.channelId);
    }

    @Override
    public void onItemFocusListener(final View view, final int position, boolean hasFocus) { // 一句话看点
        if (hasFocus) {
            final Video video = mVideoArray[position - 1];
            final String focus = video == null ? null : video.focus; // 判断为空
            if (focus != null) {
                // 一句话看点为空不显示
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CustomToast.showCustomToast(getActivity(), view, focus);
                    }
                }, 300);
            }
        } else {
            handler.removeCallbacksAndMessages(null);
            CustomToast.removeCustomToast();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Todo  优朋播放时长统计待优化
        //优朋结束统计播放时长
        StatService.onEventEnd(getActivity(), "PlayTime", getActivity().getString(R.string.playtime));
    }

    @Override
    public void onStop() {
        NetworkManager.cancelRequest(TAG);
        handler.removeCallbacksAndMessages(null);
        CustomToast.removeCustomToast();
        if (mDescAnimator != null) {
            mDescAnimator.cancel();
        }
        super.onStop();
    }


    /**
     * 选集动画
     *
     * @param formHeight 初始的高度
     * @param toHeight   要到达的高度
     */
    private void collectAnim(int formHeight, int toHeight, Animator.AnimatorListener listener) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(formHeight, toHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = collect_widget.getLayoutParams();
                layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                collect_widget.setLayoutParams(layoutParams);
            }
        });
        if (listener != null) {
            valueAnimator.addListener(listener);
        }
        valueAnimator.start();
    }

    /**
     * 设置详情页数据
     *
     * @param videoSet
     */
    public void setDetailValue(VideoSet videoSet) {
        String collectStr; // 集数
        int colletNumber; // 选集
        this.playVideoSet = videoSet;
        videoSetDao = DbUtil.queryById(videoSet.id); // 查看是否收藏和上次播放到多少集数
        setFavorResult(videoSetDao);
        if (videoSet.count != 0 && videoSet.total != 0 && videoSet.count == videoSet.total) { // 剧集已更新完
            colletNumber = videoSet.total;
            mVideoArray = new Video[colletNumber];
        } else {
            colletNumber = videoSet.count;
            mVideoArray = new Video[colletNumber];
        }
        mSelectView.bindData(colletNumber);
        tv_details_name.setText(videoSet.name);
        if (videoSet.seriesType == 0) // 电影不需要显示剧集,集数,第几集改为播放
        {
            tv_details_collect.setVisibility(View.INVISIBLE);
            rl_collect.setVisibility(View.GONE);
            tv_play_text.setText(mContext.getResources().getString(R.string.play_text));
        }
        if (videoSet.count == videoSet.total) { // 更新集数等于总集数，显示多少集全,否则显示更新至多少集
            collectStr = mContext.getString(R.string.detail_whole, videoSet.total);
        } else {
            collectStr = mContext.getString(R.string.detail_renewal, videoSet.count);
        }
        tv_details_collect.setText(collectStr);
        if ((TextUtils.isEmpty(videoSet.getIssueTime()) ? emptyString : DetailStringUtil.getIssueTime(videoSet.getIssueTime(), getActivity())).length() == 1) {
            mPlayyear = "";
        } else {
            String time = videoSet.getIssueTime();
            if (TextUtils.isEmpty(time)) {
                time = "";
            } else {
                time = DetailStringUtil.getIssueTime(time, getActivity());
                if (time.length() >= 4) {
                    time = time.substring(0, 4) + "年" + " / ";
                } else {
                    time = "";
                }
            }
            mPlayyear = time;
        }
        if (videoSet.getArea() == null) {
            mAreal = "";
        } else {
            mAreal = videoSet.getArea() + " / ";
        }
        String time = videoSet.getChannelId().equals(CategoryUtils.getCategoryByName("电影").getId()) ? videoSet.getPlayLength() + "分钟 / " : "";
        tv_details_issuetime.setText(mPlayyear + mAreal + time + checkEmpty(videoSet.tags).replace(" ","").replace("|", " / "));
        tv_details_director.setText(checkEmptys(videoSet.directors).replace("|", "  "));
        tv_details_actress.setText((TextUtils.isEmpty(videoSet.actors) ? emptyString : DetailStringUtil.getActress(videoSet.actors)).replace("|", "  ").replace("/", "  "));
        mDesc.setText(checkEmptys(videoSet.desc));
        mDesc.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int height = mDesc.getMeasuredHeight();
                        int containerHeight = mDescContainer.getMeasuredHeight();
                        int offset = height - containerHeight;
                        if (mDescAnimator == null && offset > 0) {
                            int duration = height * 100;
                            mDescAnimator = ObjectAnimator.ofInt(mDescContainer, "scrollY", 0, offset).setDuration(duration);
                            mDescAnimator.setRepeatCount(ValueAnimator.INFINITE);
                            mDescAnimator.setRepeatMode(ValueAnimator.REVERSE);
                            mDescAnimator.start();
                        }
                    }
                });
        Uri[] uriArray = {Uri.parse(videoSet.getVerticalPosterUrl()), Uri.parse(videoSet.getImage())};
        UIUtils.setFrescoImageRequests(iv_photo, uriArray);
        doReflectImage(detail_img_reflect);
        if (videoSet.seriesType != 0) {
            NetworkManager.getInstance().getVideoList(videoSet.getVideoListUrl(), videoListListener, TAG); // 选集数据
        }
        NetworkManager.getInstance().getRecommendVideoSetList(videoSet.getRecommendUrl(), favorListContentListener, TAG); // 相关推荐数据
    }

    /**
     * 设置收藏和播放集数数据
     */
    private void setFavorResult(VideoSetDao videoSetDao) {
        if (null == videoSetDao) { // null 可以理解为初次加载,播放第一集和显示未加载
            tv_play_text.setText(mContext.getResources().getString(R.string.collect_play, 1));
            notTocollect();
        } else { // 不为空，看过或者收藏过
            if (videoSetDao.count == 0) {// 为0 没有播放过，显示第一集
                tv_play_text.setText(mContext.getResources()
                        .getString(R.string.collect_play, 1));
            } else { // 不为0 播放过，显示播放第几集
                tv_play_text.setText(mContext.getResources()
                        .getString(R.string.collect_play, videoSetDao.count));
            }
            if (videoSetDao.collect == 0) { // 0 未收藏
                notTocollect();
            } else {
                collect();
            }

        }
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

    private void doReflectImage(final ImageView view) {
        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.reflect_animation);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setAnimation(animation);
                view.setImageBitmap(ImageReflectedUtil.createReflectedImage(iv_photo));
            }
        }, 1000);
    }

    /**
     * 相关推荐Item点击事件
     */
    private class RecommendOnItemClickListener implements OnItemClickListener<VideoSet> {
        @Override
        public void onItemClickLister(View view, int position, VideoSet videoSet) {
            Project.get().getConfig().openVideoDetail(getActivity(), videoSet);
            getActivity().finish();
        }
    }

    /**
     * 相关推荐焦点获得事件
     */
    private class RecommendOnItemFocuseListener implements OnItemFocusListener<VideoSet> {
        @Override
        public void onItemFocusLister(View view, int position, VideoSet videoSet, boolean hasFocus) {
            if (hasFocus) {
                mHandler.removeCallbacks(mHideRunable);
                mShadowView.moveTo(view);
                mFocusedView = view;
            } else {
                mHandler.postDelayed(mHideRunable, 16);
            }
            if (collectIsCheck) {
                handler.removeCallbacksAndMessages(null);
                collectIsCheck = false;
                collectAnim(animHeight, 0, new CollectAnimatorListener());
                rl_collect.setSelected(false);
            }
        }
    }

    /**
     * 对选集动画侦听，在动画结束的时候隐藏选集控件
     */
    private class CollectAnimatorListener extends AnimatorListenerAdapter {
        @Override
        public void onAnimationEnd(Animator animator) {
            if (mFocusedView != null) {
                mShadowView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mShadowView.moveTo(mFocusedView);
                    }
                }, 200);
            }
            collect_widget.setVisibility(View.GONE);
        }
    }
}
