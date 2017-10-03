package cn.cncgroup.tv.cncplayer.widget.selectview;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.FocusZoomUtil;

/**
 * 选集的View
 */
public class SelectView extends LinearLayout implements SectionClickListener,
        ViewPager.OnPageChangeListener, SectionFocusListener {
    private static final String TAG = "SelectView";
    static FocusZoomUtil sFocusZoomUtil;
    private ViewPager mItemViewPager; // 集数的viewPage(第一行)
    private ViewPager mSectionViewPager; // 选集的viewPager(第二行)
    private ItemPageAdapter mItemPagerAdapter; // 集数的pagerAdapter
    private SectionPageAdapter mSectionPagerAdapter; // 选集的pagerAdapter;
    private View mLastView;
    private ItemFocusListener mItemFocusListener;

    public SelectView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        sFocusZoomUtil = new FocusZoomUtil(FocusZoomUtil.ZOOM_FACTOR_LARGE);
        addContextView(context);
    }

    /**
     * 添加两个ViewPager
     */
    private void addContextView(Context context) {
        setClipChildren(false);
        setOrientation(LinearLayout.VERTICAL);
        FrameLayout itemContainer = (FrameLayout) LayoutInflater.from(context)
                .inflate(R.layout.player_selectview_viewpager, this, false);
        mItemViewPager = (ViewPager) itemContainer.findViewById(R.id.viewPager);
        mItemViewPager.setOffscreenPageLimit(100);
        mItemViewPager.setId(R.id.expandable_text);
        addView(itemContainer);
        FrameLayout sectionContainer = (FrameLayout) LayoutInflater
                .from(context)
                .inflate(R.layout.player_selectview_viewpager, this, false);
        mSectionViewPager = (ViewPager) sectionContainer
                .findViewById(R.id.viewPager);
        mSectionViewPager.setOffscreenPageLimit(100);
        mSectionViewPager.setId(R.id.expand_collapse);
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int marginTop = (int) getResources().getDimension(R.dimen.dimen_47dp);
        int marginLeft = (int) getResources().getDimension(R.dimen.dimen_015dp);
        param.setMargins(marginLeft, marginTop, 0, 0);
        mSectionViewPager.setLayoutParams(param);
        sectionContainer.setBackgroundColor(Color.TRANSPARENT);
        addView(sectionContainer);
    }

    public void setmItemFocused(final int postion) {
        if(getVisibility()!=VISIBLE) {
            clearFocus();
            return;
        }
        final int lastPage = Constant.LAST_POSTION / Constant.COLUMN_ITEM_COUNT;
        final int indexLat = Constant.LAST_POSTION % Constant.COLUMN_ITEM_COUNT;
        final int page = postion / Constant.COLUMN_ITEM_COUNT;
        final int index = postion % Constant.COLUMN_ITEM_COUNT;
        Constant.LAST_POSTION = postion;
        Log.i(TAG, "page:" + page + "--index:" + index + "--mLastIndex:" + Constant.LAST_POSTION);
        mItemViewPager.post(new Runnable() {
            @Override
            public void run() {
                SelectView.this.onItemFocusListener(null, page, true);
                ItemGridFragment lastFrag = (ItemGridFragment) mItemPagerAdapter.instantiateItem(mItemViewPager, lastPage);
                VerticalGridView lastGrid = lastFrag.getGridView();
                TextView tvLast = (TextView) lastGrid.getChildAt(indexLat).findViewById(R.id.text);
                tvLast.setTextColor(getResources().getColor(R.color.white));
                ItemGridFragment fragment = (ItemGridFragment) mItemPagerAdapter.instantiateItem(mItemViewPager, page);
                VerticalGridView gridView = fragment.getGridView();
                int count = gridView.getChildCount();
                TextView tv;
                for (int i = 0; i < count; i++) {
                    tv = (TextView) gridView.getChildAt(i).findViewById(R.id.text);
                    if (i == index) {
                        Log.i(TAG, "i == Constant.CURRENT_INDEX:" + (i == Constant.CURRENT_INDEX));
                        tv.setTextColor(getResources().getColor(R.color.blue_a0e9));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }
                }
                gridView.setSelectedPosition(index);
            }
        });
    }


    /**
     * 初始化数据
     */
    public void initView(FragmentManager manager,
                         ItemClickListener clickListener, ItemFocusListener focusListener) {
        Log.i(TAG, "initView");
        mItemFocusListener = focusListener;
        mItemPagerAdapter = new ItemPageAdapter(manager, mInnerItemFocusListener,
                clickListener);
        mItemViewPager.setAdapter(mItemPagerAdapter);
        mItemViewPager.setOnPageChangeListener(this);
        mSectionPagerAdapter = new SectionPageAdapter(manager, this, this);
        mSectionViewPager.setAdapter(mSectionPagerAdapter);
    }

    public void initView(FragmentManager manager) {
        this.initView(manager, null, null);
    }

    /**
     * 设置数据大小（总集数）
     */
    public void bindData(int count) {
        Log.i(TAG, "bindData count:" + count);
        Constant.ITEM_COUNT = count; // 总集数
        Constant.COLLECT_PAGER_COUNT = Constant.ITEM_COUNT // 上面的集数至少为1
                % Constant.COLUMN_ITEM_COUNT == 0
                ? Constant.ITEM_COUNT / Constant.COLUMN_ITEM_COUNT
                : Constant.ITEM_COUNT / Constant.COLUMN_ITEM_COUNT + 1;
        Constant.SELECT_PAGER_COUNT = Constant.COLLECT_PAGER_COUNT // 下面的集数至少为1
                % Constant.COLUMN_SECTION_COUNT == 0
                ? Constant.COLLECT_PAGER_COUNT
                / Constant.COLUMN_SECTION_COUNT
                : Constant.COLLECT_PAGER_COUNT
                / Constant.COLUMN_SECTION_COUNT + 1;
        mItemPagerAdapter.notifyDataSetChanged(); // 重绘
        mSectionPagerAdapter.notifyDataSetChanged();
        if ( Constant.ITEM_COUNT == 1)
            setVisibility(GONE);
    }

    /**
     * 选集第二行控制上面翻页
     */
    @Override
    public void onSelectionClick(View view, int section) {
        mItemViewPager.setCurrentItem(section);
    }

    @Override
    public void onPageSelected(int position) {
        int page = position / Constant.COLUMN_SECTION_COUNT;
        int index = position % Constant.COLUMN_SECTION_COUNT;
        mSectionViewPager.setCurrentItem(page);
        if (mItemViewPager.hasFocus()) {
            for (int i = 0; i < mSectionPagerAdapter.getCount(); i++) {
                if (i == page) {
                    if (mLastView != null) {
                        mLastView.setSelected(false);
                    }
                    mSectionPagerAdapter.setSelected(mSectionViewPager, i,
                            index);
                    break;
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemFocusListener(View view, int section, boolean hasFocus) {
        Log.i(TAG, "onItemFocusListener:" + section);
        if (hasFocus) {
            mItemViewPager.setCurrentItem(section);
            if (view != null) {
                if (mLastView != null) {
                    mLastView.setSelected(false);
                }
                view.setSelected(true);
                mLastView = view;
            } else {
                int page = section / Constant.COLUMN_SECTION_COUNT;
                int index = section % Constant.COLUMN_SECTION_COUNT;
                Log.i(TAG, "page:" + page);
                mSectionViewPager.setCurrentItem(page);
                for (int i = 0; i < mSectionPagerAdapter.getCount(); i++) {
                    if (i == page) {
                        if (mLastView != null) {
                            mLastView.setSelected(false);
                        }
                        mSectionPagerAdapter.setSelected(mSectionViewPager, i,
                                index);
                        break;
                    }
                }
            }
        }
    }

    private ItemFocusListener mInnerItemFocusListener = new ItemFocusListener() {
        @Override
        public void onItemFocusListener(View view, int postition, boolean hasFocus) {
            Log.i(TAG, "--postition:" + postition + "---Constant.CURRENT_INDEX:" + Constant.CURRENT_INDEX + "--hasFocus:" + hasFocus);
            if (postition - 1 == Constant.CURRENT_INDEX && hasFocus) {
                TextView tv = (TextView) view.findViewById(R.id.text);
                tv.setTextColor(getResources().getColor(R.color.blue_a0e9));
            }
            mItemFocusListener.onItemFocusListener(view, postition, hasFocus);
        }
    };
}
