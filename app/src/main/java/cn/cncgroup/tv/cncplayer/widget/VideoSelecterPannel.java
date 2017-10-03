package cn.cncgroup.tv.cncplayer.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.PlayerConstants;
import cn.cncgroup.tv.cncplayer.adapter.DefitionAdapter;
import cn.cncgroup.tv.cncplayer.callback.OnDefitionFocusChangeListener;
import cn.cncgroup.tv.cncplayer.eventbus.DefinitionEvent;
import cn.cncgroup.tv.cncplayer.widget.selectview.Constant;
import cn.cncgroup.tv.cncplayer.widget.selectview.ItemClickListener;
import cn.cncgroup.tv.cncplayer.widget.selectview.ItemFocusListener;
import cn.cncgroup.tv.cncplayer.widget.selectview.SelectView;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.HorizontalGridView;
import de.greenrobot.event.EventBus;

/**
 * 选集控件 + 一句话提示 + 分辨率View
 * Created by zhangguang on 2015/8/26.
 */
public class VideoSelecterPannel extends LinearLayout implements ItemClickListener, ItemFocusListener {
    private static final String TAG = "VideoSelecterPannel";
    private static final int VIEW_GONE = -1;
    private static final long DEFAULT_HIDDEN_TIME = 3000;
    private View mView;                      //加载layout的View
    /***
     * 传递过来的值
     **/
    private FragmentManager manager;
    private SelectView mSelectView;         //选集控件
    private DefitionAdapter mDefitionAdapter;//分辨率的适配器
    private Context mContext;
    private HorizontalGridView mHorizontalGridView;//分辨率选择View
    private ShadowView mShadowView;
    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == VIEW_GONE) {
                doShowOrHiden();
            }
        }
    };
    private View mHorizonBg;
    private View mViewBg;
    private FrameLayout mDefition;

    public VideoSelecterPannel(Context context) {
        super(context);
        this.mContext = context;
        setContentView();
    }

    public VideoSelecterPannel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        setContentView();
    }

    public VideoSelecterPannel(Context context, AttributeSet attributeSet,
                               int defStyle) {
        super(context, attributeSet, defStyle);
        this.mContext = context;
        setContentView();
    }

    /**************
     * 选集Item点击和焦点回调事件
     ********************/
    @Override
    public void onItemClickListener(View view, int position) {
        Log.i(TAG, "选集Item点击和焦点回调事件");
        doShowOrHiden();
    }

    @Override
    public void onItemFocusListener(View view, int postition, boolean hasFocus) {
        Log.i(TAG, "选集onItemFocusListener");
    }

    /**
     * 设置分辨率数据
     *
     * @param defitionList 分辨率集合
     */
    public void setDefitionViewResult(ArrayList<String> defitionList) {
        mDefitionAdapter.setmListData(defitionList);
        Log.i(TAG, "++++++++ListSize:" + defitionList.size());
        post(new Runnable() {
            @Override
            public void run() {
                mDefitionAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setContentView() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.view_videoplayer_bottom_pannel, null);
        addView(mView);
        findView();
        initView();
    }

    private void findView() {
        mSelectView = (SelectView) findViewById(R.id.video_select);
        mHorizontalGridView = (HorizontalGridView) findViewById(R.id.palyer_gridView_defition);
        mShadowView = (ShadowView) findViewById(R.id.shadow);
        mHorizonBg = findViewById(R.id.horizon_bg);
        mViewBg = findViewById(R.id.view_bg);
        mDefition = (FrameLayout) findViewById(R.id.player_rl_resolution);
    }

    private void initView() {
        mHorizontalGridView.setItemMargin((int) mContext.getResources().getDimension(R.dimen.dimen_45dp));
        manager = ((FragmentActivity) mContext).getSupportFragmentManager();
        mSelectView.initView(manager, this, this);
        mDefitionAdapter = new DefitionAdapter(mContext, new DefitionItemClickListener(), new MyOnDefitionFocusChangeListener());
        mHorizontalGridView.setAdapter(mDefitionAdapter);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(TAG, " Constant.ITEM_COUNT=" + Constant.ITEM_COUNT + "---dispatchKeyEvent event:" + event);
        if (PlayerConstants.SCREEN_TYPE == PlayerConstants.FRAGMENT_SMALL) {
            return dispatchKeyEvent(event);
        }
        show();
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_MENU == event.getKeyCode()) {
            doShowOrHiden();
        }
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            if (isShowing() && event.getAction() == KeyEvent.ACTION_UP)
                doShowOrHiden();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void setVideoCount(int size) {
        mSelectView.bindData(size);
    }

    /*****************
     * 分辨率Item点击事件回调
     *********************/
    private class DefitionItemClickListener
            implements OnItemClickListener<String> {
        @Override
        public void onItemClickLister(View view, int position,
                                      String defition) {
            Log.i(TAG, "分辨率Item点击事件:" + position);
            EventBus.getDefault().post(new DefinitionEvent(position));
            doShowOrHiden();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        Log.i(TAG, "PlayerConstants.CURRENT_DEFINATION_INDEX:" + PlayerConstants.CURRENT_DEFINATION_INDEX);
        if (Constant.ITEM_COUNT == 1) {
            changeView();
            mHorizontalGridView.requestFocus();
            mHorizontalGridView.post(new Runnable() {
                @Override
                public void run() {
                    mHorizontalGridView.getChildAt(PlayerConstants.CURRENT_DEFINATION_INDEX).requestFocus();
                }
            });
        } else {
            mSelectView.setmItemFocused(Constant.CURRENT_INDEX);
        }
        super.setVisibility(visibility);
    }

    //集数为一的View
    private void changeView() {
        mHorizonBg.setVisibility(GONE);
        mViewBg.setBackgroundResource(R.drawable.media_controller_bg);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.dimen_230dp), 0, 0);
        mDefition.setLayoutParams(params);
    }

    /**
     * 设置空间显示或隐藏
     */
    public void doShowOrHiden() {
        Log.i(TAG, "doShowOrHiden getVisibility() == View.VISIBLE?=" + (getVisibility() == View.VISIBLE));
        if (getVisibility() == View.VISIBLE) {
            clearFocus();
            mHandler.removeMessages(VIEW_GONE);
            setVisibility(View.GONE);
        } else {
            mView.requestFocus();
            setVisibility(View.VISIBLE);
            show();
        }
    }

    private void show() {
        Log.i(TAG, "Constant.ITEM_COUNT" + Constant.ITEM_COUNT);
        mHandler.removeMessages(VIEW_GONE);
        Message msg = Message.obtain();
        msg.what = VIEW_GONE;
        mHandler.sendMessageDelayed(msg, DEFAULT_HIDDEN_TIME);
    }

    /*
     * 获取是否正在显示
     */
    public boolean isShowing() {
        return getVisibility() == View.VISIBLE;
    }

    private class MyOnDefitionFocusChangeListener implements OnDefitionFocusChangeListener {
        @Override
        public void onDefitionFocusChange(View view, boolean hasFocuse, int index) {
            if (hasFocuse) {
                move(view);
            } else {
                mShadowView.setVisibility(View.INVISIBLE);
            }
            TextView tv = (TextView) view.findViewById(R.id.defition_item_tv_defition);
            Resources res = getResources();
            if (index == PlayerConstants.CURRENT_DEFINATION_INDEX && !hasFocuse) {
                tv.setTextColor(res.getColor(R.color.white_100));
            } else {
                if (hasFocuse)
                    tv.setTextColor(res.getColor(R.color.white_100));
                else
                    tv.setTextColor(res.getColor(R.color.white_40));
            }
            Log.i(TAG, "onDefitionFocusChange() hasFocuse:" + hasFocuse + ",index:" + index);

        }
    }

    private void move(View view) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mShadowView
                .getLayoutParams();
        params.width = view.getMeasuredWidth() + 48;
        params.height = view.getMeasuredHeight() + 48;
        mShadowView.setLayoutParams(params);
        mShadowView.setX(view.getX() - 15);
        mShadowView.setY(view.getY() - 25);
        mShadowView.setVisibility(View.VISIBLE);
    }
}
