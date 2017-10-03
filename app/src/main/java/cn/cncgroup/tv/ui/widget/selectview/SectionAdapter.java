package cn.cncgroup.tv.ui.widget.selectview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cncgroup.tv.R;

/**
 * 选集第二行adapter(首次)
 */
public class SectionAdapter
        extends RecyclerView.Adapter<SectionAdapter.ViewHolder>
{
	private int mPage;
	private int mCount;
	private Context mContext;
	private SectionClickListener mListener; // 第二行点击事件
	private SectionFocusListener mFocuseListener; // 第二行获得焦点事件
	private int mSelectedPosition = -1;

	public SectionAdapter(Context context, int page,
	        SectionClickListener listener,
	        SectionFocusListener sectionFocusListener)
	{
		mPage = page;
		this.mContext = context;
		if (mPage == 0)
		{
			mSelectedPosition = 0;
		}
		this.mListener = listener;
		this.mFocuseListener = sectionFocusListener;
		if (mPage + 1 == Constant.SELECT_PAGER_COUNT)
		{
			int temp = Constant.COLLECT_PAGER_COUNT
			        % Constant.COLUMN_SECTION_COUNT;
			if (temp != 0)
			{
				mCount = temp;
			}
			else
			{
				mCount = Constant.COLUMN_SECTION_COUNT;
			}
		}
		else
		{
			mCount = Constant.COLUMN_SECTION_COUNT;
		}
	}

	public void setSelected(int position)
	{
		int temp = mSelectedPosition;
		mSelectedPosition = position;
		notifyItemChanged(position);
		notifyItemChanged(temp);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new ViewHolder(LayoutInflater.from(parent.getContext())
		        .inflate(R.layout.item_section, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position)
	{
		int start = mPage * Constant.COLUMN_ITEM_COUNT
		        * Constant.COLUMN_SECTION_COUNT
		        + position * Constant.COLUMN_ITEM_COUNT + 1;
		int end = start + Constant.COLUMN_ITEM_COUNT - 1;
		if (end > Constant.ITEM_COUNT)
		{
			end = Constant.ITEM_COUNT;
		}
		holder.text.setText(start + "-" + end);
		holder.itemView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mListener.onSelectionClick(v,
		                mPage * Constant.COLUMN_SECTION_COUNT + position);
			}
		});
		holder.itemView
		        .setOnFocusChangeListener(new View.OnFocusChangeListener()
		        {
			        @Override
			        public void onFocusChange(View view, boolean b)
			        {
				        mFocuseListener.onItemFocusListener(view,
		                        mPage * Constant.COLUMN_SECTION_COUNT
		                                + position,
		                        b);
			        }
		        });
		if (mSelectedPosition == position)
		{
			holder.itemView.setSelected(true);
		}
		else
		{
			holder.itemView.setSelected(false);
		}
		if (mPage + 1 == Constant.SELECT_PAGER_COUNT
		        && position == getItemCount() - 1)
		{
			holder.itemView.setId(R.id.rightSectionId);
			holder.itemView.setNextFocusRightId(R.id.rightSectionId);
		}
	}

	@Override
	public int getItemCount()
	{
		return mCount;
	}

	static class ViewHolder extends RecyclerView.ViewHolder
	{
		TextView text;

		public ViewHolder(View itemView)
		{
			super(itemView);
			text = (TextView) itemView.findViewById(R.id.text);
		}
	}
}
