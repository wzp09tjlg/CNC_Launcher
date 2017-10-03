package cn.cncgroup.tv.view.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.modle.Search;
import cn.cncgroup.tv.modle.SearchData;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.ui.listener.IPageLoader;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * Created by zhang on 2015/10/27.
 */
public class SearchHotDomyboxFragment extends BaseFragment
        implements BaseRequest.Listener<SearchData>,
        OnItemClickListener<Search>, OnItemFocusListener<Search>, IPageLoader
{
	private static final int PAGE_SIZE = 24;
	private final String TAG = "SearchHotDomyboxFragment";
	private VerticalGridView mGridView;
	private SearchResultAdapter mAdapter;
	private TextView mTitle;
	private int mTotal;
	private ShadowView mShadowView;
	private View mFocusedView;
	private ImageView mEmpty;

	public static SearchHotDomyboxFragment getInstance()
	{
		SearchHotDomyboxFragment fragment = new SearchHotDomyboxFragment();
		return fragment;
	}

	@Override
	protected int getContentView()
	{
		return R.layout.fragment_recommend_voole;
	}

	@Override
	protected void findView(View view)
	{
		mGridView = (VerticalGridView) view.findViewById(R.id.gridview);
		mGridView.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_PAGE);
		mShadowView = (ShadowView) view.findViewById(R.id.shadow);
		mEmpty = (ImageView)view.findViewById(R.id.result_empty);
		mTitle = (TextView) view.findViewById(R.id.title);
	}

	@Override
	protected void initView() {
		mTitle.setText(getText(R.string.hot_search));
		mAdapter = new SearchResultAdapter(this, this, 8);
		mGridView.setAdapter(mAdapter);
		mGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (mFocusedView != null) {
					mShadowView.moveTo(mFocusedView.findViewById(R.id.image));
				}
			}
		});
		mShadowView.init(25);
		NetworkManager.getInstance().search("", 1, PAGE_SIZE, this, TAG);
		initLoadingState();
	}
	@Override
	public void onStop()
	{
		super.onStop();
		NetworkManager.cancelRequest(TAG);
	}

	@Override
	public void onResponse(SearchData result,boolean isFromCache)
	{
		if (getActivity() == null)
		{
			return;
		}
		mTotal = result.getResult().getTotal();
		if (mTotal == 0)
		{
			hideLoading();
			hideContent();
			showError();
			return;
		}
		if (result.getResult().getPageNo() == 1)
		{
			hideLoading();
			hideError();
			showContent();
		}
		mAdapter.addData(result,false);
	}

	@Override
	public void onFailure(int errorCode,Request request)
	{
		if (getActivity() == null)
		{
			return;
		}
		hideLoading();
		hideContent();
		showError();
	}

	@Override
	public void onItemClickLister(View view, int position, Search search)
	{
		VideoSet set = new VideoSet();
		set.setId(search.getId());
		Project.get().getConfig().openVideoDetail(getActivity(), set);
	}

	@Override
	public void onItemFocusLister(View view, int position, Search search,
	        boolean hasFocus)
	{
		if (hasFocus) {
			mFocusedView = view;
			mShadowView.moveTo(view.findViewById(R.id.image));
		} else {
			mShadowView.setVisibility(View.GONE);
		}
	}

	@Override
	public void loadPage(int nextPage)
	{
		int nextPageStart = (nextPage - 1) * PAGE_SIZE;
		if (nextPageStart < mAdapter.getItemCount()
		        && mAdapter.getItem(nextPageStart) == null)
		{
			NetworkManager.getInstance().search("", nextPage, PAGE_SIZE, this,
			        TAG);
		}
	}
}