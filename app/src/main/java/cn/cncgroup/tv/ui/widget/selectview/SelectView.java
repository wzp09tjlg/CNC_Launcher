package cn.cncgroup.tv.ui.widget.selectview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.cncgroup.tv.R;
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
    private int mCurrentPos = 0;
    private ItemFocusListener mFocusListener;

    public SelectView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        sFocusZoomUtil = new FocusZoomUtil(FocusZoomUtil.ZOOM_FACTOR_LARGE);
        initAttributes(context, attributeSet);
        addContextView(context);
    }

    /**
     * 获取自定义属性
     */
    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet,
                R.styleable.selectView);
        Constant.ITEM_MARGIN = typedArray
                .getDimensionPixelSize(R.styleable.selectView_itemMargin, 0);
        Constant.ITEM_HEIGHT = typedArray
                .getDimensionPixelSize(R.styleable.selectView_itemHeight, 0);
        typedArray.recycle();
    }

    /**
     * 添加两个ViewPager
     */
    private void addContextView(Context context) {
        setClipChildren(false);
        setOrientation(LinearLayout.VERTICAL);
        FrameLayout itemContainer = (FrameLayout) LayoutInflater.from(context)
                .inflate(R.layout.selectview_viewpager, this, false);
        mItemViewPager = (ViewPager) itemContainer.findViewById(R.id.viewPager);
        mItemViewPager.setOffscreenPageLimit(100);
        mItemViewPager.setId(R.id.expandable_text);
//		itemContainer.setBackgroundColor(
//		        getContext().getResources().getColor(R.color.yellow));
        addView(itemContainer);
        FrameLayout sectionContainer = (FrameLayout) LayoutInflater
                .from(context)
                .inflate(R.layout.selectview_viewpager, this, false);
        mSectionViewPager = (ViewPager) sectionContainer
                .findViewById(R.id.viewPager);
        sectionContainer.findViewById(R.id.iv_invisable).setVisibility(View.INVISIBLE);
        mSectionViewPager.setOffscreenPageLimit(100);
        mSectionViewPager.setId(R.id.expand_collapse);
        addView(sectionContainer);
    }

    public void setmItemFocused(final int postion) {
        final int page = postion / Constant.COLUMN_ITEM_COUNT;
        final int index = postion % Constant.COLUMN_ITEM_COUNT;
        Log.i(TAG,"page:"+page+"--index:"+index);
        mItemViewPager.post(new Runnable() {
            @Override
            public void run() {
                SelectView.this.onItemFocusListener(null,page,true);
                ItemGridFragment fragment = (ItemGridFragment) mItemPagerAdapter.instantiateItem(mItemViewPager, page);
                fragment.getGridView().setSelectedPosition(index);
            }
        });
    }

    private ItemFocusListener mListener = new ItemFocusListener() {
        @Override
        public void onItemFocusListener(View view, int postition, boolean hasFocus) {
           /* if (hasFocus && view == null) {
                int page = postition / Constant.COLUMN_ITEM_COUNT;
                SelectView.this.onItemFocusListener(null, page, true);
            }*/
            mFocusListener.onItemFocusListener(view, postition, hasFocus);
        }
    };

    /**
     * 初始化数据
     */
    public void initView(FragmentManager manager,
                         ItemClickListener clickListener, ItemFocusListener focusListener) {
        mFocusListener = focusListener;
        Log.i(TAG, "initView");
        mItemPagerAdapter = new ItemPageAdapter(manager, mListener,
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
}
