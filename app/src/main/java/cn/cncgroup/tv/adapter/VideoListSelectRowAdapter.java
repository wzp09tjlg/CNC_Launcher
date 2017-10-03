package cn.cncgroup.tv.adapter;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.widget.gridview.HorizontalGridView;

/**
 * Created by Wu on 2015/9/23.
 */
public class VideoListSelectRowAdapter extends
        RecyclerView.Adapter<VideoListSelectRowAdapter.ViewHolder>
{
	private final String TAG = "VideoListSelectRowAdapter";
	private ArrayList<VideoListSelectRowItemAdapter> mList;
	public VideoListSelectRowAdapter()
	{
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
		        R.layout.item_video_list_select_row, parent, false));
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position)
	{
		final VideoListSelectRowItemAdapter adapter = mList.get(position);
		holder.mHorizontalGridView.setAdapter(adapter);
		holder.leftTriangle.setVisibility(View.INVISIBLE);
		if (adapter.getItemCount() <= 5)
			holder.rightTriangle.setVisibility(View.INVISIBLE);
		holder.mHorizontalGridView
		        .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView,
                                                     int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && adapter.getItemCount() > 5) {
                            if (holder.mHorizontalGridView
                                    .getFirstVisibleIndex() > 0)
                                holder.leftTriangle.setVisibility(View.VISIBLE);
                            else
                                holder.leftTriangle
                                        .setVisibility(View.INVISIBLE);
                            if ((adapter.getItemCount() - 1)
                                    - holder.mHorizontalGridView
                                    .getLastVisibleIndex() > 0)
                                holder.rightTriangle
                                        .setVisibility(View.VISIBLE);
                            else
                                holder.rightTriangle
                                        .setVisibility(View.INVISIBLE);
                        }
                    }
                });
	}

	@Override
	public int getItemCount()
	{
		return mList == null ? 0 : mList.size();
	}

	public void bindData(ArrayList<VideoListSelectRowItemAdapter> list)
	{
		if (list == null)
			return;
		mList = new ArrayList<VideoListSelectRowItemAdapter>();
		for (VideoListSelectRowItemAdapter adapter : list)
		{
			mList.add(adapter);
		}
		notifyDataSetChanged();
	}

	class ViewHolder extends RecyclerView.ViewHolder
	{
		private ImageView leftTriangle;
		private ImageView rightTriangle;
		private HorizontalGridView mHorizontalGridView;

		public ViewHolder(View view)
		{
			super(view);
			leftTriangle = (ImageView) view
			        .findViewById(R.id.video_list_select_img_left_triangle);
			rightTriangle = (ImageView) view
			        .findViewById(R.id.video_list_select_img_right_triangle);
			mHorizontalGridView = (HorizontalGridView) view
			        .findViewById(R.id.video_list_select_grid_row);
		}
	}
}
