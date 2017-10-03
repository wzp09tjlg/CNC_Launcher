package cn.cncgroup.tv.view.search;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Request;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.modle.Search;
import cn.cncgroup.tv.modle.SearchData;
import cn.cncgroup.tv.modle.SearchResult;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.view.homesecond.utils.CategoryUtils;
import cn.cncgroup.tv.ui.listener.IPageLoader;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.HorizontalGridView;
import cn.cncgroup.tv.ui.widget.gridview.OnChildViewHolderSelectedListener;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.FocusZoomUtil;
import cn.cncgroup.tv.utils.PageLoadUtil;

/**
 * Created by zhangbo on 15-9-15.
 */
public class SearchResultFragment extends BaseFragment
        implements BaseRequest.Listener<SearchData>,
        OnItemClickListener<Search>, OnItemFocusListener<Search>, IPageLoader {
    public static final String KEYWORD = "keyword";
    private static final int PAGE_SIZE = 100;
    private final String TAG = "SearchRecommendFragment";
    private VerticalGridView mGridView;
    private SearchResultAdapter mAdapter;
    private int mTotal;
    private String mKeyword;
    private HorizontalGridView mTitle;
    private ArrayList<CategoryTitleAdapter.SearchMenu> mList;
    private CategoryTitleAdapter mTitleAdapter;
    private FocusZoomUtil mFocusZoomUtil;
    private ArrayList<Search> mSearchResultAll;
    private SearchResult mSearchResult;
    private String mType = "";
    private boolean isChangeData = false;
    private ShadowView mShadowView;
    private View mFocusedView;
    private ImageView mEmpty;

    public static SearchResultFragment getInstance(String keyword) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEYWORD, keyword);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_search_recommend;
    }

    @Override
    protected void findView(View view) {
        mGridView = (VerticalGridView) view.findViewById(R.id.gridview);
        mGridView.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_PAGE);
        mShadowView = (ShadowView) view.findViewById(R.id.shadow);
        mEmpty = (ImageView) view.findViewById(R.id.result_empty);
        mTitle = (HorizontalGridView) view.findViewById(R.id.rl_title);
    }

    @Override
    protected void initView() {
        initTitleList();
        Bundle bundle = getArguments();
        mFocusZoomUtil = new FocusZoomUtil(FocusZoomUtil.ZOOM_FACTOR_MEDIUM);
        mKeyword = bundle.getString(KEYWORD);
        mAdapter = new SearchResultAdapter(this, this);
        mGridView.setAdapter(mAdapter);
        NetworkManager.getInstance().search(mKeyword, 1, PAGE_SIZE, this, TAG);
        initLoadingState();
        mGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mFocusedView != null && mGridView.getFocusedChild() != null) {
                    mShadowView.moveTo(mFocusedView.findViewById(R.id.image));
                }
            }
        });
        mShadowView.init(25);
        mTitleAdapter = new CategoryTitleAdapter(mList, new ItemFocusListener());
        mTitle.setAdapter(mTitleAdapter);
        mTitle.setOnChildViewHolderSelectedListener(mOnSelectedListener);
        mTitle.setHorizontalMargin(getResources().getDimensionPixelOffset(R.dimen.dimen_10dp));
    }

    @Override
    public void onStop() {
        super.onStop();
        NetworkManager.cancelRequest(TAG);
    }

    @Override
    public void onResponse(SearchData result, boolean isFromCache) {
        if (getActivity() == null) {
            return;
        }
        mTotal = result.getResult().getTotal();
        hideLoading();
        showContent();
        if (result.getResult().getTotal() > 0) {
            mEmpty.setVisibility(View.GONE);
            mSearchResult = result.getResult();
            mSearchResultAll = result.getResult().getContent();
            if (isChangeData) {
                mAdapter.addData(result, true);
                isChangeData = false;
            } else
                mAdapter.addData(result, false);
        } else {
            mEmpty.setVisibility(View.VISIBLE);
            mAdapter.changeData(null);
        }
    }

    @Override
    public void onFailure(int errorCode, Request request) {
        if (getActivity() == null) {
            return;
        }
        hideLoading();
        hideContent();
        showError();
    }

    @Override
    public void onItemClickLister(View view, int position, Search search) {
        Log.i(TAG, "Search=" + search);
        String channel = search.getChannelId() + "";
        if (search.getStype() != null && search.getStype().equals("0") && channel.equals(CategoryUtils.getCategoryByName("综艺").getId())) {
            VideoSet set = new VideoSet();
            set.setId(search.getId());
            Log.i(TAG, "Search set.getSourceUrl()=" + set.getSourceUrl());
            Project.get().getConfig().playVideo(getActivity(), set.getId(), set.getId(),"1", 0, search.getName(), search.getImage(), search.getChannelId() + "");
            return;
        }
        VideoSet set = new VideoSet();
        set.setId(search.getId());
        Project.get().getConfig().openVideoDetail(getActivity(), set);
    }

    @Override
    public void onItemFocusLister(View view, int position, Search search,
                                  boolean hasFocus) {
        if (hasFocus) {
            mFocusedView = view;
            mShadowView.moveTo(view.findViewById(R.id.image));
            PageLoadUtil.loadPage(position, PAGE_SIZE, mTotal, this);
        } else {
            mShadowView.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadPage(int nextPage) {
        if (Project.get().getConfig() instanceof cn.cncgroup.tv.conf.model.domybox.AppConfig)
            return;
        int nextPageStart = (nextPage - 1) * PAGE_SIZE;
        if (nextPageStart < mAdapter.getItemCount()
                && mAdapter.getItem(nextPageStart) == null) {
            NetworkManager.getInstance().search(mKeyword, mType, nextPage, PAGE_SIZE,
                    this, TAG);
        }
    }

    private void initTitleList() {
        if (mList == null) {
            mList = new ArrayList<CategoryTitleAdapter.SearchMenu>();
        }
        mList.add(new CategoryTitleAdapter.SearchMenu("全部", ""
        ));
        mList.add(new CategoryTitleAdapter.SearchMenu(getString(R.string.title_movie),
                CategoryUtils.getCategoryByName("电影").getId()));
        mList.add(new CategoryTitleAdapter.SearchMenu(getString(R.string.title_teleplay),
                CategoryUtils.getCategoryByName("电视剧").getId()));
        mList.add(new CategoryTitleAdapter.SearchMenu(getString(R.string.title_variety),
                CategoryUtils.getCategoryByName("综艺").getId()));
        mList.add(new CategoryTitleAdapter.SearchMenu("少儿",
                CategoryUtils.getCategoryByName("少儿").getId()));
        mList.add(new CategoryTitleAdapter.SearchMenu("动漫",
                CategoryUtils.getCategoryByName("动漫").getId()));
    }

    private void refreshData(CategoryTitleAdapter.SearchMenu searchMenu) {
        isChangeData = true;
        mType = searchMenu.mtype;
        if (Project.get().getConfig() instanceof cn.cncgroup.tv.conf.model.domybox.AppConfig) {
            Log.i(TAG, "domybox filter");
            domyBoxFilter(searchMenu);
        } else {
            showLoading();
            Log.i(TAG, "voole filter mKeyword=" + mKeyword + "--mtype=" + mType);
            NetworkManager.getInstance().search(mKeyword, searchMenu.mtype, 1, PAGE_SIZE,//1:pagenum
                    this, TAG);
        }
    }

    private void domyBoxFilter(CategoryTitleAdapter.SearchMenu searchMenu) {
        if (mSearchResult == null || mSearchResult.getTotal() == 0) {
            mAdapter.changeData(null);
            mEmpty.setVisibility(View.VISIBLE);
            return;
        }
        mEmpty.setVisibility(View.GONE);
        if (TextUtils.isEmpty(searchMenu.mtype)) {
            SearchData result = new SearchData();
            result.setSuccess(true);
            SearchResult data = new SearchResult();
            data.setPageCount(mSearchResult.getPageCount());
            data.setTotal(mSearchResultAll.size());
            data.setPageSize(mSearchResult.getPageSize());
            data.setPageNo(mSearchResult.getPageNo());
            data.setContent(mSearchResultAll);
            result.setResult(data);
            mAdapter.addData(result, true);
            isChangeData = false;
        } else {
            SearchData result = new SearchData();
            result.setSuccess(true);
            ArrayList<Search> searchList = new ArrayList<Search>();
            for (Search searchItem : mSearchResultAll) {
//                Log.i(TAG, " searchList.add(searchItem)=" + searchMenu.mtype + "---searchItem.getChannelId()=" + searchItem.getChannelId());
                if (String.valueOf(searchItem.getChannelId()).equals(searchMenu.mtype)) {
                    searchList.add(searchItem);
                }
            }
            if (searchList.size() == 0) {
                mEmpty.setVisibility(View.VISIBLE);
            }
            SearchResult data = new SearchResult();
            data.setPageCount(searchList.size() % PAGE_SIZE + 1);
            data.setTotal(searchList.size());
            data.setPageSize(PAGE_SIZE);
            data.setPageNo(1);
            data.setContent(searchList);
            Log.i(TAG, "isChangeData" + isChangeData + "---SearchResult data =" + data);
            result.setResult(data);
            if (isChangeData) {
                mAdapter.addData(result, true);
                isChangeData = false;
            } else {
                mAdapter.addData(result, false);
            }
        }

    }

    class ItemFocusListener implements OnItemFocusListener<CategoryTitleAdapter.SearchMenu> {
        @Override
        public void onItemFocusLister(View view, int position, CategoryTitleAdapter.SearchMenu searchMenu, boolean hasFocus) {
        }
    }

    private OnChildViewHolderSelectedListener mOnSelectedListener = new OnChildViewHolderSelectedListener() {
        @Override
        public void onChildViewHolderSelected(RecyclerView parent,
                                              RecyclerView.ViewHolder holder, int position, int subposition) {
            Log.i(TAG, "onChildViewHolderSelected position=" + position);
            if (mTitleAdapter.getLastViewHolder() != null) {
                mFocusZoomUtil.onItemFocused(
                        mTitleAdapter.getLastViewHolder().itemView, false);
            }
            refreshData(mList.get(position));
            mFocusZoomUtil.onItemFocused(holder.itemView, true);
            mTitleAdapter.setLastViewHolder(holder);
        }
    };
}
