package cn.cncgroup.tv.adapter;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.modle.FilterList;

/**
 * Created by Wu on 2015/9/23.
 */
public class VideoListSelectMenuAdapter extends
        RecyclerView.Adapter<VideoListSelectMenuAdapter.ViewHolder>
{
	private final String TAG = "VideoListSelectMenuAdapter";
	private ArrayList<String> mList;

	public VideoListSelectMenuAdapter()
	{
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
		        R.layout.item_video_list_select_menu, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		final String menu = mList.get(position);
		holder.menu.setText(""
		        + ((menu.length() > 2) ? menu.substring(0, 2) : menu));
	}

	@Override
	public int getItemCount()
	{
		return mList == null ? 0 : mList.size();
	}

	public void bindData(ArrayList<FilterList> list)
	{
		if (list == null)
			return;
		mList = new ArrayList<String>();
		for (FilterList filter : list)
		{
			mList.add(filter.getName() + ":");
		}
		notifyDataSetChanged();
	}

	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView menu;

		public ViewHolder(View view)
		{
			super(view);
			menu = (TextView) view
			        .findViewById(R.id.video_list_select_text_menu);
		}
	}
}
