package cn.cncgroup.tv.cncplayer.widget.selectview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * 选集第二行Fragment
 */
@SuppressLint("ValidFragment")
public class SectionGridFragment extends Fragment
{
	public static final String PAGE = "page"; // 总集数
	public SectionClickListener mListener; // 点击事件回调
	public SectionFocusListener mFocuseListener;// 获得焦点事件
	public SectionAdapter mAdapter;
	private VerticalGridView mGridView;

	/**
	 * 构造
	 */
	public SectionGridFragment(SectionClickListener sectionClickListener,
	        SectionFocusListener sectionFocusListener)
	{
		super();
		this.mListener = sectionClickListener;
		this.mFocuseListener = sectionFocusListener;
	}

	/**
	 * 单例
	 *
	 * @param page
	 * @param listener
	 * @return
	 */
	public static SectionGridFragment getInstance(int page,
	        SectionClickListener listener,
	        SectionFocusListener sectionFocusListener)
	{
		SectionGridFragment fragment = new SectionGridFragment(listener,
		        sectionFocusListener);
		Bundle bundle = new Bundle();
		bundle.putInt(PAGE, page);
		fragment.setArguments(bundle);
		return fragment;
	}

	public void setSelected(final int position)
	{
		if (mAdapter != null)
		{
			mGridView.post(new Runnable()
			{
				@Override
				public void run()
				{
					mAdapter.setSelected(position);
					mGridView.setFocusPosition(position);
				}
			});
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.detail_gridview_vertical,
		        container, false);
		mGridView = (VerticalGridView) view.findViewById(R.id.gridview);
		mGridView.setNumColumns(Constant.COLUMN_SECTION_COUNT);
		mAdapter = new SectionAdapter(getActivity(),
		        getArguments().getInt(PAGE), mListener, mFocuseListener);
		mGridView.setAdapter(mAdapter);
		return view;
	}
}
