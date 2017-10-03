package cn.cncgroup.tv.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import cn.cncgroup.tv.adapter.WrapperAdapter;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * 纵向翻页的gridview，配合WrapperAdapter使用。 Created by zhang on 2015/6/15.
 */
public class WrapperVerticalGridView extends VerticalGridView
{
	protected int mColumn;
	private WrapperAdapter mAdapter;

	public WrapperVerticalGridView(Context context)
	{
		super(context);
	}

	public WrapperVerticalGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public void setNumColumns(int numColumns)
	{
		mColumn = numColumns;
		super.setNumColumns(numColumns);
	}

	@Override
	public void setAdapter(Adapter adapter)
	{
		mAdapter = (WrapperAdapter) adapter;
		super.setAdapter(adapter);
	}

	@Override
	public View focusSearch(View focused, int direction)
	{
		View view = super.focusSearch(focused, direction);
		final int position = ((RecyclerView.LayoutParams) focused
		        .getLayoutParams()).getViewAdapterPosition();
		if (direction == View.FOCUS_DOWN)
		{
			if (mAdapter.getCount() - 1 - position < mColumn)
			{
				setSelectedPositionSmooth(mAdapter.getCount() - 1);
			}
		}
		return view;
	}
}
