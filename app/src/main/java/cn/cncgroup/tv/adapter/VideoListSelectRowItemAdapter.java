package cn.cncgroup.tv.adapter;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.modle.FilterList;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;

/**
 * Created by Wu on 2015/9/24.
 */
public class VideoListSelectRowItemAdapter extends
        RecyclerView.Adapter<VideoListSelectRowItemAdapter.ViewHolder>
{
	private final String TAG = "VideoListSelectRowItemAdapter";
	private ArrayList<FilterList> mList;
	private OnItemClickListener<FilterList> itemClickListener;
	private int mCurrentPosition;

	public VideoListSelectRowItemAdapter(ArrayList<FilterList> list,
	        OnItemClickListener<FilterList> listener)
	{
		mList = list;
		itemClickListener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
		        R.layout.item_video_list_select_row_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position)
	{
		final FilterList filter = mList.get(position);
		holder.select.setText("" + filter.getName().trim());
		if (position == mCurrentPosition)
		{
			holder.itemView.setSelected(true);
		}
		else
		{
			holder.itemView.setSelected(false);
		}
		holder.itemView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setSelectPosition(position);
				itemClickListener.onItemClickLister(v, position, filter);
			}
		});
	}

	@Override
	public int getItemCount()
	{
		return mList == null ? 0 : mList.size();
	}

	public FilterList getItem(int position)
	{
		return mList.get(position);
	}

	public void setSelectPosition(int position)
	{
		int temp = mCurrentPosition;
		mCurrentPosition = position;
		notifyItemChanged(position);
		notifyItemChanged(temp);
	}

	public int getmCurrentPosition()
	{
		return mCurrentPosition;
	}

	public int getSelectedTag()
	{
		if (mCurrentPosition < 0 || mCurrentPosition > mList.size())
			return 0;
		else
			return mList.get(mCurrentPosition).getId();
	}

	public FilterList getSelectItem()
	{
		return mList.get(mCurrentPosition);
	}

	public int getSelectedAllTag()
	{
		if (mList.size() == 0)
			return 0;
		else
			return mList.get(0).getId();
	}

	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView select;

		public ViewHolder(View view)
		{
			super(view);
			select = (TextView) view
			        .findViewById(R.id.video_list_select_text_row_item);
		}
	}
}
