package cn.cncgroup.tv.cncplayer.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

import java.util.Timer;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.PlayerConstants;
import cn.cncgroup.tv.cncplayer.eventbus.FinishActivityEvent;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.view.footmark.FootmarkActivity;
import cn.cncgroup.tv.view.homesecond.utils.CategoryUtils;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.DensityUtil;
import cn.cncgroup.tv.utils.Utils;
import cn.cncgroup.tv.view.search.SearchActivity;
import de.greenrobot.event.EventBus;

/**
 * Created by yqh on 2015/9/18. Quick Access Pannel
 */
public class QuickAccessPannel extends RelativeLayout {
    private static final String TAG = "QuickAccessPannel";
    private static final int Item0 = 0;
    private static final int Item1 = 1;
    private static final int Item2 = 2;
    private static final int Item3 = 3;
    private static final int Item4 = 4;
    private static final int Item5 = 5;
    private static final int Item6 = 6;
    private static final int VIEW_GONE = -1;
    // widget
    private VerticalGridView mVerticalView;
    // data
    private Context mContext;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private boolean mTimerStarted = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == VIEW_GONE) {
                mTimerStarted = false;
                hiddenQuickAccessContener();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(View.GONE);
                    }
                }, 500);
            }
        }
    };
    private Timer mTimer;

    public QuickAccessPannel(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public QuickAccessPannel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public QuickAccessPannel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    private void initView() {
        int[] myDataset = new int[]{R.drawable.quickaccess_other_selecter,
                R.drawable.quickaccess_variety_selecter,
                R.drawable.quickaccess_teleplay_selecter,
                R.drawable.quickaccess_movies_selecter,
                R.drawable.quickaccess_search_selecter,
                R.drawable.quickaccess_history_selecter,
                R.drawable.quickaccess_collect_selecter};

        int[] mTextData = new int[]{R.string.quickaccess_other,
                R.string.quickaccess_variety, R.string.quickaccess_teleplay,
                R.string.quickaccess_movies, R.string.quickaccess_search,
                R.string.quickaccess_history, R.string.quickaccess_collect};
        Log.i(TAG, "initView");
        View view = View.inflate(mContext, R.layout.pannel_quickaccess, null);
        addView(view);
        mVerticalView = (VerticalGridView) view
                .findViewById(R.id.recycler_quickaccess);
        mVerticalView.setNumColumns(mTextData.length);
        Resources res = getResources();
        mVerticalView.setColumnWidth((int) res
                .getDimension(R.dimen.dimen_250dp));
        mVerticalView.setScrollEnabled(false);
        mAdapter = new MyAdapter(myDataset, mTextData);
        mVerticalView.setAdapter(mAdapter);
    }

    /**
     * 返回Item点击点击事件
     *
     * @param pos
     */
    public void onItemClick(int pos) {
        blackSelectPostion(pos);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (PlayerConstants.SCREEN_TYPE==PlayerConstants.FRAGMENT_SMALL) {
            return dispatchKeyEvent(event);
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP)
            return true;
        if (getVisibility() == View.VISIBLE) {
            startTimer();
        }
        return super.dispatchKeyEvent(event);
    }

    private void showQuickAccessContener() {
        TranslateAnimation tAnim = new TranslateAnimation(0, 0, 270, 0);
        tAnim.setDuration(500);
        mVerticalView.startAnimation(tAnim);
        mVerticalView.setFocusPosition(3);
    }

    private void hiddenQuickAccessContener() {
        TranslateAnimation tAnim = new TranslateAnimation(0, 0, 0, 270);
        tAnim.setDuration(500);
        mVerticalView.startAnimation(tAnim);
    }

    @Override
    public void setVisibility(int visibility) {
        Log.i(TAG, "setVisibility");
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startTimer();
            if (mVerticalView != null && mVerticalView.getChildAt(3) != null)
                mVerticalView.getChildAt(3).requestFocus();
        }
        showQuickAccessContener();
    }

    private void startTimer() {
        if (mTimerStarted) {
            mHandler.removeMessages(VIEW_GONE);
        }
        mHandler.removeMessages(VIEW_GONE);
        Message msg = Message.obtain();
        msg.what = VIEW_GONE;
        mHandler.sendMessageDelayed(msg, 3000);
    }

    /**
     * 返回点击对应响应
     */
    private void blackSelectPostion(int position) {
        switch (position) {
            case Item0: // 其他
                //其他添加统计
                StatService.onEvent(mContext, "Channel_PlayingPage", mContext.getString(R.string.channel_playingpage), 1);
                Project.get().getConfig().openCategory(mContext, CategoryUtils.getCategoryByName("其他"));
                break;
            case Item1: // 综艺
                //综艺添加统计
                StatService.onEvent(mContext, "Variety_PlayingPage", mContext.getString(R.string.variety_playingpage), 1);
                Project.get().getConfig().openCategory(mContext, CategoryUtils.getCategoryByName("综艺"));
                break;
            case Item2: // 电视剧
                //电视剧添加统计
                StatService.onEvent(mContext, "Teleplay_PlayingPage", mContext.getString(R.string.teleplay_playingpage), 1);
                Project.get().getConfig().openCategory(mContext, CategoryUtils.getCategoryByName("电视剧"));
                break;
            case Item3: // 电影
                //电影添加统计
                StatService.onEvent(mContext, "Film_PlayingPage", mContext.getString(R.string.film_playingpage), 1);
                Project.get().getConfig().openCategory(mContext, CategoryUtils.getCategoryByName("电影"));
                break;
            case Item4: // 搜索
                //搜索添加统计
                StatService.onEvent(mContext, "Search_PlayingPage", mContext.getString(R.string.search_playingpage), 1);
                startActivity(SearchActivity.class);
                break;
            case Item5: // 历史
                //历史添加统计
                StatService.onEvent(mContext, "Histroy_PlayingPage", mContext.getString(R.string.histroy_playingpage), 1);
                startActivity(FootmarkActivity.class);
                break;
            case Item6: // 收藏
                //收藏添加统计
                StatService.onEvent(mContext, "Collection_PlayingPage", mContext.getString(R.string.collection_playingpage), 1);
                startActivity(FootmarkActivity.class);
                break;
            default:
        }
        Log.i(TAG, " EventBus.getDefault().post");
        EventBus.getDefault().post(new FinishActivityEvent());
    }

    private void startActivity(Class<? extends Activity> clas) {
        Utils.startActivity(mContext, clas, null);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private int[] mSelecterData;
        private int[] mTextData;

        public MyAdapter(int[] selecterData, int[] textData) {
            mSelecterData = selecterData;
            mTextData = textData;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            final int sp_24 = getResources().getDimensionPixelOffset(R.dimen.dimen_24dp);
            final int sp_18 = getResources().getDimensionPixelOffset(R.dimen.dimen_18dp);
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_quick_access, parent, false);
            v.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    TextView tv = (TextView) v.findViewById(R.id.item_text);
                    if (hasFocus) {
                        params.setMargins(0, sp_18, 0, 0);
                        tv.setTextSize(DensityUtil.px2sp(mContext, getResources().getDimensionPixelSize(R.dimen.dimen_36sp)));
                    } else {
                        params.setMargins(0, sp_24, 0, 0);
                        tv.setTextSize(DensityUtil.px2sp(mContext, getResources().getDimensionPixelSize(R.dimen.dimen_28sp)));
                    }
                    Log.i(TAG, "hasFocus:" + hasFocus);
                    params.addRule(RelativeLayout.BELOW, R.id.item_icon);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    tv.setLayoutParams(params);
                }
            });
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mRootView.setOnClickListener(new ItemOnClickListener(
                    position));
            if (position == 3)
                holder.mRootView.requestFocus();
            holder.mTextView.setText(mTextData[position]);
            holder.mIcon.setBackground(getResources().getDrawable(mSelecterData[position]));
        }

        @Override
        public int getItemCount() {
            return mSelecterData.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public ImageView mIcon;
            public View mRootView;

            public ViewHolder(View v) {
                super(v);
                mRootView = v;
                mTextView = (TextView) v.findViewById(R.id.item_text);
                mIcon = (ImageView) v.findViewById(R.id.item_icon);
            }
        }
    }

    class ItemOnClickListener implements OnClickListener {
        private int mPos;

        public ItemOnClickListener(int pos) {
            mPos = pos;
        }

        @Override
        public void onClick(View v) {
            onItemClick(mPos);
        }
    }
}
