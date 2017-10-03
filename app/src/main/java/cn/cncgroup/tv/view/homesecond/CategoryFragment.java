package cn.cncgroup.tv.view.homesecond;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageSwitcher;

import java.util.Random;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.modle.Category;
import cn.cncgroup.tv.view.homesecond.utils.CategoryUtils;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.view.homesecond.adapter.CategoryAdapter;
import cn.cncgroup.tv.view.homesecond.widget.CategoryRecyclerView;

/**
 * Created by zhang on 2015/10/15
 */
public class CategoryFragment extends BaseHomeFragment implements
        OnItemClickListener<CategoryAdapter.CategoryItem>,
        OnItemFocusListener<CategoryAdapter.CategoryItem> {
    private static final int MSG_CODE = 0X10000;
    private CategoryRecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    private boolean isShowing = false;
    private int lastAnimationPosition = 0;
    //ShadowView
    private ShadowView mShadowView;
    private View mFocusedView;
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mFocusedView != null) {
                mShadowView.moveTo(mFocusedView);
            }
        }
    };
    private Runnable mHideRunable = new Runnable() {
        @Override
        public void run() {
            mShadowView.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.fragment_category;
    }

    @Override
    protected void findView(View view) {
        mRecyclerView = (CategoryRecyclerView) view.findViewById(R.id.recyclerView);
        mShadowView = (ShadowView) view.findViewById(R.id.shadow);
    }

    @Override
    protected void initView() {
        mAdapter = new CategoryAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mScrollListener);
        mShadowView.init(25);
        initHandler();
    }

    @Override
    public void onItemClickLister(View view, int position, CategoryAdapter.CategoryItem categoryItem) {
        Category category = CategoryUtils.getCategoryByName(categoryItem.name);
        if (category != null) {
            Project.get().getConfig().openCategory(getActivity(), category);
        } else {
            Project.get().getConfig().openSubject(getActivity());
        }
    }

    @Override
    public void onItemFocusLister(View view, int position,
                                  CategoryAdapter.CategoryItem categoryItem, boolean hasFocus) {
        if (hasFocus) {
            mHandler.removeCallbacks(mHideRunable);
            mFocusedView = view;
            mShadowView.moveTo(mFocusedView);
        } else {
            mHandler.postDelayed(mHideRunable, 16);
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        isShowing = true;
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        isShowing = false;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void initHandler() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_CODE) {
                    mHandler.removeMessages(MSG_CODE);
                    Random random = new Random();
                    int count = mAdapter.getItemCount();
                    int position = random.nextInt(count) % count;
                    while (position == lastAnimationPosition) {
                        position = random.nextInt(count) % count;
                    }
                    CategoryAdapter.ViewHolder viewHolder = (CategoryAdapter.ViewHolder)
                            mRecyclerView.findViewHolderForAdapterPosition(position);
                    ImageSwitcher switcher = ((ImageSwitcher) viewHolder.itemView.findViewById(R.id.categroy_switcher));
                    int animationType = random.nextInt(3);
                    if (animationType == 0) {
                        switcher.setInAnimation(getActivity(), R.anim.item_bottom2top_in);
                        switcher.setOutAnimation(getActivity(), R.anim.item_bottom2top_out);
                    } else if (animationType == 1) {
                        switcher.setInAnimation(getActivity(), R.anim.item_right2left_in);
                        switcher.setOutAnimation(getActivity(), R.anim.item_right2left_out);
                    } else {
                        switcher.setInAnimation(getActivity(), R.anim.item_left2right_in);
                        switcher.setOutAnimation(getActivity(), R.anim.item_left2right_out);
                    }
                    switcher.setImageDrawable(getResources().getDrawable(mAdapter.getItem(position).image));
                    lastAnimationPosition = position;
                    if (isShowing)
                        mHandler.sendEmptyMessageDelayed(MSG_CODE, 4 * 1000);
                }
            }
        };
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mHandler != null) {
                mHandler.removeMessages(MSG_CODE);
                mHandler.sendEmptyMessageDelayed(MSG_CODE, 4 * 1000);
            }
            isShowing = true;
        } else {
            isShowing = false;
        }
    }

    @Override
    void arrowScroll(boolean isLeft, boolean isTop) {

    }
}
