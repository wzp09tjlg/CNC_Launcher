package cn.cncgroup.tv.ui.widget.selectview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.eventbus.FocuseEvent;
import cn.cncgroup.tv.cncplayer.eventbus.VideoClickEvent;
import de.greenrobot.event.EventBus;

/**
 * 选集第一行Adapter
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>
{
	private int mPage;
	private int mCount;
	private ItemClickListener mClickListener;
	private ItemFocusListener mFocusListener;

	/**
	 * 构造方法
	 */
	public ItemAdapter(int mPage, ItemFocusListener focusListener,
	        ItemClickListener clickListener)
	{
		this.mPage = mPage;
		this.mFocusListener = focusListener;
		this.mClickListener = clickListener;
		if (mPage + 1 == Constant.COLLECT_PAGER_COUNT)
		{ // 上面的页数等于page
			int temp = Constant.ITEM_COUNT % Constant.COLUMN_ITEM_COUNT;
			if (temp != 0)
			{
				mCount = temp;
			}
			else
			{
				mCount = Constant.COLUMN_ITEM_COUNT;
			}
		}
		else
		{
			mCount = Constant.COLUMN_ITEM_COUNT;
		}
	}

	@Override
	public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
	        int viewType)
	{
		return new ViewHolder(LayoutInflater.from(parent.getContext())
		        .inflate(R.layout.item_item, parent, false));
	}

	// @Override
	// public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
	//
	// return new
	// ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
	// R.layout.select_item, viewGroup, false));
	// }

	@Override
	public void onBindViewHolder(final ItemAdapter.ViewHolder holder,
	        int position)
	{

		final int number = mPage * Constant.COLUMN_ITEM_COUNT + position + 1;
		holder.textView.setText(number + "");
		// holder.line_view.setBackgroundResource(R.drawable.select_view_line);
		holder.itemView
		        .setOnFocusChangeListener(new View.OnFocusChangeListener()
		        {
			        @Override
			        public void onFocusChange(View view, boolean b)
			        {
				        SelectView.sFocusZoomUtil.onItemFocused(holder.textView,
		                        b);
				        if (mFocusListener != null)
				        {
					        mFocusListener.onItemFocusListener(view, number, b);
					        EventBus.getDefault()
		                            .post(new FocuseEvent(number, view, b));
				        }
			        }
		        });
		holder.itemView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (mClickListener != null)
				{
					mClickListener.onItemClickListener(view, number);
					EventBus.getDefault().post(new VideoClickEvent(number));
				}
			}
		});
		if (mPage == Constant.COLLECT_PAGER_COUNT - 1
		        && position == getItemCount() - 1)
		{
			holder.itemView.setId(R.id.rightItemId);
			holder.itemView.setNextFocusRightId(R.id.rightItemId);
		}
		holder.itemView.setNextFocusUpId(R.id.rl_collect);
	}

	@Override
	public int getItemCount()
	{
		return mCount;
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		TextView textView; // 显示集数的TextView

		public ViewHolder(View collectView)
		{
			super(collectView);
			textView = (TextView) itemView.findViewById(R.id.text);
		}
	}
}
