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
 * 选集第一行Fragment
 */
@SuppressLint("ValidFragment")
public class ItemGridFragment extends Fragment
{

	public static final String PAGE = "page"; // 总集数
	private static final String TAG = "CollectGridFragment";
	public ItemFocusListener mItemFocusListener; // 第一行获得焦点回调
	public ItemClickListener mItemClickLinstener; // 第一行点击事件回调
	public VerticalGridView mGridView;

	public ItemGridFragment(ItemFocusListener focusListener,
	        ItemClickListener clickListener)
	{
		super();
		this.mItemFocusListener = focusListener;
		this.mItemClickLinstener = clickListener;
	}

	/**
	 * 单例
	 *
	 * @return
	 */
	public static ItemGridFragment getInstance(int page,
	        ItemFocusListener focusListener, ItemClickListener clickListener)
	{

		ItemGridFragment fragment = new ItemGridFragment(focusListener,
		        clickListener);
		Bundle bundle = new Bundle(); // 传递Page
		bundle.putInt(PAGE, page);
		fragment.setArguments(bundle);
		return fragment;
	}

	public void setSelected(final int position)
	{
		mGridView.setFocusPosition(position);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.detail_gridview_vertical,
		        container, false);
		mGridView = (VerticalGridView) view.findViewById(R.id.gridview);
		mGridView.setNumColumns(Constant.COLUMN_ITEM_COUNT); // 上面的条目数
		mGridView.setAdapter(new ItemAdapter(getArguments().getInt(PAGE),
				mItemFocusListener, mItemClickLinstener));
		return view;
	}

	public VerticalGridView getGridView() {
		return mGridView;
	}
}
